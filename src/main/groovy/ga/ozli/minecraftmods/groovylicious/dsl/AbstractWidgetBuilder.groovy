package ga.ozli.minecraftmods.groovylicious.dsl

import ga.ozli.minecraftmods.groovylicious.api.gui.ComponentUtils
import ga.ozli.minecraftmods.groovylicious.dsl.traits.BoundsTrait
import ga.ozli.minecraftmods.groovylicious.dsl.traits.MessageTrait
import groovy.time.Duration
import groovy.time.TimeDuration
import groovy.transform.CompileStatic
import net.minecraft.client.gui.components.Tooltip
import net.minecraft.network.chat.Component

import javax.annotation.Nullable

@CompileStatic
abstract class AbstractWidgetBuilder implements BoundsTrait, MessageTrait {
    /*
     * Done:
     * - x
     * - y
     * - width
     * - height
     * - message
     * - tooltip
     * - tooltipdelay
     * - active
     * - visible
     *
     * Todo:
     * - alpha
     * - fgcolor
     */

    @Nullable Tooltip tooltip = null
    int tooltipDelay = 0

    boolean active = true
    boolean visible = true

    AbstractWidgetBuilder() {}

    // region tooltip
    void tooltip(final Tooltip tooltip) {
        this.@tooltip = tooltip
    }

    void tooltip(final Component message) {
        this.@tooltip = Tooltip.create(message)
    }

    void tooltip(final String message) {
        this.@tooltip = Tooltip.create(ComponentUtils.stringToComponent(message))
    }

    void tooltip(final Component message, @Nullable final Component narration) {
        this.@tooltip = Tooltip.create(message, narration)
    }

    void tooltip(final String message, @Nullable final String narration) {
        this.@tooltip = Tooltip.create(ComponentUtils.stringToComponent(message), narration ? ComponentUtils.stringToComponent(narration) : null)
    }

    void setTooltip(final Tooltip tooltip) {
        this.@tooltip = tooltip
    }

    void setTooltip(final Component message) {
        this.@tooltip = Tooltip.create(message)
    }

    void setTooltip(final String message) {
        this.@tooltip = Tooltip.create(ComponentUtils.stringToComponent(message))
    }
    // endregion

    // region tooltipDelay
    void tooltipDelay(final int tooltipMsDelay) {
        this.@tooltipDelay = tooltipMsDelay
    }

    void tooltipDelay(final TimeDuration tooltipDelay) {
        this.@tooltipDelay = tooltipDelay.toMilliseconds().intValue()
    }

    void setTooltipDelay(final int tooltipMsDelay) {
        this.@tooltipDelay = tooltipMsDelay
    }

    void setTooltipDelay(final TimeDuration tooltipDelay) {
        this.@tooltipDelay = tooltipDelay.toMilliseconds().intValue()
    }
    // endregion

    // region active
    void active(final boolean active) {
        this.@active = active
    }

    void setActive(final boolean active) {
        this.@active = active
    }
    // endregion

    // region visible
    void visible(final boolean visible) {
        this.@visible = visible
    }

    void setVisible(final boolean visible) {
        this.@visible = visible
    }
    // endregion

}
