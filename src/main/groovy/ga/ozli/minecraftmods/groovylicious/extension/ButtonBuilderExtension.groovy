package ga.ozli.minecraftmods.groovylicious.extension

import groovy.transform.CompileStatic
import groovy.transform.NamedParam
import groovy.transform.NamedParams
import groovy.transform.stc.ClosureParams
import groovy.transform.stc.SimpleType
import io.github.groovymc.cgl.api.extension.EnvironmentExtension
import net.minecraft.client.gui.components.Button
import net.minecraft.client.gui.components.Tooltip
import net.minecraft.network.chat.Component
import org.jetbrains.annotations.Nullable

import static io.github.groovymc.cgl.api.extension.EnvironmentExtension.Side

@CompileStatic
@EnvironmentExtension(Side.CLIENT)
class ButtonBuilderExtension {
    static Button.Builder message(Button.Builder self, Component message) {
        self.@message = message
        return self
    }

    static Button.Builder onPress(Button.Builder self,
                                  @ClosureParams(value = SimpleType, options = 'net.minecraft.client.gui.components.Button') Button.OnPress onPress) {
        self.@onPress = onPress
        return self
    }

    static Button.Builder height(Button.Builder self, int height) {
        self.@height = height
        return self
    }
}
