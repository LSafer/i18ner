package net.lsafer.i18ner.internal

import net.lsafer.i18ner.TranslationGender

internal data class ParsedStatementKey(
    val name: String,
    val language: String?,
    val genderModifier: TranslationGender?,
    val countModifier: LongRange?,
    val attributes: Map<String, String>?,
)

internal data class ParsedStatement(
    val key: String,
    val name: String,
    val language: String?,
    val genderModifier: TranslationGender?,
    val countModifier: LongRange?,
    val attributes: Map<String, String>?,
    val template: String,
)
