package ga.ozli.minecraftmods.groovylicious.dsl

import ga.ozli.minecraftmods.groovylicious.api.gui.ComponentUtils
import ga.ozli.minecraftmods.groovylicious.dsl.traits.BoundsTrait
import ga.ozli.minecraftmods.groovylicious.dsl.traits.FontTrait
import ga.ozli.minecraftmods.groovylicious.dsl.traits.MessageTrait
import groovy.contracts.Requires
import groovy.transform.CompileStatic
import net.minecraft.client.gui.components.EditBox
import net.minecraft.network.chat.Component

import javax.annotation.Nullable
import java.util.function.Predicate

// Todo: Colour support
@CompileStatic
class EditBoxBuilder implements FontTrait, BoundsTrait, MessageTrait {
    boolean editable = true
    boolean visible = true
    boolean focused = false
    boolean canLoseFocus = true
    @Nullable String suggestion = null
    @Nullable Component hint = null
    int maxLength = 32
    Predicate<String> filter = (String text) -> true

    EditBoxBuilder() {}

    EditBoxBuilder(final Closure closure) {
        this.tap(closure)
    }

    EditBoxBuilder(final Component message) {
        this.message = message
    }

    EditBoxBuilder(final String message) {
        this.message = ComponentUtils.stringToComponent(message)
    }

    EditBoxBuilder(final Component message, final Closure closure) {
        this.message = message
        this.tap(closure)
    }

    EditBoxBuilder(final String message, final Closure closure) {
        this.message = ComponentUtils.stringToComponent(message)
        this.tap(closure)
    }

    void editable(final boolean editable) {
        this.editable = editable
    }

    void visible(final boolean visible) {
        this.visible = visible
    }

    void focused(final boolean focused) {
        this.focused = focused
    }

    void canLoseFocus(final boolean canLoseFocus) {
        this.canLoseFocus = canLoseFocus
    }

    void suggestion(@Nullable String suggestion) {
        this.suggestion = suggestion
    }

    void hint(@Nullable Component hint) {
        this.hint = hint
    }

    void hint(@Nullable String hint) {
        this.hint = hint ? ComponentUtils.stringToComponent(hint) : null
    }

    void maxLength(final int maxLength) {
        this.maxLength = maxLength
    }

    void filter(final Predicate<String> filter) {
        this.filter = filter
    }

    @Requires({ font && position && size && message }) // ensure all required fields are set
    EditBox build() {
        final EditBox editBox = new EditBox(font, position.x, position.y, size.width, size.height, message)
        editBox.editable = editable
        editBox.visible = visible
        editBox.focus = focused
        editBox.canLoseFocus = canLoseFocus
        editBox.suggestion = suggestion
        editBox.hint = hint
        editBox.maxLength = maxLength
        editBox.filter = filter
        return editBox
    }
}
