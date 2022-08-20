package ga.ozli.minecraftmods.groovylicious.transform.registroid

import groovy.transform.CompileStatic
import org.codehaus.groovy.ast.AnnotationNode
import org.codehaus.groovy.ast.ClassNode
import org.codehaus.groovy.ast.PropertyNode
import org.codehaus.groovy.ast.expr.PropertyExpression

@CompileStatic
interface RegistroidAddon {
    void makeExtra(AnnotationNode registroidAnnotation, ClassNode targetClass, PropertyNode property, RegistroidASTTransformer transformer)

    List<ClassNode> getSupportedTypes()

    List<PropertyExpression> getRequiredRegistries()
}