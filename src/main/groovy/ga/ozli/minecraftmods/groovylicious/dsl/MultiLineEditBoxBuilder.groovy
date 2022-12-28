package ga.ozli.minecraftmods.groovylicious.dsl

import ga.ozli.minecraftmods.groovylicious.api.gui.ComponentUtils
import ga.ozli.minecraftmods.groovylicious.dsl.traits.FontTrait
import groovy.transform.CompileStatic
import net.minecraft.client.gui.components.MultiLineEditBox
import net.minecraft.network.chat.Component
import org.apache.groovy.lang.annotation.Incubating

import javax.annotation.Nullable
import java.util.function.Consumer

import static groovy.lang.Closure.DELEGATE_FIRST

@Incubating
@CompileStatic
class MultiLineEditBoxBuilder extends AbstractScrollWidgetBuilder implements FontTrait {
    Component placeholder

    String initialValue = ''
    int maxLength = 32

    @Nullable Consumer<String> responder = null

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

    void placeholder(final Component placeholder) {
        this.@placeholder = placeholder
    }

    void placeholder(final String placeholder) {
        this.@placeholder = ComponentUtils.stringToComponent(placeholder)
    }

    void setPlaceholder(final Component placeholder) {
        this.@placeholder = placeholder
    }

    void setPlaceholder(final String placeholder) {
        this.@placeholder = ComponentUtils.stringToComponent(placeholder)
    }

    void initialValue(final String initialValue) {
        this.@initialValue = initialValue
    }

    void initialValue(final Component initialValue) {
        this.@initialValue = initialValue.getString()
    }

    void setInitialValue(final String initialValue) {
        this.@initialValue = initialValue
    }

    void setInitialValue(final Component initialValue) {
        this.@initialValue = initialValue.getString()
    }

    void maxLength(final int maxLength) {
        this.@maxLength = maxLength
    }

    void characterLimit(final int maxLength) {
        this.@maxLength = maxLength
    }

    void setCharacterLimit(final int maxLength) {
        this.@maxLength = maxLength
    }

    void responder(@Nullable final Consumer<String> responder) {
        this.@responder = responder
    }

    void setResponder(@Nullable final Consumer<String> responder) {
        this.@responder = responder
    }

    void valueListener(@Nullable final Consumer<String> responder) {
        this.@responder = responder
    }

    void setValueListener(@Nullable final Consumer<String> responder) {
        this.@responder = responder
    }

    MultiLineEditBox build() {
        final multiLineEditBox = new MultiLineEditBox(font, position.x, position.y, size.width, size.height, placeholder, message)
        multiLineEditBox.tooltip = tooltip
        multiLineEditBox.tooltipDelay = tooltipDelay
        multiLineEditBox.visible = visible
//        multiLineEditBox.canLoseFocus = canLoseFocus
//        multiLineEditBox.editable = editable
//        multiLineEditBox.focus = focused
//        multiLineEditBox.filter = filter
//        multiLineEditBox.formatter = formatter
//        multiLineEditBox.hint = hint
        multiLineEditBox.characterLimit = maxLength
        multiLineEditBox.value = initialValue
        multiLineEditBox.valueListener = responder ?: (String text) -> {} as Consumer<String>
//        multiLineEditBox.suggestion = suggestion
//        multiLineEditBox.scrollAmount = scrollAmount

        return multiLineEditBox
    }
}
