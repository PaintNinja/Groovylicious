package ga.ozli.minecraftmods.groovylicious.transform.registroid

import groovy.transform.CompileStatic
import org.codehaus.groovy.ast.AnnotationNode
import org.codehaus.groovy.ast.ClassNode
import org.codehaus.groovy.ast.PropertyNode
import org.codehaus.groovy.ast.expr.PropertyExpression

/**
 * An addon for {@linkplain ga.ozli.minecraftmods.groovylicious.transform.Registroid Registroid} systems. <br>
 * The addons are specified on classes annotated with {@linkplain ga.ozli.minecraftmods.groovylicious.transform.Registroid Registroid},
 * by annotating the class with different {@linkplain RegistroidAddonClass addon annotations}.
 *
 * Addons provided by Groovylicious:
 * <ul>
 *     <li>{@linkplain ga.ozli.minecraftmods.groovylicious.transform.registroid.blockitem.BlockItemAddon BlockItem addon}</li>
 * </ul>
 */
@CompileStatic
interface RegistroidAddon {
    void makeExtra(AnnotationNode registroidAnnotation, ClassNode targetClass, PropertyNode property, RegistroidASTTransformer transformer)

    List<ClassNode> getSupportedTypes()

    List<PropertyExpression> getRequiredRegistries()
}