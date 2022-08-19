package groovylicioustest

import ga.ozli.minecraftmods.groovylicious.transform.AutoRegister
import ga.ozli.minecraftmods.groovylicious.transform.defregister.RegistrationName
import groovy.transform.CompileStatic
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Blocks
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.ForgeRegistries

@CompileStatic
class AutoRegisterText {
    @AutoRegister(includeInnerClasses = true)
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, 'groovylicioustest')

    static final BlockItem ITEM1 = new BlockItem(Blocks.ACACIA_WALL_SIGN, new Item.Properties().setNoRepair())

    @RegistrationName('in_group/')
    static final class InnerNrOne {
        static final Item NR_1 = new Item(new Item.Properties())
    }
    static final class InnerNrTwo {
        @RegistrationName('nr_2')
        static final Item NR_2 = new Item(new Item.Properties())
    }
    @RegistrationName(value = 'in_second/', alwaysApply = true)
    static final class InnerNrThree {
        @RegistrationName('nr_3')
        static final Item NR_3 = new Item(new Item.Properties())
    }
}
