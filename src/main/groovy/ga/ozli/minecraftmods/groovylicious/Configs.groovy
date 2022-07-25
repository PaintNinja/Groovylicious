package ga.ozli.minecraftmods.groovylicious

import ga.ozli.minecraftmods.groovylicious.transform.Config
import groovy.transform.CompileStatic
import groovy.transform.stc.POJO
import net.minecraftforge.common.ForgeConfigSpec

@POJO
@CompileStatic
class Configs {

    //@Config
    static class Client { // this is saved as groovylicious-client.toml because the class name contains "Client"
        static boolean enable = true
    }

    //@Config
    static class Common {
        // This is optional:
        static ForgeConfigSpec.Builder myBuilder = new ForgeConfigSpec.Builder()
//            static ForgeConfigSpec.LongValue test = myBuilder.defineInRange('test', 0L, Long.MIN_VALUE, Long.MAX_VALUE) // todo: make getters/setters for explicitly defined ForgeConfigSpec.ConfigValue/IntValue/etc

        /** How fast do you need to drive to accomplish time travel? {@range 50..100} */
        static byte timeTravelMph = 88

        /**
         * Fox rotation speed in RPM<br>
         * 100 is slow, 9999 is fast
         */
        static float foxRotation = 9000.42f

        // config groups are supported - simply add inner static classes
        static class LifeOfBrian {
            /** How far are you willing to walk to see the messiah? (in blocks) */
            static long willingToWalkDistance = 2000L

            /** The holy words of the messiah's mother */
            static String quote = "He's not the messiah, he's a very naughty boy!"

            // optional
            /*static void init() {

            }*/

            static class Nested {
                static boolean nestedConfigsSupport = true

                static class AllTheNesting {
                    static boolean limitlessNesting = true
                }
            }
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

        static int numberOfCertifiedSillyWalks = 3000

        static short ducksCount = 101

        // optional
        // make sure to call this on mod construction if explicitly declared or if the config is in a separate class
        // from the mod's main class (to classload the config dataclass)
        /*static void init() {
            // this method's just here to load this config class
        }*/
    }

}
