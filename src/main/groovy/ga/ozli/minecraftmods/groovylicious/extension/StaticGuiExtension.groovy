package ga.ozli.minecraftmods.groovylicious.extension

import ga.ozli.minecraftmods.groovylicious.dsl.ButtonBuilder
import ga.ozli.minecraftmods.groovylicious.dsl.EditBoxBuilder
import ga.ozli.minecraftmods.groovylicious.dsl.PlainTextButtonBuilder
import groovy.transform.CompileStatic
import groovy.transform.stc.ClosureParams
import groovy.transform.stc.SimpleType
import io.github.groovymc.cgl.api.extension.EnvironmentExtension
import net.minecraft.client.gui.components.Button
import net.minecraft.client.gui.components.EditBox
import net.minecraft.client.gui.components.PlainTextButton
import net.minecraft.network.chat.Component

import static groovy.lang.Closure.DELEGATE_FIRST
import static io.github.groovymc.cgl.api.extension.EnvironmentExtension.Side

@CompileStatic
@EnvironmentExtension(Side.CLIENT)
class StaticGuiExtension {
    static Button builder(Button self,
                          @DelegatesTo(value = ButtonBuilder, strategy = DELEGATE_FIRST)
                          @ClosureParams(value = SimpleType, options = 'ga.ozli.minecraftmods.groovylicious.dsl.ButtonBuilder') Closure closure) {
        return new ButtonBuilder(closure).build()
    }

    static Button builder(Button self, Component message,
                          @DelegatesTo(value = ButtonBuilder, strategy = DELEGATE_FIRST)
                          @ClosureParams(value = SimpleType, options = 'ga.ozli.minecraftmods.groovylicious.dsl.ButtonBuilder') Closure closure) {
        return new ButtonBuilder(message, closure).build()
    }

    static Button builder(Button self, String message,
                          @DelegatesTo(value = ButtonBuilder, strategy = DELEGATE_FIRST)
                          @ClosureParams(value = SimpleType, options = 'ga.ozli.minecraftmods.groovylicious.dsl.ButtonBuilder') Closure closure) {
        return new ButtonBuilder(message, closure).build()
    }

    static PlainTextButton builder(PlainTextButton self,
                                   @DelegatesTo(value = PlainTextButtonBuilder, strategy = DELEGATE_FIRST)
                                   @ClosureParams(value = SimpleType, options = 'ga.ozli.minecraftmods.groovylicious.dsl.PlainTextButtonBuilder') Closure closure) {
        return new PlainTextButtonBuilder(closure).build()
    }

    static PlainTextButton builder(PlainTextButton self, Component message,
                                   @DelegatesTo(value = PlainTextButtonBuilder, strategy = DELEGATE_FIRST)
                                   @ClosureParams(value = SimpleType, options = 'ga.ozli.minecraftmods.groovylicious.dsl.PlainTextButtonBuilder') Closure closure) {
        return new PlainTextButtonBuilder(message, closure).build()
    }

    static PlainTextButton builder(PlainTextButton self, String message,
                                   @DelegatesTo(value = PlainTextButtonBuilder, strategy = DELEGATE_FIRST)
                                   @ClosureParams(value = SimpleType, options = 'ga.ozli.minecraftmods.groovylicious.dsl.PlainTextButtonBuilder') Closure closure) {
        return new PlainTextButtonBuilder(message, closure).build()
    }

    static EditBox builder(EditBox self,
                           @DelegatesTo(value = EditBoxBuilder, strategy = DELEGATE_FIRST)
                           @ClosureParams(value = SimpleType, options = 'ga.ozli.minecraftmods.groovylicious.dsl.EditBoxBuilder') Closure closure) {
        return new EditBoxBuilder(closure).build()
    }

    static EditBox builder(EditBox self, Component message,
                           @DelegatesTo(value = EditBoxBuilder, strategy = DELEGATE_FIRST)
                           @ClosureParams(value = SimpleType, options = 'ga.ozli.minecraftmods.groovylicious.dsl.EditBoxBuilder') Closure closure) {
        return new EditBoxBuilder(message, closure).build()
    }

    static EditBox builder(EditBox self, String message,
                           @DelegatesTo(value = EditBoxBuilder, strategy = DELEGATE_FIRST)
                           @ClosureParams(value = SimpleType, options = 'ga.ozli.minecraftmods.groovylicious.dsl.EditBoxBuilder') Closure closure) {
        return new EditBoxBuilder(message, closure).build()
    }
}
