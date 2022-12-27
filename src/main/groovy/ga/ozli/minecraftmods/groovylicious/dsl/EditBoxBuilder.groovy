package ga.ozli.minecraftmods.groovylicious.dsl

import ga.ozli.minecraftmods.groovylicious.api.gui.ComponentUtils
import ga.ozli.minecraftmods.groovylicious.dsl.traits.FontTrait
import groovy.contracts.Requires
import groovy.transform.CompileStatic
import net.minecraft.client.gui.components.EditBox
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.Style
import net.minecraft.util.FormattedCharSequence

import javax.annotation.Nullable
import java.util.function.BiFunction
import java.util.function.Consumer
import java.util.function.Predicate

import static groovy.lang.Closure.DELEGATE_FIRST

// Todo: Colour support
@CompileStatic
class EditBoxBuilder extends AbstractWidgetBuilder implements FontTrait {
    /*
     * Done:
     * - font
     * - x
     * - y
     * - width
     * - height
     * - message
     * - tooltip
     * - tooltipdelay
     * - visible
     * - canlosefocus
     * - editable
     * - focus
     * - filter
     * - formatter
     * - hint
     * - maxlength
     * - value
     * - responder
     * - suggestion
     *
     * Todo:
     * - alpha (in AbstractWidgetBuilder)
     * - bordered
     * - blitoffset (in AbstractWidgetBuilder -> GuiComponent)
     * - fgcolor (in AbstractWidgetBuilder)
     * - textcolor
     * - textcoloruneditable
     */

    boolean canLoseFocus = true
    boolean editable = true
    boolean focused = false

    Predicate<String> filter = (String text) -> true
    BiFunction<String, Integer, FormattedCharSequence> formatter = (String text, Integer integer) -> FormattedCharSequence.forward(text, Style.EMPTY)
    @Nullable Consumer<String> responder = null

    String initialValue = ""
    int maxLength = 32

    @Nullable String suggestion = null
    @Nullable Component hint = null

    EditBoxBuilder() {}

    EditBoxBuilder(@DelegatesTo(value = EditBoxBuilder, strategy = DELEGATE_FIRST) final Closure closure) {
        this.tap(closure)
    }

    EditBoxBuilder(final Component message) {
        this.message = message
    }

    EditBoxBuilder(final String message) {
        this.message = ComponentUtils.stringToComponent(message)
    }

    EditBoxBuilder(final Component message, @DelegatesTo(value = EditBoxBuilder, strategy = DELEGATE_FIRST) final Closure closure) {
        this.message = message
        this.tap(closure)
    }

    EditBoxBuilder(final String message, @DelegatesTo(value = EditBoxBuilder, strategy = DELEGATE_FIRST) final Closure closure) {
        this.message = ComponentUtils.stringToComponent(message)
        this.tap(closure)
    }

    void canLoseFocus(final boolean canLoseFocus) {
        this.@canLoseFocus = canLoseFocus
    }

    void editable(final boolean editable) {
        this.@editable = editable
    }

    void visible(final boolean visible) {
        this.visible = visible
    }

    void focused(final boolean focused) {
        this.@focused = focused
    }

    void filter(final Predicate<String> filter) {
        this.@filter = filter
    }

    void setFilter(final Predicate<String> filter) {
        this.@filter = filter
    }

    void formatter(final BiFunction<String, Integer, FormattedCharSequence> formatter) {
        this.@formatter = formatter
    }

    void setFormatter(final BiFunction<String, Integer, FormattedCharSequence> formatter) {
        this.@formatter = formatter
    }

    void responder(final Consumer<String> responder) {
        this.@responder = responder
    }

    void setResponder(final Consumer<String> responder) {
        this.@responder = responder
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

    void value(final String value) {
        this.@initialValue = value
    }

    void value(final Component value) {
        this.@initialValue = value.getString()
    }

    void setValue(final String value) {
        this.@initialValue = value
    }

    void setValue(final Component value) {
        this.@initialValue = value.getString()
    }

    void maxLength(final int maxLength) {
        this.@maxLength = maxLength
    }

    void suggestion(@Nullable String suggestion) {
        this.@suggestion = suggestion
    }

    void suggestion(@Nullable Component suggestion) {
        this.@suggestion = suggestion?.getString() ?: null
    }

    void setSuggestion(@Nullable String suggestion) {
        this.@suggestion = suggestion
    }

    void setSuggestion(@Nullable Component suggestion) {
        this.@suggestion = suggestion?.getString() ?: null
    }

    void hint(@Nullable Component hint) {
        this.@hint = hint
    }

    void hint(@Nullable String hint) {
        this.@hint = hint ? ComponentUtils.stringToComponent(hint) : null
    }

    void setHint(@Nullable Component hint) {
        this.@hint = hint
    }

    void setHint(@Nullable String hint) {
        this.@hint = hint ? ComponentUtils.stringToComponent(hint) : null
    }

//    @Requires({ font && position && size && message }) // ensure all required fields are set
    EditBox build() {
        final EditBox editBox = new EditBox(font, position.x, position.y, size.width, size.height, message)
        editBox.tooltip = tooltip
        editBox.tooltipDelay = tooltipDelay
        editBox.visible = visible
        editBox.canLoseFocus = canLoseFocus
        editBox.editable = editable
        editBox.focus = focused
        editBox.filter = filter
        editBox.formatter = formatter
        editBox.hint = hint
        editBox.maxLength = maxLength
        editBox.value = initialValue
        editBox.responder = responder
        editBox.suggestion = suggestion

        return editBox
    }
}
