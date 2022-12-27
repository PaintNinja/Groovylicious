package ga.ozli.minecraftmods.groovylicious.dsl

import ga.ozli.minecraftmods.groovylicious.dsl.traits.FontTrait
import groovy.contracts.Requires
import groovy.transform.CompileStatic
import net.minecraft.client.gui.components.PlainTextButton
import net.minecraft.network.chat.Component

import static groovy.lang.Closure.DELEGATE_FIRST

@CompileStatic
class PlainTextButtonBuilder extends ButtonBuilder implements FontTrait {
    PlainTextButtonBuilder() {}

    PlainTextButtonBuilder(@DelegatesTo(value = PlainTextButtonBuilder, strategy = DELEGATE_FIRST) final Closure closure) {
        this.tap(closure)
    }

    PlainTextButtonBuilder(final Component message) {
        this.message = message
    }

    PlainTextButtonBuilder(final String message) {
        this.message = message
    }

    PlainTextButtonBuilder(final Component message, @DelegatesTo(value = PlainTextButtonBuilder, strategy = DELEGATE_FIRST) final Closure closure) {
        this.message = message
        this.tap(closure)
    }

    PlainTextButtonBuilder(final String message, @DelegatesTo(value = PlainTextButtonBuilder, strategy = DELEGATE_FIRST) final Closure closure) {
        this.message = message
        this.tap(closure)
    }

    @Override
//    @Requires({ position && size && message && onPress && font })
    PlainTextButton build() {
        final plainTextButton = new PlainTextButton(position.x, position.y, size.width, size.height, message, onPress, font)
        plainTextButton.tooltip = tooltip
        plainTextButton.tooltipDelay = tooltipDelay
        plainTextButton.active = active
        plainTextButton.visible = visible

        return plainTextButton
    }
}
