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
    /**
     * The type of the config.
     */
    ModConfig.Type value() default ModConfig.Type.COMMON

    String modId() default '(autoDetected)'
    /**
     * If fields without the {@link ga.ozli.minecraftmods.groovylicious.transform.config.ConfigValue} annotation should be excluded from serialization.<br>
     * Defaults to {@code false}.
     */
    boolean excludeFieldsWithoutAnnotation() default false
    /**
     * If sub-groups without the {@link ga.ozli.minecraftmods.groovylicious.transform.config.ConfigGroup} annotation should be excluded from serialization.<br>
     * Defaults to {@code false}.
     */
    boolean excludeGroupsWithoutAnnotation() default false
}