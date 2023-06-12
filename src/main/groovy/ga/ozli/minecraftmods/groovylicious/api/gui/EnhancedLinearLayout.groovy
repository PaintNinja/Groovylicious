package ga.ozli.minecraftmods.groovylicious.api.gui


import groovy.transform.CompileStatic
import net.minecraft.client.gui.layouts.LayoutElement
import net.minecraft.client.gui.layouts.LinearLayout

@CompileStatic
class EnhancedLinearLayout extends LinearLayout/* implements WidgetContainer*/ {
    EnhancedLinearLayout(final int x, final int y, final Orientation orientation) {
        super(x, y, orientation)
    }

    EnhancedLinearLayout(final int x, final int y, final int width, final int height, final Orientation orientation) {
        super(x, y, width, height, orientation)
    }

    Orientation getOrientation() {
        return this.orientation
    }

    List<?> getChildren() {
        return this.children
    }

    <T extends LayoutElement> T addWidgetPrettyPlease(T widget) {
        return this.addChild(widget)
    }
}
