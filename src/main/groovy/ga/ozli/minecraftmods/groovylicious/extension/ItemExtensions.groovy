package ga.ozli.minecraftmods.groovylicious.extension

import groovy.transform.CompileStatic
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.ItemLike
import org.codehaus.groovy.runtime.DefaultGroovyMethods

@CompileStatic
class ItemExtensions {
    static <T> T asType(ItemLike self, Class<T> type) {
        return switch (type) {
            case ItemStack -> (T) self.asItem().defaultInstance
            default -> (T) DefaultGroovyMethods.asType(self, type)
        }
    }

    static <T> T asType(Item self, Class<T> type) {
        return switch (type) {
            case ItemStack -> (T) self.defaultInstance
            default -> (T) DefaultGroovyMethods.asType(self, type)
        }
    }
}
