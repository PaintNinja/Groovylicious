package ga.ozli.minecraftmods.groovylicious.dsl.traits

import ga.ozli.minecraftmods.groovylicious.api.GeneralUtils
import groovy.transform.CompileStatic
import net.minecraft.network.chat.Component

@CompileStatic
trait MessageTrait {
    private Component message = Component.empty()

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
        this.@message = GeneralUtils.stringToComponent(text)
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
        this.@message = GeneralUtils.stringToComponent(message)
    }

    Component getMessage() {
        return this.@message
    }
}
