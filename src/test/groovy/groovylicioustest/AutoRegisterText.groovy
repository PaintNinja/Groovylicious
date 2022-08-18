package groovylicioustest

import ga.ozli.minecraftmods.groovylicious.transform.AutoRegister
import groovy.transform.CompileStatic
import net.minecraft.world.item.Item
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.ForgeRegistries

@CompileStatic
class AutoRegisterText {
    @AutoRegister
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, 'groovylicioustest')

    static final Item ITEM1 = new Item(new Item.Properties().setNoRepair())
}
