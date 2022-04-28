package ga.ozli.minecraftmods.groovylicious.transform.config

import groovy.transform.CompileStatic
import net.minecraftforge.common.ForgeConfigSpec
import net.minecraftforge.fml.ModLoadingContext
import net.minecraftforge.fml.config.ModConfig
import org.codehaus.groovy.GroovyBugError
import org.codehaus.groovy.ast.*
import org.codehaus.groovy.ast.expr.*
import org.codehaus.groovy.ast.stmt.BlockStatement
import org.codehaus.groovy.ast.stmt.ExpressionStatement
import org.codehaus.groovy.ast.stmt.IfStatement
import org.codehaus.groovy.ast.stmt.ReturnStatement
import org.codehaus.groovy.ast.stmt.Statement
import org.codehaus.groovy.control.CompilePhase
import org.codehaus.groovy.control.SourceUnit
import org.codehaus.groovy.transform.AbstractASTTransformation
import org.codehaus.groovy.transform.GroovyASTTransformation

import java.util.regex.Matcher

import static org.objectweb.asm.Opcodes.*

@CompileStatic
@GroovyASTTransformation(phase = CompilePhase.CANONICALIZATION)
class ModConfigASTTransformation extends AbstractASTTransformation {

    static ModConfig.Type modConfigType = ModConfig.Type.COMMON
    static String modId = '$unknown'

    static ConstantExpression getPropertyValueOrDefault(PropertyNode property) {
        if ((property.type == ClassHelper.STRING_TYPE || property.type == ClassHelper.GSTRING_TYPE) && property.field.initialValueExpression === null) {
            return ConstantExpression.EMPTY_STRING
        } else {
            return new ConstantExpression((property.field.initialValueExpression as ConstantExpression)?.value?.asType(property.type.typeClass) ?: 0.asType(property.type.typeClass))
        }
    }

    @Override
    void visit(final ASTNode[] nodes, final SourceUnit source) {
        init(nodes, source) // ensure that nodes is [AnnotationNode, AnnotatedNode]

        AnnotationNode configAnnotation = nodes[0] as AnnotationNode
        AnnotatedNode targetClass = nodes[1] as AnnotatedNode

        // try to grab the value from the config annotation to determine the config type to generate
        final annotationValue = configAnnotation.getMember('value') as PropertyExpression
        if (annotationValue !== null)
            modConfigType = (annotationValue.property as ConstantExpression).value as ModConfig.Type

        // try to grab the modId from the config annotation to determine the modId to use in the generated config's filename
        def annotationModId = configAnnotation.getMember('modId')
        try {
            // try interpreting the modId as a direct String first
            annotationModId = annotationModId as ConstantExpression
            if (annotationModId !== null) {
                modId = annotationModId.value as String
                if (modId === null) {
                    addError("modId cannot be null", annotationModId)
                    return
                }
            }
        } catch (ClassCastException e) {
            // if that fails, try interpreting the modId as a variable that points to a String
            annotationModId = annotationModId as VariableExpression
            if (annotationModId !== null) {
                modId = (annotationModId.accessedVariable.initialExpression as ConstantExpression)?.value?.asType(String)
                if (modId === null) {
                    addError("modId variable cannot be null", annotationModId)
                    return
                }
            }
        }

        if (!(targetClass instanceof ClassNode))
            throw new GroovyBugError("Class annotation ${configAnnotation.getClassNode().getName()} annotated no Class, this must not happen.")

        ClassNode targetClassNode = targetClass as ClassNode

        final ClassNode configBuilderClassNode = ClassHelper.make(ForgeConfigSpec.Builder)
        /**
         * To make configs, we need a ForgeConfigSpec.Builder
         *
         * First, let's check if a builder is already explicitly defined and use that if so.
         * If we can't find one, we'll make one called $configBuilder and add it to the class
         */
        String configBuilderVariableName = '$configBuilder'
        targetClassNode.fields.each { field ->
            if (field.type == configBuilderClassNode) {
                configBuilderVariableName = field.name
                return // don't continue iterating once we've found what we're looking for
            }
        }
        if (configBuilderVariableName == '$configBuilder') {
            /**
             * Adds this field to the start of the class:
             * private static final ForgeConfigSpec.Builder $configBuilder = new ForgeConfigSpec.Builder()
             * Prepending generated variable names with a $ to avoid possible conflicts with declared variables
             */
            targetClassNode.addFieldFirst(
                    new FieldNode(
                            configBuilderVariableName,
                            ACC_PRIVATE | ACC_STATIC | ACC_FINAL,
                            configBuilderClassNode,
                            targetClassNode,
                            new ConstructorCallExpression(
                                    configBuilderClassNode,
                                    ArgumentListExpression.EMPTY_ARGUMENTS
                            )
                    )
            )
        }

        if (targetClassNode.properties.isEmpty())
            addError("Unable to detect any properties inside class '${targetClassNode.getName()}' annotated with '${configAnnotation.getClassNode().getName()}'", targetClassNode)

        // todo: support config groups by declaring inner static classes in the dataclass

        // for each property inside the class...
        targetClassNode.properties.each { PropertyNode property ->

            boolean hasGroovyDoc = property.field.groovydoc?.content ?: false
            boolean hasComments = false
            String groovyDocStr = ''
            List<Expression> groovyDocCommentsExpressions = new ArrayList<>()

            if (hasGroovyDoc) {
                groovyDocStr = property.field.groovydoc.content
                hasGroovyDoc = !(groovyDocStr.isEmpty() || groovyDocStr.isAllWhitespace())
            }

            if (hasGroovyDoc) {
                final String groovyDocComments = CodeCommentParser.parse(groovyDocStr)
                hasComments = !(groovyDocComments.isEmpty() || groovyDocComments.isAllWhitespace())

                groovyDocComments.eachLine { line ->
                    groovyDocCommentsExpressions.add(new ConstantExpression(line) as Expression)
                }
            }

            // create a new field that stores the underlying ForgeConfigSpec.ConfigValue/ForgeConfigSpec.IntValue/etc
            // for the property

            // use defineInRange only when the field has an associated groovydoc containing the @range doclet/taglet (e.g. {@range 0..5})
            boolean isRangedType = false

            // if the groovydoc contains @range, try to parse it into a NumberRange and store it in the range variable
            if (hasGroovyDoc && groovyDocStr.contains('@range') ?: false) {
                isRangedType = true

                // thanks to AterAnimAvis for the final regex, thanks to SciWhiz and SizableShrimp for helping with the regex
                Matcher rangeMatcher = groovyDocStr =~ /\{@range\s+(?<lower>(\d*\.)?\d+)(?<lowerExclusive><)?\.\.(?<upperExclusive><)?(?<upper>(\d*\.)?\d+)}/
                rangeMatcher.find()
                String lowerStr = rangeMatcher.group('lower')
                String upperStr = rangeMatcher.group('upper')

                // todo: fix this
                double lowerDouble = lowerStr.toDouble()
                long lowerLong = 0l
                boolean useLowerDouble
                try {
                    lowerLong = lowerStr.toLong()
                    useLowerDouble = false
                } catch (NumberFormatException ignored) {
                    useLowerDouble = true
                }

                // todo: fix this
                double upperDouble = upperStr.toDouble()
                long upperLong = 0l
                boolean useUpperDouble
                try {
                    upperLong = upperStr.toLong()
                    useUpperDouble = false
                } catch (NumberFormatException ignored) {
                    useUpperDouble = true
                }

                boolean isLowerExclusive = false
                try {
                    isLowerExclusive = rangeMatcher.group('isLowerExclusive') !== null
                } catch (IllegalArgumentException ignored) {
                }

                boolean isUpperExclusive = false
                try {
                    isUpperExclusive = rangeMatcher.group('isUpperExclusive') !== null
                } catch (IllegalArgumentException ignored) {
                }

                // todo: fix 99<..1000 not parsing correctly
                // todo: addError() when setting a default value outside of a defined range

                NumberRange range = new NumberRange(useLowerDouble ? lowerDouble : lowerLong, useUpperDouble ? upperDouble : upperLong, isLowerExclusive, isUpperExclusive)

                //println SV(lowerStr, upperStr, lowerDouble, lowerLong, useLowerDouble, upperDouble, upperLong, useUpperDouble, isLowerExclusive, isUpperExclusive)

                if (ConfigTypes.Bounded.primitiveCandidates.contains(property.type) && property.type != configBuilderClassNode) {
                    /**
                     * Assuming `property.name` == "test", `property.type` == `int`, property value == `42`, property groovydoc `{@range 0..100}` in this example:
                     * static ForgeConfigSpec.IntValue $configValueForTest = $configBuilder.defineInRange('test', 42, 0, 100)
                     */
                    targetClassNode.addField(
                            new FieldNode(
                                    "\$configValueFor${property.name.capitalize()}",
                                    ACC_PUBLIC | ACC_STATIC | ACC_FINAL,
                                    ConfigTypes.getWrappedType(property.type, isRangedType),
                                    targetClassNode,
                                    new MethodCallExpression(
                                            hasComments ? new MethodCallExpression(
                                                    new VariableExpression(
                                                            configBuilderVariableName,
                                                            configBuilderClassNode
                                                    ),
                                                    'comment',
                                                    new ArgumentListExpression(
                                                            groovyDocCommentsExpressions
                                                    )
                                            ) : new VariableExpression(
                                                    configBuilderVariableName,
                                                    configBuilderClassNode
                                            ),
                                            'defineInRange',
                                            new ArgumentListExpression([
                                                    new ConstantExpression(property.name) as Expression,
                                                    getPropertyValueOrDefault(property),
                                                    new ConstantExpression(range.from.asType(property.type.typeClass)) ?: ConfigTypes.Bounded.getDefaultLowerBound(property.type),
                                                    new ConstantExpression(range.to.asType(property.type.typeClass)) ?: ConfigTypes.Bounded.getDefaultUpperBound(property.type)
                                            ])
                                    )
                            )
                    )

                    /**
                     * Assuming the same example as above:
                     * static NumberRange $configRangeForTest = new NumberRange(0, 100, false, false)
                     */
                    targetClassNode.addField(
                            new FieldNode(
                                    "\$configRangeFor${property.name.capitalize()}",
                                    ACC_PUBLIC | ACC_STATIC | ACC_FINAL,
                                    ClassHelper.make(NumberRange),
                                    targetClassNode,
                                    new ConstructorCallExpression(
                                            ClassHelper.make(NumberRange),
                                            new ArgumentListExpression([
                                                    useLowerDouble ? new ConstantExpression(lowerDouble) as Expression : new ConstantExpression(lowerLong) as Expression,
                                                    useUpperDouble ? new ConstantExpression(upperDouble) : new ConstantExpression(upperLong),
                                                    isLowerExclusive ? ConstantExpression.TRUE : ConstantExpression.FALSE,
                                                    isUpperExclusive ? ConstantExpression.TRUE : ConstantExpression.FALSE
                                            ])
                                    )
                            )
                    )
                }
            } else if (ConfigTypes.primitivesList.contains(property.type) && property.type != configBuilderClassNode) {
                targetClassNode.addField(
                        "\$configValueFor${property.name.capitalize()}",
                        ACC_PUBLIC | ACC_STATIC | ACC_FINAL,
                        ConfigTypes.getWrappedType(property.type, isRangedType),
                        new MethodCallExpression(
                                hasComments ? new MethodCallExpression(
                                        new VariableExpression(
                                                configBuilderVariableName,
                                                configBuilderClassNode
                                        ),
                                        'comment',
                                        new ArgumentListExpression(
                                                groovyDocCommentsExpressions
                                        )
                                ) : new VariableExpression(
                                        configBuilderVariableName,
                                        configBuilderClassNode
                                ),
                                'define',
                                new ArgumentListExpression([
                                        new ConstantExpression(property.name) as Expression,
                                        getPropertyValueOrDefault(property),
                                ])
                        )
                )
            } else if (property.type != configBuilderClassNode) {
                println "Error: Unknown type \"${property.type}\""
                println "Supported types: ${ConfigTypes.list}"
            }

            if (ConfigTypes.primitivesList.contains(property.type) && property.type != configBuilderClassNode) {
                // generate custom get() and set() methods that redirect property access and modification (respectively) to
                // the field made earlier
                VariableScope targetClassNodeScope = new VariableScope()
                targetClassNodeScope.classScope = targetClassNode

                /**
                 * Assuming `property.name` == "test" and `property.type` == `int` in this example:
                 * static int getTest() {
                 *     return $configValueForTest.get()
                 * }
                 */
                targetClassNode.addMethod(
                        new MethodNode(
                                "get${property.name.capitalize()}",
                                ACC_PUBLIC | ACC_STATIC,
                                ClassHelper.getWrapper(property.type),
                                Parameter.EMPTY_ARRAY,
                                ClassNode.EMPTY_ARRAY,
                                new BlockStatement(
                                        [new ReturnStatement(
                                                new MethodCallExpression(
                                                        new VariableExpression(
                                                                "\$configValueFor${property.name.capitalize()}",
                                                                ConfigTypes.getWrappedType(property.type, isRangedType),
                                                        ),
                                                        'get',
                                                        ArgumentListExpression.EMPTY_ARGUMENTS
                                                )
                                        ) as Statement],
                                        targetClassNodeScope
                                )
                        )
                )

                /**
                 * Assuming `property.name` == "test" in this example:
                 * static void setTest(int newValue) {
                 *     $configValueForTest.set(newValue)
                 * }
                 *
                 * For ranged config values, the configValueForX is only set if newValue is within the accepted range:
                 * static void setTest(int newValue) {
                 *     if ($configRangeForTest.containsWithinBounds(newValue) {
                 *          $configValueForTest.set(newValue)
                 *     }
                 * }
                 *
                 * Todo: Memoize the if check
                 */
                targetClassNode.addMethod(
                        new MethodNode(
                                "set${property.name.capitalize()}",
                                ACC_PUBLIC | ACC_STATIC, // modifiers
                                ClassHelper.VOID_TYPE, // return type
                                [new Parameter(ClassHelper.getUnwrapper(property.type), 'newValue')] as Parameter[], // params
                                ClassNode.EMPTY_ARRAY, // exceptions
                                new BlockStatement( // code
                                        isRangedType ? [new IfStatement(
                                                new BooleanExpression(
                                                        new MethodCallExpression(
                                                                new VariableExpression(
                                                                        "\$configRangeFor${property.name.capitalize()}",
                                                                        ClassHelper.make(NumberRange)
                                                                ),
                                                                'containsWithinBounds',
                                                                new ArgumentListExpression(
                                                                        new VariableExpression(
                                                                                'newValue',
                                                                                ClassHelper.getUnwrapper(property.type)
                                                                        )
                                                                )
                                                        )
                                                ),
                                                new BlockStatement(
                                                        [new ExpressionStatement(
                                                                new MethodCallExpression(
                                                                        new VariableExpression(
                                                                                "\$configValueFor${property.name.capitalize()}",
                                                                                ConfigTypes.getWrappedType(property.type, isRangedType),
                                                                        ),
                                                                        'set',
                                                                        new ArgumentListExpression(
                                                                                new VariableExpression(
                                                                                        'newValue',
                                                                                        ClassHelper.getUnwrapper(property.type)
                                                                                )
                                                                        )
                                                                )
                                                        ) as Statement],
                                                        targetClassNodeScope
                                                ),
                                                new BlockStatement(
                                                        [],
                                                        targetClassNodeScope
                                                )
                                        ) as Statement] : [new ExpressionStatement(
                                                new MethodCallExpression(
                                                        new VariableExpression(
                                                                "\$configValueFor${property.name.capitalize()}",
                                                                ConfigTypes.getWrappedType(property.type, isRangedType),
                                                        ),
                                                        'set',
                                                        new ArgumentListExpression(
                                                                new VariableExpression(
                                                                        'newValue',
                                                                        ClassHelper.getUnwrapper(property.type)
                                                                )
                                                        )
                                                )
                                        ) as Statement],
                                        targetClassNodeScope
                                )
                        )
                )

                // make the property `private static final` to force Groovy to use the get() and set() methods
                property.modifiers = ACC_PRIVATE | ACC_STATIC | ACC_FINAL
            }
        }



        /**
         * private static final ForgeConfigSpec $configSpec = configBuilder.build()
         */
        targetClassNode.addField(
                new FieldNode(
                        '$configSpec',
                        ACC_PUBLIC | ACC_STATIC | ACC_FINAL,
                        ClassHelper.make(ForgeConfigSpec),
                        targetClassNode,
                        new MethodCallExpression(
                                new VariableExpression(configBuilderVariableName),
                                new ConstantExpression('build'),
                                ArgumentListExpression.EMPTY_ARGUMENTS
                        )
                )
        )

        /**
         * Assuming the modId associated with the config data class is "TestMod"...
         * static {
         *     ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, $configSpec, 'testmod.toml')
         * }
         */
        targetClassNode.addStaticInitializerStatements([
                new ExpressionStatement(
                        new MethodCallExpression(
                                new MethodCallExpression(
                                        new ClassExpression(ClassHelper.make(ModLoadingContext)),
                                        'get',
                                        ArgumentListExpression.EMPTY_ARGUMENTS
                                ),
                                'registerConfig',
                                new ArgumentListExpression(
                                        new PropertyExpression(
                                                new ClassExpression(ClassHelper.make(ModConfig.Type)),
                                                modConfigType.name()
                                        ),
                                        new VariableExpression('$configSpec'),

                                        modId == '$unknown' ? new GStringExpression(
                                                // if the modId is missing from the config annotation, use the class' module name to determine the modId
                                                '',
                                                [
                                                        new ConstantExpression(''),
                                                        new ConstantExpression('-' + modConfigType.name().toLowerCase() + '.toml')
                                                ],
                                                [
                                                        new MethodCallExpression(
                                                                new MethodCallExpression(
                                                                        VariableExpression.THIS_EXPRESSION,
                                                                        new ConstantExpression('getModule'),
                                                                        ArgumentListExpression.EMPTY_ARGUMENTS
                                                                ),
                                                                new ConstantExpression('getName'),
                                                                ArgumentListExpression.EMPTY_ARGUMENTS
                                                        ) as Expression
                                                ]
                                        ) : new ConstantExpression(
                                                // the modId is defined in the config annotation, so let's use it
                                                modId.toLowerCase() + '-' + modConfigType.name().toLowerCase() + '.toml'
                                        )
                                )
                        )
                ) as Statement
        ], false)
    }
}
