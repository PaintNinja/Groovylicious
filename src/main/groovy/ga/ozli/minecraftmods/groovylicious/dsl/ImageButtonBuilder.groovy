package ga.ozli.minecraftmods.groovylicious.dsl

import ga.ozli.minecraftmods.groovylicious.api.gui.ExtensibleScreen
import ga.ozli.minecraftmods.groovylicious.api.gui.Position
import ga.ozli.minecraftmods.groovylicious.api.gui.Size
import groovy.contracts.Requires
import groovy.transform.CompileStatic
import groovy.transform.stc.ClosureParams
import groovy.transform.stc.SimpleType
import net.minecraft.client.gui.components.Button
import net.minecraft.client.gui.components.ImageButton
import net.minecraft.resources.ResourceLocation
import org.apache.groovy.lang.annotation.Incubating

import static groovy.lang.Closure.DELEGATE_FIRST

@Incubating
@CompileStatic
class ImageButtonBuilder extends ButtonBuilder {
    ResourceLocation resourceLocation
    Position textureStartPosition
    Size textureSize
    int yDiffTex // https://discord.com/channels/313125603924639766/915304642668290119/981300196749230110

    void resourceLocation(ResourceLocation resourceLocation) {
        this.resourceLocation = resourceLocation
    }

    void textureStartPosition(Position textureStartPosition) {
        this.textureStartPosition = textureStartPosition
    }

    void textureStartPosition(@DelegatesTo(value = Position, strategy = DELEGATE_FIRST)
                  @ClosureParams(value = SimpleType, options = "ga.ozli.minecraftmods.groovylicious.api.gui.Position") final Closure closure) {
        this.position = new Position().tap(closure)
    }

    void textureStartPosition(final int x, final int y) {
        this.position = new Position(x, y)
    }

    void textureStartPosition(final Map args) {
        this.position = new Position(x: args.x as int, y: args.y as int)
    }

    void textureSize(final Size size) {
        this.size = size
    }

    void textureSize(@DelegatesTo(value = Size, strategy = DELEGATE_FIRST)
              @ClosureParams(value = SimpleType, options = "ga.ozli.minecraftmods.groovylicious.api.gui.Size") final Closure closure) {
        this.size = new Size().tap(closure)
    }

    void textureSize(final int width, final int height) {
        this.size = new Size(width, height)
    }

    void textureSize(final Map args) {
        this.size = new Size(width: args.width as int, height: args.height as int)
    }

    // todo: support for pMessage param/this.text
    @Requires({ this.position && this.size && this.textureStartPosition && this.textureSize && this.resourceLocation })
    ImageButton buildImageButton() {
        return new ImageButton(this.position.x, this.position.y, this.size.width, this.size.height,
                this.textureStartPosition.x, this.textureStartPosition.y, this.yDiffTex, this.resourceLocation,
                this.textureSize.width, this.textureSize.height, this.onPress)
    }

    @Requires({ this.position && this.size && this.resourceLocation })
    @Override
    Button buildButton() {
        return buildImageButton()
    }

    @Requires({ this.position && this.size && this.resourceLocation })
    @Override
    Closure buildClosure() {
        return { ExtensibleScreen screenInstance ->
            screenInstance.addRenderableWidget(this.buildImageButton())
        }
    }
}
