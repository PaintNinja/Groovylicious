package ga.ozli.minecraftmods.groovylicious.transform.config

import groovy.transform.CompileStatic
import net.minecraftforge.common.ForgeConfigSpec
import org.codehaus.groovy.ast.ClassHelper
import org.codehaus.groovy.ast.ClassNode
import org.codehaus.groovy.ast.GenericsType
import org.codehaus.groovy.ast.expr.ConstantExpression
import org.codehaus.groovy.ast.tools.GeneralUtils
import org.codehaus.groovy.ast.tools.GenericsUtils

@CompileStatic
class ConfigTypes {

    interface ConfigValueType {
        ClassNode getClassNode()
        default boolean supportsValidator() {
            return false
        }
    }

    static ConfigValueType getConfigValueType(ClassNode primitiveType) {
        return switch (primitiveType) {
            case ClassHelper.Byte_TYPE, ClassHelper.byte_TYPE -> Bounded.BYTE_VALUE_TYPE
            case ClassHelper.Short_TYPE, ClassHelper.short_TYPE -> Bounded.SHORT_VALUE_TYPE
            case ClassHelper.Integer_TYPE, ClassHelper.int_TYPE -> Bounded.INT_VALUE_TYPE
            case ClassHelper.Long_TYPE, ClassHelper.long_TYPE -> Bounded.LONG_VALUE_TYPE
            case ClassHelper.Float_TYPE, ClassHelper.float_TYPE -> Bounded.FLOAT_VALUE_TYPE
            case ClassHelper.Double_TYPE, ClassHelper.double_TYPE -> Bounded.DOUBLE_VALUE_TYPE
            case ClassHelper.Boolean_TYPE, ClassHelper.boolean_TYPE -> Unbounded.BOOLEAN_VALUE_TYPE
            case ClassHelper.GSTRING_TYPE, ClassHelper.STRING_TYPE -> Unbounded.STRING_VALUE_TYPE
            default -> Unbounded.GENERIC_VALUE_TYPE
            //default -> throw new Exception("Unsupported type: ${primitiveType}")
        }
    }

    /**
     * Bounded ConfigTypes can have set ranges that they accept, e.g. {@code 0..5}
     * All primitive types that can have bounds should always become bounded types - even if no range is specified.
     * This is to workaround quirks in Forge's config system.
     */
    enum Bounded implements ConfigValueType {
        /** {@code ForgeConfigSpec.IntValue} treated as a byte */
        BYTE_VALUE_TYPE(
                ClassHelper.make(ForgeConfigSpec.IntValue),
                GeneralUtils.constX(Byte.MIN_VALUE),
                GeneralUtils.constX(Byte.MAX_VALUE),
                true
        ),

        /** {@code ForgeConfigSpec.IntValue} treated as a short */
        SHORT_VALUE_TYPE(
                ClassHelper.make(ForgeConfigSpec.IntValue),
                GeneralUtils.constX(Short.MIN_VALUE),
                GeneralUtils.constX(Short.MAX_VALUE),
                true
        ),

        /** {@code ForgeConfigSpec.IntValue} */
        INT_VALUE_TYPE(
                ClassHelper.make(ForgeConfigSpec.IntValue),
                GeneralUtils.constX(Integer.MIN_VALUE),
                GeneralUtils.constX(Integer.MAX_VALUE)
        ),

        /** {@code ForgeConfigSpec.LongValue} */
        LONG_VALUE_TYPE(
                ClassHelper.make(ForgeConfigSpec.LongValue),
                GeneralUtils.constX(Long.MIN_VALUE),
                GeneralUtils.constX(Long.MAX_VALUE)
        ),

        /** {@code ForgeConfigSpec.DoubleValue} treated as a float */
        FLOAT_VALUE_TYPE(
                ClassHelper.make(ForgeConfigSpec.DoubleValue),
                GeneralUtils.constX(Float.NEGATIVE_INFINITY),
                GeneralUtils.constX(Float.POSITIVE_INFINITY),
                true
        ),

        /** {@code ForgeConfigSpec.DoubleValue} */
        DOUBLE_VALUE_TYPE(
                ClassHelper.make(ForgeConfigSpec.DoubleValue),
                GeneralUtils.constX(Double.NEGATIVE_INFINITY),
                GeneralUtils.constX(Double.POSITIVE_INFINITY)
        )

        final ClassNode classNode
        final ConstantExpression minValue
        final ConstantExpression maxValue
        final boolean specialType
        Bounded(ClassNode classNode, ConstantExpression minValue, ConstantExpression maxValue, boolean specialType = false) {
            this.classNode = classNode
            this.minValue = minValue
            this.maxValue = maxValue
            this.specialType = specialType
        }

        @Override
        ClassNode getClassNode() {
            return this.classNode
        }

        /**
         * Special bounded types are those that need special handling to correctly handle conversions to and from
         * a lower precision type that Forge's config system doesn't support (e.g. float to double).
         */
//        class Special extends Bounded implements ConfigValueType {
//            // A set of types that are suitable candidates for becoming a bounded type but need special handling
//            static Set<ClassNode> specialPrimitiveCandidates = Set.of(
//                    ClassHelper.Byte_TYPE, ClassHelper.byte_TYPE,
//                    ClassHelper.Short_TYPE, ClassHelper.short_TYPE,
//                    ClassHelper.Float_TYPE, ClassHelper.float_TYPE
//            )
//
//            final FLOAT_VALUE_TYPE = new Special(
//                    ClassHelper.make(ForgeConfigSpec.DoubleValue),
//                    GeneralUtils.constX(Float.MIN_VALUE),
//                    GeneralUtils.constX(Float.MAX_VALUE),
//                    ClassHelper.float_TYPE
//            )
//
//            final ClassNode specialType
//            Special(ClassNode classNode, ConstantExpression minValue, ConstantExpression maxValue, ClassNode specialType) {
//                super(classNode, minValue, maxValue)
//                this.specialType = specialType
//            }
//
//            @Override
//            ClassNode getClassNode() {
//                return this.classNode
//            }
//        }
    }

    enum Unbounded implements ConfigValueType {
        /** {@code ForgeConfigSpec.BooleanValue} */
        BOOLEAN_VALUE_TYPE(ClassHelper.make(ForgeConfigSpec.BooleanValue)),

        /** {@code ForgeConfigSpec.ConfigValue<String>} */
        STRING_VALUE_TYPE(
                GenericsUtils.makeClassSafeWithGenerics(
                        ClassHelper.makeCached(ForgeConfigSpec.ConfigValue),
                        new GenericsType(ClassHelper.STRING_TYPE)
                )
        ) {
            @Override
            boolean supportsValidator() {
                return true
            }
        },

        /** {@code ForgeConfigSpec.ConfigValue<?>} */
        GENERIC_VALUE_TYPE(
                GenericsUtils.makeClassSafeWithGenerics( // todo: switch to https://github.com/apache/groovy/commit/03886c5fe82cb8664da223f8cd82ada2c2c8b3c7 once new Groovy is out and on APLP
                        ClassHelper.makeCached(ForgeConfigSpec.ConfigValue),
                        GenericsUtils.buildWildcardType()
                )
        ) {
            @Override
            boolean supportsValidator() {
                return true
            }
        }

        final ClassNode classNode
        Unbounded(ClassNode classNode) {
            this.classNode = classNode
        }

        @Override
        ClassNode getClassNode() {
            return this.classNode
        }

        static class Special {
            // todo: investigate whether or not Character/char needs special handling
            /** {@code ForgeConfigSpec.ConfigValue<Character>} */
            static final ClassNode STRING_VALUE_TYPE = GenericsUtils.makeClassSafeWithGenerics(
                    ClassHelper.makeCached(ForgeConfigSpec.ConfigValue),
                    new GenericsType(ClassHelper.Character_TYPE)
            )
        }
    }
}
