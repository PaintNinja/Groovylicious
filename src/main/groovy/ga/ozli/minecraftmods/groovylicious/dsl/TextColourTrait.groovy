package ga.ozli.minecraftmods.groovylicious.dsl

import ga.ozli.minecraftmods.groovylicious.api.gui.Colour
import groovy.transform.CompileStatic
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.TextColor

import static ga.ozli.minecraftmods.groovylicious.api.gui.ColoursRegistry.instance as Colours

@CompileStatic
trait TextColourTrait {
    Colour textColour = Colours.WHITE

    void textColour(final Colour colour) {
        this.textColour = colour
    }

    void textColour(final int packed) {
        this.textColour = Colour.of(packed)
    }

    void textColour(final int red, final int green, final int blue) {
        this.textColour = Colour.of(red, green, blue)
    }

    void textColour(final int alpha, final int red, final int green, final int blue) {
        this.textColour = Colour.of(alpha, red, green, blue)
    }

    void textColour(final int[] argb) {
        this.textColour = Colour.of(argb)
    }

    void textColour(final ChatFormatting colour) {
        this.textColour = Colour.of(colour)
    }

    void textColour(final TextColor colour) {
        this.textColour = Colour.of(colour)
    }

    void textColour(final Map args) {
        this.textColour = new Colour(args)
    }
}