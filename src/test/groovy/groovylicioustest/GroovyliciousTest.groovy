package groovylicioustest

import com.matyrobbrt.gml.GMod
import com.mojang.logging.LogUtils
import groovy.transform.CompileStatic
import groovy.transform.PackageScope
import groovy.transform.stc.POJO
import org.slf4j.Logger

@POJO
@CompileStatic
@GMod('groovylicioustest')
class GroovyliciousTest {
    @PackageScope static final Logger log = LogUtils.getLogger(this)

    GroovyliciousTest() {}
}