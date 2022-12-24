package ga.ozli.minecraftmods.groovylicious.api.gui

import groovy.transform.CompileStatic
import net.minecraft.network.chat.Component

@CompileStatic
class ComponentUtils {
    static final Component PLACEHOLDER_COMPONENT = Component.literal('')

    static Component stringToComponent(final String str) {
        if (str.contains('.') && !str.contains(' ') && str.chars().anyMatch(Character::isUpperCase))
            return Component.translatable(str)
        else
            return Component.literal(str)
    }
}
