package ga.ozli.minecraftmods.groovylicious.dsl.traits

import ga.ozli.minecraftmods.groovylicious.api.gui.ComponentUtils
import groovy.transform.CompileStatic
import groovy.transform.builder.Builder
import groovy.transform.builder.SimpleStrategy
import net.minecraft.network.chat.Component

import javax.annotation.Nonnull

@CompileStatic
trait MessageTrait {
    private Component message = ComponentUtils.PLACEHOLDER_COMPONENT

    void setText(final Component text) {
        this.@message = text
    }

    /**
     * Sets the message of the button.<br>
     * Usage: <pre>
     * // for translatable message (recommended):
     * message = "groovylicious.lang.helloButton" // same as `message = Component.translatable("groovylicious.lang.helloButton")`
     *
     * // for plain message (for quick prototyping):
     * message = "Hello World" // same as `message = Component.literal("Hello World")`
     * </pre>
     * @param text
     */
    void setText(final String text) {
        this.@message = ComponentUtils.stringToComponent(text)
    }

    Component getText() {
        return this.@message
    }

    void setMessage(final Component text) {
        this.@message = text
    }

    /**
     * Sets the message of the button.<br>
     * Usage: <pre>
     * // for translatable message (recommended):
     * message = "groovylicious.lang.helloButton" // same as `message = Component.translatable("groovylicious.lang.helloButton")`
     *
     * // for plain message (for quick prototyping):
     * message = "Hello World" // same as `message = Component.literal("Hello World")`
     * </pre>
     * @param text
     */
    void setMessage(final String message) {
        this.@message = ComponentUtils.stringToComponent(message)
    }

    Component getMessage() {
        return this.@message
    }
}
