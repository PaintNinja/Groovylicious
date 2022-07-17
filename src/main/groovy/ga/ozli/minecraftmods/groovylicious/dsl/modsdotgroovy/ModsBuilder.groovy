package ga.ozli.minecraftmods.groovylicious.dsl.modsdotgroovy

import groovy.transform.CompileStatic
import groovy.transform.stc.ClosureParams
import groovy.transform.stc.SimpleType
import org.apache.groovy.lang.annotation.Incubating

import static groovy.lang.Closure.DELEGATE_FIRST

@Incubating
@CompileStatic
class ModsBuilder {
    private List<ImmutableModInfo> mods = []

    List<ImmutableModInfo> getMods() {
        return mods
    }

    void modInfo(@DelegatesTo(value = ModInfoBuilder, strategy = DELEGATE_FIRST)
              @ClosureParams(value = SimpleType, options = 'ga.ozli.minecraftmods.groovylicious.dsl.modsdotgroovy.ModInfoBuilder') final Closure closure) {
        final modInfoBuilder = new ModInfoBuilder()
        closure.delegate = modInfoBuilder
        closure.resolveStrategy = DELEGATE_FIRST
        closure.call(modInfoBuilder)
        mods << modInfoBuilder.build()
    }
}
