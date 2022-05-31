package ga.ozli.minecraftmods.groovylicious.dsl

import com.mojang.blaze3d.vertex.PoseStack
import ga.ozli.minecraftmods.groovylicious.api.gui.Alignment
import ga.ozli.minecraftmods.groovylicious.api.gui.Colour
import ga.ozli.minecraftmods.groovylicious.api.gui.ColoursRegistry
import ga.ozli.minecraftmods.groovylicious.api.gui.ExtensibleScreen
import ga.ozli.minecraftmods.groovylicious.api.gui.Position
import ga.ozli.minecraftmods.groovylicious.api.gui.Size
import groovy.transform.CompileStatic
import groovy.transform.stc.ClosureParams
import groovy.transform.stc.SimpleType
import net.minecraft.ChatFormatting
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.components.MultiLineLabel
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.TextComponent
import net.minecraft.network.chat.TranslatableComponent

@CompileStatic
class LabelBuilder {
    Component text // TODO: multi-line text support
    Position position
    Size size
    Alignment alignment = Alignment.LEFT
    Colour colour = ColoursRegistry.instance.WHITE
    int lineHeight = 9
    boolean drawShadow = true // TODO: centre-aligned with shadow

    void text(final Component component) {
        text = component
    }

    void text(final String text) {
        if (text.contains ' ') this.text = new TextComponent(text)
        else this.text = new TranslatableComponent(text)
    }

    void position(final Position position) {
        this.position = position
    }

    void position(@DelegatesTo(value = Position, strategy = Closure.DELEGATE_FIRST)
                  @ClosureParams(value = SimpleType, options = "ga.ozli.minecraftmods.groovylicious.api.gui.Position") final Closure closure) {
        this.position = new Position().tap(closure)
    }

    void position(final int x, final int y) {
        this.position = new Position(x, y)
    }

    void position(final Map args) {
        this.position = new Position(x: args.x as int, y: args.y as int)
    }

    void size(final Size size) {
        this.size = size
    }

    void size(@DelegatesTo(value = Size, strategy = Closure.DELEGATE_FIRST)
              @ClosureParams(value = SimpleType, options = "ga.ozli.minecraftmods.groovylicious.api.gui.Size") final Closure closure) {
        this.size = new Size().tap(closure)
    }

    void size(final int width, final int height) {
        this.size = new Size(width, height)
    }

    void size(final Map args) {
        this.size = new Size(width: args.width as int, height: args.height as int)
    }

    void alignment(final Alignment alignment) {
        this.alignment = alignment
    }

    void colour(final Colour colour) {
        this.colour = colour
    }

    void colour(final int packed) {
        this.colour = Colour.of(packed)
    }

    void colour(int red, int green, int blue) {
        this.colour = Colour.of(red, green, blue)
    }

    void colour(int alpha, int red, int green, int blue) {
        this.colour = Colour.of(alpha, red, green, blue)
    }

    void colour(int[] argb) {
        this.colour = Colour.of(argb)
    }

    void colour(ChatFormatting colour) {
        this.colour = Colour.of(colour)
    }

    void colour(Map args) {
        this.colour = new Colour(args)
    }

    MultiLineLabel buildLabel() {
        return MultiLineLabel.create(Minecraft.instance.font, this.text)
    }

    MultiLineLabel buildLabel(ExtensibleScreen extensibleScreen) {
        return MultiLineLabel.create(extensibleScreen.font, this.text)
    }

    // todo: null-check the relevant fields and show a more helpful error message
    Closure buildClosure() {
        return { ExtensibleScreen screenInstance, PoseStack poseStack ->
            final MultiLineLabel label = this.buildLabel(screenInstance)
            if (this.alignment === Alignment.LEFT) {
                if (this.drawShadow) label.renderLeftAligned(poseStack, this.position.x, this.position.y, this.lineHeight, this.colour.get())
                else label.renderLeftAlignedNoShadow(poseStack, this.position.x, this.position.y, this.lineHeight, this.colour.get())
            } else {
                label.renderCentered(poseStack, this.position.x, this.position.y, this.lineHeight, this.colour.get())
            }
        }
    }

}
