package ga.ozli.minecraftmods.groovylicious.extension

import ga.ozli.minecraftmods.groovylicious.api.gui.Colour
import groovy.transform.CompileStatic
import groovy.transform.stc.POJO
import io.github.groovymc.cgl.api.extension.EnvironmentExtension
import io.github.groovymc.cgl.api.chat.StyleBuilder
import net.minecraft.network.chat.Style

import static io.github.groovymc.cgl.api.extension.EnvironmentExtension.Side

@POJO
@CompileStatic
@EnvironmentExtension(Side.CLIENT)
class ColourExtension {
    static Style withColor(Style self, Colour colour) {
        self.withColor(colour?.get() ?: null)
    }

    static Style withColour(Style self, Colour colour) {
        self.withColor(colour?.get() ?: null)
    }

    static Colour getColour(Style self) {
        return new Colour(self.getColor())
    }

    static setColor(StyleBuilder self, Colour colour) {
        self.style = self.style.withColor(colour?.get() ?: null)
    }

    static setColour(StyleBuilder self, Colour colour) {
        self.style = self.style.withColor(colour?.get() ?: null)
    }
}
