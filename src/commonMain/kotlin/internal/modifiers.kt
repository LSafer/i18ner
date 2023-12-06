package net.lsafer.i18ner.internal

import net.lsafer.i18ner.TranslationGender

internal fun extractGenderModifier(modifiers: List<String>): TranslationGender? {
    for (modifier in modifiers) {
        when (modifier.lowercase()) {
            "male", "m" -> return TranslationGender.MALE
            "female", "f" -> return TranslationGender.FEMALE
        }
    }
    return null
}

private val CountModifierPattern = Regex("^([0-9]+)\\.\\.([0-9]+)$")

internal fun extractCountModifier(modifiers: List<String>): IntRange? {
    for (modifier in modifiers) {
        val match = CountModifierPattern.matchEntire(modifier) ?: continue

        val (_, start, end) = match.groupValues

        return start.toInt()..end.toInt()
    }

    return null
}
