package ga.ozli.minecraftmods.groovylicious.dsl.modsdotgroovy

import groovy.contracts.Requires
import groovy.transform.CompileStatic
import net.minecraftforge.forgespi.language.IModInfo
import org.apache.groovy.lang.annotation.Incubating

import javax.annotation.Nullable

@Incubating
@CompileStatic
interface Dependency {
    default @Nullable String modId = null
    default boolean mandatory = true
    default @Nullable String versionRange = null
    default IModInfo.Ordering ordering = IModInfo.Ordering.NONE
    default IModInfo.DependencySide side = IModInfo.DependencySide.BOTH

    @Requires({ this.modId && this.versionRange })
    default ImmutableModDependency build() {
        return new ImmutableModDependency(modId, mandatory, versionRange, ordering, side)
    }
}
