ModsDotGroovy.make {
    modLoader = 'gml'
    loaderVersion = '[3,)'

    license = 'MIT'
    issueTrackerUrl = 'https://github.com/PaintNinja/Groovylicious/issues'

    mod {
        modId = 'groovylicious'
        version = this.version

        displayUrl = 'https://www.curseforge.com/minecraft/mc-mods/Groovylicious'
        description = 'Delicious syntax sugar with the power of Groovy!'
        authors = ['Paint_Ninja', 'Matyrobbrt', 'lukebemish']

        logoFile = 'logo.png'

        dependencies {
            forge = "[45,)"
            minecraft = '[1.19.4]'
        }
    }
}
