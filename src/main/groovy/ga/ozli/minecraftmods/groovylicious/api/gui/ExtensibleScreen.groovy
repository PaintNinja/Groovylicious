package ga.ozli.minecraftmods.groovylicious.api.gui

import com.mojang.blaze3d.vertex.PoseStack
import ga.ozli.minecraftmods.groovylicious.api.StringUtils
import groovy.transform.CompileStatic
import net.minecraft.Util
import net.minecraft.client.gui.Font
import net.minecraft.client.gui.components.Widget
import net.minecraft.client.gui.components.events.GuiEventListener
import net.minecraft.client.gui.narration.NarratableEntry
import net.minecraft.client.gui.screens.ConfirmLinkScreen
import net.minecraft.client.gui.screens.Screen
import net.minecraft.network.chat.Component

import java.nio.file.Path

@CompileStatic
class ExtensibleScreen extends Screen {

    final List<Closure> onPreInit = []
    final List<Closure> onInit = []
    final List<Closure> onPostInit = []

    final List<Closure> onPreRender = []
    final List<Closure> onRender = []
    final List<Closure> onPostRender = []

    boolean drawBackground = true

    ExtensibleScreen(final Component title) {
        super(title)
    }

    ExtensibleScreen(final String title) {
        super(StringUtils.stringToComponent(title))
    }

    // --- Screen overrides ---
    @Override
    void init() {
        if (!onPreInit.isEmpty()) onPreInit.each { it.call(this) }
        onInit.each { it.call(this) }
        if (!onPostInit.isEmpty()) onPostInit.each { it.call(this) }
    }

    @Override
    void render(PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
        if (!onPreRender.isEmpty()) onPreRender.each { it.call(this, poseStack) }
        if (drawBackground) this.renderBackground(poseStack)
        onRender.each { it.call(this, poseStack) }
        super.render(poseStack, mouseX, mouseY, partialTick)
        if (!onPostRender.isEmpty()) onPostRender.each { it.call(this, poseStack) }
    }

    // --- Access widening ---
    @Override
    <T extends GuiEventListener & Widget & NarratableEntry> T addRenderableWidget(T widget) {
        return super.addRenderableWidget(widget)
    }

    @Override
    <T extends Widget> T addRenderableOnly(T widget) {
        return super.addRenderableOnly(widget)
    }

    @Override
    <T extends GuiEventListener & NarratableEntry> T addWidget(T listener) {
        return super.addWidget(listener)
    }

    Font getFont() {
        return super.font
    }

    /**
     * Prefer using {@link ExtensibleScreen#confirmOpenLink} instead as it handles asking for confirmation for you.
     */
    void openLink(URI uri) {
        this.openLink(uri)
    }

    URI getClickedLink() {
       return this.clickedLink
    }

    void setClickedLink(URI uri) {
        this.clickedLink = uri
    }

    void confirmLink(final boolean clickedYes) {
        if (clickedYes)
            this.openLink(this.clickedLink)

        this.clickedLink = null
        this.minecraft.setScreen(this)
    }

    // --- Added features ---
    void confirmOpenLink(String url) {
        confirmOpenLink(new URI(url))
    }

    void confirmOpenLink(URI uri) {
        if (this.minecraft.options.chatLinksPrompt().get()) {
            this.clickedLink = uri
            this.minecraft.screen = new ConfirmLinkScreen(this::confirmLink, uri.toString(), false)
        } else {
            this.openLink(uri)
        }
    }

    void confirmOpenFolder(Path path) {
        confirmOpenLink(path.toUri())
    }

    void openFolder(Path path) {
        openLink(path.toUri())
    }

    void confirmOpenFile(File file) {
        openLink(file.toURI())
    }

    void openFile(File file) {
        openLink(file.toURI())
    }
}
