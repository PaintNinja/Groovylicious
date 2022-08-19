package ga.ozli.minecraftmods.groovylicious.transform

import groovy.transform.CompileStatic
import org.codehaus.groovy.transform.GroovyASTTransformationClass

import java.lang.annotation.Documented
import java.lang.annotation.ElementType
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import java.lang.annotation.Target

@Documented
@CompileStatic
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.SOURCE)
@GroovyASTTransformationClass(value = 'ga.ozli.minecraftmods.groovylicious.transform.defregister.DefRegisterASTTransformer')
@interface AutoRegister {
    boolean includeInnerClasses() default false

    // TODO implement
    // boolean registerToBus() default true
}
