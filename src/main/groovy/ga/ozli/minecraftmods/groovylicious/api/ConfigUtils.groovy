package ga.ozli.minecraftmods.groovylicious.api


import groovy.transform.CompileStatic
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.screens.Screen
import net.minecraftforge.client.ConfigScreenHandler
import net.minecraftforge.fml.ModList

import java.util.function.BiFunction
import java.util.function.Function

@CompileStatic
class ConfigUtils {

    /**
     * Register a config ExtensibleScreen for the given modId.<br>
     * <br>
     * Inside your extensible screen, you can use {@code Minecraft.instance.screen = this.returnToScreen} to return to
     * the config screen.
     *
     * @param modId The modId of the mod you want to register the config screen for.
     * @param screen The ExtensibleScreen to register the config screen for.
     *
     * @throws NoSuchElementException if the modId cannot be found.
     */
//    static void registerConfigScreen(String modId, ExtensibleScreen screen) throws NoSuchElementException {
//        ModList.get().getModContainerById(modId).get().registerExtensionPoint(
//                ConfigScreenHandler.ConfigScreenFactory,
//                () -> new ConfigScreenHandler.ConfigScreenFactory((Minecraft mcInstance, Screen returnTo) -> {
//                    screen.returnToScreen = returnTo
//                    return screen
//                })
//        )
//    }

    /**
     * Similar to {@link #registerConfigScreen(String, BiFunction)} but without the mcInstance (you can use {@code Minecraft.instance} instead).
     *
     * @param modId The modId of the mod you want to register the config screen for.
     * @param function Provides the returnTo screen and expects your config screen as the result.
     */
    static void registerConfigScreen(String modId, Function<Screen, Screen> function) throws NoSuchElementException {
        ModList.get().getModContainerById(modId).get().registerExtensionPoint(
                ConfigScreenHandler.ConfigScreenFactory,
                () -> new ConfigScreenHandler.ConfigScreenFactory((Minecraft mcInstance, Screen returnTo) -> {
                    return function.apply(returnTo)
                })
        )
    }

    static void registerConfigScreen(String modId, BiFunction<Minecraft, Screen, Screen> biFunction) throws NoSuchElementException {
        ModList.get().getModContainerById(modId).get().registerExtensionPoint(
                ConfigScreenHandler.ConfigScreenFactory,
                () -> new ConfigScreenHandler.ConfigScreenFactory(biFunction)
        )
    }
}
