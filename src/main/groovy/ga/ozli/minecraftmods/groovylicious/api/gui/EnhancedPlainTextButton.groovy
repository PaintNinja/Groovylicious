package ga.ozli.minecraftmods.groovylicious.api.gui

import com.mojang.blaze3d.vertex.PoseStack
import groovy.transform.AutoFinal
import net.minecraft.client.gui.Font
import net.minecraft.client.gui.components.PlainTextButton
import net.minecraft.network.chat.Component

import static ga.ozli.minecraftmods.groovylicious.api.gui.ColoursRegistry.instance as Colours

/**
 * A PlainTextButton that supports custom text colours.
 */
@AutoFinal
class EnhancedPlainTextButton extends PlainTextButton {
    private Font font
    private Component message
    private Component underlinedMessage

    private Colour textColour

    EnhancedPlainTextButton(int x, int y, int width, int height, Component message, OnPress onPress, Font font) {
        super(x, y, width, height, message, onPress, font)
        this.font = font
        this.message = message
        this.underlinedMessage = underlinedMessage

        this.textColour = Colours.WHITE
    }

    EnhancedPlainTextButton(int x, int y, int width, int height, Component message, OnPress onPress, Font font, Colour textColour) {
        super(x, y, width, height, message, onPress, font)
        this.font = font
        this.message = message
        this.underlinedMessage = underlinedMessage

        this.textColour = textColour ?: Colours.WHITE
    }

    @Override
    void renderButton(PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
        Component component = this.isHoveredOrFocused() ? this.underlinedMessage : this.message;
        drawString(poseStack, this.font, component, this.x, this.y, textColour.get());
    }
}
