package ga.ozli.minecraftmods.groovylicious.dsl.modsdotgroovy

import groovy.transform.CompileStatic
import org.apache.groovy.lang.annotation.Incubating

import javax.annotation.Nullable

@Incubating
@CompileStatic
class ModInfoBuilder {
    /**
     * The modId of the mod. This should match the value of your mod's {@literal @}Mojo/{@literal @}Mod annotated main class.
     */
    String modId = 'unknown'

    /**
     * The friendly name of the mod. This is the name that will be displayed in the in-game Mods screen.
     */
    String displayName = modId.capitalize()

    // todo: support the "well known variables usable here"
    /**
     * The version number of the mod - there's a few well known ${} variables usable here or just hardcode it.<br>
     * ${file.jarVersion} will substitute the value of the Implementation-Version as read from the mod's JAR file metadata.<br>
     * See the associated build.gradle script for how to populate this completely automatically during a build.
     */
    String version = 'unknown'

    /**
     * A URL to query for updates for this mod.<br>
     * See <a href='https://mcforge.readthedocs.io/en/latest/gettingstarted/autoupdate/'>the JSON update specification</a>
     */
    @Nullable String updateJsonUrl = null

    /**
     * A URL for the "homepage" for this mod, displayed in the in-game Mods screen.
     */
    @Nullable String displayUrl = null

    // todo: change type to a File instead of a String
    /**
     * A file name (in the root of the mod JAR) containing a logo for display in the in-game Mods screen.
     */
    @Nullable String logoFile = null

    @Nullable String credits = null
    List<String> authors = []

    /**
     * Display Test controls the display for your mod in the server connection screen.<br>
     */
    // only in 1.19+
    //@Nullable String displayTest = null

    /**
     * A multi-line description text for the mod, displayed in the in-game Mods screen.<br>
     * Groovylicious will automatically strip the fixed length code indent for you.
     */
    String description = ''

    void setDescription(final String description) {
        this.description = description.stripIndent()
    }

    void setAuthor(final String author) {
        this.authors = [author]
    }

    void author(final String author) {
        this.authors << author
    }

    ImmutableModInfo build() {
        return new ImmutableModInfo(this.modId, this.displayName, this.version, this.updateJsonUrl, this.displayUrl, this.logoFile, this.credits, this.authors, this.description)
    }
}
