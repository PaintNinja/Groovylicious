package ga.ozli.minecraftmods.groovylicious.transform.registroid.blockitem

import ga.ozli.minecraftmods.groovylicious.transform.TransformUtils
import ga.ozli.minecraftmods.groovylicious.transform.registroid.RegistrationName
import ga.ozli.minecraftmods.groovylicious.transform.registroid.RegistroidASTTransformer
import ga.ozli.minecraftmods.groovylicious.transform.registroid.RegistroidAddon
import groovy.transform.CompileStatic
import net.minecraft.core.Registry
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import org.codehaus.groovy.ast.*
import org.codehaus.groovy.ast.expr.PropertyExpression
import org.codehaus.groovy.ast.tools.GeneralUtils
import org.objectweb.asm.Opcodes

@CompileStatic
class BlockItemAddonTransformer implements RegistroidAddon {
    public static final ClassNode ANNOTATION_TYPE = ClassHelper.make(BlockItemAddon)
    public static final ClassNode BLOCK_ITEM_TYPE = ClassHelper.make(BlockItem)
    public static final ClassNode REGISTRATION_NAME = ClassHelper.make(RegistrationName)
    public static final ClassNode BLOCK_TYPE = ClassHelper.make(Block)
    public static final ClassNode ITEM_PROPS_TYPE = ClassHelper.make(Item.Properties)
    public static final ClassNode ITEM_TYPE = ClassHelper.make(Item)

    @Override
    void makeExtra(AnnotationNode registroidAnnotation, ClassNode targetClass, PropertyNode property, RegistroidASTTransformer transformer) {
        final propertyAnnotation = property.annotations.find { it.classNode == ANNOTATION_TYPE }
        if (propertyAnnotation !== null && transformer.getMemberValue(propertyAnnotation, 'exclude')) return

        final propertyRegName = transformer.getRegName(property)
        if (targetClass.properties.any { it.type.isDerivedFrom(ITEM_TYPE) && transformer.getRegName(it) == propertyRegName }) return

        final myAnnotation = targetClass.annotations.find { it.classNode == ANNOTATION_TYPE }
        targetClass.methods.find {
            it.name == 'makeBlockItem' && it.returnType == BLOCK_ITEM_TYPE && it.parameters.size() == 1
        } ?: TransformUtils.addMethod(
                targetClassNode: targetClass,
                methodName: 'makeBlockItem',
                modifiers: Opcodes.ACC_STATIC | Opcodes.ACC_PUBLIC,
                returnType: BLOCK_ITEM_TYPE,
                parameters: new Parameter[] {
                    GeneralUtils.param(BLOCK_TYPE, 'block')
                },
                code: GeneralUtils.stmt(GeneralUtils.ctorX(BLOCK_ITEM_TYPE, GeneralUtils.args(
                        GeneralUtils.varX('block', BLOCK_TYPE),
                        GeneralUtils.castX(ITEM_PROPS_TYPE, GeneralUtils.callX(myAnnotation.members['propertyFactory'], 'call'))
                )))
        )
        final prop = property.declaringClass.addProperty(
                "\$BLOCK_ITEM_${property.name}",
                TransformUtils.CONSTANT_MODIFIERS,
                BLOCK_ITEM_TYPE,
                GeneralUtils.callX(targetClass, 'makeBlockItem', GeneralUtils.callX(property.declaringClass, property.getterNameOrDefault)),
                null, null
        )
        final regNameAnn = new AnnotationNode(REGISTRATION_NAME)
        regNameAnn.setMember('value', GeneralUtils.constX(transformer.getInitialRegName(property)))
        prop.addAnnotation(regNameAnn)
    }

    @Override
    List<ClassNode> getSupportedTypes() {
        return List.of(BLOCK_TYPE)
    }

    @Override
    List<PropertyExpression> getRequiredRegistries() {
        return List.of(
                GeneralUtils.propX(GeneralUtils.classX(ClassHelper.make(Registry)), 'ITEM_REGISTRY')
        )
    }
}
