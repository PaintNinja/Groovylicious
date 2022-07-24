package ga.ozli.minecraftmods.groovylicious.extension

import groovy.transform.CompileStatic
import groovy.transform.stc.POJO
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@POJO
@CompileStatic
class LogUtilsExtension {
    static Logger getLogger(final Class<?> clazz) {
        return LoggerFactory.getLogger(clazz)
    }

    static Logger getLogger(final String name) {
        return LoggerFactory.getLogger(name)
    }
}
