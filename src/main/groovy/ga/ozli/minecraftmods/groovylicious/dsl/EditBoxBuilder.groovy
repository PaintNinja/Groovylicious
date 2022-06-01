package ga.ozli.minecraftmods.groovylicious.dsl

import ga.ozli.minecraftmods.groovylicious.api.gui.Colour
import ga.ozli.minecraftmods.groovylicious.api.gui.ExtensibleScreen
import groovy.contracts.Requires
import groovy.transform.CompileStatic
import net.minecraft.ChatFormatting
import net.minecraft.client.gui.components.EditBox
import org.apache.groovy.lang.annotation.Incubating

import static ga.ozli.minecraftmods.groovylicious.api.gui.ColoursRegistry.instance as Colours

@Incubating
@CompileStatic
class EditBoxBuilder implements PositionTrait, SizeTrait, TextTrait {
    boolean bordered = true
    boolean canLoseFocus = true
    boolean isEditable = true
    Colour textColour = Colours.EDITBOX_TEXT
    Colour textColourUneditable = Colours.EDITBOX_TEXTUNEDITABLE

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

    void textColour(final Map args) {
        this.textColour = new Colour(args)
    }

    @Requires({ this.position && this.size && this.text })
    EditBox buildEditbox(final ExtensibleScreen extensibleScreen) {
        return new EditBox(extensibleScreen.font, this.position.x, this.position.y, this.size.width, this.size.height, this.text)
    }

    // Note: this closure is meant to be run inside a Screen's init method, such as ExtensibleScreen's onPreInit/onInit/onPostInit
    @Requires({ this.position && this.size && this.text })
    Closure buildClosure() {
        return { ExtensibleScreen screenInstance ->
            screenInstance.addRenderableWidget(this.buildEditbox(screenInstance))
        }
    }


}
