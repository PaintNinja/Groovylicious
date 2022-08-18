package ga.ozli.minecraftmods.groovylicious.transform

import groovy.transform.CompileStatic
import org.codehaus.groovy.transform.GroovyASTTransformationClass

@CompileStatic
@GroovyASTTransformationClass(value = 'ga.ozli.minecraftmods.groovylicious.transform.defregister.DefRegisterASTTransformer')
@interface AutoRegister {
}
