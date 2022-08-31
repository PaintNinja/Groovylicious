package ga.ozli.minecraftmods.groovylicious

import com.matyrobbrt.gml.GMod
import groovy.transform.CompileStatic
import groovy.transform.PackageScope
import groovy.transform.stc.POJO
import net.minecraft.resources.ResourceLocation
import net.minecraft.sounds.SoundEvent
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@POJO
@GMod(MOD_ID)
@CompileStatic
class Groovylicious {
    @PackageScope static final String MOD_ID = 'groovylicious'
    static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID)

    Groovylicious() {
        LOGGER.info "${MOD_ID.capitalize()} starting"
        LOGGER.info SV(GroovySystem.version)

//        // Setup String->ResourceLocation type inference
//        SoundEvent.metaClass.constructor << { String path ->
//            return new SoundEvent(new ResourceLocation('example', path))
//        }
    }
}
