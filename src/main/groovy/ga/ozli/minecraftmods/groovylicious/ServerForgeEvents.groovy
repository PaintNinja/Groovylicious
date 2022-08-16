package ga.ozli.minecraftmods.groovylicious

import com.matyrobbrt.gml.bus.EventBusSubscriber
import com.matyrobbrt.gml.util.Environment
import groovy.transform.CompileStatic
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.event.TickEvent
import net.minecraftforge.eventbus.api.SubscribeEvent

@CompileStatic
@EventBusSubscriber(dist = Dist.DEDICATED_SERVER, environment = Environment.DEV)
class ServerForgeEvents {
    static int every80Ticks = 0

    @SubscribeEvent
    static void onTick(final TickEvent event) {
        every80Ticks++
        if (every80Ticks === 80) {
            println SV(Configs.Common.foxRotation)
            every80Ticks = 0
        }
    }
}
