package ga.ozli.minecraftmods.groovylicious.dsl.traits

import groovy.transform.CompileStatic
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.Font

@CompileStatic
trait FontTrait {
    Font font = Minecraft.instance.font

    void font(final Font font) {
        this.font = font
    }
}