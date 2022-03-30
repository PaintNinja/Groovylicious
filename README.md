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
@ModConfig(modId = 'groovylicious')
static class Config {
    /** How fast do you need to drive to accomplish time travel? {@range 50..100} */
    static int timeTravelMph = 88

    /**
     * Fox rotation speed in RPM
     * 100 is slow, 9999 is fast
     * {@range 100.0..9999.0}
     */
    static double foxRotation = 9000.42d // note: the {@range} is optional

    /** How far are you willing to walk to see the messiah? (in blocks) */
    static long willingToWalkDistance = 2000L

    /** The holy words of the messiah's mother */
    static String lifeOfBrianQuote = "He's not the messiah, he's a very naughty boy!"

    /** Is the parrot alive? */
    static boolean parrot = false

    // this method's just here to load this config class
    static void init() {}
}
```

Usage:

```groovy
println "Is the parrot alive? ${Config.parrot}"

if (!Config.parrot) {
    Config.parrot = true // revive the parrot
}

println "How about now? ${Config.parrot}"
```

#### Bonus features

This approach provides a couple of bonus features for free, such as supporting Groovy's transparent get/set feature and full IDE support:

```groovy
// In Groovy, getters/setters/variable access are usually interchangeable for public fields
Config.getParrot() // works
Config.setParrot(true)

Config.parrot // also works
Config.parrot = true

// The Forge config system way without Groovylicious - must always call get()/set() 
// in order to get/set the underlying value we care about
Config.parrot.get()
```

![](./images/config-demo-ide-support.png)