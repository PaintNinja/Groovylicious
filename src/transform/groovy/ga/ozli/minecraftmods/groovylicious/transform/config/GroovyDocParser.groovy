package ga.ozli.minecraftmods.groovylicious.transform.config

import groovy.lang.groovydoc.Groovydoc
import groovy.transform.CompileStatic

import java.util.regex.Matcher

@CompileStatic
class GroovyDocParser {

    // todo: build the `Matcher`s once and cache them

    Groovydoc groovyDoc
    @Lazy Map<String, String> tags = this.parseTags()
    @Lazy String commentFreeText = this.toCommentFreeText() // Groovydoc without the '/**', ' * ' and '*/'
    @Lazy String htmlFreeText = this.toHtmlFreeText() // same as above but also without HTML tags (e.g. <b> and </b>)
    @Lazy String plainText = this.toPlainText() // same as above but also without JavaDoc tags (e.g. @param)

    GroovyDocParser(final Groovydoc groovyDoc) {
        this.groovyDoc = groovyDoc
    }

    String toCommentFreeText() {
        // For single line comments:
        // ^(\/\*\*\s?)(?<string>.+)(\s?\*\/) turns '/** ... */' into ...
        final String rawText = this.groovyDoc.content ?: ''
        String commentFreeText = rawText
                .stripIndent()
                .replaceAll($/^(/\*\*\s?)(?<string>.+)(\s?\*/)/$, '$2')

        if (!commentFreeText.contains('*/'))
            return commentFreeText

        // For multi-line comments:
        // ^(/\*\*\s?) turns '/** ' into ''
        // ^(\s\*\s?)(?<string>.+)? turns ' * ...' into '...'
        // ^(\s?\*\/) turns '*/' into ''
        String multiLineCommentFreeText = ''
        boolean firstLine = true
        commentFreeText.eachLine { line ->
            line = line.stripIndent()
                    .replaceAll(/^(\/\*\*\s?)/, '')
                    .replaceAll(/^(\*\s?)(?<string>.+)?/, '$2')
                    .replaceAll($/^(\*/)/$, '')

            if (firstLine) {
                firstLine = false
                multiLineCommentFreeText += line
            } else if (line != '/') {
                multiLineCommentFreeText += '\n' + line
            }

            return
        }

        return multiLineCommentFreeText[1..-1] // trim off the first newline
    }

    String toHtmlFreeText() {
        // todo: support <p> tags
        final String rawText = this.commentFreeText
        return rawText
                .replaceAll($/(<br/?>)[^\n]+/$, '\n') // make a new line if there's a <br> without a new line immediately afterwards
                .replaceAll($/<[^>]*>/$, '') // strip any remaining HTML tags
    }

    String toPlainText() {
        String plainText = this.htmlFreeText
                .replaceAll(/\{@(?<tag>\w+|\d+)(?<content>\s.+)}/, '') // strip javadoc taglets in the format: {@tag content}

        String multiLinePlainText = ''
        plainText.eachLine {
            // strip javadoc taglets in the format: @tag content
            it = it.replaceAll(/^@(?<tag>\w+|\d+)\s(?<content>.+)\s?(?:[^\n]+|[^\r]+)?/, '').stripTrailing()

            if (it != '')
                multiLinePlainText += '\n' + it

            return
        }

        if (multiLinePlainText.startsWith '\n') return multiLinePlainText[1..-1] // trim off the first newline
        else return multiLinePlainText
    }

    // returns a map of tag names to tag content (e.g. {@param foo} -> [param: foo])
    Map<String, String> parseTags() {
        String htmlFreeText = this.htmlFreeText

        final Map<String, String> tags = [:]
        htmlFreeText.eachLine { line ->
            // parse javadoc taglets in the format: {@tag content}
            Matcher bracketedTagMatcher = line =~ /(.+)?\{@(?<tag>\w+|\d+)\s(?<content>.+)}(?:.+)?/
            if (bracketedTagMatcher.matches()) {
                tags[bracketedTagMatcher.group('tag')] = bracketedTagMatcher.group('content')
            } else {
                // parse javadoc taglets in the format: @tag content
                Matcher bracketlessTagMatcher = line =~ /^@(?<tag>\w+|\d+)\s(?<content>.+)\s?(?:[^\n]+|[^\r]+)?/
                if (bracketlessTagMatcher.matches()) {
                    tags[bracketlessTagMatcher.group('tag')] = bracketlessTagMatcher.group('content')
                }
            }
            return
        }

        return tags
    }
}
