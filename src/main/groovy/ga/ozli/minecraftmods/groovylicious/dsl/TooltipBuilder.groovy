package ga.ozli.minecraftmods.groovylicious.dsl

import ga.ozli.minecraftmods.groovylicious.api.gui.ComponentUtils
import groovy.transform.Canonical
import groovy.transform.CompileStatic
import net.minecraft.client.gui.components.Tooltip
import net.minecraft.network.chat.Component

import javax.annotation.Nullable

import static groovy.lang.Closure.DELEGATE_FIRST

@Canonical
@CompileStatic
class TooltipBuilder {
    Component message
    @Nullable Component narration = null

    TooltipBuilder() {}

    TooltipBuilder(final Component message) {
        this.@message = message
    }

    TooltipBuilder(final String message) {
        this.@message = ComponentUtils.stringToComponent(message)
    }

    TooltipBuilder(final Component message, @Nullable final Component narration) {
        this.@message = message
        this.@narration = narration
    }

    TooltipBuilder(final String message, final String narration) {
        this.@message = ComponentUtils.stringToComponent(message)
        this.@narration = ComponentUtils.stringToComponent(narration)
    }

    TooltipBuilder(final Component message, final String narration) {
        this.@message = message
        this.@narration = ComponentUtils.stringToComponent(narration)
    }

    TooltipBuilder(final String message, @Nullable final Component narration) {
        this.@message = ComponentUtils.stringToComponent(message)
        this.@narration = narration
    }

    TooltipBuilder(@DelegatesTo(value = TooltipBuilder, strategy = DELEGATE_FIRST) final Closure closure) {
        this.tap(closure)
    }

    TooltipBuilder setMessage(final Component message) {
        this.@message = message
        return this
    }

    TooltipBuilder setMessage(final String message) {
        this.@message = ComponentUtils.stringToComponent(message)
        return this
    }

    TooltipBuilder setNarration(@Nullable final Component narration) {
        this.@narration = narration
        return this
    }

    TooltipBuilder setNarration(final String narration) {
        this.@narration = ComponentUtils.stringToComponent(narration)
        return this
    }

    Tooltip build() {
        if (narration) return Tooltip.create(message, narration)
        else return Tooltip.create(message)
    }
}
