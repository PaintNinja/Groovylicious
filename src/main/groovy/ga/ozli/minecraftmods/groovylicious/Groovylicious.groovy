package ga.ozli.minecraftmods.groovylicious

import com.mojang.logging.LogUtils
import ga.ozli.minecraftmods.groovylicious.api.gui.Colour
import groovy.transform.CompileDynamic
import groovy.transform.CompileStatic
import groovy.transform.PackageScope
import net.thesilkminer.mc.austin.api.Mod
import org.slf4j.Logger

import static ga.ozli.minecraftmods.groovylicious.api.gui.ColoursRegistry.instance as Colours

@CompileStatic//(extensions = ['ga.ozli.minecraftmods.groovylicious.transform.typecheckers.ColourTypeChecker'])
@Mod(MOD_ID)
class Groovylicious {
    @PackageScope static final String MOD_ID = "groovylicious"
    private static final Logger LOGGER = LogUtils.getLogger()

    Groovylicious() {
//        AstBuilder builder = new AstBuilder()
//        List<ASTNode> nodes = builder.buildFromString('println "${this.getModule().getName()}-common.toml"')
//        nodes.each {
//            println SV(it)
//        }
//        testColoursAPI()
//        println ''

//        println "ModID: ${ModLoadingContext.get().activeContainer.modId}"
        doDynamicStuff()
    }

    @CompileDynamic
    static void doDynamicStuff() {
        Configs.Common.init()
    }

    @CompileStatic
    static void testColoursAPI() {
        println Colours.WHITE
        Colours.MAGENTA = new Colour(255, 0, 255)
        println Colours.MAGENTA
        Colours.define("Groovy_Blue", new Colour(red: 53, green: 122, blue: 147))
        println Colours.GROOVY_BLUE

        Colour orange = Colour.of(255, 165, 0)
        println SV(orange)
        println ""
    }

}