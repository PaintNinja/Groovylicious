package ga.ozli.minecraftmods.groovylicious.transform.config

import groovy.transform.CompileStatic

import java.lang.annotation.ElementType
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import java.lang.annotation.Target

/**
 * Represents a value inside a {@link ga.ozli.minecraftmods.groovylicious.transform.Config config}.
 */
@CompileStatic
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.SOURCE)
@interface ConfigValue {
    /**
     * The name of the value. <br>
     * If you do not provide it, it will be determined from the field's name,
     */
    String name() default '</////autodetermined>'

    /**
     * If this field should be excluded from config serialization. <br>
     * Defaults to {@code false}.
     */
    boolean exclude() default false

    /**
     * An optional validator closure that will validate the config values. <br>
     * The closure will have the first argument the new config value, and must return a boolean (if it's valid or not). <br>
     * <strong>Important:</strong> the value passed in the closure will be {@code null} while the config is correcting, so make sure to account for that (and usually return {@code true})!
     */
    Class validator() default { true }
}