package ga.ozli.minecraftmods.groovylicious.dsl

import ga.ozli.minecraftmods.groovylicious.api.gui.ComponentUtils
import groovy.transform.CompileStatic
import groovy.transform.stc.ClosureParams
import groovy.transform.stc.FromString
import groovy.transform.stc.SimpleType
import net.minecraft.client.gui.components.Button
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent

import java.util.function.Supplier

import static groovy.lang.Closure.DELEGATE_FIRST

@CompileStatic
class ButtonBuilder extends AbstractWidgetBuilder {
    private Button.OnPress onPress = (Button button) -> {}
    private Button.CreateNarration createNarration = (Supplier<MutableComponent> supplier) -> supplier.get()

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

    ButtonBuilder onPress(@ClosureParams(value = SimpleType, options = 'net.minecraft.client.gui.components.Button')
                    final Button.OnPress onPress) {
        this.@onPress = onPress
        return this
    }

    Button.OnPress getOnPress() {
        return this.@onPress
    }

    ButtonBuilder createNarration(@ClosureParams(value = FromString, options = 'java.util.function.Supplier<net.minecraft.network.chat.MutableComponent>')
                    final Button.CreateNarration createNarration) {
        this.@createNarration = createNarration
        return this
    }

    Button.CreateNarration getCreateNarration() {
        return this.@createNarration
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
