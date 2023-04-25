package ga.ozli.minecraftmods.groovylicious.api.gui

import ga.ozli.minecraftmods.groovylicious.dsl.*
import groovy.transform.CompileDynamic
import groovy.transform.stc.ClosureParams
import groovy.transform.stc.SimpleType
import net.minecraft.client.gui.layouts.LinearLayout
import net.minecraft.network.chat.Component

import static groovy.lang.Closure.DELEGATE_FIRST

@CompileDynamic
trait WidgetContainer {
    // region button
    void button(@DelegatesTo(value = ButtonBuilder, strategy = DELEGATE_FIRST)
                @ClosureParams(value = SimpleType, options = 'ga.ozli.minecraftmods.groovylicious.dsl.ButtonBuilder') Closure closure) {
        addWidgetPrettyPlease(new ButtonBuilder(closure).build())
    }

    void button(final Component message,
                @DelegatesTo(value = ButtonBuilder, strategy = DELEGATE_FIRST)
                @ClosureParams(value = SimpleType, options = 'ga.ozli.minecraftmods.groovylicious.dsl.ButtonBuilder') Closure closure) {
        addWidgetPrettyPlease(new ButtonBuilder(message, closure).build())
    }

    void button(final String message,
                @DelegatesTo(value = ButtonBuilder, strategy = DELEGATE_FIRST)
                @ClosureParams(value = SimpleType, options = 'ga.ozli.minecraftmods.groovylicious.dsl.ButtonBuilder') Closure closure) {
        addWidgetPrettyPlease(new ButtonBuilder(message, closure).build())
    }
    // endregion

    // region stringWidget
    void stringWidget(@DelegatesTo(value = StringWidgetBuilder, strategy = DELEGATE_FIRST)
                      @ClosureParams(value = SimpleType, options = 'ga.ozli.minecraftmods.groovylicious.dsl.StringWidgetBuilder') Closure closure) {
        addWidgetPrettyPlease(new StringWidgetBuilder(closure).build())
    }

    void stringWidget(final Component message,
                      @DelegatesTo(value = StringWidgetBuilder, strategy = DELEGATE_FIRST)
                      @ClosureParams(value = SimpleType, options = 'ga.ozli.minecraftmods.groovylicious.dsl.StringWidgetBuilder') Closure closure) {
        addWidgetPrettyPlease(new StringWidgetBuilder(message, closure).build())
    }

    void stringWidget(final String message,
                      @DelegatesTo(value = StringWidgetBuilder, strategy = DELEGATE_FIRST)
                      @ClosureParams(value = SimpleType, options = 'ga.ozli.minecraftmods.groovylicious.dsl.StringWidgetBuilder') Closure closure) {
        addWidgetPrettyPlease(new StringWidgetBuilder(message, closure).build())
    }
    // endregion

    // region editBox
    void editBox(@DelegatesTo(value = EditBoxBuilder, strategy = DELEGATE_FIRST)
                 @ClosureParams(value = SimpleType, options = 'ga.ozli.minecraftmods.groovylicious.dsl.EditBoxBuilder') Closure closure) {
        addWidgetPrettyPlease(new EditBoxBuilder(closure).build())
    }

    void editBox(final Component message,
                 @DelegatesTo(value = EditBoxBuilder, strategy = DELEGATE_FIRST)
                 @ClosureParams(value = SimpleType, options = 'ga.ozli.minecraftmods.groovylicious.dsl.EditBoxBuilder') Closure closure) {
        addWidgetPrettyPlease(new EditBoxBuilder(message, closure).build())
    }

    void editBox(final String message,
                 @DelegatesTo(value = EditBoxBuilder, strategy = DELEGATE_FIRST)
                 @ClosureParams(value = SimpleType, options = 'ga.ozli.minecraftmods.groovylicious.dsl.EditBoxBuilder') Closure closure) {
        addWidgetPrettyPlease(new EditBoxBuilder(message, closure).build())
    }
    // endregion

    // region plainTextButton
    void plainTextButton(@DelegatesTo(value = PlainTextButtonBuilder, strategy = DELEGATE_FIRST)
                         @ClosureParams(value = SimpleType, options = 'ga.ozli.minecraftmods.groovylicious.dsl.PlainTextButtonBuilder') Closure closure) {
        addWidgetPrettyPlease(new PlainTextButtonBuilder(closure).build())
    }

    void plainTextButton(Component message,
                         @DelegatesTo(value = PlainTextButtonBuilder, strategy = DELEGATE_FIRST)
                         @ClosureParams(value = SimpleType, options = 'ga.ozli.minecraftmods.groovylicious.dsl.PlainTextButtonBuilder') Closure closure) {
        addWidgetPrettyPlease(new PlainTextButtonBuilder(message, closure).build())
    }

    void plainTextButton(String message,
                         @DelegatesTo(value = PlainTextButtonBuilder, strategy = DELEGATE_FIRST)
                         @ClosureParams(value = SimpleType, options = 'ga.ozli.minecraftmods.groovylicious.dsl.PlainTextButtonBuilder') Closure closure) {
        addWidgetPrettyPlease(new PlainTextButtonBuilder(message, closure).build())
    }
    // endregion

    // region linearLayout
    void linearLayout(@DelegatesTo(value = LinearLayout, strategy = DELEGATE_FIRST)
                              @ClosureParams(value = SimpleType, options = 'ga.ozli.minecraftmods.groovylicious.api.dsl.LinearLayoutBuilder') Closure closure) {
        addWidgetPrettyPlease(new LinearLayoutBuilder(closure).build())
    }
    // endregion
}
