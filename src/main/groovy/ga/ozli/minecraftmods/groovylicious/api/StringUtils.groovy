package ga.ozli.minecraftmods.groovylicious.api

import groovy.transform.CompileStatic
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.TextComponent
import net.minecraft.network.chat.TranslatableComponent

@CompileStatic
class StringUtils {
    static Component stringToComponent(final String str) {
        if (str.contains ' ') return new TextComponent(str)
        else return new TranslatableComponent(str)
    }
}