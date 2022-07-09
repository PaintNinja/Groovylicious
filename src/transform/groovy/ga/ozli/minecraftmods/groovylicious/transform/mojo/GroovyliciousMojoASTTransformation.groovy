package ga.ozli.minecraftmods.groovylicious.transform.mojo

import groovy.transform.CompileStatic
import org.codehaus.groovy.ast.ASTNode
import org.codehaus.groovy.ast.AnnotatedNode
import org.codehaus.groovy.ast.AnnotationNode
import org.codehaus.groovy.ast.ClassNode
import org.codehaus.groovy.control.CompilePhase
import org.codehaus.groovy.control.SourceUnit
import org.codehaus.groovy.transform.AbstractASTTransformation
import org.codehaus.groovy.transform.GroovyASTTransformation
import org.codehaus.groovy.transform.TransformWithPriority

@CompileStatic
@GroovyASTTransformation(phase = CompilePhase.CANONICALIZATION)
class GroovyliciousMojoASTTransformation extends AbstractASTTransformation implements TransformWithPriority {

    private static final boolean DEBUG = false

    ClassNode modClass

    @Override
    void visit(ASTNode[] nodes, SourceUnit source) {
        init(nodes, source)

        final AnnotationNode modAnnotation = nodes[0] as AnnotationNode
        final AnnotatedNode targetNode = nodes[1] as AnnotatedNode

        if (targetNode instanceof ClassNode) {
            modClass = targetNode as ClassNode
        } else {
            addError("The ${modAnnotation.classNode.name} annotation can only be applied to classes.", targetNode)
            return
        }

        if (DEBUG) {
            println "Running ${GroovyliciousMojoTransformRegistry.transforms.size()} transforms on class ${modClass.name} annotated with ${modAnnotation.classNode.name}..."
            println SV(GroovyliciousMojoTransformRegistry.transforms)
        }
        GroovyliciousMojoTransformRegistry.transforms.each { it.call(modAnnotation, modClass) }
    }

    @Override
    int priority() {
        return -10 // run after other transforms so that they have time to add to the GroovyliciousMojoTransformRegistry
    }
}
