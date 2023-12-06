package net.lsafer.i18ner.internal

import net.lsafer.i18ner.TranslationGender

private val CountModifierRangePattern = Regex("^(-?infinity|-?[_0-9]*)\\.\\.(-?infinity|-?[_0-9]*)$")

interface StatementKeyMetadataConsumer {
    fun onStatementKeyMetadataGender(gender: TranslationGender)

    fun onStatementKeyMetadataCountRange(count: LongRange)

    fun onStatementKeyMetadataAttribute(name: String, value: String)

    fun onStatementKeyMetadataInvalidModifier(name: String)
}

fun Map<String, String?>.consumeStatementKeyMetadata(consumer: StatementKeyMetadataConsumer) {
    for ((name, value) in this) {
        if (value != null) {
            consumer.onStatementKeyMetadataAttribute(name, value)
            continue
        }

        val nameLowercase = name.lowercase()

        when (nameLowercase) {
            "male", "m" -> {
                consumer.onStatementKeyMetadataGender(TranslationGender.MALE)
                continue
            }

            "female", "f" -> {
                consumer.onStatementKeyMetadataGender(TranslationGender.FEMALE)
                continue
            }
        }

        val nameInt = name.toLongOrNull()

        if (nameInt != null) {
            val range = nameInt..nameInt
            consumer.onStatementKeyMetadataCountRange(range)
            continue
        }

        val nameRangeMatch = CountModifierRangePattern.matchEntire(nameLowercase)

        if (nameRangeMatch != null) {
            val (_, startSource, endSource) = nameRangeMatch.groupValues
            val start = when (startSource.lowercase()) {
                "infinity" -> Long.MAX_VALUE
                "-infinity" -> Long.MIN_VALUE
                else -> startSource.replace("_", "").toLong()
            }
            val end = when (endSource.lowercase()) {
                "infinity" -> Long.MAX_VALUE
                "-infinity" -> Long.MIN_VALUE
                else -> endSource.replace("_", "").toLong()
            }
            val range = start..end
            consumer.onStatementKeyMetadataCountRange(range)
            continue
        }

        consumer.onStatementKeyMetadataInvalidModifier(name)
    }
}
