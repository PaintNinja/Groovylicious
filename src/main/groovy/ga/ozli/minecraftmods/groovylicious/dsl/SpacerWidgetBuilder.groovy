package ga.ozli.minecraftmods.groovylicious.dsl

import groovy.transform.CompileStatic
import net.minecraft.client.gui.components.SpacerWidget
import org.apache.groovy.lang.annotation.Incubating

import static groovy.lang.Closure.DELEGATE_FIRST

@Incubating // todo: check if this has any benefit over SpacerWidget.height() or its constructors. Does changing visible do anything?
@CompileStatic
class SpacerWidgetBuilder extends AbstractWidgetBuilder {
    SpacerWidgetBuilder() {}

    SpacerWidgetBuilder(@DelegatesTo(value = SpacerWidgetBuilder, strategy = DELEGATE_FIRST) final Closure closure) {
        this.tap(closure)
    }

    SpacerWidget build() {
        final spacer = new SpacerWidget(position.x, position.y, size.width, size.height)
        spacer.active = active
        spacer.visible = visible

        return spacer
    }
}
