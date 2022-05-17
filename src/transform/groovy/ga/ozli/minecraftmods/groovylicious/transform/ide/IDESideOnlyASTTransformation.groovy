package ga.ozli.minecraftmods.groovylicious.transform.ide

import groovy.transform.CompileStatic
import org.codehaus.groovy.ast.ASTNode
import org.codehaus.groovy.ast.AnnotatedNode
import org.codehaus.groovy.ast.ClassNode
import org.codehaus.groovy.ast.InnerClassNode
import org.codehaus.groovy.classgen.GeneratorContext
import org.codehaus.groovy.control.CompilePhase
import org.codehaus.groovy.control.SourceUnit
import org.codehaus.groovy.transform.AbstractASTTransformation
import org.codehaus.groovy.transform.GroovyASTTransformation

import java.lang.reflect.Field

import static org.objectweb.asm.Opcodes.ACC_PRIVATE
import static org.objectweb.asm.Opcodes.ACC_STATIC

@CompileStatic
@GroovyASTTransformation(phase = CompilePhase.SEMANTIC_ANALYSIS)
class IDESideOnlyASTTransformation extends AbstractASTTransformation {

    @Override
    void visit(ASTNode[] nodes, SourceUnit source) {
        init(nodes, source)
//
//        AnnotatedNode targetClass = nodes[1] as AnnotatedNode
//
//        ClassNode targetClassNode = targetClass as ClassNode
//        targetClassNode.modifiers = ACC_PRIVATE
//
//        Field field = targetClassNode.getClass().getDeclaredField('name')
//        field.setAccessible(true)
//        field.set(targetClassNode, "IDE_SIDE_ONLY")

//        targetClassNode.@name = "IDESupport"
    }
}
