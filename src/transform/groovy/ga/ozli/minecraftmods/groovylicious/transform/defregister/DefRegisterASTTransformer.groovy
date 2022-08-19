package ga.ozli.minecraftmods.groovylicious.transform.defregister

import com.matyrobbrt.gml.transform.api.ModRegistry
import com.matyrobbrt.gml.transform.gmods.GModASTTransformer
import ga.ozli.minecraftmods.groovylicious.transform.TransformUtils
import groovy.transform.CompileStatic
import groovyjarjarasm.asm.Handle
import groovyjarjarasm.asm.Type as JarType
import net.minecraft.core.Registry
import net.minecraft.resources.ResourceKey
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.ForgeRegistry
import net.minecraftforge.registries.IForgeRegistry
import net.minecraftforge.registries.RegistryObject
import org.codehaus.groovy.ast.*
import org.codehaus.groovy.ast.expr.ClassExpression
import org.codehaus.groovy.ast.expr.ClosureExpression
import org.codehaus.groovy.ast.expr.ConstantExpression
import org.codehaus.groovy.ast.expr.ListExpression
import org.codehaus.groovy.ast.expr.PropertyExpression
import org.codehaus.groovy.ast.stmt.BlockStatement
import org.codehaus.groovy.ast.stmt.ExpressionStatement
import org.codehaus.groovy.ast.tools.GeneralUtils
import org.codehaus.groovy.ast.tools.GenericsUtils
import org.codehaus.groovy.control.SourceUnit
import org.codehaus.groovy.transform.AbstractASTTransformation
import org.codehaus.groovy.transform.GroovyASTTransformation
import org.objectweb.asm.Opcodes
import org.objectweb.asm.Type

import java.lang.reflect.Modifier
import java.util.function.Predicate
import java.util.function.Supplier

@CompileStatic
@GroovyASTTransformation
final class DefRegisterASTTransformer extends AbstractASTTransformation {
    public static final ClassNode REGISTRATION_NAME_TYPE = ClassHelper.make(RegistrationName)
    public static final String REGISTER_DESC = Type.getMethodDescriptor(Type.getType(RegistryObject), Type.getType(String), Type.getType(Supplier))
    public static final ClassNode RESOURCE_KEY_TYPE = ClassHelper.make(ResourceKey)
    public static final ClassNode REGISTRY_TYPE = ClassHelper.make(Registry)
    public static final ClassNode FORGE_REGISTRY_TYPE = ClassHelper.make(IForgeRegistry)
    public static final ClassNode DEFERRED_REGISTER_TYPE = ClassHelper.make(DeferredRegister)
    @Override
    void visit(ASTNode[] nodes, SourceUnit source) {
        init(nodes, source) // ensure that nodes is [AnnotationNode, AnnotatedNode]

        final AnnotationNode annotation = nodes[0] as AnnotationNode
        final AnnotatedNode targetNode = nodes[1] as AnnotatedNode

        if (targetNode instanceof ClassNode) {
            transformClass(annotation, targetNode)
            return
        }

        // make sure the @AutoRegister annotation is only applied to fields / classes
        if (!(targetNode instanceof FieldNode)) {
            addError("The ${annotation.classNode.name} annotation can only be applied to fields and classes.", targetNode)
            return
        }
        final targetField = (FieldNode) targetNode
        final targetClass = (ClassNode) targetField.declaringClass
        transformField(annotation, targetClass, targetField)
    }

    private void transformClass(AnnotationNode annotationNode, ClassNode targetClass) {
        final closure = annotationNode.getMember('value') as ClosureExpression
        if (closure === null) {
            addError('Class-level @AutoRegister requires providing the registries in a closure!', targetClass)
            return
        }
        final expression = (((BlockStatement) closure.code).getStatements().find() as ExpressionStatement).expression

        if (expression instanceof ListExpression) {
            expression.expressions.each {
                final val = it as PropertyExpression
                final property = (val.property as ConstantExpression).value as String
                final registryField = (val.getObjectExpression() as ClassExpression).type.getField(property)
                generateDR(annotationNode, targetClass, registryField)
            }
        } else {
            final val = expression as PropertyExpression
            final declaringClass = (val.getObjectExpression() as ClassExpression).type
            final property = (val.property as ConstantExpression).value as String
            final registryField = declaringClass.getField(property)
            generateDR(annotationNode, targetClass, registryField)
        }
    }

    private void generateDR(final AnnotationNode annotationNode, final ClassNode targetClass, final FieldNode registryField) {
        if (registryField.type == RESOURCE_KEY_TYPE) {
            makeResourceKey(annotationNode, targetClass, registryField)
        } else if (registryField.type.isDerivedFrom(REGISTRY_TYPE)) {
            makeRegistry(annotationNode, targetClass, registryField)
        } else if (GeneralUtils.isOrImplements(registryField.type, FORGE_REGISTRY_TYPE)) {
            makeForgeRegistry(annotationNode, targetClass, registryField)
        } else {
            addError("No known way of representing registry from ${registryField.owner.name}.${registryField.name}", registryField)
        }
    }

    private void makeResourceKey(final AnnotationNode annotation, final ClassNode targetClass, final FieldNode registryField) {
        // ResourceKey<Registry<T>>
        final type = registryField.type.genericsTypes[0].type.genericsTypes[0].type
        final modId = ModRegistry.getData(targetClass.packageName)?.modId()
        if (modId === null) {
            addError('Could not determine modId for AutoRegister', targetClass)
            return
        }
        final defRegisterField = TransformUtils.addField(
                fieldName: registryField.name.replace('_REGISTRY', 'S').toUpperCase(Locale.ROOT),
                targetClassNode: targetClass,
                type: GenericsUtils.makeClassSafeWithGenerics(DeferredRegister, type),
                modifiers: Opcodes.ACC_STATIC | Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL,
                initialValue: GeneralUtils.callX(
                        DEFERRED_REGISTER_TYPE, 'create', GeneralUtils.args(
                        GeneralUtils.fieldX(registryField), GeneralUtils.constX(modId)
                    )
                )
        )
        transformField(annotation, targetClass, defRegisterField)
    }

    private void makeForgeRegistry(final AnnotationNode annotation, final ClassNode targetClass, final FieldNode registryField) {
        // ForgeRegistry<T>
        final type = registryField.type.genericsTypes[0].type
        final modId = ModRegistry.getData(targetClass.packageName)?.modId()
        if (modId === null) {
            addError('Could not determine modId for AutoRegister', targetClass)
            return
        }
        final defRegisterField = TransformUtils.addField(
                fieldName: registryField.name.toUpperCase(Locale.ROOT),
                targetClassNode: targetClass,
                type: GenericsUtils.makeClassSafeWithGenerics(DeferredRegister, type),
                modifiers: Opcodes.ACC_STATIC | Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL,
                initialValue: GeneralUtils.callX(
                        DEFERRED_REGISTER_TYPE, 'create', GeneralUtils.args(
                        GeneralUtils.fieldX(registryField), GeneralUtils.constX(modId)
                    )
                )
        )
        transformField(annotation, targetClass, defRegisterField)
    }

    private void makeRegistry(final AnnotationNode annotationNode, final ClassNode targetClass, final FieldNode registryField) {
        // Registry<T>
        final type = registryField.type.genericsTypes[0].type
        final modId = ModRegistry.getData(targetClass.packageName)?.modId()
        if (modId === null) {
            addError('Could not determine modId for AutoRegister', targetClass)
            return
        }
        final defRegisterField = TransformUtils.addField(
                fieldName: (registryField.name + 'S').toUpperCase(Locale.ROOT),
                targetClassNode: targetClass,
                type: GenericsUtils.makeClassSafeWithGenerics(DeferredRegister, type),
                modifiers: Opcodes.ACC_STATIC | Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL,
                initialValue: GeneralUtils.callX(
                        DEFERRED_REGISTER_TYPE, 'create', GeneralUtils.args(
                        GeneralUtils.callX(GeneralUtils.fieldX(registryField), 'key'), GeneralUtils.constX(modId)
                    )
                )
        )
        transformField(annotationNode, targetClass, defRegisterField)
    }

    private void transformField(AnnotationNode annotation, ClassNode targetClass, FieldNode targetField) {
        if (targetClass.properties.isEmpty())
            addError("Unable to detect any properties inside class '${targetClass.name}' annotated with '${annotation.classNode.name}'", targetClass)
        if (!targetField.isStatic()) {
            addError('@AutoRegister can only be applied to static fields.', targetField)
            return
        }

        final baseType = targetField.type.genericsTypes.size() == 0 ? ClassHelper.OBJECT_TYPE : targetField.type.genericsTypes[0].type

        Predicate<ClassNode> predicate
        if (baseType.isInterface()) {
            predicate = { ClassNode it -> GeneralUtils.isOrImplements(it, baseType) }
        } else {
            predicate = { ClassNode it -> it.isDerivedFrom(baseType) }
        }

        targetClass.properties.each {
            if (predicate.test(it.type)) {
                register(baseType, targetField, targetClass, it)
            }
        }

        if (getMemberValue(annotation, 'includeInnerClasses')) {
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
            }
        }

        if (annotation.members.containsKey('registerToBus') && !getMemberValue(annotation, 'registerToBus')) return
        GModASTTransformer.registerTransformer(ModRegistry.getData(targetClass.packageName)?.modId()) { ClassNode classNode, AnnotationNode annotationNode, SourceUnit source ->
            classNode.addObjectInitializerStatements(GeneralUtils.stmt(GeneralUtils.callX(
                    GeneralUtils.fieldX(targetField), 'register', GeneralUtils.callX(GeneralUtils.varX('this'), 'getModBus')
            )))
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

    private String getRegName(final PropertyNode propertyNode) {
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

    private static String getInternalName(ClassNode classNode) {
        return classNode.name.replace('.' as char, '/' as char)
    }
}
