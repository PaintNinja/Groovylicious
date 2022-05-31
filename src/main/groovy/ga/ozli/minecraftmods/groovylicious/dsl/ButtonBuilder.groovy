package ga.ozli.minecraftmods.groovylicious.dsl

import ga.ozli.minecraftmods.groovylicious.api.gui.Position
import ga.ozli.minecraftmods.groovylicious.api.gui.Size
import groovy.transform.CompileStatic
import groovy.transform.TupleConstructor
import groovy.transform.builder.Builder
import groovy.transform.builder.SimpleStrategy
import groovy.transform.stc.ClosureParams
import groovy.transform.stc.SimpleType
import net.minecraft.client.gui.components.Button
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.TextComponent
import net.minecraft.network.chat.TranslatableComponent
import org.apache.groovy.lang.annotation.Incubating

import static groovy.lang.Closure.DELEGATE_FIRST

@Incubating
@CompileStatic
class ButtonBuilder {
    Component text
    Position position
    Size size
    Closure onClick = { -> }

    ButtonBuilder() {}

//    ButtonBuilder(@DelegatesTo(value = ButtonBuilder, strategy = DELEGATE_FIRST)
//                  @ClosureParams(value = SimpleType, options = "ga.ozli.minecraftmods.groovylicious.dsl.ButtonBuilder") final Closure closure) {
//        closure.delegate = this
//        closure.resolveStrategy = DELEGATE_FIRST
//        closure.call()
//    }

    void text(final Component text) {
        this.text = text
    }

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

    void position(final Position position) {
        this.position = position
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

    void size(final Size size) {
        this.size = size
    }

    /**
     * Sets the size of the button in closure-style.<br>
     * Usage: <pre>
     * size {
     *     width = 200
     *     height = 40
     * }
     * </pre>
     * @param closure
     */
    void size(@DelegatesTo(value = Size, strategy = DELEGATE_FIRST)
              @ClosureParams(value = SimpleType, options = "ga.ozli.minecraftmods.groovylicious.api.gui.Size") final Closure closure) {
        this.size = new Size().tap(closure)
    }

    /**
     * Sets the size of the button.
     * @param width the horizontal width
     * @param height the vertical height
     */
    void size(final int width, final int height) {
        this.size = new Size(width, height)
    }

    /** Sets the size of the button with named args.<br>
     * Usage: <pre>
     * size(width: 200, height: 40)
     * </pre>
     * or:
     * <pre>
     * size width: 200,
     *      height: 40
     * </pre>
     * @param args
     */
    void size(final Map args) {
        this.size = new Size(width: args.width as int, height: args.height as int)
    }

    void onClick(final Closure closure) {
        this.onClick = closure
    }

    Button build() {
        return new Button(this.position.x, this.position.y, this.size.width, this.size.height, this.text, this.onClick)
    }
}
