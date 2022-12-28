package ga.ozli.minecraftmods.groovylicious.dsl

import groovy.transform.CompileStatic
import groovy.transform.builder.Builder
import groovy.transform.builder.SimpleStrategy

// todo: scrollbarVisible and scrollRate?
@CompileStatic
abstract class AbstractScrollWidgetBuilder extends AbstractWidgetBuilder {
    double scrollAmount = 0.0d
}
