package ga.ozli.minecraftmods.groovylicious.dsl.modsdotgroovy

import net.minecraftforge.forgespi.language.IModInfo

/**
 * Forces a dependency to be mandatory.<br>
 * Ordering is forced to 'NONE' as well as it is the only valid value when mandatory is true.
 */
trait ForceMandatoryTrait {
    final boolean mandatory = true
    final IModInfo.Ordering ordering = IModInfo.Ordering.NONE
}