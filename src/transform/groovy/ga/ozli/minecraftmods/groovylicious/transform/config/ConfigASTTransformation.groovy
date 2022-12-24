package ga.ozli.minecraftmods.groovylicious.transform.config

import com.google.common.base.Predicates
import com.google.common.base.Suppliers
import com.matyrobbrt.gml.transform.api.ModRegistry
import com.matyrobbrt.gml.transform.gmods.GModASTTransformer
import ga.ozli.minecraftmods.groovylicious.transform.PojoTransformUtils
import ga.ozli.minecraftmods.groovylicious.transform.TransformUtils
import ga.ozli.minecraftmods.groovylicious.transform.TransformTypes
import groovy.transform.CompileStatic
import groovy.transform.Memoized
import groovyjarjarasm.asm.MethodVisitor
import net.minecraftforge.common.ForgeConfigSpec
import net.minecraftforge.fml.ModLoadingContext
import net.minecraftforge.fml.config.ModConfig
import org.codehaus.groovy.ast.*
import org.codehaus.groovy.ast.expr.*
import org.codehaus.groovy.ast.tools.GeneralUtils
import org.codehaus.groovy.ast.tools.GenericsUtils
import org.codehaus.groovy.control.CompilePhase
import org.codehaus.groovy.control.SourceUnit
import org.codehaus.groovy.transform.AbstractASTTransformation
import org.codehaus.groovy.transform.GroovyASTTransformation
import org.objectweb.asm.Type

import javax.annotation.Nullable
import java.util.regex.Matcher

import static org.objectweb.asm.Opcodes.*

@CompileStatic
@GroovyASTTransformation(phase = CompilePhase.CANONICALIZATION)
class ConfigASTTransformation extends AbstractASTTransformation {

    private static final boolean DEBUG = false

    private static final ClassNode CONFIG_BUILDER_TYPE = ClassHelper.make(ForgeConfigSpec.Builder)
    private static final ClassNode SUPPLIERS_TYPE = ClassHelper.make(Suppliers)
    private static final ClassNode PREDICATES_TYPE = ClassHelper.make(Predicates)
    private static final ClassNode CONFIG_SPEC_TYPE = ClassHelper.make(ForgeConfigSpec)

    ModConfig.Type configType
    String modId
    ClassNode configDataClass

    @Override
    void visit(ASTNode[] nodes, SourceUnit source) {
        init(nodes, source) // ensure that nodes is [AnnotationNode, AnnotatedNode]

        final AnnotationNode configAnnotation = nodes[0] as AnnotationNode
        final AnnotatedNode targetNode = nodes[1] as AnnotatedNode

        // make sure the @Config annotation is only applied to classes
        if (targetNode instanceof ClassNode) {
            configDataClass = targetNode as ClassNode
        } else {
            addError("The ${configAnnotation.classNode.name} annotation can only be applied to classes.", targetNode)
            return
        }

        // make sure the @Config annotation isn't applied to empty classes
        if (configDataClass.properties.isEmpty() && !configDataClass.innerClasses.hasNext()) {
            addError("Unable to detect any properties or sub-classes inside class '${configDataClass.name}' annotated with '${configAnnotation.classNode.name}'", configDataClass)
            return
        }

        // get the configType and modId from the annotation
        configType = getMemberConfigType(configAnnotation, configDataClass)
        modId = getMemberStringValue(configAnnotation, 'modId', ModRegistry.getData(configDataClass.packageName)?.modId())

        if (DEBUG) println SV(configType, modId)

        // @Config dataclasses need an init() method for classloading itself and its inner classes (config groups)
        // Check if a compatible one already exists...
        final boolean hasExplicitRootInitMethod
        @Nullable MethodNode rootInitMethod = configDataClass.methods.find { method ->
            method.returnType == ClassHelper.VOID_TYPE && method.isStatic() && method.name == 'init'
        }
        // ...if it doesn't, make one and add it to the configDataClass
        if (rootInitMethod === null) {
            hasExplicitRootInitMethod = false
            rootInitMethod = TransformUtils.addStaticMethod(
                    targetClassNode: configDataClass,
                    methodName: 'init'
            )

            // If we needed to generate our own init method, we'll probably need to handle calling it ourselves too...
            // Let's see if any of the outer class is annotated with @GMod.
            boolean foundModMainClass = false
            if (DEBUG) println SV(configDataClass.outerClasses)
            if (!configDataClass.outerClasses.empty) {
                configDataClass.outerClasses.each { outerClass ->
                    if (DEBUG) println SV(outerClass)
                    final boolean isModMainClass = outerClass.annotations*.classNode.find { it == TransformTypes.MOD_TYPE }

                    // We found an outer class annotated with @GMod, so we know that this configDataClass is inside
                    // the Mod's main class and can add a static { configDataClass.init() } to it
                    if (isModMainClass) {
                        foundModMainClass = true
                        outerClass.addStaticInitializerStatements([
                                GeneralUtils.stmt(
                                        GeneralUtils.callX(new ClassExpression(configDataClass), 'init')
                                )
                        ], false)
                    }
                }
            }
            if (!foundModMainClass) {
                // Looks like the @GMod is in a different file, let's register a transform to the
                // GModASTTransformer to add a static { configDataClass.init() } to the Mod's main class
                if (DEBUG) println "Adding transform to @GMod"
                GModASTTransformer.registerTransformer(modId) { ClassNode modClass, AnnotationNode modAnnotation, SourceUnit sourceUnit ->
                    if (DEBUG) println "Adding a call to ${configDataClass.nameWithoutPackage}'s init() method from ${modClass.name}"
                    modClass.addStaticInitializerStatements([
                            GeneralUtils.stmt(
                                    GeneralUtils.callX(new ClassExpression(configDataClass), 'init')
                            )
                    ], false)
                }
            }
        } else {
            hasExplicitRootInitMethod = true
        }

        // loop through all the properties in the configDataClass and setup the relevant config initializers, getters and setters
        configDataClass.properties.each { property ->
            if (DEBUG) println "\n${configDataClass.name}.${SV(property.name)}"
            if (!TransformUtils.shouldExclude(property)) {
                generateConfigValue(configDataClass, property)
            }
        }

        // Inner static dataclasses in a configDataClass are considered config groups
        configDataClass.innerClasses.each { configGroup ->
            if (!TransformUtils.shouldExclude(configGroup)) {
                generateConfigGroup(configGroup, configDataClass)
            }
        }

        // If there's an explicitly defined ForgeConfigSpec field, let's remake it
        @Nullable String configSpecFieldString = configDataClass.fields.find {
            it.type == CONFIG_SPEC_TYPE && it.isStatic()
        }?.name
        if (configSpecFieldString === null) {
            configSpecFieldString = '$configSpec'
        } else {
            configDataClass.removeField(configSpecFieldString)
        }

        // @Generated
        // public static final ForgeConfigSpec $configSpec = $configBuilder.build()
        configSpecFieldString = TransformUtils.addField(
                targetClassNode: configDataClass,
                fieldName: configSpecFieldString,
                modifiers: ACC_PUBLIC | ACC_STATIC | ACC_FINAL,
                type: CONFIG_SPEC_TYPE,
                initialValue: GeneralUtils.callX(configBuilderVariable, 'build')
        ).name

        if (!hasExplicitRootInitMethod) {
            // static {
            //     ModLoadingContext.get().registerConfig(configType, $configSpec, modId)
            // }
            configDataClass.addStaticInitializerStatements([
                    GeneralUtils.stmt(
                            GeneralUtils.callX(
                                    GeneralUtils.callX(
                                            new ClassExpression(ClassHelper.make(ModLoadingContext)),
                                            'get'
                                    ),
                                    'registerConfig',
                                    GeneralUtils.args(
                                            GeneralUtils.propX(
                                                    new ClassExpression(ClassHelper.make(ModConfig.Type)),
                                                    configType.name()
                                            ),
                                            new VariableExpression(configSpecFieldString, CONFIG_SPEC_TYPE),
                                            GeneralUtils.constX(modId.toLowerCase() + '-' + configType.name().toLowerCase() + '.toml')
                                    )
                            )
                    )
            ], false)
        }
    }

    static ModConfig.Type getMemberConfigType(AnnotationNode annotation, ClassNode annotatedClass) {
        final Expression member = annotation.getMember('value')
        if (member instanceof PropertyExpression) {
            final Expression property = (member as PropertyExpression).property
            if (property !== null) {
                return ModConfig.Type.valueOf((property as ConstantExpression).value as String)
            }
        } else {
            final String className = annotatedClass.nameWithoutPackage.split('$').last()

            if (className.contains 'Client') return ModConfig.Type.CLIENT
            else if (className.contains 'Server') return ModConfig.Type.SERVER
        }

        return ModConfig.Type.COMMON
    }

    /**
     * Gets a VariableExpression that points to the explicitly declared config builder field of the configDataClass.<br>
     * If no config builder is explicitly declared in the configDataClass, one is created and added to the class, with
     * the VariableExpression pointing to that.
     * @return A VariableExpression that points to the config builder field the configDataClass.
     */
    @Memoized
    VariableExpression getConfigBuilderVariable() {
        // First, let's check if a ForgeConfigSpec.Builder field has been explicitly declared and use it if so.
        @Nullable String configBuilderFieldName = configDataClass.fields.find {
            it.type == CONFIG_BUILDER_TYPE && it.isStatic()
        }?.name

        if (configBuilderFieldName === null) {
            // If not, we'll create our own and add it to the configDataClass.
            configBuilderFieldName = '$configBuilder'

            /* Adds this field to the start of the configDataClass:
             * @Generated
             * private static final ForgeConfigSpec.Builder $configBuilder = new ForgeConfigSpec.Builder();
             *
             * Note that generated field names are prepended with '$' to avoid possible conflicts with other
             * declared fields.
             */
            TransformUtils.addConstant(
                    targetClassNode: configDataClass,
                    fieldName: '$configBuilder',
                    type: CONFIG_BUILDER_TYPE,
                    initialValue: new ConstructorCallExpression(
                            CONFIG_BUILDER_TYPE,
                            ArgumentListExpression.EMPTY_ARGUMENTS
                    )
            )
        }

        return new VariableExpression(configBuilderFieldName, CONFIG_BUILDER_TYPE)
    }

    MethodCallExpression getConfigBuilderWithComments(List<Expression> configComments) {
        if (DEBUG) println SV(configComments)
        return GeneralUtils.callX(
                configBuilderVariable,
                'comment',
                GeneralUtils.args(configComments)
        )
    }

    void generateConfigValue(ClassNode targetClassNode, PropertyNode property) {
        if (property.type == CONFIG_BUILDER_TYPE || property.type == CONFIG_SPEC_TYPE) return // don't generate config values for the ForgeConfigSpec.Builder

        // make the property private static final to force Groovy to use the get() and set() methods
        property.modifiers = TransformUtils.CONSTANT_MODIFIERS

        // calculate the capitalized property name once and reuse for performance
        final String capitalizedPropertyName = property.name.capitalize()

        // ensure a consistent propertyType is used for the config value (e.g. if it's defined as Integer, the
        // getters and setters should return and accept Integer rather than a mix of int and Integer)
        ClassNode propertyType
        if (ClassHelper.isPrimitiveType(property.type)) propertyType = ClassHelper.getUnwrapper(property.type)
        else propertyType = ClassHelper.getWrapper(property.type)

        VariableScope targetClassNodeScope = new VariableScope()
        targetClassNodeScope.classScope = targetClassNode

        // Get the ForgeConfigSpec config value type for the property
        ConfigTypes.ConfigValueType configValueType = ConfigTypes.getConfigValueType(propertyType)
        ConfigTypes.Bounded boundedConfigValueType = null
        boolean isBounded
        if (configValueType instanceof ConfigTypes.Bounded) {
            boundedConfigValueType = configValueType as ConfigTypes.Bounded
            isBounded = true
        } else if (configValueType instanceof ConfigTypes.Unbounded) {
            isBounded = false
        } else {
            addError("Unsupported config value type: ${SV(propertyType, configValueType)}", property)
            return
        }

        boolean isList = false
        if (property.type == TransformTypes.LIST_TYPE) {
            final gType = property.type.genericsTypes.size() == 0 ? ClassHelper.OBJECT_TYPE : property.type.genericsTypes[0].type
            configValueType = ConfigTypes.list(gType)
            isList = true
            propertyType = GenericsUtils.makeClassSafeWithGenerics(TransformTypes.LIST_TYPE, new GenericsType(gType))
        }

//        ClosureExpression predicate = (ClosureExpression) annotation?.members?.get('validator')
//        if (predicate !== null && !configValueType.supportsValidator()) {
//            addError("Config of type $configValueType does not support validators!", property)
//            return
//        }

        if (DEBUG) {
            println SV(configValueType)
            println SV(boundedConfigValueType)
            println SV(isBounded)
            println SV(property.groovydoc?.content)
            if (property.groovydoc != null) {
                def comment = new GroovyDocParser(property.groovydoc)
                println SV(comment.commentFreeText)
                println SV(comment.htmlFreeText)
                println SV(comment.plainText)
                println SV(comment.tags)
            }
        }

        ConstantExpression minValueBound = isBounded ? boundedConfigValueType.minValue : null
        ConstantExpression maxValueBound = isBounded ? boundedConfigValueType.maxValue : null

        // Parse the property's GroovyDoc if it exists to set the config's comment and range bounds
        // todo: move this out into its own method(?)
        boolean hasComments = false
        List<Expression> configCommentExprs = []
        if (property.groovydoc) {
            final groovyDocParser = new GroovyDocParser(property.groovydoc)
            final String configCommentStr = groovyDocParser.plainText
            hasComments = !configCommentStr?.isEmpty() && !configCommentStr.isAllWhitespace()
            configCommentStr.eachLine {
                configCommentExprs += GeneralUtils.constX(it)
            }

            final Matcher rangeMatcher = groovyDocParser.tags['range'] =~ /(?<lower>(?:\d*\.)?\d+)(?<lowerExclusive><)?\.\.(?<upperExclusive><)?(?<upper>(?:\d*\.)?\d+)/
            if (rangeMatcher.matches()) {
                String lowerStr = null
                String upperStr = null
                try {
                    lowerStr = rangeMatcher.group('lower')
                    upperStr = rangeMatcher.group('upper')
                } catch (IllegalArgumentException ignored) {}

                boolean lowerExclusive = false
                boolean upperExclusive = false
                try {
                    lowerExclusive = rangeMatcher.group('lowerExclusive') !== null
                    upperExclusive = rangeMatcher.group('upperExclusive') !== null
                } catch (IllegalArgumentException ignored) {}

                Long upperLong = null
                Long lowerLong = null
                Double upperDouble = null
                Double lowerDouble = null
                switch (propertyType) {
                    case ClassHelper.Byte_TYPE:
                    case ClassHelper.byte_TYPE:
                    case ClassHelper.Short_TYPE:
                    case ClassHelper.short_TYPE:
                    case ClassHelper.Integer_TYPE:
                    case ClassHelper.int_TYPE:
                    case ClassHelper.Long_TYPE:
                    case ClassHelper.long_TYPE:
                        upperLong = upperStr?.toLong()
                        lowerLong = lowerStr?.toLong()
                        break
                    case ClassHelper.Float_TYPE:
                    case ClassHelper.float_TYPE:
                    case ClassHelper.Double_TYPE:
                    case ClassHelper.double_TYPE:
                        upperDouble = upperStr?.toDouble()
                        lowerDouble = lowerStr?.toDouble()
                        break
                    default:
                        addError("Unsupported bounded config value type: ${SV(propertyType, configValueType)}", property)
                        return
                }

                if (DEBUG) {
                    println SV(propertyType)
                    println SV(upperLong, lowerLong)
                    println SV(upperDouble, lowerDouble)
                }

                /* todo: test all of these cases:
                 * 0..5   // [from: 0, to: 5]
                 * 0..<5  // [from: 0, to: 4]
                 * 0<..5  // [from: 1, to: 5]
                 * 0<..<5 // [from: 1, to: 4]
                 */
                final NumberRange range = new NumberRange(lowerLong ?: lowerDouble, upperLong ?: upperDouble, !lowerExclusive, !upperExclusive)
                minValueBound = GeneralUtils.constX(range.from.asType(propertyType.typeClass))
                maxValueBound = GeneralUtils.constX(range.to.asType(propertyType.typeClass))
            }
        }

        // make the appropriate underlying config value field
        // Assuming property.name == 'test' and property.type == int in this example:
        // @Generated
        // static ForgeConfigSpec.IntValue $configValueForTest = $configBuilder.defineInRange("test", 0, Integer.MIN_VALUE, Integer.MAX_VALUE)
        final configBuilder = hasComments ? getConfigBuilderWithComments(configCommentExprs) : configBuilderVariable
        TransformUtils.addField(
                targetClassNode: targetClassNode,
                fieldName: "\$configValueFor${capitalizedPropertyName}",
                modifiers: ACC_PRIVATE | ACC_STATIC | ACC_FINAL,
                type: configValueType.classNode,
                conditionalInitialValue: { ->
                    if (isList) {
                        GeneralUtils.callX(
                                configBuilder, 'defineListAllowEmpty',
                                TransformUtils.conditionalArgs(
                                        GeneralUtils.callX(TransformTypes.LIST_TYPE, 'of', GeneralUtils.constX(property.name)),
                                        GeneralUtils.callX(SUPPLIERS_TYPE, 'ofInstance', getPropertyValueOrDefault(property)),
                                        /*predicate ?: */ GeneralUtils.callX(PREDICATES_TYPE, 'alwaysTrue')
                                ))
                    } else {
                        GeneralUtils.callX(
                                configBuilder, isBounded ? 'defineInRange' : 'define',
                                TransformUtils.conditionalArgs(
                                        GeneralUtils.constX(property.name),
                                        getPropertyValueOrDefault(property),
                                        isBounded ? minValueBound : null,
                                        isBounded ? maxValueBound : null,
                                        //predicate
                                )
                        )
                    }
                } as Closure<Expression>
        )

        // make the appropriate getter method
        // Assuming property.name == 'test' and propertyType == int in this example:
        // @Generated
        // public static int getTest() {
        //     return $configValueForTest.get();
        // }
        final configFieldType = Type.getObjectType(PojoTransformUtils.getInternalName(configValueType.classNode))
        final String configFieldDesc = configFieldType.descriptor
        final bytecode = { MethodVisitor it ->
            it.visitCode()
            it.visitFieldInsn(GETSTATIC, PojoTransformUtils.getInternalName(targetClassNode), "\$configValueFor${capitalizedPropertyName}", configFieldDesc)
            it.visitMethodInsn(INVOKEVIRTUAL, configFieldType.internalName, 'get', '()Ljava/lang/Object;', false)
        }
        final getterCode = PojoTransformUtils.cast(propertyType, bytecode)
        TransformUtils.addStaticMethod(
                targetClassNode: targetClassNode,
                methodName: "get${capitalizedPropertyName}",
                returnType: propertyType,
                code: getterCode
        )

        // make the appropriate setter method
        // Assuming property.name == 'test' and propertyType == int in this example:
        // @Generated
        // public static void setTest(int newValue) {
        //     $configValueForTest.set(newValue);
        // }
        TransformUtils.addStaticMethod(
                targetClassNode: targetClassNode,
                methodName: "set${capitalizedPropertyName}",
                parameters: new Parameter[] { new Parameter(propertyType, 'newValue') },
                conditionalCode: { ->
                    GeneralUtils.stmt(GeneralUtils.bytecodeX {
                        it.visitFieldInsn(GETSTATIC, PojoTransformUtils.getInternalName(targetClassNode), "\$configValueFor${capitalizedPropertyName}", configFieldDesc)
                        final paramType = PojoTransformUtils.getType(propertyType)
                        it.visitVarInsn(paramType.getOpcode(ILOAD), 0)
                        if (ClassHelper.isPrimitiveType(propertyType)) {
                            final boxed = ClassHelper.getWrapper(propertyType)
                            it.visitMethodInsn(INVOKESTATIC, PojoTransformUtils.getInternalName(boxed), 'valueOf', Type.getMethodDescriptor(PojoTransformUtils.getType(boxed), PojoTransformUtils.getType(propertyType)), false)
                        }
                        it.visitMethodInsn(INVOKEVIRTUAL, PojoTransformUtils.getInternalName(configValueType.classNode), 'set', '(Ljava/lang/Object;)V', false)
                        it.visitInsn(RETURN)
                    })
                }
        )
    }

    void generateConfigGroup(ClassNode groupClass, ClassNode outerClass) {
        if (groupClass.modifiers != (groupClass.modifiers | ACC_STATIC)) {
            addError('Non-static config groups are not supported', groupClass)
            return
        }

        // Make our own init() method if one isn't already explicitly declared
        groupClass.methods.find { method ->
            method.returnType == ClassHelper.VOID_TYPE && method.name == 'init' && method.isStatic()
        } ?: TransformUtils.addStaticMethod(targetClassNode: groupClass, methodName: 'init')

        /*
         * @Config
         * static class Config {
         *     static {
         *         $configBuilder.push("GroupName")
         *         GroupName.init()
         *         $configBuilder.pop()
         *     }
         *
         *     static class GroupName {
         *         static void init() {}
         *     }
         * }
         */
        outerClass.addStaticInitializerStatements([
                // $configBuilder.push 'groupClass'
                GeneralUtils.stmt(GeneralUtils.callX(
                        configBuilderVariable,
                        'push',
                        GeneralUtils.args(GeneralUtils.constX(groupClass.nameWithoutPackage.split(/\$/).last()))
                )),

                // groupClass.init()
                // note: unlike the root config class' init() method that needs to be manually called if
                // explicitly declared, the group class' init() method is called automatically regardless
                GeneralUtils.stmt(GeneralUtils.callX(new ClassExpression(groupClass), 'init')),

                // $configBuilder.pop()
                GeneralUtils.stmt(GeneralUtils.callX(configBuilderVariable, 'pop'))
        ], false)

        groupClass.properties.each { property ->
            if (DEBUG) println "\n${groupClass.nameWithoutPackage.split(/\$/).last()}.${property.name}"
            generateConfigValue(groupClass, property)
        }

        groupClass.innerClasses.each { nestedConfigGroup ->
            generateConfigGroup(nestedConfigGroup, groupClass)
        }
    }

    static Expression getPropertyValueOrDefault(PropertyNode property) {
        if (property.field.initialValueExpression === null) {
            if (property.type == ClassHelper.STRING_TYPE || property.type == ClassHelper.GSTRING_TYPE) {
                return ConstantExpression.EMPTY_STRING
            } else if (property.type == TransformTypes.LIST_TYPE) {
                return GeneralUtils.callX(TransformTypes.LIST_TYPE, 'of')
            }
        }
        return property.field.initialValueExpression
    }

}
