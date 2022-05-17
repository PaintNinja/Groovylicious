package ga.ozli.minecraftmods.groovylicious.api.gui

import ga.ozli.minecraftmods.groovylicious.transform.IDESideOnly
import groovy.transform.Canonical
import groovy.transform.CompileDynamic
import groovy.transform.CompileStatic
import groovy.transform.Immutable
import groovy.transform.NamedParams
import net.minecraft.ChatFormatting
import net.minecraft.util.FastColor

class Colours {

//    class Colour {
//        int packed = 0
//        int alpha = FastColor.ARGB32.alpha(packed)
//        int red = FastColor.ARGB32.red(packed)
//        int green = FastColor.ARGB32.green(packed)
//        int blue = FastColor.ARGB32.blue(packed)
//        int[] rgb = [red, green, blue]
//        int[] argb = [alpha, red, green, blue]
//
//        Colour(int packed) {
//            this.packed = packed
//        }
//    }
//
//
//    static int getWHITE() {
//        return White.packed
//    }

    static class White {
        static int packed = ChatFormatting.WHITE.getColor()
        static int alpha = FastColor.ARGB32.alpha(packed)
        static int red = FastColor.ARGB32.red(packed)
        static int green = FastColor.ARGB32.green(packed)
        static int blue = FastColor.ARGB32.blue(packed)
        static int[] rgb = [red, green, blue]
        static int[] argb = [alpha, red, green, blue]

        static Object asType(Class type) {
            if (type === Integer) {
                return packed
            }
            return new ClassCastException()
        }
    }
////
//    static class MyOtherClass {
//        static void someAPI(Integer i) {println "I is $i"}
//    }
//
//    static class MyClass {
//        static int toInteger() { 22 }
//        static def asType(Class c) { 22 }
//    }

//    public def Colours = [
//         White: [
//                 packed: ChatFormatting.WHITE.color,
//                 alpha: FastColor.ARGB32.alpha(Colours.White.packed),
//                 red: FastColor.ARGB32.red(Colours.White.packed),
//                 green: FastColor.ARGB32.green(Colours.White.packed),
//                 blue: FastColor.ARGB32.blue(Colours.White.packed),
//                 rgb: [Colours.White.red, Colours.White.green, Colours.White.blue],
//                 argb: [Colours.White.alpha, Colours.White.red, Colours.White.green, Colours.White.blue]
//         ]
//    ]

//    public static def White = [
//            alpha: FastColor.ARGB32.alpha(White.packed),
//            red: FastColor.ARGB32.alpha(White.packed),
//            green: FastColor.ARGB32.alpha(White.packed),
//            blue: FastColor.ARGB32.alpha(White.packed)
//    ].withDefault {
//        if (White.containsKey(it.toLowerCase()))
//            White.get(it.toLowerCase())
//        else
//            ChatFormatting.WHITE.color
//    }

//    public static Map White = [
//            alpha: FastColor.ARGB32.alpha(ChatFormatting.WHITE.getColor()),
//            red: FastColor.ARGB32.red(ChatFormatting.WHITE.getColor()),
//            green: FastColor.ARGB32.green(ChatFormatting.WHITE.getColor()),
//            blue: FastColor.ARGB32.blue(ChatFormatting.WHITE.getColor())
//    ].withDefault { ChatFormatting.WHITE.getColor() }

//    public static Closure<Integer> White = { String it ->
//        int packed = ChatFormatting.WHITE.color
//
//        if (it.isEmpty() || it == 'packed')
//            return packed
//
//        def list = [
//            alpha: FastColor.ARGB32.alpha(packed),
//            red: FastColor.ARGB32.red(packed),
//            green: FastColor.ARGB32.green(packed),
//            blue: FastColor.ARGB32.blue(packed)
//        ]
//        return list[it]
//    }

    //public static int White = ChatFormatting.WHITE.color

//    public static def White = { String it ->
//        int packed = ChatFormatting.WHITE.getColor()
//
//        if (it.isEmpty() || it == 'packed')
//            return packed
//
//        def list = [
//            alpha: FastColor.ARGB32.alpha(packed),
//            red: FastColor.ARGB32.red(packed),
//            green: FastColor.ARGB32.green(packed),
//            blue: FastColor.ARGB32.blue(packed)
//        ]
//        return list[it]
//    }

//
//    private class Colour {
//        int alpha, red, green, blue, packed
//
//        Colour(int alpha, int red, int green, int blue) {
//            this.alpha = alpha;
//            this.red = red;
//            this.green = green;
//            this.blue = blue;
//            this.packed = from(alpha, red, green, blue);
//        }
//
//        Colour(int red, int green, int blue) {
//            this.alpha = 255;
//            this.red = red;
//            this.green = green;
//            this.blue = blue;
//            this.packed = from(red, green, blue);
//        }
//
//        Colour(int packed) {
//            this.alpha = FastColor.ARGB32.alpha(packed);
//            this.red = FastColor.ARGB32.red(packed);
//            this.green = FastColor.ARGB32.green(packed);
//            this.blue = FastColor.ARGB32.blue(packed);
//            this.packed = packed;
//        }
//
//        Colour(ChatFormatting chatFormattingColour) {
//            this.packed = chatFormattingColour.getColor();
//            this.alpha = FastColor.ARGB32.alpha(packed);
//            this.red = FastColor.ARGB32.red(packed);
//            this.green = FastColor.ARGB32.green(packed);
//            this.blue = FastColor.ARGB32.blue(packed);
//        }
//
//        static int from(int alpha, int red, int green, int blue) {
//            return FastColor.ARGB32.color(alpha, red, green, blue);
//        }
//
//        static int from(int red, int green, int blue) {
//            return FastColor.ARGB32.color(255, red, green, blue);
//        }
//
//        int get() {
//            return this.packed;
//        }
//
//        int[] getRGB() {
//            return new int[]{this.red, this.green, this.blue};
//        }
//
//        int[] getARGB() {
//            return new int[]{this.alpha, this.red, this.green, this.blue};
//        }
//    }
}

/**
 * Simple colour library for MC, with built-in names for all colours used by Vanilla that you can use for consistency
 */
//@CompileStatic
//@AutoFinal
//enum ColoursOld {
//    /** Used by Vanilla GUIs **/
//    White(ChatFormatting.WHITE),
//    LightGrey(10526880),
//    Subtitle(16711680),
//    Warning(8421504),
//
//    EditBox_Text(14737632),
//    EditBox_TextUneditable(7368816),
//    EditBox_CursorInsert(-3092272),
//    EditBox_Border(-6250336),
//    EditBox_BorderFocused(-1),
//    EditBox_Background(16777216),
//    EditBox_Suggestion(-8355712),
//
//    /** For completeness **/
//    PureWhite(255, 255, 255, 255),
//    Grey(ChatFormatting.GRAY),
//    DarkGrey(ChatFormatting.DARK_GRAY),
//    Black(0, 0, 0),
//
//    Red(ChatFormatting.RED),
//    DarkRed(ChatFormatting.DARK_RED),
//    Yellow(ChatFormatting.YELLOW),
//    Gold(ChatFormatting.GOLD),
//    Green(ChatFormatting.GREEN),
//    DarkGreen(ChatFormatting.DARK_GREEN),
//    Aqua(ChatFormatting.AQUA),
//    DarkAqua(ChatFormatting.DARK_AQUA),
//    Blue(ChatFormatting.BLUE),
//    DarkBlue(ChatFormatting.DARK_BLUE),
//    LightPurple(ChatFormatting.LIGHT_PURPLE),
//    DarkPurple(ChatFormatting.DARK_PURPLE)
//
//    private int alpha, red, green, blue, packed
//
//    Colours(int alpha, int red, int green, int blue) {
//        this.alpha = alpha;
//        this.red = red;
//        this.green = green;
//        this.blue = blue;
//        this.packed = from(alpha, red, green, blue);
//    }
//
//    Colours(int red, int green, int blue) {
//        this.alpha = 255;
//        this.red = red;
//        this.green = green;
//        this.blue = blue;
//        this.packed = from(red, green, blue);
//    }
//
//    Colours(int packed) {
//        this.alpha = FastColor.ARGB32.alpha(packed);
//        this.red = FastColor.ARGB32.red(packed);
//        this.green = FastColor.ARGB32.green(packed);
//        this.blue = FastColor.ARGB32.blue(packed);
//        this.packed = packed;
//    }
//
//    Colours(ChatFormatting chatFormattingColour) {
//        this.packed = chatFormattingColour.getColor();
//        this.alpha = FastColor.ARGB32.alpha(packed);
//        this.red = FastColor.ARGB32.red(packed);
//        this.green = FastColor.ARGB32.green(packed);
//        this.blue = FastColor.ARGB32.blue(packed);
//    }
//
//    static int from(int alpha, int red, int green, int blue) {
//        return FastColor.ARGB32.color(alpha, red, green, blue);
//    }
//
//    static int from(int red, int green, int blue) {
//        return FastColor.ARGB32.color(255, red, green, blue);
//    }
//
//    int get() {
//        return this.packed;
//    }
//
//    int[] getRGB() {
//        return new int[]{this.red, this.green, this.blue};
//    }
//
//    int[] getARGB() {
//        return new int[]{this.alpha, this.red, this.green, this.blue};
//    }
//}
