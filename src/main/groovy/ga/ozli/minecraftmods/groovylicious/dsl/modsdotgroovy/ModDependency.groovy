package ga.ozli.minecraftmods.groovylicious.dsl.modsdotgroovy

import groovy.transform.CompileStatic
import net.minecraftforge.forgespi.language.IModInfo
import org.apache.groovy.lang.annotation.Incubating

@Incubating
@CompileStatic
class ModDependency implements Dependency {
    String modId

    /**
     * Does this dependency have to exist? If not, ordering must also be specified.
     */
    boolean mandatory

    String versionRange

    /**
     * An ordering relationship for the dependency - BEFORE or AFTER required if the relationship is not mandatory
     */
    IModInfo.Ordering ordering

    /**
     * Side this dependency is applied on - BOTH, CLIENT or SERVER
     */
    IModInfo.DependencySide side

    ModDependency() {}

    ModDependency(String modId, boolean mandatory, String versionRange, IModInfo.Ordering ordering, IModInfo.DependencySide side) {
        this.modId = modId
        this.mandatory = mandatory
        this.versionRange = versionRange
        this.ordering = ordering
        this.side = side
    }
}
