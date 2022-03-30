package ga.ozli.minecraftmods.groovylicious.transform.config

import groovy.transform.CompileStatic

@CompileStatic
class CodeCommentParser {
    static String parse(final String rawGroovyDocText) {
        String result = ''

        rawGroovyDocText.eachLine { line ->
            String strippedLine = line.stripIndent()

            // remove the @range taglet if present
            strippedLine = strippedLine.replaceFirst(/\{@range\s+\d*\.?\d+<?\.\.<?\d*\.?\d+}/, '')

            if (strippedLine.startsWith('/** ') && strippedLine.endsWith(' */')) {
                // single line comments
                strippedLine = strippedLine[3..-4]
            } else {
                // block comments
                if (strippedLine.startsWith('/**'))
                    strippedLine = strippedLine[2..-1]
                else if (strippedLine.startsWith('*/') || strippedLine.length() < 3)
                    strippedLine = '*'
                else
                    strippedLine = strippedLine[1..-1] ?: '*'

                if (strippedLine.endsWith('*/'))
                    strippedLine = strippedLine[0..-2]
            }

            if (strippedLine != '*' && !strippedLine.isEmpty())
                result += '\n' + strippedLine
        }

        return result
    }
}
