package groovylicioustest

import com.matyrobbrt.gml.bus.EventBusSubscriber
import com.matyrobbrt.gml.util.Environment
import com.mojang.blaze3d.vertex.PoseStack
import groovy.transform.CompileStatic
import net.minecraft.client.gui.components.Button
import net.minecraft.client.gui.screens.Screen
import net.minecraft.client.gui.screens.TitleScreen
import net.minecraft.network.chat.CommonComponents
import net.minecraft.network.chat.Component
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.client.event.ScreenEvent
import net.minecraftforge.eventbus.api.SubscribeEvent

@CompileStatic
@EventBusSubscriber(dist = Dist.CLIENT, environment = Environment.DEV)
class ClientForgeEvents {

    //@SubscribeEvent
    static void onScreenOpen(final ScreenEvent.Opening event) {
        if (event.newScreen instanceof TitleScreen)
            event.newScreen = testGuiExtensions()
    }

    static Screen testGuiExtensions() {
        return new Screen(CommonComponents.GUI_DONE) {
            @Override
            protected void init() {
                super.init()
                this.addRenderableWidget(Button.builder {
                    message 'Hello World!'
                    onPress { button ->
                        GroovyliciousTest.log.info 'Button pressed'
                    }
                    position 10, 10
                    size 100, 20
                    bounds x: 10, y: 20,
                           width: 100, height: 20
                })
            }

            @Override
            void render(PoseStack pose, int mouseX, int mouseY, float partialTick) {
                this.renderBackground(pose);
                super.render(pose, mouseX, mouseY, partialTick);
            }
        }
    }

//    static Screen testScreenDSL() {
//
//        return ScreenBuilder.makeScreen("Test screen") {
//            println title // prints TextComponent{message='Test screen', ...}
//            drawBackground = true
//
//            button {
//                message "Test button"
//                position x: 200,
//                         y: 10
//
//                alignment Alignment.RIGHT
//
//                size width: 100,
//                     height: 20
//
//                onPress {
//                    println "Clicked!"
//                }
//
//                onTooltip { Button button, PoseStack poseStack, int mouseX, int mouseY ->
//                    println "Tooltip"
//                }
//
////                tooltip "Tooltip"
//            }
//
//            label {
//                message "Lorem ipsum"
//                position x: 10, y: 40
//                textColour Colours.AQUA
//                alignment Alignment.RIGHT
//            }
//
//            editBox {
//                message "Edit me!"
//                position x: 10, y: 80
//                size width: 100, height: 20
//            }
//
//            plainTextButton {
//                message "Plain message button"
//                position x: 10, y: 120
//                size width: 100, height: 20
//                onPress {
//                    println "Clicked!"
//                }
//            }
//        }
//
////        return new ExtensibleScreen("Test screen").tap {
////            drawBackground = true
////            onInit << { ExtensibleScreen thisScreen ->
////                thisScreen.addRenderableWidget(
////                        new Button(10, 10, 100, 20, new TextComponent("Test"), () -> {})
////                )
////            }
////        }
//    }
}
