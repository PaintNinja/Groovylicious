package ga.ozli.minecraftmods.groovylicious.dsl.traits

import groovy.transform.CompileStatic
import groovy.transform.stc.ClosureParams
import groovy.transform.stc.SimpleType
import net.minecraft.client.gui.components.Button

@CompileStatic
trait OnPressTrait {
    Button.OnPress onPress = (Button button) -> {}

    void onPress(@ClosureParams(value = SimpleType, options = 'net.minecraft.client.gui.components.Button')
                 final Button.OnPress onPress) {
        this.@onPress = onPress
    }

    void setOnPress(@ClosureParams(value = SimpleType, options = 'net.minecraft.client.gui.components.Button')
                 final Button.OnPress onPress) {
        this.@onPress = onPress
    }
}
