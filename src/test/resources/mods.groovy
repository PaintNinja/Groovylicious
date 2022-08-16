ModsDotGroovy.make {
    modLoader = 'gml'
    loaderVersion = '[1,)'

    license = 'MIT'
    issueTrackerUrl = 'https://github.com/PaintNinja/Groovylicious/issues'

    mod {
        modId = 'groovylicioustest'
        displayName = 'Groovylicious Testing'

        version = this.version

        description = 'Testing for groovylicious'
        authors = ['beans']

        dependencies {
            // The `forgeVersion` and `minecraftVersion` properties are computed from the `minecraft` dependency in the `build.gradle` file
            forge = "[${this.forgeVersion},)"
            // The automatically generated `minecraftVersionRange` property is computed as: [1.$minecraftMajorVersion,1.${minecraftMajorVersion + 1})
            // Example: for a Minecraft version of 1.19, the computed `minecraftVersionRange` is [1.19,1.20)
            minecraft = this.minecraftVersionRange
        }
    }
}