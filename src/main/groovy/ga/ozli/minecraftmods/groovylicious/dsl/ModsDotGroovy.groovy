package ga.ozli.minecraftmods.groovylicious.dsl

import ga.ozli.minecraftmods.groovylicious.dsl.modsdotgroovy.DependenciesBuilder
import ga.ozli.minecraftmods.groovylicious.dsl.modsdotgroovy.ImmutableModDependency
import ga.ozli.minecraftmods.groovylicious.dsl.modsdotgroovy.ImmutableModInfo
import ga.ozli.minecraftmods.groovylicious.dsl.modsdotgroovy.ModInfoBuilder
import ga.ozli.minecraftmods.groovylicious.dsl.modsdotgroovy.ModsBuilder
import groovy.transform.CompileStatic
import groovy.transform.stc.ClosureParams
import groovy.transform.stc.SimpleType
import org.apache.groovy.lang.annotation.Incubating

import static groovy.lang.Closure.DELEGATE_FIRST

@Incubating
@CompileStatic
class ModsDotGroovy {

    private List<ImmutableModInfo> mods = []
    private List<ImmutableModDependency> dependencies = []

    String loaderVersion = '[1,)'

    /**
     * The license for your mod. This is mandatory metadata and allows for easier comprehension of your redistributive properties.<br>
     * Review your options at https://choosealicense.com/. All rights reserved is the default copyright stance, and is thus the default here.
     */
    String license = null

    /**
     * A URL to refer people to when problems occur with this mod.
     */
    String issueTrackerURL = null

    void info(@DelegatesTo(value = ModInfoBuilder, strategy = DELEGATE_FIRST)
             @ClosureParams(value = SimpleType, options = 'ga.ozli.minecraftmods.groovylicious.dsl.modsdotgroovy.ModInfoBuilder') final Closure closure) {
        final modInfoBuilder = new ModInfoBuilder()
        closure.delegate = modInfoBuilder
        closure.resolveStrategy = DELEGATE_FIRST
        closure.call(modInfoBuilder)
        mods = [modInfoBuilder.build()]
    }

    void modInfo(@DelegatesTo(value = ModInfoBuilder, strategy = DELEGATE_FIRST)
                 @ClosureParams(value = SimpleType, options = 'ga.ozli.minecraftmods.groovylicious.dsl.modsdotgroovy.ModInfoBuilder') final Closure closure) {
        info(closure)
    }

    void mods(@DelegatesTo(value = ModsBuilder, strategy = DELEGATE_FIRST)
              @ClosureParams(value = SimpleType, options = 'ga.ozli.minecraftmods.groovylicious.dsl.modsdotgroovy.ModsBuilder') final Closure closure) {
        final modsBuilder = new ModsBuilder()
        closure.delegate = modsBuilder
        closure.resolveStrategy = DELEGATE_FIRST
        closure.call(modsBuilder)
        this.mods = modsBuilder.mods
    }

    void dependencies(@DelegatesTo(value = DependenciesBuilder, strategy = DELEGATE_FIRST)
                      @ClosureParams(value = SimpleType, options = 'ga.ozli.minecraftmods.groovylicious.dsl.modsdotgroovy.DependenciesBuilder') final Closure closure) {
        final dependenciesBuilder = new DependenciesBuilder()
        closure.delegate = dependenciesBuilder
        closure.resolveStrategy = DELEGATE_FIRST
        closure.call(dependenciesBuilder)
        this.dependencies = dependenciesBuilder.build()
    }

    static void makeToml(@DelegatesTo(value = ModsDotGroovy, strategy = DELEGATE_FIRST)
                                  @ClosureParams(value = SimpleType, options = 'ga.ozli.minecraftmods.groovylicious.dsl.ModsDotGroovy') final Closure closure) {
        final modsDotGroovy = new ModsDotGroovy()
        closure.delegate = modsDotGroovy
        closure.resolveStrategy = DELEGATE_FIRST
        closure.call(modsDotGroovy)
    }

}
