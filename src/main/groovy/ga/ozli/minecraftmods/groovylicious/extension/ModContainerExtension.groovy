package ga.ozli.minecraftmods.groovylicious.extension

import groovy.transform.CompileStatic
import groovy.transform.stc.POJO
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.screens.Screen
import net.minecraftforge.client.ConfigScreenHandler
import net.minecraftforge.fml.ModContainer
import io.github.groovymc.cgl.api.extension.EnvironmentExtension

import java.util.function.BiFunction
import java.util.function.Function

import static io.github.groovymc.cgl.api.extension.EnvironmentExtension.Side

@POJO
@CompileStatic
@EnvironmentExtension(Side.CLIENT)
class ModContainerExtension {
    /**
     * Registers a config screen for your mod so that the "Config" button on the Mods menu will open the provided screen.
     *
     * @param screenBiFunction Provides a Minecraft instance and the screen that the config screen should return to when closed.
     * @author Paint_Ninja via Groovylicious
     */
    static void registerConfigScreen(ModContainer self, BiFunction<Minecraft, Screen, Screen> screenBiFunction) {
        self.registerExtensionPoint(
                ConfigScreenHandler.ConfigScreenFactory,
                () -> new ConfigScreenHandler.ConfigScreenFactory(screenBiFunction)
        )
    }

    /**
     * Registers a config screen for your mod so that the "Config" button on the Mods menu will open the provided screen.
     *
     * @param screenFunction Provides the screen that the config screen should return to when closed.
     * @author Paint_Ninja via Groovylicious
     */
    static void registerConfigScreen(ModContainer self, Function<Screen, Screen> screenFunction) {
        self.registerExtensionPoint(
                ConfigScreenHandler.ConfigScreenFactory,
                () -> new ConfigScreenHandler.ConfigScreenFactory((Minecraft mcInstance, Screen returnTo) -> {
                    return screenFunction.apply(returnTo)
                })
        )
    }

    /**
     * Registers a config screen for your mod so that the "Config" button on the Mods menu will open the provided ExtensibleScreen.
     * <p>Inside your extensible screen, you can use {@code Minecraft.instance.screen = this.returnToScreen} to return to
     * the config screen.</p>
     *
     * @param screen The ExtensibleScreen to register the config screen for.
     * @author Paint_Ninja via Groovylicious
     */
//    static void registerConfigScreen(ModContainer self, ExtensibleScreen screen) {
//        self.registerExtensionPoint(
//                ConfigScreenHandler.ConfigScreenFactory,
//                () -> new ConfigScreenHandler.ConfigScreenFactory((Minecraft mcInstance, Screen returnTo) -> {
//                    screen.returnToScreen = returnTo
//                    return screen
//                })
//        )
//    }
}
