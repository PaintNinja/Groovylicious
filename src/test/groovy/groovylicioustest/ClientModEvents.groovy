package groovylicioustest

import com.matyrobbrt.gml.bus.EventBusSubscriber
import com.matyrobbrt.gml.bus.type.ModBus
import com.matyrobbrt.gml.util.Environment
import groovy.transform.CompileStatic
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Items
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.event.CreativeModeTabEvent
import net.minecraftforge.eventbus.api.SubscribeEvent

@CompileStatic
@EventBusSubscriber(value = ModBus, dist = Dist.CLIENT, environment = Environment.DEV)
class ClientModEvents {

    @SubscribeEvent
    static void registerCreativeTabs(final CreativeModeTabEvent.Register event) {
        // Java
//        event.registerCreativeModeTab(new ResourceLocation(GroovyliciousTest.MOD_ID, "example"), (CreativeModeTab.Builder builder) -> {
//            builder.title(Component.translatable("item_group.${GroovyliciousTest.MOD_ID}.example"))
//                    .icon(() -> Items.DIAMOND.defaultInstance)
//                    .displayItems((params, output) -> output.accept(Items.DIAMOND.defaultInstance))
//        })

        event.registerCreativeTab(new ResourceLocation(GroovyliciousTest.MOD_ID, "example")) {
            title = "item_group.${GroovyliciousTest.MOD_ID}.example"
            icon = Items.DIAMOND
            displayItems Items.DIAMOND, Items.EMERALD
        }
    }

}
