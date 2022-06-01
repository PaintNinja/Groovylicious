package ga.ozli.minecraftmods.groovylicious.dsl

import ga.ozli.minecraftmods.groovylicious.api.StringUtils
import ga.ozli.minecraftmods.groovylicious.api.gui.ExtensibleScreen
import groovy.transform.CompileStatic
import groovy.transform.stc.ClosureParams
import groovy.transform.stc.SimpleType
import net.minecraft.network.chat.Component
import org.apache.groovy.lang.annotation.Incubating

import static groovy.lang.Closure.DELEGATE_FIRST

@Incubating
@CompileStatic
class ScreenBuilder {

    final Component title
    boolean drawBackground = false

    final ExtensibleScreen backingScreen

    ScreenBuilder(final Component title) {
        this.title = title
        this.backingScreen = new ExtensibleScreen(title)
    }

    void button(@DelegatesTo(value = ButtonBuilder, strategy = DELEGATE_FIRST)
                @ClosureParams(value = SimpleType, options = "ga.ozli.minecraftmods.groovylicious.dsl.ButtonBuilder") final Closure closure) {
        final buttonBuilder = new ButtonBuilder()
        closure.delegate = buttonBuilder
        closure.resolveStrategy = DELEGATE_FIRST
        closure.call(buttonBuilder)
        this.backingScreen.onInit << buttonBuilder.buildClosure()
    }

    void plainTextButton(@DelegatesTo(value = PlainTextButtonBuilder, strategy = DELEGATE_FIRST)
                         @ClosureParams(value = SimpleType, options = "ga.ozli.minecraftmods.groovylicious.dsl.PlainTextButtonBuilder") final Closure closure) {
        final plainTextButtonBuilder = new PlainTextButtonBuilder()
        closure.delegate = plainTextButtonBuilder
        closure.resolveStrategy = DELEGATE_FIRST
        closure.call(plainTextButtonBuilder)
        this.backingScreen.onInit << plainTextButtonBuilder.buildClosure()
    }

    void label(@DelegatesTo(value = LabelBuilder, strategy = DELEGATE_FIRST)
               @ClosureParams(value = SimpleType, options = "ga.ozli.minecraftmods.groovylicious.dsl.LabelBuilder") final Closure closure) {
        final labelBuilder = new LabelBuilder()
        closure.delegate = labelBuilder
        closure.resolveStrategy = DELEGATE_FIRST
        closure.call(labelBuilder)
        this.backingScreen.onRender << labelBuilder.buildClosure()
    }

    void editBox(@DelegatesTo(value = EditBoxBuilder, strategy = DELEGATE_FIRST)
                 @ClosureParams(value = SimpleType, options = "ga.ozli.minecraftmods.groovylicious.dsl.EditBoxBuilder") final Closure closure) {
        final editBoxBuilder = new EditBoxBuilder()
        closure.delegate = editBoxBuilder
        closure.resolveStrategy = DELEGATE_FIRST
        closure.call(editBoxBuilder)
        this.backingScreen.onInit << editBoxBuilder.buildClosure()
    }

    ExtensibleScreen build() {
        this.backingScreen.drawBackground = this.drawBackground
        return this.backingScreen
    }

    static ExtensibleScreen makeScreen(final Component title, @DelegatesTo(value = ScreenBuilder, strategy = DELEGATE_FIRST) final Closure closure) {
        final screenBuilder = new ScreenBuilder(title)
        closure.delegate = screenBuilder
        closure.resolveStrategy = DELEGATE_FIRST
        closure.call()

        return screenBuilder.build()
    }

    static ExtensibleScreen makeScreen(final String title,
                                       @DelegatesTo(value = ScreenBuilder, strategy = DELEGATE_FIRST)
                                       @ClosureParams(value = SimpleType, options = "ga.ozli.minecraftmods.groovylicious.dsl.ScreenBuilder") final Closure closure) {
        return makeScreen(StringUtils.stringToComponent(title), closure)
    }
}

