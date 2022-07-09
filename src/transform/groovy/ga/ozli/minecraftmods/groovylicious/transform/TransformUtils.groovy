package ga.ozli.minecraftmods.groovylicious.transform

import groovy.transform.CompileStatic
import groovy.transform.Generated
import groovy.transform.NamedParam
import groovy.transform.NamedVariant
import org.codehaus.groovy.ast.AnnotationNode
import org.codehaus.groovy.ast.ClassHelper
import org.codehaus.groovy.ast.ClassNode
import org.codehaus.groovy.ast.FieldNode
import org.codehaus.groovy.ast.MethodNode
import org.codehaus.groovy.ast.Parameter
import org.codehaus.groovy.ast.expr.ArgumentListExpression
import org.codehaus.groovy.ast.expr.CastExpression
import org.codehaus.groovy.ast.expr.Expression
import org.codehaus.groovy.ast.stmt.BlockStatement
import org.codehaus.groovy.ast.stmt.Statement

import javax.annotation.Nullable

import static org.objectweb.asm.Opcodes.ACC_PUBLIC
import static org.objectweb.asm.Opcodes.ACC_STATIC
import static org.objectweb.asm.Opcodes.ACC_PRIVATE
import static org.objectweb.asm.Opcodes.ACC_FINAL

@CompileStatic
class TransformUtils {
    static final AnnotationNode GENERATED_ANNOTATION = new AnnotationNode(ClassHelper.make(Generated))

    static final int CONSTANT_MODIFIERS = ACC_PRIVATE | ACC_STATIC | ACC_FINAL

    @NamedVariant
    static MethodNode addStaticMethod(@NamedParam(required = true) final ClassNode targetClassNode,
                                      @NamedParam(required = true) final String methodName,
                                      @NamedParam final int modifiers = ACC_PUBLIC | ACC_STATIC,
                                      @NamedParam final ClassNode returnType = ClassHelper.VOID_TYPE,
                                      @NamedParam final Parameter[] parameters = Parameter.EMPTY_ARRAY,
                                      @NamedParam final ClassNode[] exceptions = ClassNode.EMPTY_ARRAY,
                                      @NamedParam final List<AnnotationNode> annotations = [GENERATED_ANNOTATION],
                                      @NamedParam final Statement code = new BlockStatement(),
                                      @NamedParam @Nullable final Closure<Statement> conditionalCode = null) {

        addMethod(targetClassNode, methodName, modifiers, returnType, parameters, exceptions, annotations, code, conditionalCode)
    }

    @NamedVariant
    static MethodNode addMethod(@NamedParam(required = true) final ClassNode targetClassNode,
                                @NamedParam(required = true) final String methodName,
                                @NamedParam final int modifiers = ACC_PUBLIC,
                                @NamedParam final ClassNode returnType = ClassHelper.VOID_TYPE,
                                @NamedParam final Parameter[] parameters = Parameter.EMPTY_ARRAY,
                                @NamedParam final ClassNode[] exceptions = ClassNode.EMPTY_ARRAY,
                                @NamedParam final List<AnnotationNode> annotations = [GENERATED_ANNOTATION],
                                @NamedParam final Statement code = new BlockStatement(),
                                @NamedParam @Nullable final Closure<Statement> conditionalCode = null) {

//        println "addMethod ${SV(args)}"
//        println "addMethod code ${SVD(args.code)}"

        if (annotations) {
            final MethodNode maybeExistingMethod = targetClassNode.getDeclaredMethod(methodName, parameters)
            if (maybeExistingMethod !== null) return maybeExistingMethod

            final MethodNode method = new MethodNode(methodName, modifiers, returnType, parameters, exceptions, conditionalCode?.call() ?: code)
            method.addAnnotations(annotations)
            targetClassNode.addMethod(method)

            return method
        } else {
            return targetClassNode.addMethod(methodName, modifiers, returnType, parameters, exceptions, conditionalCode?.call() ?: code)
        }
    }

    // same as addField() except addFieldFirst = true and the modifiers are private static final by default
    @NamedVariant
    static FieldNode addConstant(@NamedParam(required = true) final ClassNode targetClassNode,
                                 @NamedParam(required = true) final String fieldName,
                                 @NamedParam final int modifiers = CONSTANT_MODIFIERS,
                                 @NamedParam final ClassNode type = ClassHelper.OBJECT_TYPE,
                                 @NamedParam @Nullable final Closure<ClassNode> conditionalType = null,
                                 @NamedParam final List<AnnotationNode> annotations = [GENERATED_ANNOTATION],
                                 @NamedParam final Expression initialValue = null,
                                 @NamedParam @Nullable final Closure<Expression> conditionalInitialValue = null,
                                 @NamedParam final boolean addFieldFirst = true) {

        addField(targetClassNode, fieldName, modifiers, type, conditionalType, annotations, initialValue, conditionalInitialValue, addFieldFirst)
    }

    @NamedVariant
    static FieldNode addField(@NamedParam(required = true) final ClassNode targetClassNode,
                              @NamedParam(required = true) final String fieldName,
                              @NamedParam final int modifiers = ACC_PUBLIC, // note: public fields aren't always properties
                              @NamedParam final ClassNode type = ClassHelper.OBJECT_TYPE,
                              @NamedParam @Nullable final Closure<ClassNode> conditionalType = null,
                              @NamedParam final List<AnnotationNode> annotations = [GENERATED_ANNOTATION],
                              @NamedParam final Expression initialValue = null,
                              @NamedParam @Nullable final Closure<Expression> conditionalInitialValue = null,
                              @NamedParam final boolean addFieldFirst = false) {

//        println "addField ${SV(args)}"
//        println "addField initialValue ${SV(args)}"

        if (annotations) {
            final FieldNode field = new FieldNode(fieldName, modifiers, conditionalType?.call() ?: type, targetClassNode, conditionalInitialValue?.call() ?: initialValue)
            field.addAnnotations(annotations)

            if (addFieldFirst) targetClassNode.addFieldFirst(field)
            else targetClassNode.addField(field)

            return field
        } else {
            if (addFieldFirst) return targetClassNode.addFieldFirst(fieldName, modifiers, conditionalType?.call() ?: type, conditionalInitialValue?.call() ?: initialValue)
            else return targetClassNode.addField(fieldName, modifiers, conditionalType?.call() ?: type, conditionalInitialValue?.call() ?: initialValue)
        }
    }

    static ArgumentListExpression conditionalArgs(@Nullable final /** <Closure<Expression> | Expression> */ Object... expressions) {
        List<Expression> list = new ArrayList<>(expressions.length)
        for (Object expression : expressions) {
            if (expression instanceof Closure<Expression>) {
                Expression maybeExpression = (expression as Closure<Expression>).call()
                if (maybeExpression !== null) list.add(maybeExpression)
            } else if (expression !== null) {
                list.add(expression as Expression)
            }
        }
        return new ArgumentListExpression(list)
    }

    static ArgumentListExpression conditionalArgs(final List<Expression> nullableExpressions) {
        List<Expression> list = new ArrayList<>(nullableExpressions.size())
        for (Expression expression : nullableExpressions) {
            if (expression !== null) list.add(expression)
        }
        return new ArgumentListExpression(list)
    }

    static Expression conditionalCast(@Nullable final ClassNode nullableType, final Expression expression) {
        if (nullableType === null) return expression
        else return new CastExpression(nullableType, expression)
    }
}
