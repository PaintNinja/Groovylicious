package ga.ozli.minecraftmods.groovylicious.transform.config

import groovy.transform.CompileStatic

@CompileStatic
@interface ConfigGroup {
    String name() default '</////autodetermined>'

    boolean excludeFieldsWithoutAnnotation() default false
    boolean excludeGroupsWithoutAnnotation() default false
    boolean exclude() default false
}