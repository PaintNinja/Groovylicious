package ga.ozli.minecraftmods.groovylicious.extension

import ga.ozli.minecraftmods.groovylicious.dsl.ButtonBuilder
import ga.ozli.minecraftmods.groovylicious.dsl.CentredStringBuilder
import ga.ozli.minecraftmods.groovylicious.dsl.EditBoxBuilder
import ga.ozli.minecraftmods.groovylicious.dsl.PlainTextButtonBuilder
import ga.ozli.minecraftmods.groovylicious.dsl.ScreenBuilder
import ga.ozli.minecraftmods.groovylicious.dsl.TooltipBuilder
import groovy.transform.CompileStatic
import groovy.transform.stc.ClosureParams
import groovy.transform.stc.SimpleType
import io.github.groovymc.cgl.api.extension.EnvironmentExtension
import net.minecraft.client.gui.components.Button
import net.minecraft.client.gui.components.CenteredStringWidget
import net.minecraft.client.gui.components.EditBox
import net.minecraft.client.gui.components.PlainTextButton
import net.minecraft.client.gui.components.Tooltip
import net.minecraft.client.gui.screens.Screen
import net.minecraft.network.chat.Component

import static groovy.lang.Closure.DELEGATE_FIRST
import static io.github.groovymc.cgl.api.extension.EnvironmentExtension.Side

@CompileStatic
@EnvironmentExtension(Side.CLIENT)
class StaticGuiExtension {
    // region Button
    static ButtonBuilder builder(Button self) {
        return new ButtonBuilder()
    }

    static ButtonBuilder builder(Button self,
                                 @DelegatesTo(value = ButtonBuilder, strategy = DELEGATE_FIRST)
                                 @ClosureParams(value = SimpleType, options = 'ga.ozli.minecraftmods.groovylicious.dsl.ButtonBuilder') Closure closure) {
        return new ButtonBuilder(closure)
    }

    static Button create(Button self,
                          @DelegatesTo(value = ButtonBuilder, strategy = DELEGATE_FIRST)
                          @ClosureParams(value = SimpleType, options = 'ga.ozli.minecraftmods.groovylicious.dsl.ButtonBuilder') Closure closure) {
        return new ButtonBuilder(closure).build()
    }

    static Button create(Button self, Component message,
                         @DelegatesTo(value = ButtonBuilder, strategy = DELEGATE_FIRST)
                         @ClosureParams(value = SimpleType, options = 'ga.ozli.minecraftmods.groovylicious.dsl.ButtonBuilder') Closure closure) {
        return new ButtonBuilder(message, closure).build()
    }

    static Button create(Button self, String message,
                         @DelegatesTo(value = ButtonBuilder, strategy = DELEGATE_FIRST)
                         @ClosureParams(value = SimpleType, options = 'ga.ozli.minecraftmods.groovylicious.dsl.ButtonBuilder') Closure closure) {
        return new ButtonBuilder(message, closure).build()
    }
    // endregion

    // region CentredStringWidget
    static CentredStringBuilder builder(CenteredStringWidget self) {
        return new CentredStringBuilder()
    }

    static CentredStringBuilder builder(CenteredStringWidget self,
                                        @DelegatesTo(value = CentredStringBuilder, strategy = DELEGATE_FIRST)
                                        @ClosureParams(value = SimpleType, options = 'ga.ozli.minecraftmods.groovylicious.dsl.CentredStringBuilder') Closure closure) {
        return new CentredStringBuilder(closure)
    }

    static CenteredStringWidget create(CenteredStringWidget self,
                                       @DelegatesTo(value = CentredStringBuilder, strategy = DELEGATE_FIRST)
                                       @ClosureParams(value = SimpleType, options = 'ga.ozli.minecraftmods.groovylicious.dsl.CentredStringBuilder') Closure closure) {
        return new CentredStringBuilder(closure).build()
    }

    static CenteredStringWidget create(CenteredStringWidget self, Component message,
                                       @DelegatesTo(value = CentredStringBuilder, strategy = DELEGATE_FIRST)
                                       @ClosureParams(value = SimpleType, options = 'ga.ozli.minecraftmods.groovylicious.dsl.CentredStringBuilder') Closure closure) {
        return new CentredStringBuilder(message, closure).build()
    }

    static CenteredStringWidget create(CenteredStringWidget self, String message,
                                       @DelegatesTo(value = CentredStringBuilder, strategy = DELEGATE_FIRST)
                                       @ClosureParams(value = SimpleType, options = 'ga.ozli.minecraftmods.groovylicious.dsl.CentredStringBuilder') Closure closure) {
        return new CentredStringBuilder(message, closure).build()
    }
    // endregion

    // region PlainTextButton
    static PlainTextButtonBuilder builder(PlainTextButton self) {
        return new PlainTextButtonBuilder()
    }

    static PlainTextButtonBuilder builder(PlainTextButton self,
                                          @DelegatesTo(value = PlainTextButtonBuilder, strategy = DELEGATE_FIRST)
                                          @ClosureParams(value = SimpleType, options = 'ga.ozli.minecraftmods.groovylicious.dsl.PlainTextButtonBuilder') Closure closure) {
        return new PlainTextButtonBuilder(closure)
    }

    static PlainTextButton create(PlainTextButton self,
                                   @DelegatesTo(value = PlainTextButtonBuilder, strategy = DELEGATE_FIRST)
                                   @ClosureParams(value = SimpleType, options = 'ga.ozli.minecraftmods.groovylicious.dsl.PlainTextButtonBuilder') Closure closure) {
        return new PlainTextButtonBuilder(closure).build()
    }

    static PlainTextButton create(PlainTextButton self, Component message,
                                  @DelegatesTo(value = PlainTextButtonBuilder, strategy = DELEGATE_FIRST)
                                  @ClosureParams(value = SimpleType, options = 'ga.ozli.minecraftmods.groovylicious.dsl.PlainTextButtonBuilder') Closure closure) {
        return new PlainTextButtonBuilder(message, closure).build()
    }

    static PlainTextButton create(PlainTextButton self, String message,
                                  @DelegatesTo(value = PlainTextButtonBuilder, strategy = DELEGATE_FIRST)
                                  @ClosureParams(value = SimpleType, options = 'ga.ozli.minecraftmods.groovylicious.dsl.PlainTextButtonBuilder') Closure closure) {
        return new PlainTextButtonBuilder(message, closure).build()
    }
    // endregion

    // region EditBox
    static EditBoxBuilder builder(EditBox self) {
        return new EditBoxBuilder()
    }

    static EditBoxBuilder builder(EditBox self,
                                  @DelegatesTo(value = EditBoxBuilder, strategy = DELEGATE_FIRST)
                                  @ClosureParams(value = SimpleType, options = 'ga.ozli.minecraftmods.groovylicious.dsl.EditBoxBuilder') Closure closure) {
        return new EditBoxBuilder(closure)
    }

    static EditBox create(EditBox self,
                           @DelegatesTo(value = EditBoxBuilder, strategy = DELEGATE_FIRST)
                           @ClosureParams(value = SimpleType, options = 'ga.ozli.minecraftmods.groovylicious.dsl.EditBoxBuilder') Closure closure) {
        return new EditBoxBuilder(closure).build()
    }

    static EditBox create(EditBox self, Component message,
                          @DelegatesTo(value = EditBoxBuilder, strategy = DELEGATE_FIRST)
                          @ClosureParams(value = SimpleType, options = 'ga.ozli.minecraftmods.groovylicious.dsl.EditBoxBuilder') Closure closure) {
        return new EditBoxBuilder(message, closure).build()
    }

    static EditBox create(EditBox self, String message,
                          @DelegatesTo(value = EditBoxBuilder, strategy = DELEGATE_FIRST)
                          @ClosureParams(value = SimpleType, options = 'ga.ozli.minecraftmods.groovylicious.dsl.EditBoxBuilder') Closure closure) {
        return new EditBoxBuilder(message, closure).build()
    }
    // endregion

    // region Screen
    static ScreenBuilder builder(Screen self) {
        return new ScreenBuilder()
    }

    static ScreenBuilder builder(Screen self,
                                 @DelegatesTo(value = ScreenBuilder, strategy = DELEGATE_FIRST)
                                 @ClosureParams(value = SimpleType, options = 'ga.ozli.minecraftmods.groovylicious.dsl.ScreenBuilder') Closure closure) {
        return new ScreenBuilder(closure)
    }

    static Screen create(Screen self,
                          @DelegatesTo(value = ScreenBuilder, strategy = DELEGATE_FIRST)
                          @ClosureParams(value = SimpleType, options = 'ga.ozli.minecraftmods.groovylicious.dsl.ScreenBuilder') Closure closure) {
        return new ScreenBuilder(closure).build()
    }

    static Screen create(Screen self, Component title,
                                 @DelegatesTo(value = ScreenBuilder, strategy = DELEGATE_FIRST)
                                 @ClosureParams(value = SimpleType, options = 'ga.ozli.minecraftmods.groovylicious.dsl.ScreenBuilder') Closure closure) {
        return new ScreenBuilder(title, closure).build()
    }

    static Screen create(Screen self, String title,
                                 @DelegatesTo(value = ScreenBuilder, strategy = DELEGATE_FIRST)
                                 @ClosureParams(value = SimpleType, options = 'ga.ozli.minecraftmods.groovylicious.dsl.ScreenBuilder') Closure closure) {
        return new ScreenBuilder(title, closure).build()
    }
    // endregion

    // region Tooltip
    static TooltipBuilder builder(Tooltip self) {
        return new TooltipBuilder()
    }

    static TooltipBuilder builder(Tooltip self,
                                  @DelegatesTo(value = TooltipBuilder, strategy = DELEGATE_FIRST)
                                  @ClosureParams(value = SimpleType, options = 'ga.ozli.minecraftmods.groovylicious.dsl.TooltipBuilder') Closure closure) {
        return new TooltipBuilder(closure)
    }

    static Tooltip create(Tooltip self,
                          @DelegatesTo(value = TooltipBuilder, strategy = DELEGATE_FIRST)
                          @ClosureParams(value = SimpleType, options = 'ga.ozli.minecraftmods.groovylicious.dsl.TooltipBuilder') Closure closure) {
        return new TooltipBuilder(closure).build()
    }
    // endregion
}
