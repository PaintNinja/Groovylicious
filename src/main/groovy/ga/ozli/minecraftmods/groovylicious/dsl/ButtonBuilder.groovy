package ga.ozli.minecraftmods.groovylicious.dsl

import ga.ozli.minecraftmods.groovylicious.api.gui.ComponentUtils
import ga.ozli.minecraftmods.groovylicious.dsl.traits.OnPressTrait

import groovy.contracts.Requires
import groovy.transform.CompileStatic
import net.minecraft.client.gui.components.Button
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent

import java.util.function.Supplier

import static groovy.lang.Closure.DELEGATE_FIRST

@CompileStatic
class ButtonBuilder extends AbstractWidgetBuilder implements OnPressTrait {
    Button.CreateNarration createNarration = (Supplier<MutableComponent> supplier) -> supplier.get()

    ButtonBuilder() {}

    ButtonBuilder(@DelegatesTo(value = ButtonBuilder, strategy = DELEGATE_FIRST) final Closure closure) {
        this.tap(closure)
    }

    ButtonBuilder(final Component message) {
        this.message = message
    }

    ButtonBuilder(final String message) {
        this.message = ComponentUtils.stringToComponent(message)
    }

    ButtonBuilder(final Component message, @DelegatesTo(value = ButtonBuilder, strategy = DELEGATE_FIRST) final Closure closure) {
        this.message = message
        this.tap(closure)
    }

    ButtonBuilder(final String message, @DelegatesTo(value = ButtonBuilder, strategy = DELEGATE_FIRST) final Closure closure) {
        this.message = ComponentUtils.stringToComponent(message)
        this.tap(closure)
    }

//    @Requires({ position && size && message && onPress && tooltip && createNarration }) // ensure all required fields are set
    Button build() {
        final button = Button.builder(message, onPress)
                .bounds(position.x, position.y, size.width, size.height)
                .tooltip(tooltip)
                .createNarration(createNarration)
                .build()
        button.tooltipDelay = tooltipDelay
        button.active = active
        button.visible = visible
        
        return button
    }
}
