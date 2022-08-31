package ga.ozli.minecraftmods.groovylicious.transform

import groovy.transform.CompileStatic
import net.minecraftforge.common.ForgeConfigSpec

import java.lang.annotation.ElementType
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import java.lang.annotation.Target

/**
 * Indicates to parent transform annotations that this class/method/field should be skipped.
 * <br><br>
 * <p>For example, annotating a field with {@linkplain Exclude @Exclude} inside a dataclass
 * annotated with {@linkplain Config @Config} would skip turning the field into a {@link ForgeConfigSpec.ConfigValue}.</p>
 */
@CompileStatic
@Retention(RetentionPolicy.SOURCE)
@Target([ElementType.TYPE, ElementType.METHOD, ElementType.FIELD])
@interface Exclude {}