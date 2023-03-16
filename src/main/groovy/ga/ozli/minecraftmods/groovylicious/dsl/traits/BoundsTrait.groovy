package ga.ozli.minecraftmods.groovylicious.dsl.traits

import ga.ozli.minecraftmods.groovylicious.api.gui.Position
import ga.ozli.minecraftmods.groovylicious.api.gui.Size
import groovy.transform.CompileStatic
import groovy.transform.NamedParam
import groovy.transform.NamedParams

@CompileStatic
trait BoundsTrait implements PositionTrait, SizeTrait {
    void bounds(final int x, final int y, final int width, final int height) {
        position x, y
        size width, height
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

    // note: different Map types are used here to circumvent a Groovy compiler bug where it doesn't take
    //       generics into account when resolving @NamedParams method overloads. Last checked: Groovy 4.0.7
    void bounds(@NamedParams([
                    @NamedParam(value = "x", type = Integer, required = true),
                    @NamedParam(value = "y", type = Integer, required = true),
                    @NamedParam(value = "width", type = Integer, required = true),
                    @NamedParam(value = "height", type = Integer, required = true)
                ])
                final LinkedHashMap<String, Integer> args) {
        bounds(args.x, args.y, args.width, args.height)
    }

    void bounds(@NamedParams([
                    @NamedParam(value = "position", type = Position, required = true),
                    @NamedParam(value = "size", type = Size, required = true)
                ])
                final HashMap<String, Object> args) {
        bounds(args.position as Position, args.size as Size)
    }
}
