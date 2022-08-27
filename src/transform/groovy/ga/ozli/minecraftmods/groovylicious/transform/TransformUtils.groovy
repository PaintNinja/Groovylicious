package ga.ozli.minecraftmods.groovylicious.transform


import groovy.transform.CompileStatic
import groovy.transform.Generated
import groovy.transform.NamedParam
import groovy.transform.NamedVariant
import groovy.transform.stc.ClosureParams
import groovy.transform.stc.SimpleType
import org.codehaus.groovy.ast.*
import org.codehaus.groovy.ast.expr.ArgumentListExpression
import org.codehaus.groovy.ast.expr.CastExpression
import org.codehaus.groovy.ast.expr.Expression
import org.codehaus.groovy.ast.stmt.BlockStatement
import org.codehaus.groovy.ast.stmt.Statement
import org.codehaus.groovy.ast.tools.GeneralUtils
import org.objectweb.asm.Type

import javax.annotation.Nullable

import static org.objectweb.asm.Opcodes.*

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
        final List<Expression> list = new ArrayList<>(expressions.length)
        for (Object expression : expressions) {
            if (expression instanceof Closure<Expression>) {
                final Expression maybeExpression = (expression as Closure<Expression>).call()
                if (maybeExpression !== null) list.add(maybeExpression)
            } else if (expression !== null) {
                list.add(expression as Expression)
            }
        }
        return new ArgumentListExpression(list)
    }

    static ArgumentListExpression conditionalArgs(final List<Expression> nullableExpressions) {
        final List<Expression> list = new ArrayList<>(nullableExpressions.size())
        for (final Expression expression : nullableExpressions) {
            if (expression !== null) list.add(expression)
        }
        return new ArgumentListExpression(list)
    }

    static Expression conditionalCast(@Nullable final ClassNode nullableType, final Expression expression) {
        if (nullableType === null) return expression
        else return new CastExpression(nullableType, expression)
    }

    static String getInternalName(ClassNode classNode) {
        return classNode.name.replace('.' as char, '/' as char)
    }

    public static final Set<PrimitiveType> DIRECT_PRIMITIVE_CAST = new HashSet<>()
    static {
        DIRECT_PRIMITIVE_CAST.add(new PrimitiveType(ClassHelper.int_TYPE, ClassHelper.Integer_TYPE, 'intValue'))
        DIRECT_PRIMITIVE_CAST.add(new PrimitiveType(ClassHelper.boolean_TYPE, ClassHelper.Boolean_TYPE, 'booleanValue'))
        DIRECT_PRIMITIVE_CAST.add(new PrimitiveType(ClassHelper.double_TYPE, ClassHelper.Double_TYPE, 'doubleValue'))
    }

    public static final ClassNode LIST = ClassHelper.make(List)

    static Statement cast(ClassNode target, @ClosureParams(value = SimpleType, options = 'groovyjarjarasm.asm.MethodVisitor') Closure closure) {
        if (target == LIST) {
            return GeneralUtils.stmt(GeneralUtils.bytecodeX(target) {
                closure(it)
                it.visitTypeInsn(CHECKCAST, Type.getInternalName(List))
            })
        }
        for (final entry : DIRECT_PRIMITIVE_CAST) {
            if (entry.matches(target)) return entry.cast(target, closure)
        }
        GeneralUtils.stmt(GeneralUtils.castX(target, GeneralUtils.bytecodeX(closure)))
    }

    static Type getType(ClassNode classNode) {
        return switch (classNode) {
            case ClassHelper.int_TYPE -> Type.INT_TYPE
            case ClassHelper.boolean_TYPE -> Type.BOOLEAN_TYPE
            case ClassHelper.double_TYPE -> Type.DOUBLE_TYPE
            case ClassHelper.float_TYPE -> Type.FLOAT_TYPE
            case ClassHelper.short_TYPE -> Type.SHORT_TYPE
            case ClassHelper.char_TYPE -> Type.CHAR_TYPE
            case ClassHelper.long_TYPE -> Type.LONG_TYPE
            case ClassHelper.byte_TYPE -> Type.BYTE_TYPE
            default -> Type.getObjectType(getInternalName(classNode))
        }
    }

    static class PrimitiveType {
        ClassNode primitive, boxed
        String conversionMethod, primitiveDesc

        PrimitiveType(ClassNode primitive, ClassNode boxed, String conversionMethod) {
            this.primitive = primitive
            this.boxed = boxed
            this.conversionMethod = conversionMethod
            this.primitiveDesc = getType(primitive).getDescriptor()
        }

        boolean matches(ClassNode type) {
            return primitive == type || boxed == type
        }

        Statement cast(ClassNode target, Closure closure) {
            if (target == primitive) {
                return GeneralUtils.stmt(GeneralUtils.bytecodeX(primitive) {
                    closure(it)
                    final boxedInternal = getInternalName(boxed)
                    it.visitTypeInsn(CHECKCAST, boxedInternal)
                    it.visitMethodInsn(INVOKEVIRTUAL, boxedInternal, conversionMethod, '()' + primitiveDesc, false)
                })
            }
            return GeneralUtils.stmt(GeneralUtils.bytecodeX(boxed) {
                closure(it)
                it.visitTypeInsn(CHECKCAST, getInternalName(boxed))
            })
        }
    }
}
