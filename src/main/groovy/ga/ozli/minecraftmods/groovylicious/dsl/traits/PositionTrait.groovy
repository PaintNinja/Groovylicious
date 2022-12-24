package ga.ozli.minecraftmods.groovylicious.dsl.traits

import ga.ozli.minecraftmods.groovylicious.api.gui.Position
import groovy.transform.CompileStatic
import groovy.transform.stc.ClosureParams
import groovy.transform.stc.FirstParam
import groovy.transform.stc.SimpleType

import static groovy.lang.Closure.DELEGATE_FIRST

@CompileStatic
trait PositionTrait {
    Position position

    void position(final Position position) {
        this.position = position
    }

    /**
     * Sets the position of the button.<br>
     * Usage: <pre>
     * position(10, 40)
     * </pre>
     * or:
     * <pre>
     * position(x: 10, y: 40)
     * </pre>
     * or:
     * <pre>
     * position x: 10,
     *          y: 40
     * </pre>
     * @param x the horizontal axis
     * @param y the vertical axis
     */
    void position(final int x, final int y) {
        this.position = new Position(x, y)
    }

    /**
     * Sets the position of the button in closure-style.<br>
     * Usage: <pre>
     * position {
     *     x = 10
     *     y = 40
     * }
     * </pre>
     * @param closure
     */
    void position(@DelegatesTo(value = Position, strategy = DELEGATE_FIRST)
                  @ClosureParams(value = FirstParam.FirstGenericType) final Closure<Position> closure) {
        this.position = new Position().tap(closure)
    }
}