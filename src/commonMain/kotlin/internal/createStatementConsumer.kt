package net.lsafer.i18ner.internal

import net.lsafer.i18ner.TranslationGender

internal fun createStatementConsumer(
    filename: String?,
    strict: Boolean,
    onStatement: (ParsedStatement) -> Unit,
): StatementConsumer {
    return object : StatementConsumer, StatementKeyOnetimeConsumer, StatementKeyMetadataOnetimeConsumer {
        var currentLine = ""
        var currentLineNumber = -1
        var currentTemplate = ""

        var currentKey = ""
        var currentName = ""
        var currentLanguage: String? = null

        var currentGenderModifier: TranslationGender? = null
        var currentCountModifier: LongRange? = null
        var currentAttributes: MutableMap<String, String> = mutableMapOf()

        // StatementConsumer

        override fun onStatementStart(lineNumber: Int, line: String) {
            currentLine = line
            currentLineNumber = lineNumber
        }

        override fun onStatementFinish() {
            val statement = ParsedStatement(
                key = currentKey,
                name = currentName,
                language = currentLanguage,
                genderModifier = currentGenderModifier,
                countModifier = currentCountModifier,
                attributes = currentAttributes,
                template = currentTemplate
            )

            onStatement(statement)
            onStatementIgnore()
        }

        override fun onStatementIgnore() {
            currentLine = ""
            currentLineNumber = -1
            currentTemplate = ""

            currentKey = ""
            currentName = ""
            currentLanguage = null

            currentGenderModifier = null
            currentCountModifier = null
            currentAttributes = mutableMapOf()
        }

        override fun onStatementKey(key: String) {
            currentKey = key
            key.consumeStatementKey(this)
        }

        override fun onStatementTemplate(template: String) {
            currentTemplate = template
        }

        override fun onStatementMissingAssignmentOperator() {
            if (!strict) return
            val message =
                "Missing `=` operator for key `$currentName` on ${filename ?: "unknown"} at $currentLineNumber:\n$currentLine"
            throw IllegalArgumentException(message)
        }

        override fun onStatementUnclosedValueString() {
            if (!strict) return
            val message =
                "Unclosed value literal for key `$currentName` on ${filename ?: "unknown"} at $currentLineNumber:\n$currentLine"
            throw IllegalArgumentException(message)
        }

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
                "Unclosed metadata object for key `$currentName` on ${filename ?: "unknown"} at $currentLineNumber:\n$currentLine"
            throw IllegalArgumentException(message)
        }

        // StatementKeyMetadataConsumer

        override fun onStatementKeyMetadataGenderModifier(gender: TranslationGender) {
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
                "Invalid modifier `$name` for key `$currentName` on ${filename ?: "unknown"} at $currentLineNumber:\n$currentLine"
            throw IllegalArgumentException(message)
        }
    }
}
