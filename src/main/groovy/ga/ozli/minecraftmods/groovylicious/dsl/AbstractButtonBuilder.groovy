package ga.ozli.minecraftmods.groovylicious.dsl

import groovy.transform.CompileStatic
import net.minecraft.client.gui.components.Button
import net.minecraft.network.chat.MutableComponent

import java.util.function.Supplier

@CompileStatic
abstract class AbstractButtonBuilder extends AbstractWidgetBuilder {
    Button.OnPress onPress = (Button button) -> {}
    Button.CreateNarration createNarration = (Supplier<MutableComponent> supplier) -> supplier.get()
}
