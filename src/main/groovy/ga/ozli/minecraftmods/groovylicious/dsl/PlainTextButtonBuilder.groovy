package ga.ozli.minecraftmods.groovylicious.dsl

import ga.ozli.minecraftmods.groovylicious.api.gui.ExtensibleScreen
import groovy.contracts.Requires
import groovy.transform.CompileStatic
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.components.Button
import net.minecraft.client.gui.components.PlainTextButton
import org.apache.groovy.lang.annotation.Incubating

@Incubating
@CompileStatic
class PlainTextButtonBuilder extends ButtonBuilder {

    @Requires({ this.position && this.size && this.text })
    PlainTextButton buildPlainTextButton() {
        return new PlainTextButton(this.position.x, this.position.y, this.size.width, this.size.height, this.text, this.onPress, Minecraft.instance.font)
    }

    @Requires({ this.position && this.size && this.text })
    PlainTextButton buildPlainTextButton(final ExtensibleScreen extensibleScreen) {
        return new PlainTextButton(this.position.x, this.position.y, this.size.width, this.size.height, this.text, this.onPress, extensibleScreen.font)
    }

    @Requires({ this.position && this.size && this.text })
    @Override
    Button buildButton() {
        return buildPlainTextButton()
    }

    @Requires({ this.position && this.size && this.text })
    @Override
    Closure buildClosure() {
        return { ExtensibleScreen screenInstance ->
            screenInstance.addRenderableWidget(this.buildPlainTextButton(screenInstance))
        }
    }
}
