package ga.ozli.minecraftmods.groovylicious.dsl.traits

import ga.ozli.minecraftmods.groovylicious.api.gui.Alignment
import groovy.transform.CompileStatic

@CompileStatic
trait AlignmentTrait {
    private Alignment alignment = Alignment.CENTRE

    void alignLeft() {
        this.alignment = Alignment.LEFT
    }

    void alignCentre() {
        this.alignment = Alignment.CENTRE
    }

    void alignRight() {
        this.alignment = Alignment.RIGHT
    }

    void setAlignment(final Alignment alignment) {
        this.alignment = alignment
    }

    Alignment getAlignment() {
        return this.alignment
    }

    void setAlign(final Alignment alignment) {
        this.alignment = alignment
    }

    Alignment getAlign() {
        return this.alignment
    }
}
