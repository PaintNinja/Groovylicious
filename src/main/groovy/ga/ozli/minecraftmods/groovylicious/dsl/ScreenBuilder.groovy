package ga.ozli.minecraftmods.groovylicious.dsl

import ga.ozli.minecraftmods.groovylicious.api.GeneralUtils
import ga.ozli.minecraftmods.groovylicious.api.gui.ExtensibleScreen
import ga.ozli.minecraftmods.groovylicious.api.gui.WidgetContainer
import groovy.transform.CompileDynamic
import groovy.transform.CompileStatic
import groovy.transform.builder.Builder
import groovy.transform.builder.SimpleStrategy
import groovy.transform.stc.ClosureParams
import groovy.transform.stc.SimpleType
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.components.Renderable
import net.minecraft.client.gui.components.events.GuiEventListener
import net.minecraft.client.gui.layouts.LayoutElement
import net.minecraft.client.gui.narration.NarratableEntry
import net.minecraft.network.chat.Component

import static groovy.lang.Closure.DELEGATE_FIRST

@CompileStatic
@Builder(builderStrategy = SimpleStrategy)
class ScreenBuilder implements WidgetContainer {
    @Lazy
    private ExtensibleScreen backingScreen = new ExtensibleScreen(title)

    private Component title
    boolean renderBackground = true

    ScreenBuilder() {}

    ScreenBuilder(final Component title) {
        this.@title = title
    }

    ScreenBuilder(final String title) {
        this.@title = GeneralUtils.stringToComponent(title)
    }

    ScreenBuilder(@DelegatesTo(value = ScreenBuilder, strategy = DELEGATE_FIRST) final Closure closure) {
        this.tap(closure)
    }

    ScreenBuilder(final Component title, @DelegatesTo(value = ScreenBuilder, strategy = DELEGATE_FIRST) final Closure closure) {
        this.@title = title
        this.tap(closure)
    }

    ScreenBuilder(final String title, @DelegatesTo(value = ScreenBuilder, strategy = DELEGATE_FIRST) final Closure closure) {
        this.@title = GeneralUtils.stringToComponent(title)
        this.tap(closure)
    }

    // region title
    ScreenBuilder setTitle(final Component title) {
        this.@title = title
        return this
    }

    ScreenBuilder setTitle(final String title) {
        this.@title = GeneralUtils.stringToComponent(title)
        return this
    }

    Component getTitle() {
        return this.@title
    }
    // endregion title

    int getScreenHeight() {
        return this.backingScreen.height
    }

    int getScreenWidth() {
        return this.backingScreen.width
    }

    static Minecraft getMinecraft() {
        return Minecraft.instance
    }

    void onInit(@DelegatesTo(value = ExtensibleScreen, strategy = DELEGATE_FIRST)
                @ClosureParams(value = SimpleType, options = 'ga.ozli.minecraftmods.groovylicious.api.gui.ExtensibleScreen') Closure closure) {
        this.backingScreen.onInit << closure
    }

    void onRender(@DelegatesTo(value = ExtensibleScreen, strategy = DELEGATE_FIRST)
                  @ClosureParams(value = SimpleType, options = ['ga.ozli.minecraftmods.groovylicious.api.gui.ExtensibleScreen', 'com.mojang.blaze3d.vertex.PoseStack', 'int', 'int', 'float']) Closure closure) {
        this.backingScreen.onRender << closure
    }

    ExtensibleScreen build() {
        this.backingScreen.renderBackground = this.renderBackground

        return this.backingScreen
    }

    <T extends GuiEventListener & Renderable & NarratableEntry> void addRenderableWidget(T widget) {
        onInit { ExtensibleScreen screen -> screen.addRenderableWidget(widget) }
    }
}
