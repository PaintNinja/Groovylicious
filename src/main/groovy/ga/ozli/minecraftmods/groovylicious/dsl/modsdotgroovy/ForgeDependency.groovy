package ga.ozli.minecraftmods.groovylicious.dsl.modsdotgroovy

import groovy.transform.CompileStatic
import net.minecraftforge.forgespi.language.IModInfo
import org.apache.groovy.lang.annotation.Incubating

@Incubating
@CompileStatic
class ForgeDependency implements Dependency {
    final String modId = 'forge'
    final boolean mandatory = true
    final IModInfo.Ordering ordering = IModInfo.Ordering.NONE

    String versionRange

    /**
     * Side this dependency is applied on - BOTH, CLIENT or SERVER
     */
    IModInfo.DependencySide side
}
