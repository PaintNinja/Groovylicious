package ga.ozli.minecraftmods.groovylicious.transform.registroid

import groovy.transform.CompileStatic

import java.lang.annotation.ElementType
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import java.lang.annotation.Target

@CompileStatic
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
@interface RegistroidAddonClass {
    Class value()
}