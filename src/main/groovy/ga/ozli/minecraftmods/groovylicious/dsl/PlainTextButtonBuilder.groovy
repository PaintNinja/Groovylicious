package ga.ozli.minecraftmods.groovylicious.dsl

import ga.ozli.minecraftmods.groovylicious.api.gui.Colour
import ga.ozli.minecraftmods.groovylicious.api.gui.EnhancedPlainTextButton
import ga.ozli.minecraftmods.groovylicious.api.gui.ExtensibleScreen
import groovy.contracts.Requires
import groovy.transform.CompileStatic
import net.minecraft.ChatFormatting
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.components.Button
import org.apache.groovy.lang.annotation.Incubating

import static ga.ozli.minecraftmods.groovylicious.api.gui.ColoursRegistry.instance as Colours

@Incubating
@CompileStatic
class PlainTextButtonBuilder extends ButtonBuilder implements TextColourTrait {

    @Requires({ this.position && this.size && this.text })
    EnhancedPlainTextButton buildPlainTextButton() {
        return new EnhancedPlainTextButton(this.position.x, this.position.y, this.size.width, this.size.height, this.text, this.onPress, Minecraft.instance.font, textColour)
    }

    @Requires({ this.position && this.size && this.text })
    EnhancedPlainTextButton buildPlainTextButton(final ExtensibleScreen extensibleScreen) {
        return new EnhancedPlainTextButton(this.position.x, this.position.y, this.size.width, this.size.height, this.text, this.onPress, extensibleScreen.font, textColour)
    }

    @Requires({ this.position && this.size && this.text })
    @Override
    Button buildButton() {
        return buildPlainTextButton()
    }

    @Requires({ this.position && this.size && this.text })
    @Override
    Closure buildClosure() {
        return { ExtensibleScreen screenInstance ->
            screenInstance.addRenderableWidget(this.buildPlainTextButton(screenInstance))
        }
    }
}
