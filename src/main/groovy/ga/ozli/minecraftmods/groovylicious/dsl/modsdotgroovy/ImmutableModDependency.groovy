package ga.ozli.minecraftmods.groovylicious.dsl.modsdotgroovy

import groovy.transform.AutoFinal
import groovy.transform.CompileStatic
import groovy.transform.Immutable
import net.minecraftforge.forgespi.language.IModInfo
import org.apache.groovy.lang.annotation.Incubating

@AutoFinal
@Immutable
@Incubating
@CompileStatic
class ImmutableModDependency extends ModDependency implements Dependency {
    String modId
    boolean mandatory
    String versionRange
    IModInfo.Ordering ordering
    IModInfo.DependencySide side
}
