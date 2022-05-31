package ga.ozli.minecraftmods.groovylicious.dsl

import ga.ozli.minecraftmods.groovylicious.api.gui.Position
import groovy.transform.CompileStatic
import groovy.transform.stc.ClosureParams
import groovy.transform.stc.SimpleType

import static groovy.lang.Closure.DELEGATE_FIRST

@CompileStatic
trait PositionTrait {
    Position position

    void position(final Position position) {
        this.position = position
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
                  @ClosureParams(value = SimpleType, options = "ga.ozli.minecraftmods.groovylicious.api.gui.Position") final Closure closure) {
        this.position = new Position().tap(closure)
    }

    /**
     * Sets the position of the button.
     * @param x the horizontal axis
     * @param y the vertical axis
     */
    void position(final int x, final int y) {
        this.position = new Position(x, y)
    }

    /** Sets the position of the button with named args.<br>
     * Usage: <pre>
     * position(x: 10, y: 40)
     * </pre>
     * or:
     * <pre>
     * position x: 10,
     *          y: 40
     * </pre>
     * @param args
     */
    void position(final Map args) {
        this.position = new Position(x: args.x as int, y: args.y as int)
    }
}