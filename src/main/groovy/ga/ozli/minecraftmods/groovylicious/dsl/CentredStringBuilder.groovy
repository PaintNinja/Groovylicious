package ga.ozli.minecraftmods.groovylicious.dsl

import ga.ozli.minecraftmods.groovylicious.dsl.traits.FontTrait
import groovy.transform.CompileStatic
import net.minecraft.client.gui.components.CenteredStringWidget
import net.minecraft.network.chat.Component

import static groovy.lang.Closure.DELEGATE_FIRST

// todo: colour
@CompileStatic
class CentredStringBuilder extends AbstractWidgetBuilder implements FontTrait {
    CentredStringBuilder() {}

    CentredStringBuilder(@DelegatesTo(value = CentredStringBuilder, strategy = DELEGATE_FIRST) final Closure closure) {
        this.tap(closure)
    }

    CentredStringBuilder(final String message) {
        this.message = message
    }

    CentredStringBuilder(final Component message) {
        this.message = message
    }

    CentredStringBuilder(final String message, @DelegatesTo(value = CentredStringBuilder, strategy = DELEGATE_FIRST) final Closure closure) {
        this.message = message
        this.tap(closure)
    }

    CentredStringBuilder(final Component message, @DelegatesTo(value = CentredStringBuilder, strategy = DELEGATE_FIRST) final Closure closure) {
        this.message = message
        this.tap(closure)
    }

//    @Requires({ position && size && message && font })
    CenteredStringWidget build() {
        final centeredStringWidget = new CenteredStringWidget(position.x, position.y, size.width, size.height, message, font)
        centeredStringWidget.tooltip = tooltip
        centeredStringWidget.tooltipDelay = tooltipDelay
        centeredStringWidget.active = active
        centeredStringWidget.visible = visible

        return centeredStringWidget
    }
}
