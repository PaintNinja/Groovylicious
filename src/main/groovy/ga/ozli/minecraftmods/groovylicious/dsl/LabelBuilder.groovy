package ga.ozli.minecraftmods.groovylicious.dsl

import com.mojang.blaze3d.vertex.PoseStack
import ga.ozli.minecraftmods.groovylicious.api.gui.Alignment
import ga.ozli.minecraftmods.groovylicious.api.gui.Colour
import ga.ozli.minecraftmods.groovylicious.api.gui.ExtensibleScreen
import groovy.contracts.Requires
import groovy.transform.CompileStatic
import net.minecraft.ChatFormatting
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.components.MultiLineLabel
import org.apache.groovy.lang.annotation.Incubating

import static ga.ozli.minecraftmods.groovylicious.api.gui.ColoursRegistry.instance as Colours

@Incubating
@CompileStatic
class LabelBuilder implements PositionTrait, SizeTrait, TextTrait {
    // TODO: multi-line text support
    Alignment alignment = Alignment.LEFT
    Colour textColour = Colours.WHITE
    int lineHeight = 9
    boolean drawShadow = true // TODO: centre-aligned with shadow

    void alignment(final Alignment alignment) {
        this.alignment = alignment
    }

    void textColour(final Colour colour) {
        this.textColour = colour
    }

    void textColour(final int packed) {
        this.textColour = Colour.of(packed)
    }

    void textColour(final int red, final int green, final int blue) {
        this.textColour = Colour.of(red, green, blue)
    }

    void textColour(final int alpha, final int red, final int green, final int blue) {
        this.textColour = Colour.of(alpha, red, green, blue)
    }

    void textColour(final int[] argb) {
        this.textColour = Colour.of(argb)
    }

    void textColour(final ChatFormatting colour) {
        this.textColour = Colour.of(colour)
    }

    void textColour(final Map args) {
        this.textColour = new Colour(args)
    }

    void lineHeight(final int lineHeight) {
        this.lineHeight = lineHeight
    }

    void drawShadow(final boolean drawShadow) {
        this.drawShadow = drawShadow
    }

    @Requires({ this.text })
    MultiLineLabel buildLabel() {
        return MultiLineLabel.create(Minecraft.instance.font, this.text)
    }

    @Requires({ this.text })
    MultiLineLabel buildLabel(final ExtensibleScreen extensibleScreen) {
        return MultiLineLabel.create(extensibleScreen.font, this.text)
    }

    // Note: this closure is meant to be run inside a Screen's render method, such as ExtensibleScreen's onPreRender/onRender/onPostRender
    @Requires({ this.text && this.position && this.textColour }) // make sure the text, position and colour aren't null
    Closure buildClosure() {
        return { ExtensibleScreen screenInstance, PoseStack poseStack ->
            final MultiLineLabel label = this.buildLabel(screenInstance)
            if (this.alignment === Alignment.LEFT) {
                if (this.drawShadow) label.renderLeftAligned(poseStack, this.position.x, this.position.y, this.lineHeight, this.textColour.get())
                else label.renderLeftAlignedNoShadow(poseStack, this.position.x, this.position.y, this.lineHeight, this.textColour.get())
            } else {
                label.renderCentered(poseStack, this.position.x, this.position.y, this.lineHeight, this.textColour.get())
            }
        }
    }

}
