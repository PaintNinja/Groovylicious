package ga.ozli.minecraftmods.groovylicious.transform

import com.matyrobbrt.gml.GMod
import groovy.transform.CompileStatic
import org.codehaus.groovy.ast.ClassHelper
import org.codehaus.groovy.ast.ClassNode

@CompileStatic
class TransformTypes {
    static final ClassNode MOD_TYPE = ClassHelper.make(GMod)
    static final ClassNode EXCLUDE_TYPE = ClassHelper.make(Exclude)
    static final ClassNode LIST_TYPE = ClassHelper.make(List)
}
