package ga.ozli.minecraftmods.groovylicious.api.gui

import com.mojang.blaze3d.vertex.PoseStack
import groovy.transform.CompileStatic
import net.minecraft.client.gui.Font
import net.minecraft.client.gui.components.Widget
import net.minecraft.client.gui.components.events.GuiEventListener
import net.minecraft.client.gui.narration.NarratableEntry
import net.minecraft.client.gui.screens.Screen
import net.minecraft.network.chat.Component
import org.intellij.lang.annotations.Language

@CompileStatic
class ExtensibleScreen extends Screen {

    final List<Closure> onPreInit = []
    final List<Closure> onInit = []
    final List<Closure> onPostInit = []

    final List<Closure> onPreRender = []
    final List<Closure> onRender = []
    final List<Closure> onPostRender = []

    boolean drawBackground = true

    ExtensibleScreen(Component title) {
        super(title)
    }

    Font getFont() {
        return super.font
    }

    @Override
    void init() {
        onPreInit.each { it.call() }
        onInit.each { it.call() }
        onPostInit.each { it.call() }
    }

    @Override
    void render(PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
        onPreRender.each { it.call() }
        if (drawBackground) this.renderBackground(poseStack)
        onRender.each { it.call(this, poseStack) }
        super.render(poseStack, mouseX, mouseY, partialTick)
        onPostRender.each {it.call(this, poseStack) }
    }

    @Override
    <T extends GuiEventListener & Widget & NarratableEntry> T addRenderableWidget(T widget) {
        return super.addRenderableWidget(widget)
    }

//    def addRenderableOnly(widget) {
//        return super.addRenderableOnly(widget)
//    }
//
//    def addWidget(listener) {
//        return super.addWidget(listener)
//    }
}

//                // alternative syntax 1
//                 size {
//                    width = 100
//                    height = 20
//                }
//
//                // alternative syntax 2
//                size = new Size(width: 100, height: 20)