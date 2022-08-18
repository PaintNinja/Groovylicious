package groovylicioustest

import ga.ozli.minecraftmods.groovylicious.transform.Config
import ga.ozli.minecraftmods.groovylicious.transform.config.ConfigGroup
import ga.ozli.minecraftmods.groovylicious.transform.config.ConfigValue
import groovy.transform.CompileStatic
import groovy.transform.stc.POJO
import net.minecraftforge.common.ForgeConfigSpec
import net.minecraftforge.fml.config.ModConfig

@POJO
@CompileStatic
class Configs {

    @Config
    static class Client { // this is saved as groovylicious-client.toml because the class name contains "Client"
        static boolean enable = true
        static Boolean enable2 = false

        static int yes = 14
        static Integer no = 200

        static List<String> stuff = ['12']
    }

//    @Config
    static class GroupOnlyTest {
        static class Group {
            static boolean enable = true
        }
        static class OtherOne {
            static int yes = 12
        }
    }

//    @Config
    static class RangeTest {
        /**
         * Ranged short test
         * {@range 12..2000}
         */
        static short rangedShort = 120

        /**
         * Ranged int test
         * @range 13..3000
         */
        static int rangedInt = 130
    }

    // @Config(value = ModConfig.Type.CLIENT)
    static class Common {
        // This is optional:
        static ForgeConfigSpec.Builder myBuilder = new ForgeConfigSpec.Builder()

        /** How fast do you need to drive to accomplish time travel? {@range 50..100} */
        @ConfigValue(exclude = true)
        static byte timeTravelMph = 88

        /**
         * Fox rotation speed in RPM<br>
         * 100 is slow, 9999 is fast
         */
        @ConfigValue(name = 'shush')
        static float foxRotation = 9000.42f

        @ConfigValue(name = 'testList')
        static List<String> myValues

        // config groups are supported - simply add inner static classes
        @ConfigGroup(name = 'hi', excludeFieldsWithoutAnnotation = true)
        static class LifeOfBrian {
            /** How far are you willing to walk to see the messiah? (in blocks) */
            static long willingToWalkDistance = 2000L

            /** The holy words of the messiah's mother */
            @ConfigValue(name = 'customName', validator = { String it ->
                if (it === null) return true
                final isValid = it.contains('he')
                if (!isValid) throw new RuntimeException()
                return isValid
            })
            static String quote = "He's not the messiah, he's a very naughty boy!"

            // optional
            /*static void init() {

            }*/

            @CompileStatic
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
