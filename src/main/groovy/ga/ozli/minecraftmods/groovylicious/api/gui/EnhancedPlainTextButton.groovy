package ga.ozli.minecraftmods.groovylicious.api.gui

import com.mojang.blaze3d.vertex.PoseStack
import groovy.transform.CompileStatic
import net.minecraft.client.gui.Font
import net.minecraft.client.gui.components.PlainTextButton
import net.minecraft.network.chat.Component

import javax.annotation.Nullable

import static ga.ozli.minecraftmods.groovylicious.api.gui.ColoursRegistry.instance as Colours

/**
 * A PlainTextButton that supports custom text colours.
 */
@CompileStatic
class EnhancedPlainTextButton extends PlainTextButton {

    private final int textColour

    EnhancedPlainTextButton(int x, int y, int width, int height, Component message, OnPress onPress, Font font, @Nullable Colour textColour = null) {
        super(x, y, width, height, message, onPress, font)

        this.textColour = textColour?.get() ?: Colours.WHITE.get()
    }

    @Override
    void renderButton(PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
        final Component component = this.isHoveredOrFocused() ? this.underlinedMessage : this.message
        drawString(poseStack, this.font, component, this.x, this.y, textColour)
    }
}
