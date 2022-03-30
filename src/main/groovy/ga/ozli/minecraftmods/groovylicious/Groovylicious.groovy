package ga.ozli.minecraftmods.groovylicious

import ga.ozli.minecraftmods.groovylicious.transform.ModConfig
import groovy.transform.CompileStatic
import groovy.transform.PackageScope
import groovy.util.logging.Log4j2
import net.thesilkminer.mc.austin.api.Mod

@CompileStatic
@Mod(MOD_ID)
class Groovylicious {
    @PackageScope static final String MOD_ID = "groovylicious"

    Groovylicious() {
        Config.init()
    }

    @ModConfig(modId = 'groovylicious')
    static class Config {
        // This is optional:
        //static ForgeConfigSpec.Builder myBuilder = new ForgeConfigSpec.Builder()

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