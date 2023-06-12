package ga.ozli.minecraftmods.groovylicious.api.gui

import ga.ozli.minecraftmods.groovylicious.dsl.*
import groovy.transform.CompileStatic
import groovy.transform.stc.ClosureParams
import groovy.transform.stc.SimpleType
import net.minecraft.client.gui.components.Renderable
import net.minecraft.client.gui.components.events.GuiEventListener
import net.minecraft.client.gui.narration.NarratableEntry
import net.minecraft.network.chat.Component

import static groovy.lang.Closure.DELEGATE_FIRST

@CompileStatic
interface WidgetContainer {
    <T extends GuiEventListener & Renderable & NarratableEntry> void addRenderableWidget(T widget)

    // region button
    default void button(@DelegatesTo(value = ButtonBuilder, strategy = DELEGATE_FIRST)
                @ClosureParams(value = SimpleType, options = 'ga.ozli.minecraftmods.groovylicious.dsl.ButtonBuilder') Closure closure) {
        addRenderableWidget(new ButtonBuilder(closure).build())
    }

    default void button(final Component message,
                @DelegatesTo(value = ButtonBuilder, strategy = DELEGATE_FIRST)
                @ClosureParams(value = SimpleType, options = 'ga.ozli.minecraftmods.groovylicious.dsl.ButtonBuilder') Closure closure) {
        addRenderableWidget(new ButtonBuilder(message, closure).build())
    }

    default void button(final String message,
                @DelegatesTo(value = ButtonBuilder, strategy = DELEGATE_FIRST)
                @ClosureParams(value = SimpleType, options = 'ga.ozli.minecraftmods.groovylicious.dsl.ButtonBuilder') Closure closure) {
        addRenderableWidget(new ButtonBuilder(message, closure).build())
    }
    // endregion

    // region stringWidget
    default void stringWidget(@DelegatesTo(value = StringWidgetBuilder, strategy = DELEGATE_FIRST)
                      @ClosureParams(value = SimpleType, options = 'ga.ozli.minecraftmods.groovylicious.dsl.StringWidgetBuilder') Closure closure) {
        addRenderableWidget(new StringWidgetBuilder(closure).build())
    }

    default void stringWidget(final Component message,
                      @DelegatesTo(value = StringWidgetBuilder, strategy = DELEGATE_FIRST)
                      @ClosureParams(value = SimpleType, options = 'ga.ozli.minecraftmods.groovylicious.dsl.StringWidgetBuilder') Closure closure) {
        addRenderableWidget(new StringWidgetBuilder(message, closure).build())
    }

    default void stringWidget(final String message,
                      @DelegatesTo(value = StringWidgetBuilder, strategy = DELEGATE_FIRST)
                      @ClosureParams(value = SimpleType, options = 'ga.ozli.minecraftmods.groovylicious.dsl.StringWidgetBuilder') Closure closure) {
        addRenderableWidget(new StringWidgetBuilder(message, closure).build())
    }
    // endregion

    // region editBox
    default void editBox(@DelegatesTo(value = EditBoxBuilder, strategy = DELEGATE_FIRST)
                 @ClosureParams(value = SimpleType, options = 'ga.ozli.minecraftmods.groovylicious.dsl.EditBoxBuilder') Closure closure) {
        addRenderableWidget(new EditBoxBuilder(closure).build())
    }

    default void editBox(final Component message,
                 @DelegatesTo(value = EditBoxBuilder, strategy = DELEGATE_FIRST)
                 @ClosureParams(value = SimpleType, options = 'ga.ozli.minecraftmods.groovylicious.dsl.EditBoxBuilder') Closure closure) {
        addRenderableWidget(new EditBoxBuilder(message, closure).build())
    }

    default void editBox(final String message,
                 @DelegatesTo(value = EditBoxBuilder, strategy = DELEGATE_FIRST)
                 @ClosureParams(value = SimpleType, options = 'ga.ozli.minecraftmods.groovylicious.dsl.EditBoxBuilder') Closure closure) {
        addRenderableWidget(new EditBoxBuilder(message, closure).build())
    }
    // endregion

    // region plainTextButton
    default void plainTextButton(@DelegatesTo(value = PlainTextButtonBuilder, strategy = DELEGATE_FIRST)
                         @ClosureParams(value = SimpleType, options = 'ga.ozli.minecraftmods.groovylicious.dsl.PlainTextButtonBuilder') Closure closure) {
        addRenderableWidget(new PlainTextButtonBuilder(closure).build())
    }

    default void plainTextButton(Component message,
                         @DelegatesTo(value = PlainTextButtonBuilder, strategy = DELEGATE_FIRST)
                         @ClosureParams(value = SimpleType, options = 'ga.ozli.minecraftmods.groovylicious.dsl.PlainTextButtonBuilder') Closure closure) {
        addRenderableWidget(new PlainTextButtonBuilder(message, closure).build())
    }

    default void plainTextButton(String message,
                         @DelegatesTo(value = PlainTextButtonBuilder, strategy = DELEGATE_FIRST)
                         @ClosureParams(value = SimpleType, options = 'ga.ozli.minecraftmods.groovylicious.dsl.PlainTextButtonBuilder') Closure closure) {
        addRenderableWidget(new PlainTextButtonBuilder(message, closure).build())
    }
    // endregion

    // region linearLayout
//    default void linearLayout(@DelegatesTo(value = LinearLayout, strategy = DELEGATE_FIRST)
//                              @ClosureParams(value = SimpleType, options = 'ga.ozli.minecraftmods.groovylicious.api.dsl.LinearLayoutBuilder') Closure closure) {
//        addRenderableWidget(new LinearLayoutBuilder(closure).build())
//    }
    // endregion
}
