package ga.ozli.minecraftmods.groovylicious.extension

import com.mojang.logging.LogUtils
import groovy.transform.CompileStatic
import groovy.transform.stc.POJO
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@POJO
@CompileStatic
class LogUtilsExtension {
    /**
     * @return An SLF4J logger for the given class
     * @author Paint_Ninja via Groovylicious
     */
    static Logger getLogger(LogUtils self, Class <?> clazz) {
        return LoggerFactory.getLogger(clazz)
    }

    /**
     * @return An SLF4J logger with the given name
     * @author Paint_Ninja via Groovylicious
     */
    static Logger getLogger(LogUtils self, String name) {
        return LoggerFactory.getLogger(name)
    }
}
