package ga.ozli.minecraftmods.groovylicious.dsl

import com.mojang.blaze3d.vertex.PoseStack
import ga.ozli.minecraftmods.groovylicious.api.gui.Alignment
import ga.ozli.minecraftmods.groovylicious.api.gui.Colour
import ga.ozli.minecraftmods.groovylicious.api.gui.ExtensibleScreen
import ga.ozli.minecraftmods.groovylicious.api.gui.Position
import ga.ozli.minecraftmods.groovylicious.api.gui.Size
import groovy.transform.CompileStatic
import groovy.transform.stc.ClosureParams
import groovy.transform.stc.SimpleType
import net.minecraft.ChatFormatting
import net.minecraft.client.gui.components.Button
import net.minecraft.client.gui.components.MultiLineLabel
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.TextComponent
import net.minecraft.network.chat.TranslatableComponent
import org.apache.groovy.lang.annotation.Incubating

import static groovy.lang.Closure.DELEGATE_FIRST
import static ga.ozli.minecraftmods.groovylicious.api.gui.ColoursRegistry.instance as Colours

@Incubating
@CompileStatic
class ScreenBuilder {

    final Component title
    boolean drawBackground

    final ExtensibleScreen backingScreen

    ScreenBuilder(final Component title) {
        this.title = title
        this.backingScreen = new ExtensibleScreen(title)
    }

    void button(@DelegatesTo(value = ButtonDsl, strategy = DELEGATE_FIRST)
                @ClosureParams(value = SimpleType, options = "ga.ozli.minecraftmods.groovylicious.dsl.ButtonDsl") final Closure closure) {
        final ButtonDsl button = new ButtonDsl()
        closure.setDelegate(button)
        closure.call(button)
        this.backingScreen.onInit << { -> backingScreen.addRenderableWidget(button.build()) }
    }

    void label(@DelegatesTo(value = LabelDsl, strategy = DELEGATE_FIRST)
               @ClosureParams(value = SimpleType, options = "ga.ozli.minecraftmods.groovylicious.dsl.LabelDsl") final Closure closure) {
        final LabelDsl label = new LabelDsl()
        closure.setDelegate(label)
        closure.call(label)
        this.backingScreen.onRender << label.build()
    }

    ExtensibleScreen build() {
        this.backingScreen.drawBackground = this.drawBackground
        return this.backingScreen
    }

    static ExtensibleScreen makeScreen(final Component title, @DelegatesTo(value = ScreenBuilder, strategy = DELEGATE_FIRST) final Closure closure) {
        final screenBuilder = new ScreenBuilder(title)
        closure.delegate = screenBuilder
        closure.resolveStrategy = DELEGATE_FIRST
        closure.call()

        return screenBuilder.build()
    }

    static ExtensibleScreen makeScreen(final String title, @DelegatesTo(value = ScreenBuilder, strategy = DELEGATE_FIRST) final Closure closure) {
        final Component titleComponent
        if (title.contains ' ') titleComponent = new TextComponent(title)
        else titleComponent = new TranslatableComponent(title)

        return makeScreen(titleComponent, closure)
    }
}

@CompileStatic
class ButtonDsl {
    Component text
    Position position
    Size size
    Closure onClick

    /**
     * Sets the text of the button.<br>
     * Usage: <pre>
     * // for translatable text (recommended):
     * text "groovylicious.lang.helloButton" // same as `text = new TranslatableComponent("groovylicious.lang.helloButton")`
     *
     * // for plain text (for quick prototyping):
     * text "Hello World" // same as `text = new TextComponent("Hello World")`
     * </pre>
     * @param text
     */
    void text(final String text) {
        if (text.contains ' ') this.text = new TextComponent(text)
        else this.text = new TranslatableComponent(text)
    }

    /**
     * Sets the position of the button in closure-style.<br>
     * Usage: <pre>
     * position {
     *     x = 10
     *     y = 40
     * }
     * </pre>
     * @param closure
     */
    void position(@DelegatesTo(value = Position, strategy = DELEGATE_FIRST)
                  @ClosureParams(value = SimpleType, options = "ga.ozli.minecraftmods.groovylicious.api.gui.Position") final Closure closure) {
        this.position = new Position().tap(closure)
    }

    /**
     * Sets the position of the button.
     * @param x the horizontal axis
     * @param y the vertical axis
     */
    void position(final int x, final int y) {
        this.position = new Position(x, y)
    }

    /** Sets the position of the button with named args.<br>
     * Usage: <pre>
     * position(x: 10, y: 40)
     * </pre>
     * or:
     * <pre>
     * position x: 10,
     *          y: 40
     * </pre>
     * @param args
     */
    void position(final Map args) {
        this.position = new Position(x: args.x as int, y: args.y as int)
    }

    void size(@DelegatesTo(value = Size, strategy = DELEGATE_FIRST)
              @ClosureParams(value = SimpleType, options = "ga.ozli.minecraftmods.groovylicious.api.gui.Size") final Closure closure) {
        this.size = new Size().tap(closure)
    }

    void size(final int width, final int height) {
        this.size = new Size(width, height)
    }

    void size(final Map args) {
        this.size = new Size(width: args.width as int, height: args.height as int)
    }

    void onClick(final Closure closure) {
        this.onClick = closure
    }

    Button build() {
        return new Button(this.position.x, this.position.y, this.size.width, this.size.height, this.text, onClick)
    }

}

@CompileStatic
class LabelDsl {
    Component text // TODO: multi-line text support
    Position position
    Size size
    Alignment alignment = Alignment.LEFT
    Colour colour = Colours.WHITE
    int lineHeight = 9
    boolean drawShadow = true // TODO: centre-aligned with shadow

    void text(final String text) {
        if (text.contains ' ') this.text = new TextComponent(text)
        else this.text = new TranslatableComponent(text)
    }

    void position(@DelegatesTo(value = Position, strategy = DELEGATE_FIRST)
                  @ClosureParams(value = SimpleType, options = "ga.ozli.minecraftmods.groovylicious.api.gui.Position") final Closure closure) {
        this.position = new Position().tap(closure)
    }

    void position(final int x, final int y) {
        this.position = new Position(x, y)
    }

    void position(final Map args) {
        this.position = new Position(x: args.x as int, y: args.y as int)
    }

    void size(@DelegatesTo(value = Size, strategy = DELEGATE_FIRST)
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

    // todo: null-check the relevant fields and show a more helpful error message
    Closure build() {
        return { ExtensibleScreen screenInstance, PoseStack poseStack ->
            final MultiLineLabel label = MultiLineLabel.create(screenInstance.font, this.text)
            if (this.alignment = Alignment.LEFT) {
                if (this.drawShadow) label.renderLeftAligned(poseStack, this.position.x, this.position.y, this.lineHeight, this.getColour().packed)
                else label.renderLeftAlignedNoShadow(poseStack, this.position.x, this.position.y, this.lineHeight, this.colour.get())
            } else {
                label.renderCentered(poseStack, this.position.x, this.position.y, this.lineHeight, this.colour.get())
            }
        }
    }

}
