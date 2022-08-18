package ga.ozli.minecraftmods.groovylicious.transform.config

import groovy.transform.CompileStatic

@CompileStatic
@interface ConfigValue {
    String name() default '</////autodetermined>'

    boolean exclude() default false
}