package ga.ozli.minecraftmods.groovylicious.transform.registroid.recipetype

import ga.ozli.minecraftmods.groovylicious.transform.registroid.RegistroidAddonClass
import groovy.transform.CompileStatic

import java.lang.annotation.*

/**
 * An {@link ga.ozli.minecraftmods.groovylicious.transform.registroid.RegistroidAddon Registroid addon} which makes
 * all {@linkplain net.minecraft.world.item.crafting.RecipeType RecipeType} fields within the class be created using
 * {@linkplain net.minecraft.world.item.crafting.RecipeType#simple(net.minecraft.resources.ResourceLocation)}, with the
 * name being the registry name of the RecipeType field. You do <strong>not</strong> need to initialise the field. <br> <br>
 * Example in:
 * <pre>
 * {@code static final RecipeType<MyRecipe> HELLO_WORLD}
 * </pre>
 * Example out:
 * <pre>
 * {@code static final RecipeType<MyRecipe> HELLO_WORLD = RecipeType.simple(new ResourceLocation(yourModId, 'hello_world'))}
 * </pre>
 */
@Documented
@CompileStatic
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
@RegistroidAddonClass(RecipeTypeAddonTransformer)
@interface RecipeTypeAddon {}