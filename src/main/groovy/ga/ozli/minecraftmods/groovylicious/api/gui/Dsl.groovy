package ga.ozli.minecraftmods.groovylicious.api.gui

class Dsl {

//    static void ColoursDsl() {
//        println "White"
//    }

    static def getColoursDsl() {
        return ColoursDsl {}
    }

    static void ColoursDsl(@DelegatesTo(value = WhiteDsl, strategy = Closure.DELEGATE_FIRST) final Closure closure) {
        final WhiteDsl white = new WhiteDsl()

        closure.delegate = white
        closure.resolveStrategy = Closure.DELEGATE_FIRST
        closure.call()
    }
}

class WhiteDsl {
    static int alpha = 0
    void red() {}
    void green() {}
    void blue() {}
}