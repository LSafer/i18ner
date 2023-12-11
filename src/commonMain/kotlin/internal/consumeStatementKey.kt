package net.lsafer.i18ner.internal

private val TAG_OPEN_CHARS = listOf('#')
private val METADATA_OPEN_CLOSE_CHARS = mapOf('[' to ']', '{' to '}', '(' to ')', '<' to '>')
private val METADATA_SEPARATOR_CHARS = charArrayOf(',', '&')
private val METADATA_ASSIGNMENT_CHARS = charArrayOf('-', '|', '=', ':', ' ')

internal interface StatementKeyOnetimeConsumer {
    fun onStatementKeyName(name: String)

    fun onStatementKeyTag(tag: String)

    fun onStatementKeyMetadata(metadata: Map<String, String?>)

    fun onStatementKeyUnclosedMetadataObject()
}

internal fun String.consumeStatementKey(consumer: StatementKeyOnetimeConsumer) {
    fun consumeName(offset: Int, terminal: Int) {
        val name = substring(offset, terminal).trim()
        consumer.onStatementKeyName(name)
    }

    fun consumeTag(offset: Int, terminal: Int) {
        val name = substring(offset + 1, terminal).trim()
        consumer.onStatementKeyTag(name)
    }

    fun consumeMetadata(offset: Int, terminal: Int) {
        val metadata = substring(offset + 1, terminal)
            .splitToSequence(*METADATA_SEPARATOR_CHARS)
            .filter { it.isNotBlank() }
            .map { it.trim().split(*METADATA_ASSIGNMENT_CHARS, limit = 2) }
            .associate { splits ->
                val k = splits[0]
                val v = splits.getOrNull(1)
                k.trim() to v?.trim()
            }

        consumer.onStatementKeyMetadata(metadata)
    }

    var nameConsumed = false
    var tagOffset = -1
    var tagConsumed = false
    var metadataOffset = -1
    var metadataCloseChar = '\u0000'
    var metadataConsumed = false

    for ((i, char) in this.withIndex()) {
        when {
            metadataOffset != -1 && !metadataConsumed -> {
                if (char == metadataCloseChar) {
                    metadataConsumed = true
                    consumeMetadata(metadataOffset, i)
                }
            }

            tagOffset == -1 && char in TAG_OPEN_CHARS -> {
                tagOffset = i

                if (!nameConsumed) {
                    nameConsumed = true
                    consumeName(0, i)
                }
            }

            metadataOffset == -1 && char in METADATA_OPEN_CLOSE_CHARS -> {
                metadataOffset = i
                metadataCloseChar = METADATA_OPEN_CLOSE_CHARS[char]!!

                if (!nameConsumed) {
                    nameConsumed = true
                    consumeName(0, i)
                }
                if (!tagConsumed && tagOffset != -1) {
                    tagConsumed = true
                    consumeTag(tagOffset, i)
                }
            }
        }
    }

    if (!nameConsumed)
        consumeName(0, length)
    if (!tagConsumed && tagOffset != -1)
        consumeTag(tagOffset, length)
    if (!metadataConsumed && metadataOffset != -1)
        consumer.onStatementKeyUnclosedMetadataObject()
}
