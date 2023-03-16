package ga.ozli.minecraftmods.groovylicious.dsl

import groovy.transform.CompileStatic
import groovy.transform.builder.Builder
import groovy.transform.builder.SimpleStrategy
import groovy.transform.stc.ClosureParams
import groovy.transform.stc.FromString
import groovy.transform.stc.SimpleType
import net.minecraft.client.gui.components.Button
import net.minecraft.client.gui.components.Checkbox
import net.minecraft.network.chat.Component
import org.apache.groovy.lang.annotation.Incubating

import static groovy.lang.Closure.DELEGATE_FIRST

// todo: text colour, texture
@Incubating
@CompileStatic
@Builder(builderStrategy = SimpleStrategy)
class CheckboxBuilder extends AbstractButtonBuilder {
    boolean selected = false
    boolean showLabel = true

    CheckboxBuilder() {}

    CheckboxBuilder(@DelegatesTo(value = CheckboxBuilder, strategy = DELEGATE_FIRST) final Closure closure) {
        this.tap(closure)
    }

    CheckboxBuilder(final Component message) {
        this.message = message
    }

    CheckboxBuilder(final String message) {
        this.message = message
    }

    CheckboxBuilder(final Component message, @DelegatesTo(value = CheckboxBuilder, strategy = DELEGATE_FIRST) final Closure closure) {
        this.message = message
        this.tap(closure)
    }

    CheckboxBuilder(final String message, @DelegatesTo(value = CheckboxBuilder, strategy = DELEGATE_FIRST) final Closure closure) {
        this.message = message
        this.tap(closure)
    }

    CheckboxBuilder onPress(@ClosureParams(value = SimpleType, options = 'net.minecraft.client.gui.components.Button')
                          final Button.OnPress onPress) {
        this.onPress = onPress
        return this
    }

    CheckboxBuilder createNarration(@ClosureParams(value = FromString, options = 'java.util.function.Supplier<net.minecraft.network.chat.MutableComponent>')
                                  final Button.CreateNarration createNarration) {
        this.createNarration = createNarration
        return this
    }

    Checkbox build() {
        final checkbox = new Checkbox(position.x, position.y, size.width, size.height, message, selected, showLabel)
        checkbox.tooltip = tooltip
        checkbox.tooltipDelay = tooltipDelay
        checkbox.active = active
        checkbox.visible = visible

        return checkbox
    }
}
