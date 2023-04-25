package ga.ozli.minecraftmods.groovylicious.dsl

import ga.ozli.minecraftmods.groovylicious.api.gui.EnhancedLinearLayout
import groovy.transform.CompileDynamic
import groovy.transform.CompileStatic
import net.minecraft.client.gui.layouts.LinearLayout

import static groovy.lang.Closure.DELEGATE_FIRST

@CompileStatic
class LinearLayoutBuilder extends AbstractLayoutBuilder {
    private EnhancedLinearLayout backingLinearLayout = new EnhancedLinearLayout(position.x, position.y, LinearLayout.Orientation.VERTICAL)

    LinearLayout.Orientation orientation = LinearLayout.Orientation.VERTICAL

    LinearLayoutBuilder() {}

    LinearLayoutBuilder(@DelegatesTo(value = LinearLayoutBuilder, strategy = DELEGATE_FIRST) final Closure closure) {
        this.tap(closure)
    }

    void setOrientation(final LinearLayout.Orientation orientation) {
        this.orientation = orientation
        final oldBackingLinearLayout = backingLinearLayout
        backingLinearLayout = copyLinearLayout(oldBackingLinearLayout, new EnhancedLinearLayout(position.x, position.y, size.width, size.height, orientation))
    }

    LinearLayout.Orientation getOrientation() {
        return orientation
    }

    EnhancedLinearLayout build() {
        return copyLinearLayout(backingLinearLayout, new EnhancedLinearLayout(position.x, position.y, size.width, size.height, orientation))
    }

    @CompileDynamic
    private static EnhancedLinearLayout copyLinearLayout(final EnhancedLinearLayout oldLinearLayout, final EnhancedLinearLayout newLinearLayout) {
        oldLinearLayout.children.each { /* AbstractLayout.AbstractChildWrapper */ child ->
            newLinearLayout.addChild(child.child, child.layoutSettings)
        }
        return newLinearLayout
    }
}
