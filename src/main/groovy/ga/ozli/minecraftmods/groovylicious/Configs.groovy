package ga.ozli.minecraftmods.groovylicious

import ga.ozli.minecraftmods.groovylicious.transform.ModConfig
import groovy.transform.CompileStatic
import groovy.transform.stc.POJO
import net.minecraftforge.common.ForgeConfigSpec

@POJO
@CompileStatic
class Configs {

    @ModConfig
    static class Common {
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

        // config groups are supported (WIP) - simply add inner static classes
        static class LifeOfBrian {
            /** How far are you willing to walk to see the messiah? (in blocks) */
            static long willingToWalkDistance = 2000L

            /** The holy words of the messiah's mother */
            static String quote = "He's not the messiah, he's a very naughty boy!"

            // optional
            /*static void initGroup() {

            }*/
        }

        /** Is the parrot alive? */
        static boolean parrot = true

        /**
         * For those cases where you need to be really specific with lots of decimal places
         * {@range 1.0..10.0}
         */
        static double reallySpecific = 7.55555555555556d // the {@range} is optional

        // a config value with no default, no range and no groovydoc comment
        static String noDefaultString

        // optional
        // make sure to call this on mod construction if explicitly declared or if the config is in a separate class
        // from the mod's main class (to classload the config dataclass)
        /*static void init() {
            // this method's just here to load this config class
        }*/
    }
}
