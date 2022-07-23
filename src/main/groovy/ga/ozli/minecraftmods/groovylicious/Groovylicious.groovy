package ga.ozli.minecraftmods.groovylicious

import com.mojang.logging.LogUtils
import ga.ozli.minecraftmods.groovylicious.api.gui.Colour
import ga.ozli.minecraftmods.groovylicious.dsl.ModsDotGroovy
import groovy.transform.CompileDynamic
import groovy.transform.CompileStatic
import groovy.transform.PackageScope
import net.minecraftforge.forgespi.language.IModInfo
import net.thesilkminer.mc.austin.api.Mod
import org.slf4j.Logger

import static ga.ozli.minecraftmods.groovylicious.api.gui.ColoursRegistry.instance as Colours

@CompileStatic//(extensions = ['ga.ozli.minecraftmods.groovylicious.transform.typecheckers.ColourTypeChecker'])
@Mod(MOD_ID)
class Groovylicious {
    @PackageScope static final String MOD_ID = "groovylicious"
    private static final Logger LOGGER = LogUtils.getLogger()

    Groovylicious() {
//        testColoursAPI()
//        doDynamicStuff()
    }

    @CompileDynamic
    void doDynamicStuff() {
        //Configs.Common.init()

        ModsDotGroovy.makeToml {
            license = "MIT"

            // Alias for mods { mod { ... } }
            info {
                modId = MOD_ID
                version = "${file.jarVersion}"
                logoFile = 'logo.png'
                author = 'Paint_Ninja'
                description = '''
                Delicious syntax sugar with the power of Groovy!
                '''
            }

            // Similar to the above, but supports multiple mods
            mods {
                modInfo {
                    modId = MOD_ID
                    version = "${file.jarVersion}"
                    logoFile = 'logo.png'
                    author = 'Paint_Ninja'
                    description = '''
                    Delicious syntax sugar with the power of Groovy!
                    '''
                }
            }

            dependencies {
                forge: '[40.1.60,)'
                minecraft: '[1.18.2,1.19)'

                forge {
                    versionRange = "[40.1.60,)"
                }

                minecraft {
                    versionRange = "[1.18.2,1.19)"
                }

                mod {
                    modId = "examplemod"
                    versionRange = "[1.0.0,1.1.0)"
                    side = IModInfo.DependencySide.CLIENT
                    mandatory = false
                    ordering = IModInfo.Ordering.AFTER
                }

                // equiv to the mod dependency entry above
                // todo: support Map<String, Closure> inside dependencies block
//                examplemod: {
//                    versionRange = "[1.0.0,1.1.0)"
//                    side = IModInfo.DependencySide.CLIENT
//                    mandatory = false
//                    ordering = IModInfo.Ordering.AFTER
//                }
            }
        }
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