package groovylicioustest

import ga.ozli.minecraftmods.groovylicious.transform.Registroid
import groovy.transform.CompileStatic
import groovy.transform.stc.POJO
import net.minecraft.core.Registry
import net.minecraft.resources.ResourceLocation
import net.minecraft.sounds.SoundEvent
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockBehaviour
import net.minecraft.world.level.material.Material
import net.minecraftforge.registries.ForgeRegistries

@POJO
@CompileStatic
@Registroid({ [Registry.ITEM, ForgeRegistries.BLOCKS, Registry.SOUND_EVENT_REGISTRY] })
class RegistroidTest {
    static final SoundEvent MY_SOUND = new SoundEvent(new ResourceLocation('groovylicioustest', 'my_sound'))
    static final Block MY_BLOCK = new Block(BlockBehaviour.Properties.of(Material.DIRT))
    static final Item MY_BI = new BlockItem(MY_BLOCK, new Item.Properties())
}
