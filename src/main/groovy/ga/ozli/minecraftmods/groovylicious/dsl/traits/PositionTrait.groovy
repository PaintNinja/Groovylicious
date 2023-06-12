package ga.ozli.minecraftmods.groovylicious.dsl.traits

import ga.ozli.minecraftmods.groovylicious.api.gui.Position
import groovy.transform.CompileStatic
import groovy.transform.NamedParam
import groovy.transform.NamedParams
import groovy.transform.stc.ClosureParams
import groovy.transform.stc.SimpleType

import static groovy.lang.Closure.DELEGATE_FIRST

@CompileStatic
trait PositionTrait {
    private Position position = Position.DEFAULT

    void setPosition(final Position position) {
        this.@position = position
    }

    /**
     * Sets the position of the widget.<br>
     * Usage: <pre>
     * position(10, 40)
     * </pre>
     * @param x the horizontal axis
     * @param y the vertical axis
     */
    void position(final int x, final int y) {
        this.@position = new Position(x, y)
    }

    /**
     * Sets the position of the widget in closure-style.<br>
     * Usage: <pre>
     * position {
     *     x = 10
     *     y = 40
     * }
     * </pre>
     * @param closure
     */
    void position(@DelegatesTo(value = Position, strategy = DELEGATE_FIRST)
                  @ClosureParams(value = SimpleType, options = 'ga.ozli.minecraftmods.groovylicious.api.gui.Position') final Closure closure) {
        this.@position = new Position().tap(closure)
    }

    /**
     * Sets the position of the widget with named parameters.<br>
     * Usage: <pre>
     * position(x: 10, y: 40)
     * </pre>
     * or:
     * <pre>
     * position x: 10,
     *          y: 40
     * </pre>
     * @return
     */
    void position(@NamedParams([@NamedParam(value = 'x', type = Integer), @NamedParam(value = 'y', type = Integer)])
                  final Map<String, Integer> params) {
        this.@position = new Position(
                params?.x?.asType(Integer) ?: Position.DEFAULT.x,
                params?.y?.asType(Integer) ?: Position.DEFAULT.y
        )
    }

    Position getPosition() {
        return this.@position
    }

    void setX(final int x) {
        this.@position.x = x
    }

    int getX() {
        return this.@position.x
    }

    void setY(final int y) {
        this.@position.y = y
    }

    int getY() {
        return this.@position.y
    }
}
