package ga.ozli.minecraftmods.groovylicious.transform

import groovy.transform.CompileStatic
import groovy.transform.stc.ClosureParams
import groovy.transform.stc.SimpleType
import org.codehaus.groovy.ast.ClassHelper
import org.codehaus.groovy.ast.ClassNode
import org.codehaus.groovy.ast.stmt.Statement
import org.codehaus.groovy.ast.tools.GeneralUtils
import org.objectweb.asm.Type

import static org.objectweb.asm.Opcodes.CHECKCAST
import static org.objectweb.asm.Opcodes.INVOKEVIRTUAL

/**
 * TransformUtils that handle more @POJO-like bytecode generation.
 * <p>Faster and more Java-like code output, at the cost of likely losing metaclass support, Groovy truth, etc...</p>
 */
@CompileStatic
class PojoTransformUtils {
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

        return GeneralUtils.stmt(GeneralUtils.castX(target, GeneralUtils.bytecodeX(closure)))
    }

    /**
     * Converts a Groovy AST type to a lower-level ObjectWeb ASM type
     */
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
