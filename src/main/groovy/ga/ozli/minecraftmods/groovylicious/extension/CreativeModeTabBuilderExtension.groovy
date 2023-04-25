package ga.ozli.minecraftmods.groovylicious.extension

import ga.ozli.minecraftmods.groovylicious.api.GeneralUtils
import groovy.transform.CompileStatic
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.CreativeModeTab.ItemDisplayParameters
import net.minecraft.world.item.CreativeModeTab.Output
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.ItemLike

@CompileStatic
@Category(CreativeModeTab.Builder)
class CreativeModeTabBuilderExtension {
    CreativeModeTab.Builder title(final String title) {
        return this.title(GeneralUtils.stringToComponent(title))
    }

    CreativeModeTab.Builder icon(final ItemStack icon) {
        return this.icon(() -> icon)
    }

    CreativeModeTab.Builder icon(final Item icon) {
        return this.icon(() -> icon as ItemStack)
    }

    /**
     * @param displayItems A list of ItemStacks or ItemLike objects
     */
    CreativeModeTab.Builder displayItems(final List displayItems) {
        return this.displayItems((ItemDisplayParameters params, Output output) -> {
            displayItems.each(output.&accept)
        })
    }

    CreativeModeTab.Builder displayItems(final ItemLike... displayItems) {
        return this.displayItems((ItemDisplayParameters params, Output output) -> {
            displayItems.each(output.&accept)
        })
    }

    CreativeModeTab.Builder displayItems(final ItemStack... displayItems) {
        return this.displayItems((ItemDisplayParameters params, Output output) -> {
            displayItems.each(output.&accept)
        })
    }
}
