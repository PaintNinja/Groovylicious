package ga.ozli.minecraftmods.groovylicious.transform.registroid.blockitem

import ga.ozli.minecraftmods.groovylicious.transform.registroid.RegistroidAddonClass
import net.minecraft.world.item.Item

@RegistroidAddonClass(BlockItemAddonTransformer)
@interface BlockItemAddon {
    Class<? extends Closure<Item.Properties>> propertyFactory() default { null }
    boolean exclude() default false
}