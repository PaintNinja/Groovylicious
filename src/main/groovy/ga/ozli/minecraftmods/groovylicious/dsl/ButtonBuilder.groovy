package ga.ozli.minecraftmods.groovylicious.dsl

import ga.ozli.minecraftmods.groovylicious.api.gui.ExtensibleScreen
import groovy.contracts.Requires
import groovy.transform.CompileStatic
import groovy.transform.NullCheck
import net.minecraft.client.gui.components.Button
import org.apache.groovy.lang.annotation.Incubating

@Incubating
@CompileStatic
class ButtonBuilder implements PositionTrait, SizeTrait, TextTrait {
    Closure onClick = { -> }

    void onClick(final Closure closure) {
        this.onClick = closure
    }

    @Requires({ this.position && this.size && this.text }) // make sure the position, size and text aren't null
    Button buildButton() {
        return new Button(this.position.x, this.position.y, this.size.width, this.size.height, this.text, this.onClick)
    }

    @Requires({ this.position && this.size && this.text })
    Closure buildClosure() {
        return { ExtensibleScreen screenInstance ->
            screenInstance.addRenderableWidget(this.buildButton())
        }
    }
}
