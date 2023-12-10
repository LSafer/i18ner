package net.lsafer.i18ner.internal

import net.lsafer.i18ner.Gender

internal fun String.parseStatementKey(
    filename: String?,
    line: String?,
    lineNumber: Int?,
    strict: Boolean,
): ParsedStatementKey {
    var currentName = ""
    var currentLanguage: String? = null

    var currentGenderModifier: Gender? = null
    var currentCountModifier: LongRange? = null
    val currentAttributes: MutableMap<String, String> = mutableMapOf()

    consumeStatementKey(object : StatementKeyOnetimeConsumer, StatementKeyMetadataOnetimeConsumer {
        // StatementKeyConsumer

        override fun onStatementKeyName(name: String) {
            currentName = name
        }

        override fun onStatementKeyTag(tag: String) {
            currentLanguage = tag
        }

        override fun onStatementKeyMetadata(metadata: Map<String, String?>) {
            metadata.consumeStatementKeyMetadata(this)
        }

        override fun onStatementKeyUnclosedMetadataObject() {
            if (!strict) return
            val message =
                "Unclosed metadata object for key `$currentName` on ${filename ?: "unknown"} at $lineNumber:\n$line"
            throw IllegalArgumentException(message)
        }

        // StatementKeyMetadataConsumer

        override fun onStatementKeyMetadataGenderModifier(gender: Gender) {
            currentGenderModifier = gender
        }

        override fun onStatementKeyMetadataCountModifier(count: LongRange) {
            currentCountModifier = count
        }

        override fun onStatementKeyMetadataAttribute(name: String, value: String) {
            currentAttributes[name] = value
        }

        override fun onStatementKeyMetadataInvalidModifier(name: String) {
            if (!strict) return
            val message =
                "Invalid modifier `$name` for key `$currentName` on ${filename ?: "unknown"} at $lineNumber:\n$line"
            throw IllegalArgumentException(message)
        }
    })

    return ParsedStatementKey(
        name = currentName,
        language = currentLanguage,
        genderModifier = currentGenderModifier,
        countModifier = currentCountModifier,
        attributes = currentAttributes
    )
}
