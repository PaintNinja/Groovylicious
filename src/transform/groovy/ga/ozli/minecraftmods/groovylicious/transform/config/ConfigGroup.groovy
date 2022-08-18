package ga.ozli.minecraftmods.groovylicious.transform.config

import groovy.transform.CompileStatic

import java.lang.annotation.ElementType
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import java.lang.annotation.Target

/**
 * Represents a group inside a {@link ga.ozli.minecraftmods.groovylicious.transform.Config config}.
 */
@CompileStatic
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
@interface ConfigGroup {
    /**
     * The name of the group. <br>
     * If you do not provide it, it will be determined from the class' name,
     */
    String name() default '</////autodetermined>'

    /**
     * If fields without the {@link ConfigValue} annotation should be excluded from serialization.<br>
     * Defaults to {@code false}.
     */
    boolean excludeFieldsWithoutAnnotation() default false
    /**
     * If sub-groups without the {@link ConfigGroup} annotation should be excluded from serialization.<br>
     * Defaults to {@code false}.
     */
    boolean excludeGroupsWithoutAnnotation() default false
    /**
     * If this group should be excluded from config serialization. <br>
     * Defaults to {@code false}.
     */
    boolean exclude() default false
}