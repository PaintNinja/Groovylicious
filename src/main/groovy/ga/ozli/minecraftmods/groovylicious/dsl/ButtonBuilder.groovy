package ga.ozli.minecraftmods.groovylicious.dsl

import ga.ozli.minecraftmods.groovylicious.api.gui.ExtensibleScreen
import groovy.contracts.Requires
import groovy.transform.CompileStatic
import groovy.transform.stc.ClosureParams
import groovy.transform.stc.FromAbstractTypeMethods
import groovy.transform.stc.PickFirstResolver
import net.minecraft.client.gui.components.Button
import org.apache.groovy.lang.annotation.Incubating

@Incubating
@CompileStatic
class ButtonBuilder implements PositionTrait, SizeTrait, TextTrait {
    Button.OnPress onPress = (Button button) -> {}
    Button.OnTooltip onTooltip

    void onPress(@ClosureParams(value = FromAbstractTypeMethods,
            options = "net.minecraft.client.gui.components.Button.OnPress") final Closure closure) {
        this.onPress = closure
    }

    // todo: change to onHover?
    // todo: add a wrapper including the ExtensibleScreen so we can run Screen.renderTooltip() with the text
    // todo: add simple text tooltip api (e.g. `tooltipText = "I'm shown on hover"`)
    void onTooltip(@ClosureParams(value = FromAbstractTypeMethods,
            options = "net.minecraft.client.gui.components.Button.OnTooltip",
            conflictResolutionStrategy = PickFirstResolver) final Closure closure) {
        this.onTooltip = closure
    }

    @Requires({ this.position && this.size && this.text }) // make sure the position, size and text aren't null
    Button buildButton() {
        if (this.onTooltip) return new Button(this.position.x, this.position.y, this.size.width, this.size.height, this.text, this.onPress, this.onTooltip)
        else return new Button(this.position.x, this.position.y, this.size.width, this.size.height, this.text, this.onPress)
    }

    @Requires({ this.position && this.size && this.text })
    Closure buildClosure() {
        return { ExtensibleScreen screenInstance ->
            screenInstance.addRenderableWidget(this.buildButton())
        }
    }
}
