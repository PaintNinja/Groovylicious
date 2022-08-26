package ga.ozli.minecraftmods.groovylicious.transform.registroid.recipetype

import ga.ozli.minecraftmods.groovylicious.transform.registroid.RegistroidASTTransformer
import ga.ozli.minecraftmods.groovylicious.transform.registroid.RegistroidAddon
import groovy.transform.CompileStatic
import net.minecraft.core.Registry
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.crafting.RecipeType
import org.codehaus.groovy.ast.AnnotationNode
import org.codehaus.groovy.ast.ClassHelper
import org.codehaus.groovy.ast.ClassNode
import org.codehaus.groovy.ast.PropertyNode
import org.codehaus.groovy.ast.expr.PropertyExpression
import org.codehaus.groovy.ast.tools.GeneralUtils

@CompileStatic
class RecipeTypeAddonTransformer implements RegistroidAddon {
    private static final ClassNode RECIPE_TYPE_TYPE = ClassHelper.make(RecipeType)
    private static final ClassNode RL_TYPE = ClassHelper.make(ResourceLocation)
    @Override
    void process(AnnotationNode registroidAnnotation, ClassNode targetClass, PropertyNode property, RegistroidASTTransformer transformer, String modId) {
        property.field.setInitialValueExpression(
                GeneralUtils.callX(RECIPE_TYPE_TYPE, 'simple', GeneralUtils.ctorX(
                        RL_TYPE, GeneralUtils.args(
                        GeneralUtils.constX(modId), GeneralUtils.constX(transformer.getRegName(property))
                    )
                ))
        )
    }

    @Override
    List<ClassNode> getSupportedTypes() {
        return [RECIPE_TYPE_TYPE]
    }

    @Override
    List<PropertyExpression> getRequiredRegistries() {
        return [
                GeneralUtils.propX(GeneralUtils.classX(ClassHelper.make(Registry)), 'RECIPE_TYPE_REGISTRY')
        ]
    }
}
