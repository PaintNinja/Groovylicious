package ga.ozli.minecraftmods.groovylicious.transform.registroid.recipetype

import ga.ozli.minecraftmods.groovylicious.transform.registroid.RegistroidAddonClass
import groovy.transform.CompileStatic

import java.lang.annotation.*

@Documented // TODO docs
@CompileStatic
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
@RegistroidAddonClass(RecipeTypeAddonTransformer)
@interface RecipeTypeAddon {}