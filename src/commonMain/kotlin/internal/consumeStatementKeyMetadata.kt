package net.lsafer.i18ner.internal

import net.lsafer.i18ner.I18nerInternalApi
import net.lsafer.i18ner.TranslationGender

internal interface StatementKeyMetadataOnetimeConsumer {
    fun onStatementKeyMetadataGenderModifier(gender: TranslationGender)

    fun onStatementKeyMetadataCountModifier(count: LongRange)

    fun onStatementKeyMetadataAttribute(name: String, value: String)

    fun onStatementKeyMetadataInvalidModifier(name: String)
}

@OptIn(I18nerInternalApi::class)
internal fun Map<String, String?>.consumeStatementKeyMetadata(consumer: StatementKeyMetadataOnetimeConsumer) {
    for ((name, value) in this) {
        if (value != null) {
            consumer.onStatementKeyMetadataAttribute(name, value)
            continue
        }

        val genderModifier = name.parseGenderModifierOrNull()

        if (genderModifier != null)
            consumer.onStatementKeyMetadataGenderModifier(genderModifier)

        val countModifier = name.parseLongRangeString()

        if (countModifier != null) {
            consumer.onStatementKeyMetadataCountModifier(countModifier)
            continue
        }

        consumer.onStatementKeyMetadataInvalidModifier(name)
    }
}
