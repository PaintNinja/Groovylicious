package ga.ozli.minecraftmods.groovylicious.transform.mojo

import groovy.transform.CompileStatic
import groovy.transform.PackageScope
import org.codehaus.groovy.ast.AnnotationNode
import org.codehaus.groovy.ast.ClassNode

@CompileStatic
class GroovyliciousMojoTransformRegistry {
    @PackageScope static final List<Closure> transforms = []

    static void addTransform(final Closure closure) {
        transforms << closure
    }

    static void addAnnotation(final AnnotationNode annotationNode) {
        transforms << { AnnotationNode modAnnotation, ClassNode modClass ->
            modClass.addAnnotation(annotationNode)
        }
    }

    static void addAnnotations(final List<AnnotationNode> annotationNodes) {
        transforms << { AnnotationNode modAnnotation, ClassNode modClass ->
            modClass.addAnnotations(annotationNodes)
        }
    }
}
