package ga.ozli.minecraftmods.groovylicious.transform.registroid

import groovy.transform.CompileStatic
import org.codehaus.groovy.ast.AnnotationNode
import org.codehaus.groovy.ast.ClassNode
import org.codehaus.groovy.ast.PropertyNode
import org.codehaus.groovy.ast.expr.PropertyExpression

/**
 * An addon for {@linkplain ga.ozli.minecraftmods.groovylicious.transform.Registroid Registroid} systems. <br>
 * The addons are specified on classes annotated with {@linkplain ga.ozli.minecraftmods.groovylicious.transform.Registroid Registroid},
 * by annotating the class with different {@linkplain RegistroidAddonClass addon annotations}.
 * <br>
 * Addons provided by Groovylicious:
 * <ul>
 *     <li>{@linkplain ga.ozli.minecraftmods.groovylicious.transform.registroid.blockitem.BlockItemAddon BlockItem addon}</li>
 *     <li>{@linkplain ga.ozli.minecraftmods.groovylicious.transform.registroid.recipetype.RecipeTypeAddon RecipeType addon}</li>
 *     <li>{@linkplain ga.ozli.minecraftmods.groovylicious.transform.registroid.sound.SoundEventAddon SoundEvent addon}</li>
 * </ul>
 */
@CompileStatic
interface RegistroidAddon {
    /**
     * Processes a property of one of the {@linkplain #getSupportedTypes() supported types}.
     */
    void process(AnnotationNode registroidAnnotation, ClassNode targetClass, PropertyNode property, RegistroidASTTransformer transformer, String modId)

    /**
     * The types this addon can process. (e.g. {@linkplain net.minecraft.world.item.Item Item}, {@linkplain net.minecraft.world.level.block.Block block})
     */
    List<ClassNode> getSupportedTypes()

    /**
     * A list of property expressions representing the registries this addon needs in order to properly work.
     * @see ga.ozli.minecraftmods.groovylicious.transform.Registroid#value()
     */
    List<PropertyExpression> getRequiredRegistries()
}