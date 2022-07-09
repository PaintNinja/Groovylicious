package ga.ozli.minecraftmods.groovylicious.transform

import groovy.transform.CompileStatic
import net.minecraftforge.fml.config.ModConfig
import org.codehaus.groovy.transform.GroovyASTTransformationClass

import java.lang.annotation.ElementType
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import java.lang.annotation.Target

@CompileStatic
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
@GroovyASTTransformationClass('ga.ozli.minecraftmods.groovylicious.transform.config.ConfigASTTransformation')
@interface Config {
    ModConfig.Type value() default ModConfig.Type.COMMON
    String modId() default '(autoDetected)'
}