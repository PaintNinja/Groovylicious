package ga.ozli.minecraftmods.groovylicious.dsl

import com.mojang.blaze3d.vertex.PoseStack
import ga.ozli.minecraftmods.groovylicious.api.StringUtils
import ga.ozli.minecraftmods.groovylicious.api.gui.Alignment
import ga.ozli.minecraftmods.groovylicious.api.gui.ExtensibleScreen

import groovy.contracts.Requires
import groovy.transform.CompileStatic
import groovy.transform.stc.ClosureParams
import groovy.transform.stc.FromAbstractTypeMethods
import groovy.transform.stc.PickFirstResolver
import net.minecraft.client.gui.components.Button
import net.minecraft.network.chat.Component
import org.apache.groovy.lang.annotation.Incubating

@Incubating
@CompileStatic
class ButtonBuilder implements PositionTrait, SizeTrait, TextTrait {
    Button.OnPress onPress = (Button button) -> {}
    Button.OnTooltip onTooltip
    Alignment alignment = Alignment.LEFT
    Component tooltipText = null

    void onPress(@ClosureParams(value = FromAbstractTypeMethods,
            options = "net.minecraft.client.gui.components.Button.OnPress") final Closure closure) {
        this.onPress = closure
    }

    // todo: change to onHover?
    // todo: add a wrapper including the ExtensibleScreen so we can run Screen.renderTooltip() with the text
    void onTooltip(@ClosureParams(value = FromAbstractTypeMethods,
            options = "net.minecraft.client.gui.components.Button.OnTooltip",
            conflictResolutionStrategy = PickFirstResolver) final Closure closure) {
        this.onTooltip = closure
    }

    void tooltip(final Component component) {
        this.tooltipText = component
    }

    void tooltip(final String text) {
        this.tooltipText = StringUtils.stringToComponent(text)
    }

    void alignment(final Alignment alignment) {
        this.alignment = alignment
    }

    @Requires({ this.position && this.size && this.text }) // make sure the position, size and text aren't null
    Button buildButton(ExtensibleScreen screen) {
        if (alignment === Alignment.CENTRE) {
            this.position.x = this.position.x - (this.size.width / 2) as int
        } else if (alignment === Alignment.RIGHT) {
            this.position.x = this.position.x - this.size.width
        }

        if (this.onTooltip) {
            if (this.tooltipText !== null) {
                this.onTooltip = { Button button, PoseStack poseStack, int mouseX, int mouseY ->
                    screen.renderTooltip(poseStack, this.tooltipText, mouseX, mouseY)
                }
            }
            return new Button(this.position.x, this.position.y, this.size.width, this.size.height, this.text, this.onPress, this.onTooltip)
        } else {
            return new Button(this.position.x, this.position.y, this.size.width, this.size.height, this.text, this.onPress)
        }
    }

    @Requires({ this.position && this.size && this.text })
    Closure buildClosure() {
        return { ExtensibleScreen screenInstance ->
            screenInstance.addRenderableWidget(this.buildButton(screenInstance))
        }
    }
}
