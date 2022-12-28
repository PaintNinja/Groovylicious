package ga.ozli.minecraftmods.groovylicious.dsl

import ga.ozli.minecraftmods.groovylicious.api.gui.ComponentUtils
import ga.ozli.minecraftmods.groovylicious.api.gui.ExtensibleScreen
import groovy.transform.CompileStatic
import groovy.transform.builder.Builder
import groovy.transform.builder.SimpleStrategy
import groovy.transform.stc.ClosureParams
import groovy.transform.stc.SimpleType
import net.minecraft.client.Minecraft
import net.minecraft.network.chat.Component

import static groovy.lang.Closure.DELEGATE_FIRST

@CompileStatic
@Builder(builderStrategy = SimpleStrategy)
class ScreenBuilder {
    @Lazy
    private ExtensibleScreen backingScreen = new ExtensibleScreen(title)

    private Component title
    boolean renderBackground = true

    ScreenBuilder() {}

    ScreenBuilder(final Component title) {
        this.@title = title
    }

    ScreenBuilder(final String title) {
        this.@title = ComponentUtils.stringToComponent(title)
    }

    ScreenBuilder(@DelegatesTo(value = ScreenBuilder, strategy = DELEGATE_FIRST) final Closure closure) {
        this.tap(closure)
    }

    ScreenBuilder(final Component title, @DelegatesTo(value = ScreenBuilder, strategy = DELEGATE_FIRST) final Closure closure) {
        this.@title = title
        this.tap(closure)
    }

    ScreenBuilder(final String title, @DelegatesTo(value = ScreenBuilder, strategy = DELEGATE_FIRST) final Closure closure) {
        this.@title = ComponentUtils.stringToComponent(title)
        this.tap(closure)
    }

    // region title
    ScreenBuilder setTitle(final Component title) {
        this.@title = title
        return this
    }

    ScreenBuilder setTitle(final String title) {
        this.@title = ComponentUtils.stringToComponent(title)
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

    // region button
    void button(@DelegatesTo(value = ButtonBuilder, strategy = DELEGATE_FIRST)
                @ClosureParams(value = SimpleType, options = 'ga.ozli.minecraftmods.groovylicious.dsl.ButtonBuilder') Closure closure) {
        this.backingScreen.onInit << { ExtensibleScreen screen -> screen.addRenderableWidget(new ButtonBuilder(closure).build()) }
    }

    void button(final Component message,
                @DelegatesTo(value = ButtonBuilder, strategy = DELEGATE_FIRST)
                @ClosureParams(value = SimpleType, options = 'ga.ozli.minecraftmods.groovylicious.dsl.ButtonBuilder') Closure closure) {
        this.backingScreen.onInit << { ExtensibleScreen screen -> screen.addRenderableWidget(new ButtonBuilder(message, closure).build()) }
    }

    void button(final String message,
                @DelegatesTo(value = ButtonBuilder, strategy = DELEGATE_FIRST)
                @ClosureParams(value = SimpleType, options = 'ga.ozli.minecraftmods.groovylicious.dsl.ButtonBuilder') Closure closure) {
        this.backingScreen.onInit << { ExtensibleScreen screen -> screen.addRenderableWidget(new ButtonBuilder(message, closure).build()) }
    }
    // endregion

    // region centredString
    void centredString(@DelegatesTo(value = CentredStringBuilder, strategy = DELEGATE_FIRST)
                       @ClosureParams(value = SimpleType, options = 'ga.ozli.minecraftmods.groovylicious.dsl.CentredStringBuilder') Closure closure) {
        this.backingScreen.onInit << { ExtensibleScreen screen -> screen.addRenderableWidget(new CentredStringBuilder(closure).build()) }
    }

    void centredString(final Component message,
                       @DelegatesTo(value = CentredStringBuilder, strategy = DELEGATE_FIRST)
                       @ClosureParams(value = SimpleType, options = 'ga.ozli.minecraftmods.groovylicious.dsl.CentredStringBuilder') Closure closure) {
        this.backingScreen.onInit << { ExtensibleScreen screen -> screen.addRenderableWidget(new CentredStringBuilder(message, closure).build()) }
    }

    void centredString(final String message,
                       @DelegatesTo(value = CentredStringBuilder, strategy = DELEGATE_FIRST)
                       @ClosureParams(value = SimpleType, options = 'ga.ozli.minecraftmods.groovylicious.dsl.CentredStringBuilder') Closure closure) {
        this.backingScreen.onInit << { ExtensibleScreen screen -> screen.addRenderableWidget(new CentredStringBuilder(message, closure).build()) }
    }
    // endregion

    // region editBox
    void editBox(@DelegatesTo(value = EditBoxBuilder, strategy = DELEGATE_FIRST)
                 @ClosureParams(value = SimpleType, options = 'ga.ozli.minecraftmods.groovylicious.dsl.EditBoxBuilder') Closure closure) {
        this.backingScreen.onInit << { ExtensibleScreen screen -> screen.addRenderableWidget(new EditBoxBuilder(closure).build()) }
    }

    void editBox(final Component message,
                 @DelegatesTo(value = EditBoxBuilder, strategy = DELEGATE_FIRST)
                 @ClosureParams(value = SimpleType, options = 'ga.ozli.minecraftmods.groovylicious.dsl.EditBoxBuilder') Closure closure) {
        this.backingScreen.onInit << { ExtensibleScreen screen -> screen.addRenderableWidget(new EditBoxBuilder(message, closure).build()) }
    }

    void editBox(final String message,
                 @DelegatesTo(value = EditBoxBuilder, strategy = DELEGATE_FIRST)
                 @ClosureParams(value = SimpleType, options = 'ga.ozli.minecraftmods.groovylicious.dsl.EditBoxBuilder') Closure closure) {
        this.backingScreen.onInit << { ExtensibleScreen screen -> screen.addRenderableWidget(new EditBoxBuilder(message, closure).build()) }
    }
    // endregion

    // region plainTextButton
    void plainTextButton(@DelegatesTo(value = PlainTextButtonBuilder, strategy = DELEGATE_FIRST)
                         @ClosureParams(value = SimpleType, options = 'ga.ozli.minecraftmods.groovylicious.dsl.PlainTextButtonBuilder') Closure closure) {
        this.backingScreen.onInit << { ExtensibleScreen screen -> screen.addRenderableWidget(new PlainTextButtonBuilder(closure).build()) }
    }

    void plainTextButton(Component message,
                         @DelegatesTo(value = PlainTextButtonBuilder, strategy = DELEGATE_FIRST)
                         @ClosureParams(value = SimpleType, options = 'ga.ozli.minecraftmods.groovylicious.dsl.PlainTextButtonBuilder') Closure closure) {
        this.backingScreen.onInit << { ExtensibleScreen screen -> screen.addRenderableWidget(new PlainTextButtonBuilder(message, closure).build()) }
    }

    void plainTextButton(String message,
                         @DelegatesTo(value = PlainTextButtonBuilder, strategy = DELEGATE_FIRST)
                         @ClosureParams(value = SimpleType, options = 'ga.ozli.minecraftmods.groovylicious.dsl.PlainTextButtonBuilder') Closure closure) {
        this.backingScreen.onInit << { ExtensibleScreen screen -> screen.addRenderableWidget(new PlainTextButtonBuilder(message, closure).build()) }
    }
    // endregion

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
}
