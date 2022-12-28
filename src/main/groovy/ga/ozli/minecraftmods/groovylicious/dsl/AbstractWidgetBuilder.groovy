package ga.ozli.minecraftmods.groovylicious.dsl

import ga.ozli.minecraftmods.groovylicious.api.gui.ComponentUtils
import ga.ozli.minecraftmods.groovylicious.dsl.traits.BoundsTrait
import ga.ozli.minecraftmods.groovylicious.dsl.traits.MessageTrait
import groovy.time.TimeDuration
import groovy.transform.CompileStatic
import groovy.transform.builder.Builder
import groovy.transform.builder.SimpleStrategy
import net.minecraft.client.gui.components.Tooltip
import net.minecraft.network.chat.Component

import javax.annotation.Nullable

import static groovy.lang.Closure.DELEGATE_FIRST

@CompileStatic
@Builder(builderStrategy = SimpleStrategy)
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

    private @Nullable Tooltip tooltip = null
    private int tooltipDelay = 0

    boolean active = true
    boolean visible = true

    // region tooltip
    void setTooltip(@Nullable final Tooltip tooltip) {
        this.@tooltip = tooltip
    }

    void setTooltip(final Component message) {
        this.@tooltip = Tooltip.create(message)
    }

    void setTooltip(final String message) {
        this.@tooltip = Tooltip.create(ComponentUtils.stringToComponent(message))
    }

    void tooltip(@DelegatesTo(value = Tooltip, strategy = DELEGATE_FIRST) final Closure closure) {
        this.@tooltip = new TooltipBuilder().tap(closure).build()
    }

    @Nullable
    Tooltip getTooltip() {
        return this.@tooltip
    }
    // endregion

    // region tooltipDelay
    void setTooltipDelay(final int tooltipDelayMs) {
        this.@tooltipDelay = tooltipDelayMs
    }

    void setTooltipDelay(final TimeDuration tooltipDelay) {
        this.@tooltipDelay = tooltipDelay.toMilliseconds().intValue()
    }

    int getTooltipDelay() {
        return this.@tooltipDelay
    }
    // endregion
}
