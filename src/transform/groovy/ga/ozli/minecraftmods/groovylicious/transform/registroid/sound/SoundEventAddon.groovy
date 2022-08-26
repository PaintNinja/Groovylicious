package ga.ozli.minecraftmods.groovylicious.transform.registroid.sound

import ga.ozli.minecraftmods.groovylicious.transform.registroid.RegistroidAddonClass
import ga.ozli.minecraftmods.groovylicious.transform.registroid.recipetype.RecipeTypeAddonTransformer
import groovy.transform.CompileStatic

import java.lang.annotation.*

@Documented // TODO docs
@CompileStatic
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
@RegistroidAddonClass(SoundEventAddonTransformer)
@interface SoundEventAddon {}