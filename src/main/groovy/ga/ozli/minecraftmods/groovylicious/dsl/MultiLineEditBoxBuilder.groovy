package ga.ozli.minecraftmods.groovylicious.dsl

import groovy.contracts.Requires
import groovy.transform.CompileStatic
import net.minecraft.client.gui.components.MultiLineEditBox
import net.minecraft.network.chat.Component

import static groovy.lang.Closure.DELEGATE_FIRST

@CompileStatic
class MultiLineEditBoxBuilder extends EditBoxBuilder {
    MultiLineEditBoxBuilder() {}

    MultiLineEditBoxBuilder(@DelegatesTo(value = MultiLineEditBoxBuilder, strategy = DELEGATE_FIRST) final Closure closure) {
        this.tap(closure)
    }

    MultiLineEditBoxBuilder(final Component message) {
        this.message = message
    }

    MultiLineEditBoxBuilder(final String message) {
        this.message = message
    }

    MultiLineEditBoxBuilder(final Component message, @DelegatesTo(value = MultiLineEditBoxBuilder, strategy = DELEGATE_FIRST) final Closure closure) {
        this.message = message
        this.tap(closure)
    }

    MultiLineEditBoxBuilder(final String message, @DelegatesTo(value = MultiLineEditBoxBuilder, strategy = DELEGATE_FIRST) final Closure closure) {
        this.message = message
        this.tap(closure)
    }

//    @Override
//    @Requires({ font && position && size && placeholder && message })
//    MultiLineEditBox build() {
//        final placeholder = Component.literal('')
//        final multiLineEditBox = new MultiLineEditBox(font, position.x, position.y, size.width, size.height, placeholder, message)
//        multiLineEditBox.tooltip = tooltip
//        multiLineEditBox.tooltipDelay = tooltipDelay
//        multiLineEditBox.visible = visible
//        multiLineEditBox.canLoseFocus = canLoseFocus
//        multiLineEditBox.editable = editable
//        multiLineEditBox.focus = focused
//        multiLineEditBox.filter = filter
//        multiLineEditBox.formatter = formatter
//        multiLineEditBox.hint = hint
//        multiLineEditBox.maxLength = maxLength
//        multiLineEditBox.value = initialValue
//        multiLineEditBox.responder = responder
//        multiLineEditBox.suggestion = suggestion
//
//        return multiLineEditBox
//    }
}
