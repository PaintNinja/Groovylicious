package ga.ozli.minecraftmods.groovylicious.transform.config

import groovy.transform.CompileStatic
import net.minecraftforge.common.ForgeConfigSpec
import org.codehaus.groovy.ast.ClassHelper
import org.codehaus.groovy.ast.ClassNode
import org.codehaus.groovy.ast.GenericsType
import org.codehaus.groovy.ast.expr.ConstantExpression
import org.codehaus.groovy.ast.tools.GenericsUtils

@CompileStatic
class ConfigTypes {

    // todo: look into FieldNode.originType instead of FieldNode.type
    // todo: support bounded floats

    /** Bounded ConfigTypes have set ranges that they accept, e.g. {@code 0..5} */
    static class Bounded {
        /** {@code ForgeConfigSpec.IntValue} */
        static ClassNode intValue = ClassHelper.make(ForgeConfigSpec.IntValue)

        /** {@code ForgeConfigSpec.LongValue} */
        static ClassNode longValue = ClassHelper.make(ForgeConfigSpec.LongValue)

        /** {@code ForgeConfigSpec.DoubleValue} */
        static ClassNode doubleValue = ClassHelper.make(ForgeConfigSpec.DoubleValue)

        /** A set of types that are suitable candidates for becoming a bounded type */
        static Set<ClassNode> primitiveCandidates = Set.of(
                ClassHelper.Integer_TYPE, ClassHelper.int_TYPE,
                ClassHelper.Long_TYPE, ClassHelper.long_TYPE,
                ClassHelper.Double_TYPE, ClassHelper.double_TYPE
        )

        /**
         * Gets the MIN_VALUE of the associated wrapped property.type
         * @param type the property.type
         * @return the associated MIN_VALUE inside a ConstantExpression
         */
        static ConstantExpression getDefaultLowerBound(ClassNode type) {
            return switch (type) {
                case Bounded.intValue, Unbounded.intValue,
                     ClassHelper.Integer_TYPE, ClassHelper.int_TYPE -> new ConstantExpression(Integer.MIN_VALUE)

                case Bounded.longValue, Unbounded.longValue,
                     ClassHelper.Long_TYPE, ClassHelper.long_TYPE -> new ConstantExpression(Long.MIN_VALUE)

                case Bounded.doubleValue, Unbounded.doubleValue,
                     ClassHelper.Double_TYPE, ClassHelper.double_TYPE -> new ConstantExpression(Double.MIN_VALUE)

                default -> throw new Exception("Unsupported type: ${type}")
            }
        }

        /**
         * Gets the MAX_VALUE of the associated wrapped property.type
         * @param type the property.type
         * @return the associated MAX_VALUE inside a ConstantExpression
         */
        static ConstantExpression getDefaultUpperBound(ClassNode type) {
            return switch (type) {
                case Bounded.intValue, Unbounded.intValue,
                     ClassHelper.Integer_TYPE, ClassHelper.int_TYPE -> new ConstantExpression(Integer.MAX_VALUE)

                case Bounded.longValue, Unbounded.longValue,
                     ClassHelper.Long_TYPE, ClassHelper.long_TYPE -> new ConstantExpression(Long.MAX_VALUE)

                case Bounded.doubleValue, Unbounded.doubleValue,
                     ClassHelper.Double_TYPE, ClassHelper.double_TYPE -> new ConstantExpression(Double.MAX_VALUE)

                default -> throw new Exception("Unsupported type: ${type}")
            }
        }
    }

    static class Unbounded {
        /** {@code ForgeConfigSpec.ConfigValue<Integer>} */
        static ClassNode intValue = GenericsUtils.makeClassSafeWithGenerics(
                ClassHelper.make(ForgeConfigSpec.ConfigValue),
                new GenericsType(ClassHelper.Integer_TYPE)
        )

        /** {@code ForgeConfigSpec.ConfigValue<Long>} */
        static ClassNode longValue = GenericsUtils.makeClassSafeWithGenerics(
                ClassHelper.make(ForgeConfigSpec.ConfigValue),
                new GenericsType(ClassHelper.Long_TYPE)
        )

        /** {@code ForgeConfigSpec.ConfigValue<Double>} */
        static ClassNode doubleValue = GenericsUtils.makeClassSafeWithGenerics(
                ClassHelper.make(ForgeConfigSpec.ConfigValue),
                new GenericsType(ClassHelper.Double_TYPE)
        )

        /** {@code ForgeConfigSpec.ConfigValue<Float>} */
        static ClassNode floatValue = GenericsUtils.makeClassSafeWithGenerics(
                ClassHelper.make(ForgeConfigSpec.ConfigValue),
                new GenericsType(ClassHelper.Float_TYPE)
        )

        /** {@code ForgeConfigSpec.BooleanValue} */
        static ClassNode booleanValue = ClassHelper.make(ForgeConfigSpec.BooleanValue)

        /** {@code ForgeConfigSpec.ConfigValue<String>} */
        static ClassNode stringValue = GenericsUtils.makeClassSafeWithGenerics(
                ClassHelper.make(ForgeConfigSpec.ConfigValue),
                new GenericsType(ClassHelper.STRING_TYPE)
        )

        /** {@code ForgeConfigSpec.ConfigValue<?>} */
        static ClassNode genericValue = GenericsUtils.makeClassSafeWithGenerics(
                ClassHelper.make(ForgeConfigSpec.ConfigValue),
                GenericsUtils.buildWildcardType(ClassHelper.make(ForgeConfigSpec.ConfigValue))
        )
    }

    static List<ClassNode> list = List.of(
            Bounded.intValue, Bounded.longValue, Bounded.doubleValue,
            Unbounded.intValue, Unbounded.longValue, Unbounded.doubleValue, Unbounded.floatValue, Unbounded.booleanValue, Unbounded.stringValue
    )

    static List<ClassNode> primitivesList = List.of(
            ClassHelper.Integer_TYPE, ClassHelper.int_TYPE,
            ClassHelper.Long_TYPE, ClassHelper.long_TYPE,
            ClassHelper.Double_TYPE, ClassHelper.double_TYPE,
            ClassHelper.Float_TYPE, ClassHelper.float_TYPE,
            ClassHelper.Boolean_TYPE, ClassHelper.boolean_TYPE,
            ClassHelper.STRING_TYPE, ClassHelper.GSTRING_TYPE
    )

    /**
     * Gets the wrapped ForgeConfigSpec type for a given standard library type
     * @param unwrappedType should be a {@code property.type}
     * @param bounded whether or not the returned wrapped type should be a bounded version (e.g. bounded IntValue vs unbounded ConfigValue<Integer>)
     * @return the associated wrapped ForgeConfigSpec type
     */
    static ClassNode getWrappedType(ClassNode unwrappedType, boolean bounded) {
        if (bounded) {
            return switch (unwrappedType) {
                case ClassHelper.Integer_TYPE, ClassHelper.int_TYPE -> Bounded.intValue
                case ClassHelper.Long_TYPE, ClassHelper.long_TYPE -> Bounded.longValue
                case ClassHelper.Double_TYPE, ClassHelper.double_TYPE -> Bounded.doubleValue
                default -> Unbounded.genericValue
                //default -> throw new Exception("Unsupported bound type: ${unwrappedType}")
            }
        } else {
            switch (unwrappedType) {
                case ClassHelper.Integer_TYPE, ClassHelper.int_TYPE -> Unbounded.intValue
                case ClassHelper.Long_TYPE, ClassHelper.long_TYPE -> Unbounded.longValue
                case ClassHelper.Double_TYPE, ClassHelper.double_TYPE -> Unbounded.doubleValue
                case ClassHelper.Float_TYPE, ClassHelper.float_TYPE -> Unbounded.floatValue
                case ClassHelper.Boolean_TYPE, ClassHelper.boolean_TYPE -> Unbounded.booleanValue
                case ClassHelper.STRING_TYPE, ClassHelper.GSTRING_TYPE -> Unbounded.stringValue
                default -> Unbounded.genericValue
                //default -> throw new Exception("Unsupported unbound type: ${unwrappedType}")
            }
        }
    }

}
