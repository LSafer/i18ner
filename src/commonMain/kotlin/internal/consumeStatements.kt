package net.lsafer.i18ner.internal

import net.lsafer.i18ner.TranslationGender

internal interface StatementConsumer {
    fun onStatementStart(lineNumber: Int, line: String)

    fun onStatementKey(key: String)

    fun onStatementValue(value: String)

    fun onStatementFinish()

    fun onStatementIgnore()

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
            consumer.onStatementValue("")
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
                    consumer.onStatementValue(value.toString())
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

        consumer.onStatementValue(value0.substring(offset, terminal))
        consumer.onStatementFinish()
    }
}

data class ConsumedStatement(
    val key: String,
    val keyName: String,
    val keyTag: String?,
    val keyMetadata: Map<String, String?>?,
    val keyMetadataGender: TranslationGender?,
    val keyMetadataCountRange: LongRange?,
    val keyMetadataAttributes: Map<String, String>?,
    val value: String,
)

internal fun createStatementConsumer(
    filename: String?,
    strict: Boolean,
    onStatement: (ConsumedStatement) -> Unit,
): StatementConsumer {
    return object : StatementConsumer, StatementKeyConsumer, StatementKeyMetadataConsumer {
        var currentLine = ""
        var currentLineNumber = -1
        var currentValue = ""

        var currentKey = ""
        var currentKeyName = ""
        var currentKeyTag: String? = null

        var currentKeyMetadata: Map<String, String?>? = null
        var currentKeyMetadataGender: TranslationGender? = null
        var currentKeyMetadataCountRange: LongRange? = null
        var currentKeyMetadataAttributes: MutableMap<String, String> = mutableMapOf()

        override fun onStatementStart(lineNumber: Int, line: String) {
            currentLine = line
            currentLineNumber = lineNumber
        }

        override fun onStatementKey(key: String) {
            currentKey = key
            key.consumeStatementKey(this)
        }

        override fun onStatementKeyName(name: String) {
            currentKeyName = name
        }

        override fun onStatementKeyTag(tag: String) {
            currentKeyTag = tag
        }

        override fun onStatementKeyMetadata(metadata: Map<String, String?>) {
            currentKeyMetadata = metadata
            metadata.consumeStatementKeyMetadata(this)
        }

        override fun onStatementKeyMetadataGender(gender: TranslationGender) {
            currentKeyMetadataGender = gender
        }

        override fun onStatementKeyMetadataCountRange(count: LongRange) {
            currentKeyMetadataCountRange = count
        }

        override fun onStatementKeyMetadataAttribute(name: String, value: String) {
            currentKeyMetadataAttributes[name] = value
        }

        override fun onStatementValue(value: String) {
            currentValue = value
        }

        override fun onStatementFinish() {
            val statement = ConsumedStatement(
                key = currentKey,
                keyName = currentKeyName,
                keyTag = currentKeyTag,
                keyMetadata = currentKeyMetadata,
                keyMetadataGender = currentKeyMetadataGender,
                keyMetadataCountRange = currentKeyMetadataCountRange,
                keyMetadataAttributes = currentKeyMetadataAttributes,
                value = currentValue
            )

            onStatement(statement)
            onStatementIgnore()
        }

        override fun onStatementIgnore() {
            currentLine = ""
            currentLineNumber = -1
            currentValue = ""

            currentKey = ""
            currentKeyName = ""
            currentKeyTag = null

            currentKeyMetadata = null
            currentKeyMetadataGender = null
            currentKeyMetadataCountRange = null
            currentKeyMetadataAttributes = mutableMapOf()
        }

        override fun onStatementMissingAssignmentOperator() {
            if (!strict) return
            val message =
                "Missing `=` operator for key `$currentKeyName` on ${filename ?: "unknown"} at $currentLineNumber:\n$currentLine"
            throw IllegalArgumentException(message)
        }

        override fun onStatementUnclosedValueString() {
            if (!strict) return
            val message =
                "Unclosed value literal for key `$currentKeyName` on ${filename ?: "unknown"} at $currentLineNumber:\n$currentLine"
            throw IllegalArgumentException(message)
        }

        override fun onStatementKeyUnclosedMetadataObject() {
            if (!strict) return
            val message =
                "Unclosed metadata object for key `$currentKeyName` on ${filename ?: "unknown"} at $currentLineNumber:\n$currentLine"
            throw IllegalArgumentException(message)
        }

        override fun onStatementKeyMetadataInvalidModifier(name: String) {
            if (!strict) return
            val message =
                "Invalid modifier `$name` for key `$currentKeyName` on ${filename ?: "unknown"} at $currentLineNumber:\n$currentLine"
            throw IllegalArgumentException(message)
        }
    }
}
