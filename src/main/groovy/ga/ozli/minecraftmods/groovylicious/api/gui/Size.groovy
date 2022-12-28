package ga.ozli.minecraftmods.groovylicious.api.gui

import groovy.transform.Canonical
import groovy.transform.CompileStatic

@CompileStatic
@Canonical
class Size {
    static final Size DEFAULT = new Size(100, 20)

    int width
    int height
}
