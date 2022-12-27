package groovylicioustest

import com.mojang.blaze3d.vertex.PoseStack
import ga.ozli.minecraftmods.groovylicious.api.gui.ExtensibleScreen
import groovy.time.Duration
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
    private static class Pages {
        protected static final Screen page0 = new Screen(Component.literal("Page 0")) {
            int previousLabelYPos = 0

            @Override
            protected void init() {
                super.init()
                this.addRenderableWidget(CenteredStringWidget.builder {
                    message = 'Groovylicious Screen Test'
                    size this.width, 9

                    previousLabelYPos = this.height / 3 as int
                    position 0, previousLabelYPos
                })
                this.addRenderableWidget(CenteredStringWidget.builder {
                    message = 'Each page introduces new/different features for incremental testing'
                    size this.width, 9

                    position 0, previousLabelYPos + 19
                })

                this.addRenderableWidget(Button.builder {
                    message = 'Previous page'
                    size 100, 20
                    x = 5
                    y = this.height - 25
                    onPress { Button button -> this.minecraft.setScreen(previousPage) }
                })
                this.addRenderableWidget(CenteredStringWidget.builder {
                    message = 'Page 0'
                    size this.width, 9
                    x = 0
                    y = this.height - 25 + 4
                })
                this.addRenderableWidget(Button.builder {
                    message = 'Next page'
                    size 100, 20
                    x = this.width - 105
                    y = this.height - 25
                    onPress { Button button -> this.minecraft.setScreen(nextPage) }
                })
            }

            @Override
            void render(PoseStack pose, int mouseX, int mouseY, float partialTick) {
                this.renderBackground(pose)
                super.render(pose, mouseX, mouseY, partialTick)
            }
        }

        protected static Screen getPage1() {
            return Screen.builder('Page 1') {
                centredString {
                    message = 'Screen builder test'
                    size screenWidth, 9
                    position 0, screenHeight / 3 as int
                }
                button('Previous page') {
                    size 100, 20
                    x = 5
                    y = screenHeight - 25
                    onPress { Button button -> minecraft.setScreen(previousPage) }
                }
                centredString {
                    message 'Page 1'
                    size screenWidth, 9
                    x = 0
                    y = screenHeight - 25 + 4
                }
                button {
                    text 'Next page'
                    size 100, 20
                    x = screenWidth - 105
                    y = screenHeight - 25
                    onPress { Button button -> minecraft.setScreen(nextPage) }
                }
            }
        }

        protected static Screen getPage2() {
            return Screen.builder('Page 2') { screenBuilder ->
                centredString {
                    message = 'Screen builder with generics for page buttons'
                    size screenWidth, 9
                    position 0, screenHeight / 3 as int
                }
                centredString {
                    message 'Page 2'
                    size screenWidth, 9
                    x = 0
                    y = screenHeight - 25 + 4
                }
                onInit { ExtensibleScreen extensibleScreen ->
                    final previousPageButton = Button.builder('Previous page') {
                        size 100, 20
                        position 5, screenBuilder.screenHeight - 25
                        onPress { Button button -> screenBuilder.minecraft.setScreen(previousPage) }
                    }
                    extensibleScreen.addRenderableWidget(previousPageButton)
                    extensibleScreen.addRenderableWidget(Button.builder {
                        text 'Next page'
                        size 100, 20
                        x screenBuilder.screenWidth - 105
                        y screenBuilder.screenHeight - 25
                        onPress { Button button -> screenBuilder.minecraft.setScreen(nextPage) }
                    })
                }
            }
        }

        @Lazy
        protected static final List<Closure<Button>> buttons = [
            { int screenWidth, int screenHeight ->
                Button.builder('Previous page') {
                    size 100, 20
                    position 5, screenHeight - 25
                    onPress { Button button -> Minecraft.instance.setScreen(previousPage) }
                }
            },
            { int screenWidth, int screenHeight ->
                Button.builder {
                    text 'Next page'
                    size 100, 20
                    x screenWidth - 105
                    y screenHeight - 25
                    onPress { Button button -> Minecraft.instance.setScreen(nextPage) }
                }
            }
        ]

        protected static Screen getPage3() {
            return Screen.builder('Page 3') { screenBuilder ->
                centredString {
                    message = 'Screen builder with closure-called generics for page buttons and editbox'
                    size screenWidth, 9
                    position 0, screenHeight / 3 as int
                }
                centredString {
                    message 'Page 3'
                    size screenWidth, 9
                    x = 0
                    y = screenHeight - 25 + 4
                }
                editBox {
                    hint = 'Edit me!'
                    tooltip "I'm a tooltip"
                    use(TimeCategory) {
                        tooltipDelay 2.seconds
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
            return Screen.builder('Page 4') {
                centredString {
                    message = 'Many different widgets'
                    size screenWidth, 9
                    position 0, 5
                }

                editBox {
                    hint = 'EditBox'
                    tooltip "I'm a tooltip"
                    use(TimeCategory) {
                        tooltipDelay 1.second
                    }
                    bounds 5, 20, 100, 20
                }

                plainTextButton {
                    text 'PlainTextButton'
                    size 100, 20
                    position 5, 45
                }

                button {
                    text 'Button'
                    size 100, 20
                    position 5, 70
                }

                centredString {
                    message 'Page 4'
                    size screenWidth, 9
                    x = 0
                    y = screenHeight - 25 + 4
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
