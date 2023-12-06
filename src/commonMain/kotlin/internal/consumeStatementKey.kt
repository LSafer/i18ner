package net.lsafer.i18ner.internal

internal interface StatementKeyConsumer {
    fun onStatementKeyName(name: String)

    fun onStatementKeyTag(tag: String)

    fun onStatementKeyMetadata(metadata: Map<String, String?>)

    // errors

    fun onStatementKeyUnclosedMetadataObject()
}

internal fun String.consumeStatementKey(consumer: StatementKeyConsumer) {
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
            .splitToSequence(',')
            .filter { it.isNotBlank() }
            .map { it.split('-', limit = 2) }
            .associate { splits ->
                val k = splits[0].trim()
                val v = splits.getOrNull(1)
                k to v
            }

        consumer.onStatementKeyMetadata(metadata)
    }

    var nameConsumed = false
    var tagOffset = -1
    var tagConsumed = false
    var metadataOffset = -1
    var metadataConsumed = false

    for ((i, char) in this.withIndex()) {
        when {
            metadataOffset != -1 && !metadataConsumed -> {
                if (char == ']') {
                    metadataConsumed = true
                    consumeMetadata(metadataOffset, i)
                }
            }

            tagOffset == -1 && char == '#' -> {
                tagOffset = i

                if (!nameConsumed) {
                    nameConsumed = true
                    consumeName(0, i)
                }
            }

            metadataOffset == -1 && char == '[' -> {
                metadataOffset = i

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
