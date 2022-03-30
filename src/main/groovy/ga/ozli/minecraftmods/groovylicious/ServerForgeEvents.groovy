package ga.ozli.minecraftmods.groovylicious

import groovy.transform.CompileStatic
import groovy.util.logging.Log4j2
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.event.TickEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod
import net.thesilkminer.mc.austin.api.EventBus
import net.thesilkminer.mc.austin.api.EventBusSubscriber

import static net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus

@CompileStatic
@EventBusSubscriber(modId = Groovylicious.MOD_ID, bus = EventBus.FORGE, dist = Dist.DEDICATED_SERVER)
class ServerForgeEvents {
    static int every80Ticks = 0

    @SubscribeEvent
    static void onTick(final TickEvent event) {
        every80Ticks++
        if (every80Ticks === 80) {
            every80Ticks = 0
        }
    }
}
