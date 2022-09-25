package ga.ozli.minecraftmods.groovylicious.api.gui

import groovy.transform.CompileStatic
import groovy.transform.EqualsAndHashCode
import groovy.transform.Memoized
import groovy.transform.Pure
import groovy.transform.ToString
import groovy.transform.stc.POJO
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.TextColor
import net.minecraft.util.FastColor

/**
 * Represents a colour in Minecraft GUIs
 *
 * Usage:
 * <pre>
 * {@code
 * Colour orange = Colour.of(255, 165, 0)
 * println orange // prints Colour(packed: -23296, argb: [255, 255, 165, 0])
 *
 * assert orange.packed == orange.get() == orange as int == -23296
 * assert orange.alpha == orange.getAlpha() == orange.a == 255
 * assert orange.red == orange.getRed() == orange.r == 255
 * assert orange.green == orange.getGreen() == orange.g == 165
 * assert orange.blue == orange.getBlue() == orange.b == 0
 * assert orange.argb == orange.getARGB() == orange.ARGB == [255, 255, 165, 0] as int[]
 * assert orange.rgb == orange.getRGB() == orange.RGB == [255, 165, 0] as int[]
 * }
 * </pre>
 *
 * Note: Groovylicious patches Vanilla methods to support this class, so explicit calls to {@code .get()}/{@code .packed} are not required.
 * For example:
 * <pre>
 * {@code
 * Colour semiTransparentPink = new Colour(alpha: 127, red: 255, green: 192, blue: 203)
 * Screen.drawCenteredString(poseStack, this.font, new TextComponent("I'm pink"),
 *              this.width / 2, this.height / 2 - 50, semiTransparentPink) // using semiTransparentPink.packed is optional here
 * }
 * </pre>
 *
 * @see {@link ColoursRegistry}
 *
 * @author Paint_Ninja
 */
@POJO
@CompileStatic
@EqualsAndHashCode(includes = 'packed')
@ToString(includePackage = false, includeNames = true, includes = ['packed', 'argb'], nameValueSeparator = ': ')
class Colour {
    final int packed
    final int[] argb, rgb

    Colour(int packed) {
        this.packed = packed
        this.argb = [FastColor.ARGB32.alpha(packed), FastColor.ARGB32.red(packed), FastColor.ARGB32.green(packed), FastColor.ARGB32.blue(packed)]
        this.rgb = argb[1..3] as int[]
    }

    Colour(int red, int green, int blue) {
        this.packed = FastColor.ARGB32.color(255, red, green, blue)
        this.argb = [255, red, green, blue]
        this.rgb = argb[1..3] as int[]
    }

    Colour(int alpha, int red, int green, int blue) {
        this.packed = FastColor.ARGB32.color(alpha, red, green, blue)
        this.argb = [alpha, red, green, blue]
        this.rgb = argb[1..3] as int[]
    }

    Colour(int[] argb) {
        this.packed = FastColor.ARGB32.color(argb[0], argb[1], argb[2], argb[3])
        this.argb = argb
        this.rgb = argb[1..3] as int[]
    }

    Colour(ChatFormatting colour) {
        this.packed = colour.getColor()
        this.argb = [FastColor.ARGB32.alpha(packed), FastColor.ARGB32.red(packed), FastColor.ARGB32.green(packed), FastColor.ARGB32.blue(packed)]
        this.rgb = argb[1..3] as int[]
    }

    Colour(TextColor colour) {
        this.packed = colour.getValue()
        this.argb = [FastColor.ARGB32.alpha(packed), FastColor.ARGB32.red(packed), FastColor.ARGB32.green(packed), FastColor.ARGB32.blue(packed)]
        this.rgb = argb[1..3] as int[]
    }

    Colour(Map<String, Integer> args) {
        if (args.argb) { // new Colour(argb: [255, 255, 255, 255])
            this.argb = args.argb as int[]
            this.rgb = argb[1..3] as int[]
        } else if (args.rgb) { // new Colour(rgb: [255, 255, 255], alpha: 255)
            this.rgb = args.rgb as int[]
            this.argb = [args.alpha ?: 255, rgb[0], rgb[1], rgb[2]]
        } else { // new Colour(alpha: 255, red: 255, green: 255, blue: 255)
            this.argb = [args.alpha ?: 255, args.red, args.green, args.blue]
            this.rgb = argb[1..3] as int[]
        }
        this.packed = FastColor.ARGB32.color(argb[0], argb[1], argb[2], argb[3])
    }

    int get() { this.packed }
    int getAlpha() { this.argb[0] }
    int getRed() { this.rgb[0] }
    int getGreen() { this.rgb[1] }
    int getBlue() { this.rgb[2] }
    int[] getARGB() { this.argb }
    int[] getRGB() { this.rgb }
    int getA() { this.getAlpha() }
    int getR() { this.getRed() }
    int getG() { this.getGreen() }
    int getB() { this.getBlue() }

    @Memoized
    TextColor getTextColor() { TextColor.fromRgb(this.packed) }

    /* Conversion handler
     *
     * Example usage:
     * --------------
     * Colour colour = Colour.from(red: 255, green: 0, blue: 0)
     * int packed = colour as int
     * assert colour.packed == packed
     */
    Object asType(Class type) {
        return switch (type) {
            case int -> this.packed
            case int[] -> this.argb
            case ChatFormatting -> {
                ChatFormatting.values().each { chatFormatting ->
                    if (chatFormatting.getColor() == this.packed)
                        yield chatFormatting
                }
                yield null // Return null if the colour is not the same as any of the ChatFormatting colours
            }
            default -> throw new ClassCastException("Cannot cast to ${type.name}")
        }
    }

    // Factory methods for creating colours
    @Pure
    static Colour of(int packed) {
        return new Colour(packed)
    }

    @Pure
    static Colour of(int red, int green, int blue) {
        return new Colour(red, green, blue)
    }

    @Pure
    static Colour of(int alpha, int red, int green, int blue) {
        return new Colour(alpha, red, green, blue)
    }

    @Pure
    static Colour of(int[] argb) {
        return new Colour(argb)
    }

    @Pure
    static Colour of(ChatFormatting colour) {
        return new Colour(colour)
    }

    @Pure
    static Colour of(TextColor colour) {
        return new Colour(colour)
    }

    @Pure
    static Colour of(Map args) {
        return new Colour(args)
    }
}
