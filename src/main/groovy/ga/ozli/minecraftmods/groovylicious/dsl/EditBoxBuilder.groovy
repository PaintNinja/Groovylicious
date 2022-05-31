package ga.ozli.minecraftmods.groovylicious.dsl

import ga.ozli.minecraftmods.groovylicious.api.gui.ExtensibleScreen
import groovy.transform.CompileStatic
import net.minecraft.client.gui.components.EditBox

@CompileStatic
class EditBoxBuilder {
    boolean bordered = true
    boolean canLoseFocus = true
    boolean isEditable = true

//    EditBox buildEditbox(ExtensibleScreen extensibleScreen) {
//        return new EditBox(extensibleScreen.font, )
//    }
}
