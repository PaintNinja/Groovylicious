package ga.ozli.minecraftmods.groovylicious.dsl

import ga.ozli.minecraftmods.groovylicious.api.gui.ComponentUtils
import ga.ozli.minecraftmods.groovylicious.dsl.traits.FontTrait
import groovy.transform.CompileStatic
import groovy.transform.builder.Builder
import groovy.transform.builder.SimpleStrategy
import net.minecraft.client.gui.components.MultiLineEditBox
import net.minecraft.network.chat.Component
import org.apache.groovy.lang.annotation.Incubating

import javax.annotation.Nullable
import java.util.function.Consumer

import static groovy.lang.Closure.DELEGATE_FIRST

@Incubating
@CompileStatic
@Builder(builderStrategy = SimpleStrategy)
class MultiLineEditBoxBuilder extends AbstractScrollWidgetBuilder implements FontTrait {
    private Component placeholder

    private String initialValue = ''
    private int maxLength = 32

    @Nullable
    private Consumer<String> responder = null

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

    // region placeholder
    MultiLineEditBoxBuilder setPlaceholder(final Component placeholder) {
        this.@placeholder = placeholder
        return this
    }

    MultiLineEditBoxBuilder setPlaceholder(final String placeholder) {
        this.@placeholder = ComponentUtils.stringToComponent(placeholder)
        return this
    }

    Component getPlaceholder() {
        return this.@placeholder
    }
    // endregion

    // region initialValue
    MultiLineEditBoxBuilder setInitialValue(final String initialValue) {
        this.@initialValue = initialValue
        return this
    }

    MultiLineEditBoxBuilder setInitialValue(final Component initialValue) {
        this.@initialValue = initialValue.getString()
        return this
    }

    String getInitialValue() {
        return this.@initialValue
    }

    MultiLineEditBoxBuilder setValue(final String value) {
        this.@initialValue = value
        return this
    }

    MultiLineEditBoxBuilder setValue(final Component value) {
        this.@initialValue = value.getString()
        return this
    }

    String getValue() {
        return this.@initialValue
    }
    // endregion

    // region maxLength
    MultiLineEditBoxBuilder setMaxLength(final int maxLength) {
        this.@maxLength = maxLength
        return this
    }

    int getMaxLength() {
        return this.@maxLength
    }

    MultiLineEditBoxBuilder setCharacterLimit(final int maxLength) {
        this.@maxLength = maxLength
        return this
    }

    int getCharacterLimit() {
        return this.@maxLength
    }
    // endregion

    // region responder
    MultiLineEditBoxBuilder setResponder(@Nullable final Consumer<String> responder) {
        this.@responder = responder
        return this
    }

    @Nullable Consumer<String> getResponder() {
        return this.@responder
    }

    MultiLineEditBoxBuilder setValueListener(@Nullable final Consumer<String> responder) {
        this.@responder = responder
        return this
    }

    @Nullable Consumer<String> getValueListener(@Nullable final Consumer<String> responder) {
        return this.@responder
    }
    // endregion

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
