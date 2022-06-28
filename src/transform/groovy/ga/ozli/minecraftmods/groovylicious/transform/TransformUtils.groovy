package ga.ozli.minecraftmods.groovylicious.transform

import groovy.transform.CompileStatic
import groovy.transform.Generated
import org.codehaus.groovy.ast.AnnotationNode
import org.codehaus.groovy.ast.ClassHelper
import org.codehaus.groovy.ast.ClassNode
import org.codehaus.groovy.ast.MethodNode
import org.codehaus.groovy.ast.Parameter
import org.codehaus.groovy.ast.stmt.BlockStatement
import org.codehaus.groovy.ast.stmt.Statement

import static org.objectweb.asm.Opcodes.ACC_PUBLIC
import static org.objectweb.asm.Opcodes.ACC_STATIC

@CompileStatic
class TransformUtils {
    static final AnnotationNode GENERATED_ANNOTATION = new AnnotationNode(ClassHelper.make(Generated))

    static MethodNode addStaticMethod(Map args) {
        final int modifiers = args['modifiers'] as Integer ?: ACC_PUBLIC | ACC_STATIC

        addMethod targetClassNode: args.targetClassNode,
                  methodName: args.methodName,
                  modifiers: modifiers,
                  returnType: args.returnType,
                  parameters: args.parameters,
                  exceptions: args.exceptions,
                  annotations: args.annotations,
                  code: args.code
    }

    static MethodNode addMethod(Map args) {
        final ClassNode targetClassNode = args['targetClassNode'] as ClassNode
        final String methodName = args['methodName'] as String
        final int modifiers = args['modifiers'] as Integer ?: ACC_PUBLIC
        final ClassNode returnType = args['returnType'] as ClassNode ?: ClassHelper.VOID_TYPE
        final Parameter[] parameters = args['parameters'] as Parameter[] ?: Parameter.EMPTY_ARRAY
        final ClassNode[] exceptions = args['exceptions'] as ClassNode[] ?: ClassNode.EMPTY_ARRAY
        final List<AnnotationNode> annotations = args['annotations'] as List<AnnotationNode> ?: [GENERATED_ANNOTATION]
        final Statement code = args['code'] as Statement ?: new BlockStatement()

        if (annotations) {
            final MethodNode maybeExistingMethod = targetClassNode.getDeclaredMethod(methodName, parameters)
            if (maybeExistingMethod !== null) return maybeExistingMethod

            final MethodNode method = new MethodNode(methodName, modifiers, returnType, parameters, exceptions, code)
            method.addAnnotations(annotations)
            targetClassNode.addMethod(method)

            return method
        } else {
            return targetClassNode.addMethod(methodName, modifiers, returnType, parameters, exceptions, code)
        }
    }
}
