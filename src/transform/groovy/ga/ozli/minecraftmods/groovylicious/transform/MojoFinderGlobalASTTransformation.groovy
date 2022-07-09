package ga.ozli.minecraftmods.groovylicious.transform

import groovy.transform.CompileStatic
import net.thesilkminer.mc.austin.api.Mod
import net.thesilkminer.mc.austin.api.Mojo
import org.codehaus.groovy.ast.ASTNode
import org.codehaus.groovy.ast.AnnotationNode
import org.codehaus.groovy.ast.ClassHelper
import org.codehaus.groovy.ast.ClassNode
import org.codehaus.groovy.control.CompilePhase
import org.codehaus.groovy.control.SourceUnit
import org.codehaus.groovy.transform.ASTTransformation
import org.codehaus.groovy.transform.GroovyASTTransformation

/**
 * Finds classes annotated with @Mojo/@Mod and adds @GroovyliciousMojo to them.<br>
 * <br>
 * Note: You do not need to copy this global transform if you want to add your own implied transforms to the Mojo class!
 * GroovyliciousMojo provides an API to do so without needing global transforms through the methods in MojoTransformRegistry.
 * @see ga.ozli.minecraftmods.groovylicious.transform.mojo.GroovyliciousMojoTransformRegistry
 */
@CompileStatic
@GroovyASTTransformation(phase = CompilePhase.SEMANTIC_ANALYSIS)
class MojoFinderGlobalASTTransformation implements ASTTransformation {

    private static final AnnotationNode MOD_ANNOTATION = new AnnotationNode(ClassHelper.make(Mod))
    private static final AnnotationNode MOJO_ANNOTATION = new AnnotationNode(ClassHelper.make(Mojo))
    private static final AnnotationNode GROOVYLICIOUS_MOJO_ANNOTATION = new AnnotationNode(ClassHelper.make(Mojo))

    private static final boolean DEBUG = false

    @Override
    void visit(ASTNode[] nodes, SourceUnit source) {
        final List<ClassNode> classes = source.AST.classes
        for (final classNode in classes) {
            final List<AnnotationNode> annotations = classNode.annotations
            if (annotations.isEmpty()) continue

            final boolean hasMojo = annotations.find {
                it == MOD_ANNOTATION || it == MOJO_ANNOTATION
            }

            if (hasMojo) {
                if (DEBUG) println "Found Mojo: ${classNode.name}"
                classNode.addAnnotation(GROOVYLICIOUS_MOJO_ANNOTATION)
            }
        }
    }
}
