package ga.ozli.minecraftmods.groovylicious.transform

import groovy.transform.CompileStatic
import org.codehaus.groovy.transform.GroovyASTTransformationClass

import java.lang.annotation.Documented
import java.lang.annotation.ElementType
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import java.lang.annotation.Target

/**
 * An annotation which transforms a class with static registrable object properties into a {@linkplain net.minecraftforge.registries.DeferredRegister} + {@linkplain net.minecraftforge.registries.RegistryObject} + getters class. <br>
 * This annotation can be used in either field or class mode. <br><br>
 * <h2>Field mode</h2>
 * The annotation is applied on a {@linkplain net.minecraftforge.registries.DeferredRegister DeferredRegister} field. The transformer will scan all properties of the DeferredRegister's type in the field's class, and convert them to registry objects,
 * with getters calling {@linkplain net.minecraftforge.registries.RegistryObject#get()}.
 *
 * The way registry name are determined can be found in the {@linkplain ga.ozli.minecraftmods.groovylicious.transform.registroid.RegistrationName RegistrationName} documentation.
 */
@Documented
@CompileStatic
@Retention(RetentionPolicy.SOURCE)
@Target([ElementType.FIELD, ElementType.TYPE])
@GroovyASTTransformationClass(value = 'ga.ozli.minecraftmods.groovylicious.transform.registroid.RegistroidASTTransformer')
@interface Registroid {
    boolean includeInnerClasses() default false

    boolean registerToBus() default true

    Class<? extends Closure<?>> value() default {}
}
