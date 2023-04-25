package ga.ozli.minecraftmods.groovylicious.extension

import ga.ozli.minecraftmods.groovylicious.dsl.CreativeTabBuilder
import groovy.transform.CompileStatic
import groovy.transform.stc.ClosureParams
import groovy.transform.stc.SimpleType
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.CreativeModeTab
import net.minecraftforge.event.CreativeModeTabEvent

@CompileStatic
@Category(CreativeModeTabEvent.Register)
class CreativeModeTabEventRegisterExtension {
    CreativeModeTab registerCreativeTab(ResourceLocation name, @DelegatesTo(value = CreativeTabBuilder, strategy = Closure.DELEGATE_FIRST)
                                                               @ClosureParams(value = SimpleType, options = 'ga.ozli.minecraftmods.groovylicious.dsl.CreativeTabBuilder') final Closure closure) {
        return this.registerCreativeModeTab(name, (CreativeModeTab.Builder builder) -> new CreativeTabBuilder(closure).builder)
    }
}
