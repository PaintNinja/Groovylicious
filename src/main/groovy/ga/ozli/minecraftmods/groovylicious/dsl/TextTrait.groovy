package ga.ozli.minecraftmods.groovylicious.dsl

import ga.ozli.minecraftmods.groovylicious.api.StringUtils
import groovy.transform.CompileStatic
import net.minecraft.network.chat.Component

import javax.annotation.Nonnull

@CompileStatic
trait TextTrait {
    Component text

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
    void text(@Nonnull final String text) {
        this.text = StringUtils.stringToComponent(text)
    }
}