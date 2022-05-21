package ga.ozli.minecraftmods.groovylicious.transform

import static org.codehaus.groovy.control.customizers.builder.CompilerCustomizationBuilder.withConfig
import org.codehaus.groovy.control.CompilerConfiguration

withConfig(configuration) {
    CompilerConfiguration.GROOVYDOC
    System.setProperty('groovy.attach.groovydoc', 'true')
}
