package ga.ozli.minecraftmods.groovylicious.transform.registroid.sound

import ga.ozli.minecraftmods.groovylicious.transform.registroid.RegistroidAddonClass
import ga.ozli.minecraftmods.groovylicious.transform.registroid.recipetype.RecipeTypeAddonTransformer
import groovy.transform.CompileStatic

import java.lang.annotation.*

/**
 * An {@link ga.ozli.minecraftmods.groovylicious.transform.registroid.RegistroidAddon Registroid addon} which makes
 * all {@linkplain net.minecraft.sounds.SoundEvent SoundEvent} fields within the class be created using
 * the correct deduced registry name. You do <strong>not</strong> need to initialise the field. <br><br>
 * Example in:
 * <pre>
 * {@code static final SoundEvent HELLO_WORLD}
 * {@code static final SoundEvent HELLO_THERE = new SoundEvent(null, 0.12f)}
 * </pre>
 * Example out:
 * <pre>
 * {@code static final SoundEvent HELLO_WORLD = new SoundEvent(new ResourceLocation(yourModId, 'hello_world'))}
 * {@code static final SoundEvent HELLO_THERE = new SoundEvent(new ResourceLocation(yourModId, 'hello_there'), 0.12f)}
 * </pre>
 */
@Documented
@CompileStatic
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
@RegistroidAddonClass(SoundEventAddonTransformer)
@interface SoundEventAddon {}