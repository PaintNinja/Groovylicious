package ga.ozli.minecraftmods.groovylicious.api.gui

import groovy.transform.Canonical
import groovy.transform.CompileStatic
import net.minecraft.client.gui.components.Button

@CompileStatic
@Canonical
class Size {
    static final Size DEFAULT = new Size(Button.DEFAULT_WIDTH, Button.DEFAULT_HEIGHT)

    int width
    int height
}
