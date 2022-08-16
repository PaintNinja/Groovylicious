package ga.ozli.minecraftmods.groovylicious.transform

import com.matyrobbrt.gml.transform.api.GModTransformer
import org.codehaus.groovy.ast.AnnotationNode
import org.codehaus.groovy.ast.ClassHelper
import org.codehaus.groovy.ast.ClassNode
import org.codehaus.groovy.control.SourceUnit

class GroovyliciousMojoAdder implements GModTransformer {
    private static final AnnotationNode GROOVYLICIOUS_MOJO_ANNOTATION = new AnnotationNode(ClassHelper.make(GroovyliciousMojo))

    @Override
    void transform(ClassNode classNode, AnnotationNode annotationNode, SourceUnit source) {
        println "Adding @Groovylicious Mojo to $classNode"
        classNode.addAnnotation(GROOVYLICIOUS_MOJO_ANNOTATION)
    }
}
