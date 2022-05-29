package ga.ozli.minecraftmods.groovylicious.transform

import groovy.transform.CompileStatic
import org.apache.groovy.lang.annotation.Incubating
import org.codehaus.groovy.transform.GroovyASTTransformationClass

import java.lang.annotation.ElementType
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import java.lang.annotation.Target

@Incubating
@CompileStatic
@Retention(RetentionPolicy.SOURCE)
@Target([ElementType.TYPE, ElementType.FIELD])
@GroovyASTTransformationClass('ga.ozli.minecraftmods.groovylicious.transform.config.ModConfigASTTransformation')
@interface ModConfig {
    net.minecraftforge.fml.config.ModConfig.Type value() default net.minecraftforge.fml.config.ModConfig.Type.COMMON
    String modId() default 'unknown'
}