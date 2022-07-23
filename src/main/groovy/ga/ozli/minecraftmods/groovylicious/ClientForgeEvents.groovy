package ga.ozli.minecraftmods.groovylicious

import com.mojang.blaze3d.vertex.PoseStack
import ga.ozli.minecraftmods.groovylicious.dsl.ScreenBuilder
import groovy.transform.CompileStatic
import net.minecraft.client.gui.components.Button
import net.minecraft.client.gui.screens.Screen
import net.minecraft.client.gui.screens.TitleScreen
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.client.event.ScreenEvent
import net.thesilkminer.mc.austin.api.EventBus
import net.thesilkminer.mc.austin.api.EventBusSubscriber

import static ga.ozli.minecraftmods.groovylicious.api.gui.ColoursRegistry.instance as Colours

@CompileStatic
@EventBusSubscriber(modId = Groovylicious.MOD_ID, bus = EventBus.FORGE, dist = Dist.CLIENT)
class ClientForgeEvents {

    //@SubscribeEvent
    static void onScreenOpen(final ScreenEvent.Opening event) {
        if (event.currentScreen instanceof TitleScreen)
            event.newScreen = testScreenDSL()
    }

    static Screen testScreenDSL() {

        return ScreenBuilder.makeScreen("Test screen") {
            println title // prints TextComponent{text='Test screen', ...}
            drawBackground = true

            button {
                text "Test button"
                position x: 10,
                         y: 10

                size width: 100,
                     height: 20

                onPress {
                    println "Clicked!"
                }

                onTooltip { Button button, PoseStack poseStack, int mouseX, int mouseY ->
                    println "Tooltip"
                }
            }

            label {
                text "Lorem ipsum"
                position x: 10, y: 40
                textColour Colours.AQUA
            }

            editBox {
                text "Edit me!"
                position x: 10, y: 80
                size width: 100, height: 20
            }

            plainTextButton {
                text "Plain text button"
                position x: 10, y: 120
                size width: 100, height: 20
                onPress {
                    println "Clicked!"
                }
            }
        }

//        return new ExtensibleScreen("Test screen").tap {
//            drawBackground = true
//            onInit << { ExtensibleScreen thisScreen ->
//                thisScreen.addRenderableWidget(
//                        new Button(10, 10, 100, 20, new TextComponent("Test"), () -> {})
//                )
//            }
//        }
    }
}
