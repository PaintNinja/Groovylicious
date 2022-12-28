package groovylicioustest

import com.mojang.blaze3d.vertex.PoseStack

import ga.ozli.minecraftmods.groovylicious.api.gui.ExtensibleScreen
import ga.ozli.minecraftmods.groovylicious.api.gui.Position
import ga.ozli.minecraftmods.groovylicious.api.gui.Size
import groovy.time.TimeCategory
import groovy.transform.CompileDynamic
import groovy.transform.CompileStatic
import groovy.transform.PackageScope
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.components.Button
import net.minecraft.client.gui.components.CenteredStringWidget
import net.minecraft.client.gui.screens.Screen
import net.minecraft.network.chat.Component

@PackageScope
@CompileStatic
class ScreenTests {
    private static int currentPage = 0

    static Screen testScreenDSL() {
        return getScreen(0)
    }

    static Screen getNextPage() {
        try {
            return getScreen(++currentPage)
        } catch (final MissingPropertyException e) {
            return getScreen(--currentPage)
        }
    }

    static Screen getPreviousPage() {
        try {
            return getScreen(--currentPage)
        } catch (final MissingPropertyException e) {
            return getScreen(++currentPage)
        }
    }

    @CompileDynamic
    private static Screen getScreen(final int index) {
        return Pages."page$index"
    }

    @CompileDynamic // to allow for incremental testing - comment out to test all pages at once
    @SuppressWarnings('unused') // called dynamically by getScreen(int index)
    private static class Pages {
        protected static final Screen page0 = new Screen(Component.literal("Page 0")) {
            int previousLabelYPos = 0

            @Override
            protected void init() {
                super.init()
                this.addRenderableWidget(CenteredStringWidget.builder {
                    message = 'Groovylicious Screen Test'
                    size {
                        width = this.width
                        height = 9
                    }
                    previousLabelYPos = this.height / 3 as int
                    position {
                        x = 0
                        y = previousLabelYPos
                    }
                }.build())
                this.addRenderableWidget(CenteredStringWidget.builder {
                    message = 'Each page introduces new/different features for incremental testing'
                    size {
                        width = this.width
                        height = 9
                    }
                    position {
                        x = 0
                        y = previousLabelYPos + 19
                    }
                }.build())

                this.addRenderableWidget(Button.builder {
                    message = 'Previous page'
                    size = new Size(100, 20)
                    position = new Position(5, this.height - 25)
                    onPress { Button button -> Minecraft.getInstance().setScreen(previousPage) }
                    active = false
                }.build())
                this.addRenderableWidget(CenteredStringWidget.builder {
                    message = 'Page 0'
                    size {
                        width = this.width
                        height = 9
                    }
                    position {
                        x = 0
                        y = this.height - 25 + 4
                    }
                }.build())
                this.addRenderableWidget(
                        Button.builder(Component.literal('Next page'), (Button button) -> Minecraft.getInstance().setScreen(nextPage))
                                .height(20)
                                .width(100)
                                .pos(this.width - 105, this.height - 25)
                                .build()
                )
            }

            @Override
            void render(PoseStack pose, int mouseX, int mouseY, float partialTick) {
                this.renderBackground(pose)
                super.render(pose, mouseX, mouseY, partialTick)
            }
        }

        protected static Screen getPage1() {
            final builder = Screen.builder()
                    .setTitle('Page 1')
                    .setRenderBackground(true)

            builder.tap {
                title = 'Page 1'
                centredString {
                    message = 'Screen builder test'
                    size screenWidth, 9
                    position 0, screenHeight / 3 as int
                }
                button('Previous page') {
                    size 100, 20
                    position 5, screenHeight - 25
                    onPress { Button button -> minecraft.setScreen(previousPage) }
                }
                centredString {
                    message = 'Page 1'
                    size screenWidth, 9
                    position x: 0, y: screenHeight - 25 + 4
                }
                button {
                    text = 'Next page'
                    size 100, 20
                    position screenWidth - 105, screenHeight - 25
                    onPress { Button button -> minecraft.setScreen(nextPage) }
                }
            }

            return builder.build()
        }

        protected static Screen getPage2() {
            return Screen.builder { screenBuilder ->
                title = 'Page 2'
                centredString {
                    message = 'Screen builder with generics for page buttons'
                    size {
                        width = screenWidth
                        height = 9
                    }
                    position {
                        x = 0
                        y = screenHeight / 3 as int
                    }
                }
                centredString('Page 2') {
                    size {
                        width = screenWidth
                        height = 9
                    }
                    position {
                        x = 0
                        y = screenHeight - 25 + 4
                    }
                }
                onInit { ExtensibleScreen extensibleScreen ->
                    final previousPageButton = Button.builder {
                        message = 'Previous page'
                        size width: 100,
                             height: 20
                        position x: 5, y: screenHeight - 25
                        onPress { Button button -> screenBuilder.minecraft.setScreen(previousPage) }
                    }.build()
                    extensibleScreen.addRenderableWidget(previousPageButton)
                    extensibleScreen.addRenderableWidget(Button.builder {
                        text = 'Next page'
                        size width: 100, height: 20
                        position {
                            x = screenBuilder.screenWidth - 105
                            y = screenBuilder.screenHeight - 25
                        }
                        onPress { Button button -> screenBuilder.minecraft.setScreen(nextPage) }
                    }.build())
                }
            }.build()
        }

        @Lazy
        protected static final List<Closure<Button>> buttons = [
            { int screenWidth, int screenHeight ->
                Button.builder {
                    message = 'Previous page'
                    size {
                        width = 100
                        height = 20
                    }
                    position {
                        x = 5
                        y = screenHeight - 25
                    }
                    onPress { Button button -> Minecraft.instance.setScreen(previousPage) }
                }.build()
            },
            { int screenWidth, int screenHeight ->
                Button.builder {
                    text = 'Next page'
                    size {
                        width = 100
                        height = 20
                    }
                    position {
                        x = screenWidth - 105
                        y = screenHeight - 25
                    }
                    onPress { Button button -> Minecraft.instance.setScreen(nextPage) }
                }.build()
            }
        ]

        protected static Screen getPage3() {
            return Screen.create('Page 3') { screenBuilder ->
                title = 'Page 3'
                centredString {
                    message = 'Screen.create() with closure-called generics for page buttons and editbox'
                    size {
                        width = screenWidth
                        height = 9
                    }
                    position {
                        x = 0
                        y = screenHeight / 3 as int
                    }
                }
                centredString {
                    message = 'Page 3'
                    size {
                        width = screenWidth
                        height = 9
                    }
                    position {
                        x = 0
                        y = screenHeight - 25 + 4
                    }
                }
                editBox {
                    hint = 'Edit me!'
                    tooltip = "I'm a tooltip"
                    use(TimeCategory) {
                        tooltipDelay = 2.seconds
                    }
                    bounds screenWidth / 2 as int - 50, screenHeight / 2 as int, 100, 20
                }
                onInit { ExtensibleScreen extensibleScreen ->
                    buttons.each { Closure<Button> button ->
                        extensibleScreen.addRenderableWidget(button(screenBuilder.screenWidth, screenBuilder.screenHeight))
                    }
                }
                onRender { extensibleScreen, poseStack, mouseX, mouseY, partialTick ->
                    drawString(poseStack, Minecraft.getInstance().font, 'Custom onRender code drawing a string', 5, 5, 0xFFFFFF)
                    extensibleScreen.superRender(poseStack, mouseX, mouseY, partialTick)
                }
            }
        }

        protected static Screen getPage4() {
            return Screen.create('Page 4') {
                centredString {
                    message = 'Many different widgets'
                    size {
                        width = screenWidth
                        height = 9
                    }
                    position 0, 5
                }

                editBox {
                    hint = 'EditBox'
                    tooltip = "I'm a tooltip"
                    tooltipDelay = 1000
                    bounds x: 5, y: 20, width: 100, height: 20
                }

                plainTextButton {
                    text = 'PlainTextButton'
                    size width: 100, height: 20
                    position x: 5, y: 45
                }

                button {
                    // note that the size isn't specified here, so it'll default to width: 200, height: 20
                    text = 'Button'
                    position = new Position(x: 5, y: 70)
                }

                centredString {
                    message = 'Page 4'
                    size {
                        width = screenWidth
                        height = 9
                    }
                    position {
                        x = 0
                        y = screenHeight - 25 + 4
                    }
                    font = Minecraft.instance.font
                }
                onInit { ExtensibleScreen extensibleScreen ->
                    buttons.each { Closure<Button> button ->
                        extensibleScreen.addRenderableWidget(button(screenWidth, screenHeight))
                    }
                }
            }
        }
    }
}
