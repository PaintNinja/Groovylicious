package ga.ozli.minecraftmods.groovylicious.extension

import groovy.transform.CompileStatic
import groovy.transform.NamedParam
import groovy.transform.NamedParams
import groovy.transform.stc.ClosureParams
import groovy.transform.stc.SimpleType
import io.github.groovymc.cgl.api.extension.EnvironmentExtension
import net.minecraft.client.gui.components.Button
import net.minecraft.client.gui.components.MultiLineEditBox
import net.minecraft.client.gui.components.MultilineTextField
import net.minecraft.client.gui.components.Tooltip
import net.minecraft.network.chat.Component
import org.jetbrains.annotations.Nullable

import static io.github.groovymc.cgl.api.extension.EnvironmentExtension.Side

@CompileStatic
@Category(Button.Builder)
@EnvironmentExtension(Side.CLIENT)
class ButtonBuilderExtension {
    Button.Builder height(int height) {
        //this.@height = height
        return this
    }
}
