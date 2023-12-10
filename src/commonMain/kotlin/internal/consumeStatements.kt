package net.lsafer.i18ner.internal

internal interface StatementConsumer {
    fun onStatementStart(lineNumber: Int, line: String)

    fun onStatementFinish()

    fun onStatementIgnore()

    fun onStatementKey(key: String)

    fun onStatementTemplate(template: String)

    fun onStatementMissingAssignmentOperator()

    fun onStatementUnclosedValueString()
}

internal fun Sequence<String>.consumeStatements(consumer: StatementConsumer) {
    val iterator = withIndex().iterator()

    mainLoop@ while (iterator.hasNext()) {
        val (lineNumber, line) = iterator.next()

        consumer.onStatementStart(lineNumber, line)

        // handle blank lines
        if (line.isBlank()) {
            consumer.onStatementIgnore()
            continue@mainLoop
        }

        val segments = line.split('=', limit = 2)
        val key = segments[0].trim()
        // the value segment in the first line
        val value0 = segments.getOrNull(1)

        // handle whole line comments
        if (key[0] == '#') {
            consumer.onStatementIgnore()
            continue@mainLoop
        }

        consumer.onStatementKey(key)

        // handle lines with no `=` sign
        if (value0 == null) {
            consumer.onStatementMissingAssignmentOperator()
            consumer.onStatementFinish()
            continue@mainLoop
        }

        // handle blank values
        if (value0.isBlank()) {
            consumer.onStatementTemplate("")
            consumer.onStatementFinish()
            continue@mainLoop
        }

        var quote = '\''
        var quoteIndex = -1

        // find the first quote if any
        for (i in value0.indices) {
            when (val c = value0[i]) {
                '\'', '\"' -> {
                    quote = c
                    quoteIndex = i
                    break
                }

                else -> {
                    if (!c.isWhitespace())
                        break
                }
            }
        }

        // if the value starts with a `quote`, the value could be multiline,
        // everything before the second unescaped `quote` is within the content,
        // everything after the second unescaped `quote` is a comment
        // + the unescaped `quote` should not be included in the value.
        if (quoteIndex >= 0) {
            val value = StringBuilder()
            var valueN = value0.drop(quoteIndex + 1)

            // escaped on `StringBuilder.append` and not
            // on `StringBuilder.toString` to not treat
            // escape characters at the end of the lines as an
            // escape to the characters at the
            // beginning of the next lines
            while (true) {
                val terminal = valueN.indexOfNotEscapedByBackslash(quote)

                if (terminal < 0) {
                    // the literal has not ended in this line
                    // take the next line as value continuation
                    value.append(valueN.unescapeBackslashEscapes(quote))

                    // reached EOF without closing the literal
                    if (!iterator.hasNext()) {
                        consumer.onStatementUnclosedValueString()
                        consumer.onStatementIgnore()
                        continue@mainLoop
                    }

                    value.appendLine()
                    valueN = iterator.next().value
                } else {
                    // the literal has ended in this line
                    // treat the remaining as comments
                    value.append(valueN.take(terminal).unescapeBackslashEscapes(quote))
                    consumer.onStatementTemplate(value.toString())
                    consumer.onStatementFinish()
                    continue@mainLoop
                }
            }
        }

        // if the value does not start with `quote`,
        // everything after the first `#` is a comment
        // + the `#` should not be included in the value.

        var terminal = value0.indexOf('#')
        var offset = 0

        if (terminal == -1) {
            terminal = value0.length
        }

        // shortcut for trimming instead of creating a new string
        for (i in terminal - 1 downTo 0) {
            if (!value0[i].isWhitespace()) {
                terminal = i + 1
                break
            }
        }
        for (i in 0..<terminal) {
            if (!value0[i].isWhitespace()) {
                offset = i
                break
            }
        }

        consumer.onStatementTemplate(value0.substring(offset, terminal))
        consumer.onStatementFinish()
    }
}
