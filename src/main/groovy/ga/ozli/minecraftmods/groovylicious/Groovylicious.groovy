package ga.ozli.minecraftmods.groovylicious

import ga.ozli.minecraftmods.groovylicious.api.gui.Colour

import groovy.transform.CompileStatic
import groovy.transform.PackageScope
import net.minecraftforge.common.ForgeConfigSpec
import net.thesilkminer.mc.austin.api.Mod

import static ga.ozli.minecraftmods.groovylicious.api.gui.Colours.instance as Colours

@CompileStatic//(extensions = ['ga.ozli.minecraftmods.groovylicious.transform.typecheckers.ColourTypeChecker'])
@Mod(MOD_ID)
class Groovylicious {
    @PackageScope static final String MOD_ID = "groovylicious"

    Groovylicious() {
//        Config.init()
//        AstBuilder builder = new AstBuilder()
//        List<ASTNode> nodes = builder.buildFromString('println "${this.getModule().getName()}-common.toml"')
//        nodes.each {
//            println SV(it)
//        }
        testColoursAPI()
        println ''
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

//        Colours.MyOtherClass.metaClass.someAPI = { i ->
//            delegate.someAPI(i as Integer)
//        }
//        Colours.MyOtherClass.someAPI(Colours.MyClass)
//
//        Dsl.coloursDsl.alpha

//        println Colours.White.alpha
//        println Colours.White

//        ColoursNew.define("magenta", 255, 0, 255)
//        println ColoursNew.Magenta

//        // what I have
//        println ColoursNew.WHITE.argb
//        println ColoursNew.White

        //ArrayList<Integer> list = List.of(1, 2, 3)

//        println Colours.White
//        println Colours.White as int
//        println Colours.White.packed
//        println Colours.White.argb


    }

//    @ModConfig
    static class Config {
        // This is optional:
        static ForgeConfigSpec.Builder myBuilder = new ForgeConfigSpec.Builder()
        static ForgeConfigSpec.LongValue test = myBuilder.defineInRange('test', 0L, Long.MIN_VALUE, Long.MAX_VALUE)

        /** How fast do you need to drive to accomplish time travel? {@range 50..100} */
        static int timeTravelMph = 88

        /**
         * Fox rotation speed in RPM
         * 100 is slow, 9999 is fast
         */
        static float foxRotation = 9000.42f

        /** How far are you willing to walk to see the messiah? (in blocks) */
        static long willingToWalkDistance = 2000L

        /** The holy words of the messiah's mother */
        static String lifeOfBrianQuote = "He's not the messiah, he's a very naughty boy!"

        /** Is the parrot alive? */
        static boolean parrot = false

        /**
         * For those cases where you need to be really specific with lots of decimal places
         * {@range 1.0..10.0}
         */
        static double reallySpecific = 7.55555555555556d // the {@range} is optional

        // a config value with no default, no range and no groovydoc comment
        static String noDefaultString

        static void init() {
            // this method's just here to load this config class
        }
    }
}