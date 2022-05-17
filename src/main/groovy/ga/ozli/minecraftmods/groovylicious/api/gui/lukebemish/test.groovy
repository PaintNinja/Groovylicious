import groovy.transform.CompileStatic
import groovy.transform.ToString

@CompileStatic
@ToString(includes = ['packed'])
class Colour {
    final int packed

    Colour(int packed) {
        this.packed = packed
        // Do all the setup as necessary here.
    }

    // static init default colors
    static final Colour WHITE = new Colour(0xffffff)

    // general utility functions - might not be necessary depending on the use.
    static Collection<Colour> values() {
        return backingMap.values()
    }
    static Colour get(String key) {
        return backingMap.get(key)
    }


    //Internal
    private static final Map<String, Colour> backingMap = new HashMap()
    static {
        // add static-inited colours to the map
        Colour.properties.forEach((k,v)->{
            if (v instanceof Colour) backingMap.put((String)k,v)
        })
    }

    static $static_propertyMissing(String property) {
        Colour colour = backingMap.get(property)
        if (colour!=null) return colour
        throw new MissingPropertyException(property, Colour.class)
    }

    static $static_propertyMissing(String property, Object newValue) {
        if (!backingMap.containsKey(property)) {
            backingMap.put(property, (Colour) newValue)
            return
        }
        // So that colours can't be overwritten
        throw new ReadOnlyPropertyException(property, Colour.class)
    }
}

// Basic test that shows that it compiles and runs
//@CompileStatic
class Test {
    static void main(String[] args) {
        Colour white = Colour.WHITE  // Autocomplete works here
        Colour.MAGENTA = new Colour(0xff00ff)
        Colour magenta = Colour.MAGENTA // Autocomplete doesn't work here, but it compiles and runs fine
        println(white)
        println(magenta)
        // Colour.MAGENTA = new Colour(0xffff00) this would throw an exception, since it's already been set.
    }
}