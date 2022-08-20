package groovylicioustest

import ga.ozli.minecraftmods.groovylicious.transform.Registroid
import ga.ozli.minecraftmods.groovylicious.transform.registroid.RegistrationName
import ga.ozli.minecraftmods.groovylicious.transform.registroid.blockitem.BlockItemAddon
import groovy.transform.CompileStatic
import groovy.transform.stc.POJO
import net.minecraft.network.chat.Component
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Rarity
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockBehaviour
import net.minecraft.world.level.material.Material
import net.minecraftforge.registries.ForgeRegistries
import org.jetbrains.annotations.Nullable

@POJO
@CompileStatic
@BlockItemAddon({ Block it ->
    new BlockItem(it, new Item.Properties()
            .setNoRepair()
            .defaultDurability(12)) {
        @Override
        void appendHoverText(ItemStack pStack, @Nullable @javax.annotation.Nullable Level pLevel, List<Component> pTooltip, TooltipFlag pFlag) {
            super.appendHoverText(pStack, pLevel, pTooltip, pFlag)
            pTooltip.add(Component.empty())
        }
    }
})
@Registroid({ ForgeRegistries.BLOCKS })
class RegistroidTest {
    static final Block SHOULD_HAVE_BI = new Block(BlockBehaviour.Properties.of(Material.DIRT))

    @BlockItemAddon(exclude = true)
    static final Block EXPLICIT_EXCLUDE = new Block(Block.Properties.of(Material.AMETHYST))

    static final Block WITH_EXPLICIT_BI = new Block(Block.Properties.of(Material.ICE_SOLID))
    @RegistrationName('with_explicit_bi')
    static final Item EXPLICIT_BI = new BlockItem(WITH_EXPLICIT_BI, new Item.Properties())

    @BlockItemAddon({ new Item.Properties().tab(CreativeModeTab.TAB_INVENTORY).rarity(Rarity.COMMON) })
    static final Block CUSTOM_BI = new Block(BlockBehaviour.Properties.of(Material.BAMBOO))
}