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
@Target(ElementType.TYPE)
@GroovyASTTransformationClass('ga.ozli.minecraftmods.groovylicious.transform.mojo.GroovyliciousMojoASTTransformation')
@interface GroovyliciousMojo {}