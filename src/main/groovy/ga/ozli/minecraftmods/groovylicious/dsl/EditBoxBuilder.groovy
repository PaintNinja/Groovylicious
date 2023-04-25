package ga.ozli.minecraftmods.groovylicious.dsl

import ga.ozli.minecraftmods.groovylicious.api.GeneralUtils
import ga.ozli.minecraftmods.groovylicious.dsl.traits.FontTrait
import groovy.transform.CompileStatic
import groovy.transform.builder.Builder
import groovy.transform.builder.SimpleStrategy
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
// Todo: Does TupleConstructor account for the super classes and traits?
@CompileStatic
@Builder(builderStrategy = SimpleStrategy)
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

    private String initialValue = ''
    private int maxLength = 32

    private @Nullable String suggestion = null
    private @Nullable Component hint = null

    EditBoxBuilder() {}

    EditBoxBuilder(@DelegatesTo(value = EditBoxBuilder, strategy = DELEGATE_FIRST) final Closure closure) {
        this.tap(closure)
    }

    EditBoxBuilder(final Component message) {
        this.message = message
    }

    EditBoxBuilder(final String message) {
        this.message = GeneralUtils.stringToComponent(message)
    }

    EditBoxBuilder(final Component message, @DelegatesTo(value = EditBoxBuilder, strategy = DELEGATE_FIRST) final Closure closure) {
        this.message = message
        this.tap(closure)
    }

    EditBoxBuilder(final String message, @DelegatesTo(value = EditBoxBuilder, strategy = DELEGATE_FIRST) final Closure closure) {
        this.message = GeneralUtils.stringToComponent(message)
        this.tap(closure)
    }

    // region initialValue
    EditBoxBuilder setInitialValue(final String initialValue) {
        this.@initialValue = initialValue
        return this
    }

    EditBoxBuilder setInitialValue(final Component initialValue) {
        this.@initialValue = initialValue.getString()
        return this
    }

    String getInitialValue() {
        return this.@initialValue
    }

    EditBoxBuilder setValue(final String value) {
        this.@initialValue = value
        return this
    }

    EditBoxBuilder setValue(final Component value) {
        this.@initialValue = value.getString()
        return this
    }

    String getValue() {
        return this.@initialValue
    }
    // endregion

    // region maxLength
    EditBoxBuilder setMaxLength(final int maxLength) {
        this.@maxLength = maxLength
        return this
    }

    int getMaxLength() {
        return this.@maxLength
    }

    EditBoxBuilder setCharacterLimit(final int maxLength) {
        this.@maxLength = maxLength
        return this
    }

    int getCharacterLimit() {
        return this.@maxLength
    }
    // endregion

    // region suggestion
    EditBoxBuilder setSuggestion(@Nullable final String suggestion) {
        this.@suggestion = suggestion
        return this
    }

    EditBoxBuilder setSuggestion(@Nullable final Component suggestion) {
        this.@suggestion = suggestion?.getString() ?: null
        return this
    }

    @Nullable
    String getSuggestion() {
        return this.@suggestion
    }
    // endregion

    // region hint
    EditBoxBuilder setHint(@Nullable final Component hint) {
        this.@hint = hint
        return this
    }

    EditBoxBuilder setHint(@Nullable final String hint) {
        this.@hint = hint ? GeneralUtils.stringToComponent(hint) : null
        return this
    }

    @Nullable
    Component getHint() {
        return this.@hint
    }
    // endregion

//    @Requires({ font && position && size && message }) // ensure all required fields are set
    EditBox build() {
        final EditBox editBox = new EditBox(font, position.x, position.y, size.width, size.height, message)
        editBox.tooltip = tooltip
        editBox.tooltipDelay = tooltipDelay
        editBox.visible = visible
        editBox.canLoseFocus = canLoseFocus
        editBox.editable = editable
        editBox.focused = focused
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
