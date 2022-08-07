package ga.ozli.minecraftmods.groovylicious.transform.config

import groovy.lang.groovydoc.Groovydoc
import groovy.transform.CompileStatic
import groovy.transform.Memoized

import java.util.regex.Matcher
import java.util.regex.Pattern

@CompileStatic
class GroovyDocParser {

    Groovydoc groovyDoc
    @Lazy Map<String, String> tags = this.parseTags()
    @Lazy String commentFreeText = this.toCommentFreeText() // Groovydoc without the '/**', ' * ' and '*/'
    @Lazy String htmlFreeText = this.toHtmlFreeText() // same as above but also without HTML tags (e.g. <b> and </b>)
    @Lazy String plainText = this.toPlainText() // same as above but also without JavaDoc tags (e.g. @param)

    @Lazy private static final Pattern bracketedTagPattern = Pattern.compile(/(.+)?\{@(?<tag>\w+|\d+)\s(?<content>.+)}(?:.+)?/)
    @Lazy private static final Pattern bracketlessTagPattern = Pattern.compile(/^@(?<tag>\w+|\d+)\s(?<content>.+)\s?(?:[^\n]+|[^\r]+)?/)

    GroovyDocParser(final Groovydoc groovyDoc) {
        this.groovyDoc = groovyDoc
    }

    String toCommentFreeText() {
        // For single line comments:
        // ^(\/\*\*\s?)(?<string>.+)(\s?\*\/) turns '/** ... */' into ...
        final String rawText = this.groovyDoc.content ?: ''
        final String commentFreeText = rawText
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
        final String plainText = this.htmlFreeText
                .replaceAll(/\{@(?:\w+|\d+)\s.+}/, '') // strip javadoc taglets in the format: {@tag content}

        String multiLinePlainText = ''
        plainText.eachLine {
            // strip javadoc taglets in the format: @tag content
            it = it.replaceAll(/^@(?:\w+|\d+)\s.+\s?(?:[^\n]+|[^\r]+)?/, '').stripTrailing()

            if (it != '')
                multiLinePlainText += '\n' + it

            return
        }

        if (multiLinePlainText.startsWith '\n') return multiLinePlainText[1..-1] // trim off the first newline
        else return multiLinePlainText
    }

    // returns a map of tag names to tag content (e.g. {@param foo} -> [param: foo])
    Map<String, String> parseTags() {
        final String htmlFreeText = this.htmlFreeText

        final Map<String, String> tags = [:]
        htmlFreeText.eachLine { line ->
            // parse javadoc taglets in the format: {@tag content}
            final Matcher bracketedTagMatcher = line =~ bracketedTagPattern
            if (bracketedTagMatcher.matches()) {
                tags[bracketedTagMatcher.group('tag')] = bracketedTagMatcher.group('content')
            } else {
                // parse javadoc taglets in the format: @tag content
                final Matcher bracketlessTagMatcher = line =~ bracketlessTagPattern
                if (bracketlessTagMatcher.matches()) {
                    tags[bracketlessTagMatcher.group('tag')] = bracketlessTagMatcher.group('content')
                }
            }
            return
        }

        return tags
    }
}
