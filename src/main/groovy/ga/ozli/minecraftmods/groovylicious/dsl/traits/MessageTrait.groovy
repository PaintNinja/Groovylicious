package ga.ozli.minecraftmods.groovylicious.dsl.traits

import ga.ozli.minecraftmods.groovylicious.api.gui.ComponentUtils
import groovy.transform.CompileStatic
import net.minecraft.network.chat.Component

import javax.annotation.Nonnull

@CompileStatic
trait MessageTrait {
    Component message = ComponentUtils.PLACEHOLDER_COMPONENT

    void text(final Component text) {
        this.message = text
    }

    /**
     * Sets the message of the button.<br>
     * Usage: <pre>
     * // for translatable message (recommended):
     * message "groovylicious.lang.helloButton" // same as `message = Component.translatable("groovylicious.lang.helloButton")`
     *
     * // for plain message (for quick prototyping):
     * message "Hello World" // same as `message = Component.literal("Hello World")`
     * </pre>
     * @param text
     */
    void text(@Nonnull final String text) {
        this.message = ComponentUtils.stringToComponent(text)
    }

    void message(final Component message) {
        this.message = message
    }

    /**
     * Sets the message of the button.<br>
     * Usage: <pre>
     * // for translatable message (recommended):
     * message "groovylicious.lang.helloButton" // same as `message = Component.translatable("groovylicious.lang.helloButton")`
     *
     * // for plain message (for quick prototyping):
     * message "Hello World" // same as `message = Component.literal("Hello World")`
     * </pre>
     * @param text
     */
    void message(@Nonnull final String message) {
        this.message = ComponentUtils.stringToComponent(message)
    }
}
