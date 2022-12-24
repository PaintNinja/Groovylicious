package ga.ozli.minecraftmods.groovylicious.dsl.traits

import ga.ozli.minecraftmods.groovylicious.api.gui.Size
import groovy.transform.CompileStatic
import groovy.transform.NamedVariant
import groovy.transform.stc.ClosureParams
import groovy.transform.stc.FirstParam
import groovy.transform.stc.SimpleType

import static groovy.lang.Closure.DELEGATE_FIRST

@CompileStatic
trait SizeTrait {
    Size size

    void size(final Size size) {
        this.size = size
    }

    /**
     * Sets the size of the button.<br>
     * Usage: <pre>
     * size(200, 40)
     * </pre>
     * or:
     * <pre>
     * size(width: 200, height: 40)
     * </pre>
     * or:
     * <pre>
     * size width: 200,
     *      height: 40
     * </pre>
     * @param width the horizontal width
     * @param height the vertical height
     */
    @NamedVariant
    void size(final int width, final int height) {
        this.size = new Size(width, height)
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
              @ClosureParams(value = FirstParam.FirstGenericType) final Closure<Size> closure) {
        this.size = new Size().tap(closure)
    }
}