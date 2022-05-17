package ga.ozli.minecraftmods.groovylicious.transform

import org.codehaus.groovy.ast.ClassNode
import org.codehaus.groovy.classgen.GeneratorContext
import org.codehaus.groovy.control.SourceUnit

import static org.codehaus.groovy.control.customizers.builder.CompilerCustomizationBuilder.withConfig
import org.codehaus.groovy.control.CompilerConfiguration

withConfig(configuration) {
    CompilerConfiguration.GROOVYDOC
    System.setProperty('groovy.attach.groovydoc', 'true')

//    withConfig(configuration) {
//        inline(phase:'CONVERSION') { SourceUnit source, GeneratorContext context, ClassNode classNode ->
//            context.compileUnit.classes.each { ClassNode clazz ->
//                if (clazz.annotations.contains('IDESideOnly'))
//                    //context.class.
//                    source = new SourceUnit(source.getName(), '', configuration, null, null)
//            }
//        }
//    }
}
