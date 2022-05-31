package ga.ozli.minecraftmods.groovylicious.dsl

import ga.ozli.minecraftmods.groovylicious.api.gui.Size
import groovy.transform.CompileStatic
import groovy.transform.stc.ClosureParams
import groovy.transform.stc.SimpleType

import static groovy.lang.Closure.DELEGATE_FIRST

@CompileStatic
trait SizeTrait {
    Size size

    void size(final Size size) {
        this.size = size
    }

    /**
     * Sets the size of the button in closure-style.<br>
     * Usage: <pre>
     * size {
     *     width = 200
     *     height = 40
     * }
     * </pre>
     * @param closure
     */
    void size(@DelegatesTo(value = Size, strategy = DELEGATE_FIRST)
              @ClosureParams(value = SimpleType, options = "ga.ozli.minecraftmods.groovylicious.api.gui.Size") final Closure closure) {
        this.size = new Size().tap(closure)
    }

    /**
     * Sets the size of the button.
     * @param width the horizontal width
     * @param height the vertical height
     */
    void size(final int width, final int height) {
        this.size = new Size(width, height)
    }

    /** Sets the size of the button with named args.<br>
     * Usage: <pre>
     * size(width: 200, height: 40)
     * </pre>
     * or:
     * <pre>
     * size width: 200,
     *      height: 40
     * </pre>
     * @param args
     */
    void size(final Map args) {
        this.size = new Size(width: args.width as int, height: args.height as int)
    }
}