package ga.ozli.minecraftmods.groovylicious.dsl.traits

import ga.ozli.minecraftmods.groovylicious.api.gui.ComponentUtils
import groovy.transform.CompileStatic
import net.minecraft.client.gui.components.Tooltip
import net.minecraft.network.chat.Component

import javax.annotation.Nullable

@CompileStatic
trait TooltipTrait {
    @Nullable Tooltip tooltip = null

    void tooltip(final Tooltip tooltip) {
        this.tooltip = tooltip
    }

    void tooltip(final Component message) {
        this.tooltip = Tooltip.create(message)
    }

    void tooltip(final String message) {
        this.tooltip = Tooltip.create(ComponentUtils.stringToComponent(message))
    }

    void tooltip(final Component message, @Nullable final Component narration) {
        this.tooltip = Tooltip.create(message, narration)
    }

    void tooltip(final String message, @Nullable final String narration) {
        this.tooltip = Tooltip.create(ComponentUtils.stringToComponent(message), narration ? ComponentUtils.stringToComponent(narration) : null)
    }
}