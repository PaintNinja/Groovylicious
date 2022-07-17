package ga.ozli.minecraftmods.groovylicious.dsl.modsdotgroovy

import groovy.transform.AutoFinal
import groovy.transform.CompileStatic
import groovy.transform.Immutable
import org.apache.groovy.lang.annotation.Incubating

import javax.annotation.Nullable

@AutoFinal
@Immutable
@Incubating
@CompileStatic
class ImmutableModInfo {
    String modId
    String displayName
    String version
    @Nullable String updateJsonUrl
    @Nullable String displayUrl
    @Nullable String logoFile
    @Nullable String credits
    List<String> authors
    String description
}
