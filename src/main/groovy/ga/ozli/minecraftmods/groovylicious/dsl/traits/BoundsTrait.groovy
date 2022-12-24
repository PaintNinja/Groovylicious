package ga.ozli.minecraftmods.groovylicious.dsl.traits

import ga.ozli.minecraftmods.groovylicious.api.gui.Position
import ga.ozli.minecraftmods.groovylicious.api.gui.Size
import groovy.transform.CompileStatic
import groovy.transform.NamedVariant

@CompileStatic
trait BoundsTrait implements PositionTrait, SizeTrait {
    @NamedVariant
    void bounds(final int x, final int y, final int width, final int height) {
        position(x, y)
        size(width, height)
    }

    void bounds(final Position position, final Size size) {
        bounds(position.x, position.y, size.width, size.height)
    }

    void bounds(final Position position, final int width, final int height) {
        bounds(position.x, position.y, width, height)
    }

    void bounds(final int x, final int y, final Size size) {
        bounds(x, y, size.width, size.height)
    }

    void setBounds(final int x, final int y, final int width, final int height) {
        bounds(x, y, width, height)
    }

    void setBounds(final Position position, final Size size) {
        bounds(position.x, position.y, size.width, size.height)
    }

    void setBounds(final Position position, final int width, final int height) {
        bounds(position.x, position.y, width, height)
    }

    void setBounds(final int x, final int y, final Size size) {
        bounds(x, y, size.width, size.height)
    }
}
