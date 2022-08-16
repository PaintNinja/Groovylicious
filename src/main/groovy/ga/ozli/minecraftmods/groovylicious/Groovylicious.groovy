package ga.ozli.minecraftmods.groovylicious

import com.matyrobbrt.gml.GMod
import groovy.transform.CompileStatic
import groovy.transform.PackageScope
import groovy.transform.stc.POJO
import groovy.util.logging.Slf4j

@POJO
@Slf4j(category = MOD_ID)
@GMod(MOD_ID)
@CompileStatic
class Groovylicious {
    @PackageScope static final String MOD_ID = 'groovylicious'

    Groovylicious() {
        log.info "${MOD_ID.capitalize()} starting"
        log.info SV(GroovySystem.version)
    }
}
