package ga.ozli.minecraftmods.groovylicious.extension

import ga.ozli.minecraftmods.groovylicious.api.gui.Position
import ga.ozli.minecraftmods.groovylicious.api.gui.Size
import groovy.transform.CompileStatic
import io.github.groovymc.cgl.api.extension.EnvironmentExtension
import io.github.groovymc.cgl.api.extension.EnvironmentExtension.Side
import net.minecraft.client.gui.layouts.AbstractLayout

@CompileStatic
@EnvironmentExtension(Side.CLIENT)
class InstanceGuiExtension {
    // region AbstractLayout
    static void setPosition(AbstractLayout self, Position position) {
        self.x = position.x
        self.y = position.y
    }

    static Position getPosition(AbstractLayout self) {
        return new Position(self.x, self.y)
    }

    static Size getSize(AbstractLayout self) {
        return new Size(self.width, self.height)
    }
    // endregion
}
