[![Groovylicious](./src/main/resources/logo.png)](https://www.curseforge.com/minecraft/mc-mods/groovylicious)

*Delicious syntax sugar with the power of Groovy!*

## About

Groovylicious is a mod that eases development of Minecraft Forge mods by providing Groovy APIs, DSLs and compile-time AST transformations. This results in cleaner, easier to understand code with reduced boilerplate and ceremony for mod developers.

## Features

### `@ModConfig`

While Forge already provides a config system, Groovylicious further simplifies the use of it by letting you simply write a standard dataclass and annotating it with `@ModConfig`.

When you do this, Groovylicious generates all the necessary code at compile-time to transparently register and manage your mod's config with the Forge config system. This means that you can then access and change the variables directly and they'll be reflected in the underlying config file.

#### Demo

The config class:

```groovy
@Config
static class Config {

    /** Is the parrot alive? */
    static boolean isParrotAlive = false
    
    /** How fast do you need to drive to accomplish time travel? {@range 50..100} */
    static int timeTravelMph = 88

    /**
     * Fox rotation speed in RPM
     * 100 is slow, 9999 is fast
     * @range 100.0..9999.0
     */
    static double foxRotation = 9000.42d // note: the {@range} is optional

    // byte, short and float are supported as well
    static byte lexDucks = 101
    
    // Config groups are supported - simply add inner static classes
    static class LifeOfBrian {
        /** How far are you willing to walk to see the messiah? (in blocks) */
        static long willingToWalkDistance = 2000L

        /** The holy words of the messiah's mother */
        static String lifeOfBrianQuote = "He's not the messiah, he's a very naughty boy!"
        
        // Nested config groups are supported, too
        static class Lyrics {
            // GroovyDoc is optional - if ommitted, no comment is defined for the config value
            static String brightSideOfLife = """
                If life seems jolly rotten
                There's something you've forgotten
                And that's to laugh and smile and dance and sing
            """
        }
    }
}
```

Usage:

```groovy
println "Is the parrot alive? ${Config.isParrotAlive}"

if (!Config.isParrotAlive) {
    Config.isParrotAlive = true // revive the parrot
}

println "How about now? ${Config.isParrotAlive}"
```

#### Bonus features

This approach provides a couple of bonus features for free, such as supporting Groovy's transparent get/set feature and full IDE support:

```groovy
// In Groovy, getters/setters/variable access are usually interchangeable for public fields
Config.getTimeTravelMph() // works
Config.settimeTravelMph(90)

Config.timeTravelMph // also works
Config.timeTravelMph = 90

// The Forge config system way without Groovylicious - must always call get()/set() 
// in order to get/set the underlying value we care about
Config.timeTravelMph.get()
```

![](./images/config-demo-ide-support.png)

#### Advanced usage

If you need to do something more complex with your configs, you can simply define an explicit `static void init()` method and Groovylicious will stop at an earlier stage during code generation for your config dataclass.

The builder has all the config values defined on it and built as a config spec, but registration and calling your init() is left to you. You can explicitly define a builder, or use the implied one in `@CompileDynamic` mode, likewise with the `ForgeConfigSpec`.

You can also add explicit `init()` methods to each config group, however unlike the root `init()`, config group `init()` methods are always called regardless of whether they were implicitly or explicitly defined.

### Colour API
This API helps you use consistent colours in your code (and across mods) without having to worry about the exact colour values. It also provides a convenient way to use the same colours that Minecraft GUIs use without needing to check the constants by hand.

First is the `Colour` type: this lets you make a colour from a packed int, (A)RGB values or `ChatFormatting`. You can then grab individual colour channels from this `Colour` type and optionally register it to the `Colours` registry.

The `Colours` registry is like an expandable enum that contains colours used by Vanilla Minecraft and other mods, facilitating consistency and providing a central place to store your `Colour`s. All of Vanilla's `ChatFormatting` colours are also available here.

#### Demo

```groovy
import ga.ozli.minecraftmods.groovylicious.api.gui.Colour
import static ga.ozli.minecraftmods.groovylicious.api.gui.ColoursRegistry.instance as Colours

final Colour orange = new Colour(red: 255, green: 165, blue: 0)
println SV(orange) // prints orange=Colour(packed: -23296, argb: [255, 255, 165, 0])
println orange.getRed() // prints 255
println orange.packed // prints -23296
println orange.RGB // prints [255, 165, 0]

Colours.EDITBOX_TEXT // returns the text colour used inside edit boxes in Minecraft

Colours.ORANGE = orange // register the orange colour we made earlier so that it can be used anywhere
```

### `ExtensibleScreen` and Screen-related DSLs
ExtensibleScreen provides a way of creating screens at runtime and hooking into specific parts of overriden methods such as `init()` and `render()`.

The DSL provides an alternative way of creating Screens and the widgets that go on them. Aspects such as drawing text shadows, alignment, colouring and TextComponent vs TranslationComponent are simplified and no longer require calling different methods or creating specific types.

If you need to perform custom rendering or init logic, you can modify the appropriate `Closure` list in the `ExtensibleScreen` instance (such as `onPreRender`) or use the appropriate closure in the DSL (such as `onPreRender { /* custom pre-render code here */ }`).

#### Demo

```groovy
import ga.ozli.minecraftmods.groovylicious.dsl.ScreenBuilder

static Screen makeDemoScreen() {
    return new ScreenBuilder().makeScreen("Demo Screen") {
        println title // prints TextComponent{text='Demo screen', ...}
        drawBackground = true
        
        button {
            text = "demo.clickMe"
            position x: 10, y: 10
            size x: 100, y: 20
            onClick {
                println "Clicked!"
            }
        }
        
        label {
            text = "Demo label"
            position x: 10, y: 40
            colour = Colours.ORANGE
        }
    }
}
```