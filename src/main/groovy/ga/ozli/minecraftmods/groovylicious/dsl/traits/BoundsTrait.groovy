package ga.ozli.minecraftmods.groovylicious.dsl.traits

import ga.ozli.minecraftmods.groovylicious.api.gui.Position
import ga.ozli.minecraftmods.groovylicious.api.gui.Size
import groovy.transform.CompileStatic
import groovy.transform.NamedParam
import groovy.transform.NamedParams

@CompileStatic
trait BoundsTrait implements PositionTrait, SizeTrait {
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

    // todo: map args support
//    void bounds(@NamedParams([
//                    @NamedParam(value = "x", type = Integer),
//                    @NamedParam(value = "y", type = Integer),
//                    @NamedParam(value = "width", type = Integer),
//                    @NamedParam(value = "height", type = Integer),
//                    @NamedParam(value = "position", type = Position),
//                    @NamedParam(value = "size", type = Size)
//                ])
//                final Map<String, Integer> bounds) {
//        if (bounds.containsKey('position')) {
//            if (bounds.containsKey('size')) {
//                bounds(bounds.position, bounds.size)
//            } else if (bounds.containsKey('width') && bounds.containsKey('height')) {
//                bounds(bounds.position, bounds.width, bounds.height)
//            } else {
//                throw new IllegalArgumentException("Bounds arg map must contain either 'size' or ('width' and 'height')")
//            }
//        } else if (bounds.containsKey('size')) {
//            bounds(bounds.x ?: 0, bounds.y ?: 0, bounds.size)
//        } else {
//            throw new IllegalArgumentException("Missing 'position' or 'size' in bounds arg map")
//        }
//    }

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
