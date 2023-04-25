package groovylicioustest

import com.matyrobbrt.gml.GMod
import com.mojang.logging.LogUtils

import ga.ozli.minecraftmods.groovylicious.api.gui.Colour
import groovy.transform.CompileStatic
import groovy.transform.PackageScope
import groovy.transform.stc.POJO
import org.slf4j.Logger

import static ga.ozli.minecraftmods.groovylicious.api.gui.ColoursRegistry.instance as Colours

@POJO
@CompileStatic
@GMod('groovylicioustest')
class GroovyliciousTest {
    @PackageScope static final Logger log = LogUtils.getLogger(this)
    @PackageScope static final String MOD_ID = 'groovylicioustest'

    GroovyliciousTest() {
        log.info "GroovyliciousTest starting"
        final Colour purple = new Colour(102, 61, 128)
        Colours.PURPLE = purple
        println purple
    }
}
