package groovylicioustest

import com.matyrobbrt.gml.bus.EventBusSubscriber
import com.matyrobbrt.gml.bus.type.ModBus
import com.matyrobbrt.gml.util.Environment
import com.mojang.blaze3d.vertex.PoseStack
import ga.ozli.minecraftmods.groovylicious.dsl.ScreenBuilder
import groovy.transform.CompileDynamic
import groovy.transform.CompileStatic
import groovy.transform.TypeChecked
import net.minecraft.client.gui.components.Button
import net.minecraft.client.gui.components.EditBox
import net.minecraft.client.gui.screens.OptionsScreen
import net.minecraft.client.gui.screens.Screen
import net.minecraft.client.gui.screens.TitleScreen
import net.minecraft.network.chat.CommonComponents
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.client.event.ScreenEvent
import net.minecraftforge.event.CreativeModeTabEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import static groovylicioustest.GroovyliciousTest.log

@CompileStatic
@EventBusSubscriber(dist = Dist.CLIENT, environment = Environment.DEV)
class ClientForgeEvents {

    @SubscribeEvent
    static void onScreenOpen(final ScreenEvent.Opening event) {
        if (event.newScreen instanceof TitleScreen /*&& event.currentScreen === null*/)
//            event.newScreen = new TestScreen()
            event.newScreen = ScreenTests.testScreenDSL()
    }

    static class TestScreen extends Screen {
        TestScreen() {
            super(Component.literal("Test screen"))
        }

        @Override
        void render(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
            this.renderBackground(poseStack)
            super.render(poseStack, mouseX, mouseY, partialTicks)
        }

        @Override
        protected void init() {
            super.init()

            // Vanilla
//            this.addRenderableWidget(
//                    Button.builder(Component.literal("Button"), (Button button) -> log.info('Button pressed!'))
//                            .pos(this.width - 105, this.height - 25)
//                            .width(100)
//                            .build()
//            )

            this.addRenderableWidget(
                    Button.builder {
                        message = 'Button'
                        position this.width - 105, this.height - 25
                        size width: 100, height: 20
                        width = 100
                        onPress = { Button button -> log.info('Button pressed!') }
                    }.build()
            )
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
