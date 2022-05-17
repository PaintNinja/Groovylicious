package ga.ozli.minecraftmods.groovylicious.api.gui

import groovy.transform.AutoFinal
import groovy.transform.CompileStatic
import net.minecraft.ChatFormatting
import net.minecraft.util.FastColor

/**
 * Simple colour library for MC, with built-in names for all colours used by Vanilla that you can use for consistency
 *
 * Usage:
 * Colours.White // equivalent to Colours.WHITE.packed, Colours.WHITE.get() or ChatFormatting.WHITE.getColor()
 * Colours.WHITE.argb // gets the ARGB value of the colour white
 * Colours.RED.rgb // gets the RGB value of the colour red
 * Colours.Aqua // gets the packed value of the colour aqua
 * Colours.GOLD.blue // gets the blue channel of the colour gold
 *
 * You can also define your own colours and use the features provided by this class, either dynamically or statically:
 * Dynamically:
 * Colours.define("MAGENTA", 255, 0, 255)
 * Colours.Magenta // gets the packed value of the colour magenta
 *
 * Statically:
 * class CustomColours {
 *    static final Colours MAGENTA = new Colours(0, 255, 255)
 *    static int Magenta = MAGENTA.packed
 * }
 * CustomColours.Magenta
 */
@AutoFinal
@CompileStatic
enum ColoursNew {
    /** Used by Vanilla GUIs **/
    WHITE(ChatFormatting.WHITE.getColor()),
    LIGHT_GREY(10526880),
    SUBTITLE(16711680),
    WARNING(8421504),

    EDITBOX_TEXT(14737632),
    EDITBOX_TEXTUNEDITABLE(7368816),
    EDITBOX_CURSORINSERT(-3092272),
    EDITBOX_BORDER(-6250336),
    EDITBOX_BORDERFOCUSED(-1),
    EDITBOX_BACKGROUND(16777216),
    EDITBOX_SUGGESTION(-8355712),

    /** For completeness **/
    GREY(ChatFormatting.GRAY.getColor()),
    DARK_GREY(ChatFormatting.DARK_GRAY.getColor()),
    BLACK(ChatFormatting.BLACK.getColor()),

    RED(ChatFormatting.RED.getColor()),
    DARK_RED(ChatFormatting.DARK_RED.getColor()),
    YELLOW(ChatFormatting.YELLOW.getColor()),
    GOLD(ChatFormatting.GOLD.getColor()),
    GREEN(ChatFormatting.GREEN.getColor()),
    DARK_GREEN(ChatFormatting.DARK_GREEN.getColor()),
    AQUA(ChatFormatting.AQUA.getColor()),
    DARK_AQUA(ChatFormatting.DARK_AQUA.getColor()),
    BLUE(ChatFormatting.BLUE.getColor()),
    DARK_BLUE(ChatFormatting.DARK_BLUE.getColor()),
    LIGHT_PURPLE(ChatFormatting.LIGHT_PURPLE.getColor()),
    DARK_PURPLE(ChatFormatting.DARK_PURPLE.getColor())

    int alpha, red, green, blue, packed
    int[] rgb = [red, green, blue]
    int[] argb = [alpha, red, green, blue]

    /** ARGB constructor **/
    ColoursNew(int alpha, int red, int green, int blue) {
        this.alpha = alpha
        this.red = red
        this.green = green
        this.blue = blue
        this.packed = from(alpha, red, green, blue)
    }

    /** RGB constructor **/
    ColoursNew(int red, int green, int blue) {
        this.alpha = 255
        this.red = red
        this.green = green
        this.blue = blue
        this.packed = from(255, red, green, blue)
    }

    /** Packed colour constructor **/
    ColoursNew(int packed) {
        this.alpha = FastColor.ARGB32.alpha(packed)
        this.red = FastColor.ARGB32.red(packed)
        this.green = FastColor.ARGB32.green(packed)
        this.blue = FastColor.ARGB32.blue(packed)
        this.packed = packed
    }

    /**
     * Creates a packed colour from the given ARGB values
     * @param alpha the alpha/opacity channel
     * @param red the red channel
     * @param green the green channel
     * @param blue the blue channel
     * @return the packed colour
     */
    static int from(int alpha, int red, int green, int blue) {
        return FastColor.ARGB32.color(alpha, red, green, blue)
    }

    /**
     * Creates a packed colour from the given RGB values
     * @param red the red channel
     * @param green the green channel
     * @param blue the blue channel
     * @return the packed colour
     */
    static int from(int red, int green, int blue) {
        return FastColor.ARGB32.color(255, red, green, blue)
    }

//    @CompileDynamic
//    static void define(String name, int red, int green, int blue) {
//        final String enumName = name.toUpperCase(Locale.ROOT)
//        final String packedName = enumName.toLowerCase().replace('_', ' ').capitalize().replace(' ', '')
//        final colour = new ColoursNew(red, green, blue)
//        this.metaClass.'static'."get${enumName}" << colour
//        this.metaClass.'static'."get${packedName}" << colour.packed
//    }

    static def $static_propertyMissing(final String name) {
        throw new NoSuchFieldError("The colour \"$name\" hasn't been defined yet")
    }

    Object asType(Class type) {
        if (type === Integer) {
            return this.packed
        }
        return new ClassCastException()
    }

    /** Alias for COLOUR_NAME.packed **/
    int get() {
        return this.packed
    }

    /**
     * Aliases for colourName
     *
     * While this code could be much shorter by defining a static metaclass method for each colour in the enum,
     * this more traditional way has better IDE support.
     */

    public static int White = WHITE.packed
    public static int LightGrey = LIGHT_GREY.packed
    public static int Subtitle = SUBTITLE.packed
    public static int Warning = WARNING.packed

    public static int EditboxText = EDITBOX_TEXT.packed
    public static int EditboxTextUneditable = EDITBOX_TEXTUNEDITABLE.packed
    public static int EditboxCursorInsert = EDITBOX_CURSORINSERT.packed
    public static int EditboxBorder = EDITBOX_BORDER.packed
    public static int EditboxBorderFocused = EDITBOX_BORDERFOCUSED.packed
    public static int EditboxBackground = EDITBOX_BACKGROUND.packed
    public static int EditboxSuggestion = EDITBOX_SUGGESTION.packed

    public static int Grey = GREY.packed
    public static int DarkGrey = DARK_GREY.packed
    public static int Black = BLACK.packed

    public static int Red = RED.packed
    public static int DarkRed = DARK_RED.packed
    public static int Yellow = YELLOW.packed
    public static int Gold = GOLD.packed
    public static int Green = GREEN.packed
    public static int DarkGreen = DARK_GREEN.packed
    public static int Aqua = AQUA.packed
    public static int DarkAqua = DARK_AQUA.packed
    public static int Blue = BLUE.packed
    public static int DarkBlue = DARK_BLUE.packed
    public static int LightPurple = LIGHT_PURPLE.packed
    public static int DarkPurple = DARK_PURPLE.packed

    /** Colour groups **/
    @AutoFinal
    static class Editbox {
        public static int Text = EDITBOX_TEXT.packed
        public static int TextUneditable = EDITBOX_TEXTUNEDITABLE.packed
        public static int CursorInsert = EDITBOX_CURSORINSERT.packed
        public static int Border = EDITBOX_BORDER.packed
        public static int BorderFocused = EDITBOX_BORDERFOCUSED.packed
        public static int Background = EDITBOX_BACKGROUND.packed
        public static int Suggestion = EDITBOX_SUGGESTION.packed
    }

    @AutoFinal
    static class Chat {
        public static int White = WHITE.packed
        public static int Grey = GREY.packed
        public static int DarkGrey = DARK_GREY.packed
        public static int Black = BLACK.packed
        public static int Red = RED.packed
        public static int DarkRed = DARK_RED.packed
        public static int Yellow = YELLOW.packed
        public static int Gold = GOLD.packed
        public static int Green = GREEN.packed
        public static int DarkGreen = DARK_GREEN.packed
        public static int Aqua = AQUA.packed
        public static int DarkAqua = DARK_AQUA.packed
        public static int Blue = BLUE.packed
        public static int DarkBlue = DARK_BLUE.packed
        public static int LightPurple = LIGHT_PURPLE.packed
        public static int DarkPurple = DARK_PURPLE.packed
    }
}
