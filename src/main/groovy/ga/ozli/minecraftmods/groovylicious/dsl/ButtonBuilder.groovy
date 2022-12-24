package ga.ozli.minecraftmods.groovylicious.dsl

import ga.ozli.minecraftmods.groovylicious.api.gui.ComponentUtils
import ga.ozli.minecraftmods.groovylicious.dsl.traits.BoundsTrait
import ga.ozli.minecraftmods.groovylicious.dsl.traits.MessageTrait
import ga.ozli.minecraftmods.groovylicious.dsl.traits.OnPressTrait
import ga.ozli.minecraftmods.groovylicious.dsl.traits.TooltipTrait
import groovy.contracts.Requires
import groovy.transform.CompileStatic
import net.minecraft.client.gui.components.Button
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent

import java.util.function.Supplier

@CompileStatic
class ButtonBuilder implements BoundsTrait, MessageTrait, OnPressTrait, TooltipTrait {
    Button.CreateNarration createNarration = (Supplier<MutableComponent> supplier) -> supplier.get()

    ButtonBuilder() {}

    ButtonBuilder(final Closure closure) {
        this.tap(closure)
    }

    ButtonBuilder(final Component message) {
        this.message = message
    }

    ButtonBuilder(final String message) {
        this.message = ComponentUtils.stringToComponent(message)
    }

    ButtonBuilder(final Component message, final Closure closure) {
        this.message = message
        this.tap(closure)
    }

    ButtonBuilder(final String message, final Closure closure) {
        this.message = ComponentUtils.stringToComponent(message)
        this.tap(closure)
    }

    @Requires({ position && size && message && onPress && tooltip && createNarration }) // ensure all required fields are set
    Button build() {
        return Button.builder(message, onPress)
                .bounds(position.x, position.y, size.width, size.height)
                .tooltip(tooltip)
                .createNarration(createNarration)
                .build()
    }
}
