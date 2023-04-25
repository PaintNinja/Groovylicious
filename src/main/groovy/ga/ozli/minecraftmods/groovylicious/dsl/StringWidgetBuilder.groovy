package ga.ozli.minecraftmods.groovylicious.dsl

import ga.ozli.minecraftmods.groovylicious.api.gui.Alignment
import ga.ozli.minecraftmods.groovylicious.dsl.traits.AlignmentTrait
import ga.ozli.minecraftmods.groovylicious.dsl.traits.FontTrait
import groovy.transform.CompileStatic
import net.minecraft.client.gui.components.StringWidget
import net.minecraft.network.chat.Component

import static groovy.lang.Closure.DELEGATE_FIRST

// todo: colour
@CompileStatic
class StringWidgetBuilder extends AbstractWidgetBuilder implements FontTrait, AlignmentTrait {
    StringWidgetBuilder() {}

    StringWidgetBuilder(@DelegatesTo(value = StringWidgetBuilder, strategy = DELEGATE_FIRST) final Closure closure) {
        this.tap(closure)
    }

    StringWidgetBuilder(final Component message) {
        this.message = message
    }

    StringWidgetBuilder(final String message) {
        this.message = message
    }

    StringWidgetBuilder(final Component message, @DelegatesTo(value = StringWidgetBuilder, strategy = DELEGATE_FIRST) final Closure closure) {
        this.message = message
        this.tap(closure)
    }

    StringWidgetBuilder(final String message, @DelegatesTo(value = StringWidgetBuilder, strategy = DELEGATE_FIRST) final Closure closure) {
        this.message = message
        this.tap(closure)
    }

//    @Requires({ position && size && message && font })
    StringWidget build() {
        final stringWidget = new StringWidget(position.x, position.y, size.width, size.height, message, font)
        stringWidget.tooltip = tooltip
        stringWidget.tooltipDelay = tooltipDelay
        stringWidget.active = active
        stringWidget.visible = visible

        return switch (align) {
            case Alignment.LEFT -> stringWidget.alignLeft()
            case Alignment.CENTRE -> stringWidget.alignCenter()
            case Alignment.RIGHT -> stringWidget.alignRight()
        }
    }
}
