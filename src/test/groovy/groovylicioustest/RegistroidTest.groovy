package groovylicioustest

import ga.ozli.minecraftmods.groovylicious.transform.Registroid
import ga.ozli.minecraftmods.groovylicious.transform.registroid.RegistrationName
import ga.ozli.minecraftmods.groovylicious.transform.registroid.blockitem.BlockItemAddon
import groovy.transform.CompileStatic
import groovy.transform.stc.POJO
import net.minecraft.core.Registry
import net.minecraft.resources.ResourceLocation
import net.minecraft.sounds.SoundEvent
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockBehaviour
import net.minecraft.world.level.material.Material
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.ForgeRegistries

@POJO
@CompileStatic
@Registroid({ [ForgeRegistries.BLOCKS, Registry.SOUND_EVENT_REGISTRY] })
class RegistroidTest {
    static final Block SOME_TEST = new Block(BlockBehaviour.Properties.of(Material.DIRT))
    static final SoundEvent TEST_SOUND = new SoundEvent(new ResourceLocation('groovylicioustest', 'test_sound'))

    @Registroid
    static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Registry.ITEM_REGISTRY, 'groovylicioustest')
    static final Item SOME_ITEM = new Item(new Item.Properties())

    @Registroid({ Registry.BLOCK_REGISTRY })
    @BlockItemAddon({ new Item.Properties().setNoRepair() })
    static final class InnerClass {
        @RegistrationName('hello_world')
        static final Block A_BLOCK_WITH_BLOCK_ITEM = new Block(Block.Properties.of(Material.BUBBLE_COLUMN))
    }
}