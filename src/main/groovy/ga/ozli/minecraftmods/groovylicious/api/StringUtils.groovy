package ga.ozli.minecraftmods.groovylicious.api

import groovy.transform.CompileStatic
import net.minecraft.network.chat.Component

@CompileStatic
class StringUtils {
    static Component stringToComponent(final String str) {
        if (str.contains ' ') return Component.literal(str)
        else return Component.translatable(str)
    }
}