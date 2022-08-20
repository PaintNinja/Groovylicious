package ga.ozli.minecraftmods.groovylicious.transform

import groovy.transform.CompileStatic
import org.codehaus.groovy.transform.GroovyASTTransformationClass

import java.lang.annotation.Documented
import java.lang.annotation.ElementType
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import java.lang.annotation.Target

/**
 * An annotation which transforms a class with static registrable object properties into a {@linkplain net.minecraftforge.registries.DeferredRegister} + {@linkplain net.minecraftforge.registries.RegistryObject} + getters class. <br>
 * This annotation can be used in either field or class mode. <br><br>
 * <h2>Field mode</h2>
 * The annotation is applied on a {@linkplain net.minecraftforge.registries.DeferredRegister DeferredRegister} field. The transformer will scan all properties of the DeferredRegister's type in the field's class, and convert them to registry objects,
 * with getters calling {@linkplain net.minecraftforge.registries.RegistryObject#get()}.
 *
 * <h2>Class mode</h2>
 * In class mode, DR fields can be automatically created, by specifying the registries to create them for, in the {@linkplain Registroid#value() value} property.
 * The created DRs will behave like they do in field mode.<br>
 * Moreover, {@linkplain ga.ozli.minecraftmods.groovylicious.transform.registroid.RegistroidAddon addons} can be added to the DRs in the class by annotation the class with the respective {@linkplain ga.ozli.minecraftmods.groovylicious.transform.registroid.RegistroidAddonClass addon annotations}.
 *
 * The way registry name are determined can be found in the {@linkplain ga.ozli.minecraftmods.groovylicious.transform.registroid.RegistrationName RegistrationName} documentation.
 */
@Documented
@CompileStatic
@Retention(RetentionPolicy.SOURCE)
@Target([ElementType.FIELD, ElementType.TYPE])
@GroovyASTTransformationClass(value = 'ga.ozli.minecraftmods.groovylicious.transform.registroid.RegistroidASTTransformer')
@interface Registroid {
    /**
     * If objects from inner classes should be transformed as well. <br>
     * Defaults to false.
     */
    boolean includeInnerClasses() default false

    /**
     * If the bus(es) should be automatically registered to your {@linkplain com.matyrobbrt.gml.BaseGMod#getModBus() mod event bus} automatically. <br>
     * Defaults to true.
     */
    boolean registerToBus() default true

    /**
     * Only used by class mode! <br>
     * In this property you specify the registries you want DRs to be automatically created, as a closure. <br>
     * The closure <b>must</b> constantly return one of the following types:
     * <ul>
     *     <li>{@linkplain net.minecraft.core.Registry Registry}</li>
     *     <li>{@linkplain net.minecraft.resources.ResourceKey ResourceKey}</li>
     *     <li>{@linkplain net.minecraftforge.registries.IForgeRegistry ForgeRegistry}</li>
     *     <li>or a list (using the {@code [val1, val2] format}) of the above (can be empty, or having multiple accepted types combined)</li>
     * </ul>
     * This value is optional, and when not specified, it will behave like an empty list.
     */
    Class<? extends Closure<?>> value() default {}
}
