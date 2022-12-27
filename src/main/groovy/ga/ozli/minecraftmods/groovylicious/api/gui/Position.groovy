package ga.ozli.minecraftmods.groovylicious.api.gui

import groovy.transform.Canonical
import groovy.transform.CompileStatic

@CompileStatic
@Canonical
class Position {
    static final Position DEFAULT = new Position(0, 0)

    int x
    int y
}
