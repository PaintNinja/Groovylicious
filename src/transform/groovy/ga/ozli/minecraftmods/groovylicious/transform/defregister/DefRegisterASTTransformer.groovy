package ga.ozli.minecraftmods.groovylicious.transform.defregister

import ga.ozli.minecraftmods.groovylicious.transform.TransformUtils
import groovy.transform.CompileStatic
import net.minecraftforge.registries.RegistryObject
import org.codehaus.groovy.ast.*
import org.codehaus.groovy.ast.expr.ConstantExpression
import org.codehaus.groovy.ast.expr.VariableExpression
import org.codehaus.groovy.ast.tools.GeneralUtils
import org.codehaus.groovy.ast.tools.GenericsUtils
import org.codehaus.groovy.control.SourceUnit
import org.codehaus.groovy.transform.AbstractASTTransformation
import org.codehaus.groovy.transform.GroovyASTTransformation
import org.objectweb.asm.Opcodes
import org.objectweb.asm.Type

@CompileStatic
@GroovyASTTransformation
final class DefRegisterASTTransformer extends AbstractASTTransformation {
    public static final ClassNode REGISTRATION_NAME_TYPE = ClassHelper.make(RegistrationName)
    @Override
    void visit(ASTNode[] nodes, SourceUnit source) {
        init(nodes, source) // ensure that nodes is [AnnotationNode, AnnotatedNode]

        final AnnotationNode annotation = nodes[0] as AnnotationNode
        final AnnotatedNode targetNode = nodes[1] as AnnotatedNode

        // make sure the @ModConfig annotation is only applied to classes
        if (!(targetNode instanceof FieldNode)) {
            addError("The ${annotation.classNode.name} annotation can only be applied to fields.", targetNode)
            return
        }
        final targetField = (FieldNode) targetNode
        final targetClass = (ClassNode) targetField.declaringClass

        if (targetNode.properties.isEmpty())
            addError("Unable to detect any properties inside class '${targetNode.name}' annotated with '${annotation.classNode.name}'", targetClass)

        final expr = GeneralUtils.varX(targetField.name, targetField.type)
        targetClass.properties.each {
            register(expr, targetClass, it)
        }
    }

    private static void register(final VariableExpression drVar, final ClassNode clazz, final PropertyNode property) {
        property.modifiers = TransformUtils.CONSTANT_MODIFIERS

        final targetClassNodeScope = new VariableScope()
        targetClassNodeScope.classScope = clazz
        final closure = GeneralUtils.closureX(GeneralUtils.stmt(property.initialExpression))
        closure.setVariableScope(targetClassNodeScope)
        final field = TransformUtils.addField(
                targetClassNode: clazz,
                fieldName: "\$registryFieldFor${property.name.capitalize()}",
                modifiers: Opcodes.ACC_PRIVATE | Opcodes.ACC_FINAL | Opcodes.ACC_STATIC,
                type: GenericsUtils.makeClassSafeWithGenerics(RegistryObject, property.type),
                initialValue: GeneralUtils.callX(
                        drVar, 'register', GeneralUtils.args(
                        GeneralUtils.constX(getRegName(property)), closure
                    )
                )
        )
        property.field.setInitialValueExpression(new ConstantExpression(null))

        property.setGetterBlock(GeneralUtils.stmt(GeneralUtils.bytecodeX(property.type) {
            it.visitCode()
            it.visitFieldInsn(Opcodes.GETSTATIC, getInternalName(clazz), field.name, Type.getDescriptor(RegistryObject))
            it.visitMethodInsn(Opcodes.INVOKEVIRTUAL, Type.getInternalName(RegistryObject), 'get', '()Ljava/lang/Object;', true)
            it.visitTypeInsn(Opcodes.CHECKCAST, getInternalName(property.type))
        }))
    }

    private static String getRegName(final PropertyNode propertyNode) {
        final ann = propertyNode.annotations.find { it.classNode == REGISTRATION_NAME_TYPE }
        if (ann !== null) return getMemberStringValue(ann, 'value')
        return propertyNode.name
    }

    private static String getInternalName(ClassNode classNode) {
        return classNode.name.replace('.' as char, '/' as char)
    }
}
