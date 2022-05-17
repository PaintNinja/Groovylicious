package ga.ozli.minecraftmods.groovylicious.transform.typecheckers

import org.codehaus.groovy.ast.MethodNode
import org.codehaus.groovy.ast.expr.PropertyExpression
import org.codehaus.groovy.transform.stc.GroovyTypeCheckingExtensionSupport

class ColourTypeChecker extends GroovyTypeCheckingExtensionSupport.TypeCheckingDSL {
    @Override
    Object run() {
//        beforeVisitMethod { MethodNode method ->
//            println "before visit method"
//            handled = true
//        }
        unresolvedVariable { variable ->
            println "checking unresolved variable"
            if (variable.name == "green") {
                storeType(variable, classNodeFor(Integer))
                handled = true
            }
        }
        unresolvedProperty { PropertyExpression property ->
//            addStaticTypeError("Nope", property)
            println "checking unresolved property"
            println "property: " + property.getPropertyAsString()
            if (property.propertyAsString == "green") {
                storeType(property, classNodeFor(Integer))
                handled = true
            } else {
                addStaticTypeError("Unknown colour type: ${property.propertyAsString}. Please use one of: alpha, red, green, blue, rgb, argb, packed", property)
            }
        }
        finish {
            println "Finished type checking"
        }
    }
}
