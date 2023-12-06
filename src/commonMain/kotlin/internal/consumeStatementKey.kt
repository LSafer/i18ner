package net.lsafer.i18ner.internal

internal interface StatementKeyConsumer {
    fun onStatementKeyName(name: String)

    fun onStatementKeyTag(tag: String)

    fun onStatementKeyMetadata(metadata: Map<String, String?>)

    // errors

    fun onStatementKeyUnclosedMetadataObject()
}

internal fun String.consumeStatementKey(consumer: StatementKeyConsumer) {
    var nameConsumed = false
    var tagOffset = -1
    var tagConsumed = false
    var metadataOffset = -1
    var metadataConsumed = false

    for ((i, char) in this.withIndex()) {
        fun tryConsumeName() {
            if (!nameConsumed) {
                nameConsumed = true
                val name = substring(0, i).trim()
                consumer.onStatementKeyName(name)
            }
        }

        fun tryConsumeTag() {
            if (!tagConsumed && tagOffset != -1) {
                tagConsumed = true
                val tag = substring(tagOffset + 1, i).trim()
                consumer.onStatementKeyTag(tag)
            }
        }

        when {
            metadataOffset != -1 && !metadataConsumed -> {
                if (char == ']') {
                    metadataConsumed = true
                    val metadata = substring(metadataOffset + 1, i)
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
            }

            tagOffset == -1 && char == '#' -> {
                tagOffset = i
                tryConsumeName()
            }

            metadataOffset == -1 && char == '[' -> {
                metadataOffset = i
                tryConsumeName()
                tryConsumeTag()
            }
        }
    }

    if (metadataOffset != -1 != metadataConsumed)
        consumer.onStatementKeyUnclosedMetadataObject()
}
