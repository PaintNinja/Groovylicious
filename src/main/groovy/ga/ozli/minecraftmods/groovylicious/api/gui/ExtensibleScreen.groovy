package ga.ozli.minecraftmods.groovylicious.api.gui

import com.mojang.blaze3d.vertex.PoseStack
import groovy.transform.CompileStatic
import net.minecraft.client.gui.components.Renderable
import net.minecraft.client.gui.components.events.GuiEventListener
import net.minecraft.client.gui.narration.NarratableEntry
import net.minecraft.client.gui.screens.Screen
import net.minecraft.network.chat.Component

import static groovy.lang.Closure.DELEGATE_FIRST

@CompileStatic
class ExtensibleScreen extends Screen {
    boolean renderBackground = true
    final List<Closure> onInit = new ArrayList<>()
    final List<Closure> onRender = new ArrayList<>()

    ExtensibleScreen(final Component title) {
        super(title)
    }

    ExtensibleScreen(final String title) {
        super(ComponentUtils.stringToComponent(title))
    }

    void renderBackground(final boolean renderBackground) {
        this.renderBackground = renderBackground
    }

    @Override
    protected void init() {
        for (final Closure closure in onInit) {
            closure.delegate = this
            closure.resolveStrategy = DELEGATE_FIRST
            closure.call(this)
        }
    }

    @Override
    void render(PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
        if (renderBackground) this.renderBackground(poseStack)

        if (onRender.isEmpty()) super.render(poseStack, mouseX, mouseY, partialTick)
        else for (final Closure closure in onRender) {
            closure.delegate = this
            closure.resolveStrategy = DELEGATE_FIRST
            closure.call(this, poseStack, mouseX, mouseY, partialTick)
        }
    }

    // region access widening
    @Override
    <T extends GuiEventListener & Renderable & NarratableEntry> T addRenderableWidget(T widget) {
        return super.addRenderableWidget(widget)
    }

    @Override
    <T extends Renderable> T addRenderableOnly(T renderable) {
        return super.addRenderableOnly(renderable)
    }

    @Override
    <T extends GuiEventListener & NarratableEntry> T addWidget(T listener) {
        return super.addWidget(listener)
    }

    @Override
    void removeWidget(GuiEventListener listener) {
        super.removeWidget(listener)
    }

    @Override
    void clearWidgets() {
        super.clearWidgets()
    }

    void superRender(PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
        super.render(poseStack, mouseX, mouseY, partialTick)
    }
    // endregion
}
