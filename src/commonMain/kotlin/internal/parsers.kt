package net.lsafer.i18ner.internal

import net.lsafer.i18ner.I18nerInternalApi
import net.lsafer.i18ner.TranslationGender

internal val CountRangeModifierPattern = Regex(
    pattern = "^(-?infinity|-?[_0-9]*)\\.\\.(-?infinity|-?[_0-9]*)$",
    option = RegexOption.IGNORE_CASE
)

internal fun LongRange.formatLongRangeString(): String {
    if (isEmpty()) return ""

    return buildString {
        when (first) {
            Long.MIN_VALUE -> append("-infinity")
            Long.MAX_VALUE -> append("infinity")
            else -> append(first)
        }

        if (first != last) {
            append("...")

            when (last) {
                Long.MIN_VALUE -> append("-infinity")
                Long.MAX_VALUE -> append("infinity")
                else -> append(last)
            }
        }
    }
}

@I18nerInternalApi
fun String.parseLongRangeString(): LongRange? {
    val numberMatch = toLongOrNull()

    if (numberMatch != null) {
        return numberMatch..numberMatch
    }

    val rangeMatch = CountRangeModifierPattern.matchEntire(this)

    if (rangeMatch != null) {
        val (_, startSource, endSource) = rangeMatch.groupValues
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
        return start..end
    }

    return null
}

@I18nerInternalApi
fun String.parseGenderModifierOrNull(): TranslationGender? {
    return when (lowercase()) {
        "male", "m" -> TranslationGender.MALE

        "female", "f" -> TranslationGender.FEMALE

        else -> null
    }
}
