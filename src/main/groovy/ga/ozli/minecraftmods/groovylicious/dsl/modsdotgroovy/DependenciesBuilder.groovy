package ga.ozli.minecraftmods.groovylicious.dsl.modsdotgroovy


import groovy.transform.CompileStatic
import groovy.transform.stc.ClosureParams
import groovy.transform.stc.SimpleType
import org.apache.groovy.lang.annotation.Incubating

import static groovy.lang.Closure.DELEGATE_ONLY

@Incubating
@CompileStatic
class DependenciesBuilder extends HashMap {

    private List<ImmutableModDependency> dependencies = []

    void mod(@DelegatesTo(value = ModDependency, strategy = DELEGATE_ONLY)
             @ClosureParams(value = SimpleType, options = 'ga.ozli.minecraftmods.groovylicious.dsl.modsdotgroovy.ModDependency') final Closure closure) {
        final modDependency = new ModDependency()
        closure.delegate = modDependency
        closure.resolveStrategy = DELEGATE_ONLY
        closure.call(modDependency)
        dependencies << modDependency.build()
    }

    void mod(final String modId,
             @DelegatesTo(value = ModDependency, strategy = DELEGATE_ONLY)
             @ClosureParams(value = SimpleType, options = 'ga.ozli.minecraftmods.groovylicious.dsl.modsdotgroovy.ModDependency') final Closure closure) {
        final modDependency = new ModDependency()
        modDependency.modId = modId
        closure.delegate = modDependency
        closure.resolveStrategy = DELEGATE_ONLY
        closure.call(modDependency)
        dependencies << modDependency.build()
    }

    void minecraft(@DelegatesTo(value = MinecraftDependency, strategy = DELEGATE_ONLY)
                   @ClosureParams(value = SimpleType, options = 'ga.ozli.minecraftmods.groovylicious.dsl.modsdotgroovy.MinecraftDependency') final Closure closure) {
        final minecraftDependency = new MinecraftDependency()
        closure.delegate = minecraftDependency
        closure.resolveStrategy = DELEGATE_ONLY
        closure.call(minecraftDependency)
        dependencies << minecraftDependency.build()
    }

    void minecraft(final String versionRange) {
        final minecraftDependency = new MinecraftDependency()
        minecraftDependency.versionRange = versionRange
        dependencies << minecraftDependency.build()
    }

    void forge(@DelegatesTo(value = ForgeDependency, strategy = DELEGATE_ONLY)
               @ClosureParams(value = SimpleType, options = 'ga.ozli.minecraftmods.groovylicious.dsl.modsdotgroovy.ForgeDependency') final Closure closure) {
        final forgeDependency = new ForgeDependency()
        closure.delegate = forgeDependency
        closure.resolveStrategy = DELEGATE_ONLY
        closure.call(forgeDependency)
        dependencies << forgeDependency.build()
    }

    void forge(final String versionRange) {
        final forgeDependency = new ForgeDependency()
        forgeDependency.versionRange = versionRange
        dependencies << forgeDependency.build()
    }

    void methodMissing(String name,
                       @DelegatesTo(value = ForgeDependency, strategy = DELEGATE_ONLY)
                       @ClosureParams(value = SimpleType, options = 'ga.ozli.minecraftmods.groovylicious.dsl.modsdotgroovy.ModDependency') final Closure closure) {
        mod(name, closure)
    }

    List<ImmutableModDependency> build() {
        this.each { key, value ->
            key = key as String

            final dependency = new ModDependency()
            if (value instanceof Closure) {
                final closure = value as Closure
                dependency.modId = key as String
                closure.delegate = dependency
                closure.resolveStrategy = DELEGATE_ONLY
                closure.call(dependency)
            } else {
                // assume key and value are both strings
                dependency.modId = key as String
                dependency.versionRange = value as String
            }
            dependencies << dependency.build()
        }
        return dependencies
    }

}
