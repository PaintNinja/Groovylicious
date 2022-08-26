package ga.ozli.minecraftmods.groovylicious.transform.registroid

import com.matyrobbrt.gml.transform.api.ModRegistry
import com.matyrobbrt.gml.transform.gmods.GModASTTransformer
import ga.ozli.minecraftmods.groovylicious.transform.TransformUtils
import groovy.transform.CompileStatic
import groovy.transform.Generated
import groovyjarjarasm.asm.Handle
import groovyjarjarasm.asm.Type as JarType
import net.minecraft.core.Registry
import net.minecraft.resources.ResourceKey
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.IForgeRegistry
import net.minecraftforge.registries.RegistryObject
import org.codehaus.groovy.ast.*
import org.codehaus.groovy.ast.expr.*
import org.codehaus.groovy.ast.stmt.BlockStatement
import org.codehaus.groovy.ast.stmt.ExpressionStatement
import org.codehaus.groovy.ast.tools.GeneralUtils
import org.codehaus.groovy.ast.tools.GenericsUtils
import org.codehaus.groovy.control.CompilePhase
import org.codehaus.groovy.control.SourceUnit
import org.codehaus.groovy.transform.AbstractASTTransformation
import org.codehaus.groovy.transform.GroovyASTTransformation
import org.codehaus.groovy.transform.TransformWithPriority
import org.objectweb.asm.Opcodes
import org.objectweb.asm.Type

import java.util.function.Predicate
import java.util.function.Supplier

@CompileStatic
@GroovyASTTransformation(phase = CompilePhase.SEMANTIC_ANALYSIS)
final class RegistroidASTTransformer extends AbstractASTTransformation implements TransformWithPriority {
    public static final ClassNode REGISTRATION_NAME_TYPE = ClassHelper.make(RegistrationName)
    public static final String REGISTER_DESC = Type.getMethodDescriptor(Type.getType(RegistryObject), Type.getType(String), Type.getType(Supplier))
    public static final ClassNode RESOURCE_KEY_TYPE = ClassHelper.make(ResourceKey)
    public static final ClassNode REGISTRY_TYPE = ClassHelper.make(Registry)
    public static final ClassNode FORGE_REGISTRY_TYPE = ClassHelper.make(IForgeRegistry)
    public static final ClassNode DEFERRED_REGISTER_TYPE = ClassHelper.make(DeferredRegister)
    public static final ClassNode ADDON_CLASS_TYPE = ClassHelper.make(RegistroidAddonClass)
    @Override
    void visit(ASTNode[] nodes, SourceUnit source) {
        init(nodes, source) // ensure that nodes is [AnnotationNode, AnnotatedNode]

        final AnnotationNode annotation = nodes[0] as AnnotationNode
        final AnnotatedNode targetNode = nodes[1] as AnnotatedNode

        if (targetNode instanceof ClassNode) {
            transformClass(annotation, targetNode, targetNode.annotations.stream().flatMap {
                it.classNode.annotations.stream()
            }.filter { it.classNode == ADDON_CLASS_TYPE }
            .map { Class.forName(getMemberClassValue(it, 'value').name, true, getClass().classLoader) }
            .map { it.getDeclaredConstructor().newInstance() as RegistroidAddon }.toList())
            return
        }

        // make sure the annotation is only applied to fields / classes
        if (!(targetNode instanceof FieldNode)) {
            addError("The ${annotation.classNode.name} annotation can only be applied to fields and classes.", targetNode)
            return
        }
        final targetField = (FieldNode) targetNode
        final targetClass = (ClassNode) targetField.declaringClass
        transformField(annotation, targetClass, targetField)
    }

    private void transformClass(AnnotationNode annotationNode, ClassNode targetClass, List<RegistroidAddon> addons) {
        final closure = annotationNode.getMember('value') as ClosureExpression
        Expression expression
        if (closure === null) {
            // One may use it for addons only
            expression = new ListExpression()
        } else {
            expression = (((BlockStatement) closure.code).getStatements().find() as ExpressionStatement).expression
        }
        final existingDRs = targetClass.fields.stream()
            .filter { it.type == DEFERRED_REGISTER_TYPE }
            .map { it.type.genericsTypes[0].type }
            .toList()

        final modId = ModRegistry.getData(targetClass.packageName)?.modId()
        if (modId === null) {
            addError('Could not determine modId for AutoRegister', targetClass)
            return
        }

        List<FieldNode> drFields = []
        if (expression instanceof ListExpression) {
            expression.expressions.each {
                final val = it as PropertyExpression
                final property = (val.property as ConstantExpression).value as String
                final registryField = (val.getObjectExpression() as ClassExpression).type.getField(property)
                drFields.push(generateDR(targetClass, registryField, modId))
            }
        } else {
            final val = expression as PropertyExpression
            final declaringClass = (val.getObjectExpression() as ClassExpression).type
            final property = (val.property as ConstantExpression).value as String
            final registryField = declaringClass.getField(property)
            drFields.push(generateDR(targetClass, registryField, modId))
        }

        addons.stream().flatMap {it.requiredRegistries.stream()}.forEach {
            final declaringClass = (it.getObjectExpression() as ClassExpression).type
            final property = (it.property as ConstantExpression).value as String
            final registryField = declaringClass.getField(property)
            final dr = generateDR(targetClass, registryField, modId, false)
            final drGenericType = dr.type.genericsTypes[0].type
            if (!drFields.any { it.type.genericsTypes[0].type == drGenericType || it.name == dr.name } && !existingDRs.contains(drGenericType)) {
                drFields.push(dr)
                targetClass.addField(dr)
            }
        }

        final Map<ClassNode, String> drToModId = [:]
        drFields.each {
            if (it.initialValueExpression instanceof StaticMethodCallExpression) {
                final args = (it.initialValueExpression as StaticMethodCallExpression).arguments
                if (args instanceof ArgumentListExpression && args.size() == 2 && args.getExpression(1) instanceof ConstantExpression) {
                    final val = (args.getExpression(1) as ConstantExpression).value
                    if (val instanceof String) {
                        drToModId[it.type.genericsTypes[0].type] = val
                    }
                }
            }
        }

        List.copyOf(targetClass.properties).each { PropertyNode prop ->
            addons.each {
                final found = it.supportedTypes.find { TransformUtils.isSubclass(prop.type, it) }
                if (found !== null) {
                    it.process(annotationNode, targetClass, prop, this, recursivelyFind(found, drToModId))
                }
            }
        }

        if (getMemberValue(annotationNode, 'includeInnerClasses')) {
            targetClass.innerClasses.each {
                registerInnerAddons(annotationNode, targetClass, it, drToModId, addons)
            }
        }

        drFields = drFields.stream().filter { it !== null }.toList()
        if (hasDuplicates(drFields, targetClass)) return
        drFields.each {
            transformField(annotationNode, targetClass, it)
        }
    }

    private void registerInnerAddons(final AnnotationNode annotationNode, final ClassNode targetClass, final InnerClassNode innerClass,
                                     final Map<ClassNode, String> drToModIds, final List<RegistroidAddon> addons) {
        List.copyOf(innerClass.properties).each { PropertyNode prop ->
            addons.each {
                final found = it.supportedTypes.find { TransformUtils.isSubclass(prop.type, it) }
                if (found !== null) {
                    it.process(annotationNode, targetClass, prop, this, recursivelyFind(found, drToModIds))
                }
            }
        }
        innerClass.innerClasses.each { registerInnerAddons(annotationNode, targetClass, it, drToModIds, addons) }
    }

    private boolean hasDuplicates(List<FieldNode> fields, ClassNode targetClass) {
        final Set<ClassNode> types = new HashSet<>()
        for (final field : fields) {
            // DeferredRegister<T>
            final drType = field.type.genericsTypes[0].type
            if (drType in types) {
                addError("Registroid ambiguity: Found multiple DeferredRegisters pointing to the same type, ${drType.name}", targetClass)
                return true
            }
            types.add(drType)
        }
        return false
    }

    private FieldNode generateDR(final ClassNode targetClass, final FieldNode registryField, final String modId, boolean addToClass = true) {
        if (registryField.type == RESOURCE_KEY_TYPE) {
            makeResourceKey(targetClass, registryField, modId, addToClass)
        } else if (registryField.type.isDerivedFrom(REGISTRY_TYPE)) {
            makeRegistry(targetClass, registryField, modId, addToClass)
        } else if (GeneralUtils.isOrImplements(registryField.type, FORGE_REGISTRY_TYPE)) {
            makeForgeRegistry(targetClass, registryField, modId, addToClass)
        } else {
            addError("No known way of representing registry from ${registryField.owner.name}.${registryField.name}", registryField)
            return null
        }
    }

    private static FieldNode makeResourceKey(final ClassNode targetClass, final FieldNode registryField, final String modId, boolean addToClass = true) {
        // ResourceKey<Registry<T>>
        final type = registryField.type.genericsTypes[0].type.genericsTypes[0].type
        final field = new FieldNode(
                registryField.name.replace('_REGISTRY', 'S').toUpperCase(Locale.ROOT),
                Opcodes.ACC_STATIC | Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL,
                GenericsUtils.makeClassSafeWithGenerics(DeferredRegister, type), targetClass,
                GeneralUtils.callX(
                        DEFERRED_REGISTER_TYPE, 'create', GeneralUtils.args(
                        GeneralUtils.fieldX(registryField), GeneralUtils.constX(modId)
                    )
                )
        )
        field.addAnnotation(TransformUtils.GENERATED_ANNOTATION)
        if (addToClass) {
            targetClass.addField(field)
        }
        return field
    }

    private static FieldNode makeForgeRegistry(final ClassNode targetClass, final FieldNode registryField, final String modId, boolean addToClass = true) {
        // ForgeRegistry<T>
        final type = registryField.type.genericsTypes[0].type
        final field = new FieldNode(
                registryField.name.toUpperCase(Locale.ROOT),
                Opcodes.ACC_STATIC | Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL,
                GenericsUtils.makeClassSafeWithGenerics(DeferredRegister, type),
                targetClass, GeneralUtils.callX(
                DEFERRED_REGISTER_TYPE, 'create', GeneralUtils.args(
                    GeneralUtils.fieldX(registryField), GeneralUtils.constX(modId)
                )
            )
        )
        field.addAnnotation(TransformUtils.GENERATED_ANNOTATION)
        if (addToClass) {
            targetClass.addField(field)
        }
        return field
    }

    private static FieldNode makeRegistry(final ClassNode targetClass, final FieldNode registryField, final String modId, boolean addToClass = true) {
        // Registry<T>
        final type = registryField.type.genericsTypes[0].type
        final field = new FieldNode(
                (registryField.name + 'S').toUpperCase(Locale.ROOT),
                Opcodes.ACC_STATIC | Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL,
                GenericsUtils.makeClassSafeWithGenerics(DeferredRegister, type), targetClass,
                GeneralUtils.callX(
                        DEFERRED_REGISTER_TYPE, 'create', GeneralUtils.args(
                        GeneralUtils.callX(GeneralUtils.fieldX(registryField), 'key'), GeneralUtils.constX(modId)
                    )
                )
        )
        field.addAnnotation(TransformUtils.GENERATED_ANNOTATION)
        if (addToClass) {
            targetClass.addField(field)
        }
        return field
    }

    private void transformField(AnnotationNode annotation, ClassNode targetClass, FieldNode targetField) {
        if (targetClass.properties.isEmpty())
            addError("Unable to detect any properties inside class '${targetClass.name}' annotated with '${annotation.classNode.name}'", targetClass)
        if (!targetField.isStatic()) {
            addError('@Registroid can only be applied to static fields.', targetField)
            return
        }
        if (!targetField.isPublic()) {
            final wasFinal = targetField.isFinal()
            targetField.modifiers = Opcodes.ACC_STATIC | Opcodes.ACC_PUBLIC
            if (wasFinal) {
                targetField.modifiers = targetField.modifiers | Opcodes.ACC_FINAL
            }
        }

        final baseType = targetField.type.genericsTypes.size() == 0 ? ClassHelper.OBJECT_TYPE : targetField.type.genericsTypes[0].type

        final Predicate<ClassNode> predicate = { ClassNode it -> TransformUtils.isSubclass(it, baseType) }

        targetClass.properties.each {
            if (predicate.test(it.type)) {
                register(baseType, targetField, targetClass, it)
            }
        }

        if (getMemberValue(annotation, 'includeInnerClasses')) {
            registerInners(predicate, targetClass, baseType, targetField)
        }

        if ((getMemberValue(annotation, 'registerToBus') ?: true)) {
            GModASTTransformer.registerTransformer(ModRegistry.getData(targetClass.packageName)?.modId()) { ClassNode classNode, AnnotationNode annotationNode, SourceUnit source ->
                TransformUtils.addLastCtorStatement(classNode, GeneralUtils.stmt(GeneralUtils.callX(
                        GeneralUtils.fieldX(targetField), 'register', GeneralUtils.callX(GeneralUtils.varX('this'), 'getModBus')
                )))
            }
        }
    }

    private void registerInners(final Predicate<ClassNode> predicate, final ClassNode targetClass, final ClassNode baseType, final FieldNode targetField) {
        targetClass.innerClasses.each { InnerClassNode inner ->
            inner.properties.each {
                if (predicate.test(it.type)) {
                    register(baseType, targetField, inner, it)
                }
            }
            if (!inner.methods.any { it.parameters.size() == 0 && it.name == 'init' && it.isStatic() }) {
                TransformUtils.addStaticMethod(
                        targetClassNode: inner,
                        methodName: 'init',
                )
            }

            targetClass.addStaticInitializerStatements([GeneralUtils.stmt(
                    GeneralUtils.callX(inner, 'init')
            )], false)
            inner.innerClasses.each { InnerClassNode secondInner ->
                registerInners(predicate, inner, baseType, targetField)
            }
        }
    }

    private void register(final ClassNode registryType, final FieldNode drVar, final ClassNode clazz, final PropertyNode property) {
        if (!property.isStatic()) return

        property.modifiers = TransformUtils.CONSTANT_MODIFIERS

        final targetClassNodeScope = new VariableScope()
        targetClassNodeScope.classScope = clazz
        final closure = GeneralUtils.closureX(GeneralUtils.stmt(property.initialExpression))
        closure.setVariableScope(targetClassNodeScope)

        final lambdaMethod = clazz.addMethod("autoregister\$syn\$${property.name}", Opcodes.ACC_PRIVATE | Opcodes.ACC_STATIC | Opcodes.ACC_SYNTHETIC,
            registryType, Parameter.EMPTY_ARRAY, ClassNode.EMPTY_ARRAY, GeneralUtils.returnS(property.initialExpression))

        final regObjectType = GenericsUtils.makeClassSafeWithGenerics(RegistryObject, property.type)
        final field = TransformUtils.addField(
                targetClassNode: clazz,
                fieldName: "\$registryObjectFor${property.name.capitalize()}",
                modifiers: Opcodes.ACC_PRIVATE | Opcodes.ACC_FINAL | Opcodes.ACC_STATIC,
                type: regObjectType,
                initialValue: GeneralUtils.bytecodeX(regObjectType) {
                    it.visitFieldInsn(Opcodes.GETSTATIC, getInternalName(drVar.owner), drVar.name, Type.getDescriptor(DeferredRegister))
                    it.visitLdcInsn(getRegName(property))
                    final getDesc = "()L${getInternalName(registryType)};"
                    it.visitInvokeDynamicInsn('get', '()Ljava/util/function/Supplier;',
                            new Handle(Opcodes.H_INVOKESTATIC, 'java/lang/invoke/LambdaMetafactory', 'metafactory', '(Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodHandle;Ljava/lang/invoke/MethodType;)Ljava/lang/invoke/CallSite;', false),
                            new Object[] {
                                    JarType.getType('()Ljava/lang/Object;'), new Handle(Opcodes.H_INVOKESTATIC, getInternalName(clazz), lambdaMethod.name,
                                    getDesc, false), JarType.getType(getDesc)
                            })
                    it.visitMethodInsn(Opcodes.INVOKEVIRTUAL, Type.getInternalName(DeferredRegister), 'register', REGISTER_DESC, false)
                }
        )
        clazz.removeField(property.field.name)

        property.setGetterBlock(GeneralUtils.stmt(GeneralUtils.bytecodeX(property.type) {
            it.visitFieldInsn(Opcodes.GETSTATIC, getInternalName(clazz), field.name, Type.getDescriptor(RegistryObject))
            it.visitMethodInsn(Opcodes.INVOKEVIRTUAL, Type.getInternalName(RegistryObject), 'get', '()Ljava/lang/Object;', false)
            it.visitTypeInsn(Opcodes.CHECKCAST, getInternalName(property.type))
        }))
    }

    String getRegName(final PropertyNode propertyNode) {
        final regNameOnClass = propertyNode.declaringClass.annotations.find { it.classNode == REGISTRATION_NAME_TYPE }
        final boolean alwaysApply = regNameOnClass === null ? false : getMemberValue(regNameOnClass, 'alwaysApply')
        final prefix = regNameOnClass === null ? '' : getMemberStringValue(regNameOnClass, 'value')
        final ann = propertyNode.annotations.find { it.classNode == REGISTRATION_NAME_TYPE }
        if (ann !== null) {
            final name = getMemberStringValue(ann, 'value')
            return alwaysApply ? prefix + name : name
        }
        return prefix + propertyNode.name.toLowerCase(Locale.ROOT)
    }

    @SuppressWarnings('GrMethodMayBeStatic')
    String getInitialRegName(final PropertyNode propertyNode) {
        final ann = propertyNode.annotations.find { it.classNode == REGISTRATION_NAME_TYPE }
        return ann === null ? propertyNode.name.toLowerCase(Locale.ROOT) : getMemberStringValue(ann, 'value')
    }

    private static String getInternalName(ClassNode classNode) {
        return classNode.name.replace('.' as char, '/' as char)
    }

    private static String recursivelyFind(final ClassNode type, final Map<ClassNode, String> map) {
        String val = map[type]
        if (val) return val
        if (type.superClass !== null && (val = recursivelyFind(type.superClass, map)) !== null) {
            return val
        }
        for (final iface : type.interfaces) {
            if ((val = recursivelyFind(iface, map)) !== null) return val
        }
        return null
    }

    @Override
    int priority() {
        return 10
    }
}
