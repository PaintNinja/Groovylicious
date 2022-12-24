package ga.ozli.minecraftmods.groovylicious.dsl

import ga.ozli.minecraftmods.groovylicious.api.gui.ComponentUtils
import ga.ozli.minecraftmods.groovylicious.dsl.traits.BoundsTrait
import ga.ozli.minecraftmods.groovylicious.dsl.traits.FontTrait
import ga.ozli.minecraftmods.groovylicious.dsl.traits.MessageTrait
import ga.ozli.minecraftmods.groovylicious.dsl.traits.OnPressTrait
import groovy.contracts.Requires
import groovy.transform.CompileStatic
import net.minecraft.client.gui.components.PlainTextButton
import net.minecraft.network.chat.Component

// Todo: double check there's setters for all fields in the traits
@CompileStatic
class PlainTextButtonBuilder implements BoundsTrait, MessageTrait, OnPressTrait, FontTrait {
    PlainTextButtonBuilder() {}

    PlainTextButtonBuilder(final Closure closure) {
        this.tap(closure)
    }

    PlainTextButtonBuilder(final Component message) {
        this.message = message
    }

    PlainTextButtonBuilder(final String message) {
        this.message = ComponentUtils.stringToComponent(message)
    }

    PlainTextButtonBuilder(final Component message, final Closure closure) {
        this.message = message
        this.tap(closure)
    }

    PlainTextButtonBuilder(final String message, final Closure closure) {
        this.message = ComponentUtils.stringToComponent(message)
        this.tap(closure)
    }

    @Requires({ position && size && message && onPress && font })
    PlainTextButton build() {
        return new PlainTextButton(position.x, position.y, size.width, size.height, message, onPress, font)
    }
}
