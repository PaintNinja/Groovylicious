package ga.ozli.minecraftmods.groovylicious

import com.mojang.logging.LogUtils
import groovy.transform.CompileStatic
import groovy.transform.PackageScope
import groovy.transform.stc.POJO
import net.minecraft.util.Mth
import net.thesilkminer.mc.austin.api.Mod
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@POJO
@CompileStatic
@Mod(MOD_ID)
class Groovylicious {
    @PackageScope static final String MOD_ID = 'groovylicious'
    private static final Logger LOGGER = LoggerFactory.getLogger(Groovylicious)

    Groovylicious() {
        LOGGER.info "${MOD_ID.capitalize()} starting"
    }
}
