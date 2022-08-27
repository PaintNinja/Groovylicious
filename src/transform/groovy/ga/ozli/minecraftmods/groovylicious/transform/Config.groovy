package ga.ozli.minecraftmods.groovylicious.transform

import groovy.transform.CompileStatic
import net.minecraftforge.fml.config.ModConfig
import org.codehaus.groovy.transform.GroovyASTTransformationClass

import java.lang.annotation.ElementType
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import java.lang.annotation.Target

@CompileStatic
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
@GroovyASTTransformationClass('ga.ozli.minecraftmods.groovylicious.transform.config.ConfigASTTransformation')
@interface Config {
    /**
     * The desired config type. One of:
     * <ul>
     *     <li><strong>Common</strong>: For configuration that needs to be loaded on both environments - both clients and servers.</li>
     *     <li><strong>Client</strong>: For configuration affecting <i>only</i> client state such as graphical options. Only loaded on clients.</li>
     *     <li><strong>Server</strong>: A special type of configuration that is associated with a server instance or singleplayer world.
     *         Only loaded on servers. Synced on player join and stored in a server or world-specific "serverconfig" folder".</li>
     * </ul>
     * <p>By default, Groovylicious will infer the appropriate config type based on your config's classname (e.g. a config class
     * called "Client" would automatically be a Client config type), falling back to Common unless explicitly defined otherwise.</p>
     */
    ModConfig.Type value() default ModConfig.Type.COMMON

    String modId() default '(autoDetected)'
}
