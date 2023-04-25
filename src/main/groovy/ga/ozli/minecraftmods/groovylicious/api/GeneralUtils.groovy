package ga.ozli.minecraftmods.groovylicious.api

import com.matyrobbrt.gml.GMLModLoadingContext
import groovy.transform.CompileStatic
import groovy.transform.Memoized
import groovy.transform.Pure
import net.minecraft.network.chat.Component
import org.jetbrains.annotations.Nullable

import java.util.regex.Pattern

@CompileStatic
final class GeneralUtils {
    private static final Pattern TRANSLATABLE = Pattern.compile(/([\da-z]\.[\da-z])+/)

    /**
     * @return null if the mod bus event didn't set the mod container when firing the event
     */
    static @Nullable String getModId() {
        return GMLModLoadingContext.get()?.container?.modId
    }

    @Pure
    @Memoized
    static Component stringToComponent(@Nullable final String string) {
        if (string === null || string.isEmpty()) return Component.empty()

        if (string.chars().anyMatch(Character::isUpperCase) && !TRANSLATABLE.matcher(string).matches())
            return Component.literal(string)
        else
            return Component.translatable(string)
    }
}
