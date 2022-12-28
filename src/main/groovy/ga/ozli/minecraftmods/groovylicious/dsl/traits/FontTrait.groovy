package ga.ozli.minecraftmods.groovylicious.dsl.traits

import groovy.transform.CompileStatic
import groovy.transform.builder.Builder
import groovy.transform.builder.SimpleStrategy
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.Font

@CompileStatic
trait FontTrait {
    Font font = Minecraft.instance.font
}
