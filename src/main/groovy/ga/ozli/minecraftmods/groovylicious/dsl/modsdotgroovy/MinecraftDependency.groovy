package ga.ozli.minecraftmods.groovylicious.dsl.modsdotgroovy

import groovy.transform.CompileStatic
import net.minecraftforge.forgespi.language.IModInfo
import org.apache.groovy.lang.annotation.Incubating

@Incubating
@CompileStatic
class MinecraftDependency implements Dependency, ForceMandatoryTrait {
    final String modId = 'minecraft'

    String versionRange

    /**
     * Side this dependency is applied on - BOTH, CLIENT or SERVER
     */
    IModInfo.DependencySide side
}
