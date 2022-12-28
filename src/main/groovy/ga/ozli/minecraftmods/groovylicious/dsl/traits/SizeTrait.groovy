package ga.ozli.minecraftmods.groovylicious.dsl.traits

import ga.ozli.minecraftmods.groovylicious.api.gui.Position
import ga.ozli.minecraftmods.groovylicious.api.gui.Size
import groovy.transform.CompileStatic
import groovy.transform.NamedParam
import groovy.transform.NamedParams
import groovy.transform.NamedVariant
import groovy.transform.builder.Builder
import groovy.transform.builder.SimpleStrategy
import groovy.transform.stc.ClosureParams
import groovy.transform.stc.FirstParam
import groovy.transform.stc.SimpleType

import static groovy.lang.Closure.DELEGATE_FIRST

@CompileStatic
trait SizeTrait {
    private Size size = Size.DEFAULT

    void setSize(final Size size) {
        this.@size = size
    }

    /**
     * Sets the size of the widget.<br>
     * Usage: <pre>
     * size(200, 40)
     * </pre>
     * @param width the horizontal width
     * @param height the vertical height
     */
    void size(final int width, final int height) {
        this.size = new Size(width, height)
    }

    /**
     * Sets the size of the widget in closure-style.<br>
     * Usage: <pre>
     * size {
     *     width = 200
     *     height = 40
     * }
     * </pre>
     * @param closure
     */
    void size(@DelegatesTo(value = Size, strategy = DELEGATE_FIRST)
              @ClosureParams(value = SimpleType, options = 'ga.ozli.minecraftmods.groovylicious.api.gui.Size') final Closure closure) {
        this.size = new Size().tap(closure)
    }

    /**
     * Sets the size of the widget with named parameters.<br>
     * Usage: <pre>
     * size(width: 200, height: 40)
     * </pre>
     * or:
     * <pre>
     * size width: 200,
     *      height: 40
     * </pre>
     * @return
     */
    void size(@NamedParams([@NamedParam(value = 'width', type = Integer), @NamedParam(value = 'height', type = Integer)])
                  final Map<String, Integer> params) {
        this.size = new Size(
                params?.width?.asType(Integer) ?: Size.DEFAULT.width,
                params?.height?.asType(Integer) ?: Size.DEFAULT.height
        )
    }

    Size getSize() {
        return this.size
    }
}