package net.lsafer.i18ner.yaml

import net.lsafer.i18ner.*
import net.lsafer.i18ner.internal.parseGenderModifierOrNull
import net.lsafer.i18ner.internal.parseLongRangeString
import net.mamoe.yamlkt.*

/**
 * @param source the raw yaml source.
 * @param strict true, to enable stricter parsing; Some issues might not be reported.
 * @param filename optional; Helps to track issues.
 */
data class YamlMessageDefinitionsOptions(
    var source: String = "",
    var strict: Boolean = false,
    var filename: String? = null,
)

fun createYamlMessageDefinitions(
    source: String,
    block: YamlMessageDefinitionsOptions.() -> Unit = {},
): Map<String, MessageDefinition> {
    val options = YamlMessageDefinitionsOptions()
    options.source = source
    options.apply(block)
    return createYamlMessageDefinitions(options)
}

fun createYamlMessageDefinitions(
    options: YamlMessageDefinitionsOptions,
): Map<String, MessageDefinition> {
    val input = Yaml.decodeYamlMapFromString(options.source)

    return buildMap {
        for (entry in input) {
            val key = parseKeyOrNull(
                keyElement = entry.key,
                filename = options.filename,
                strict = options.strict
            ) ?: continue
            val value = parseValueOrNull(
                valueElement = entry.value,
                key = key,
                filename = options.filename,
                strict = options.strict
            ) ?: continue

            put(key, value)
        }
    }
}

private fun parseKeyOrNull(
    keyElement: YamlElement,
    filename: String?,
    strict: Boolean,
): String? {
    if (keyElement is YamlPrimitive)
        return keyElement.content.orEmpty()

    if (!strict)
        return null

    throw IllegalArgumentException("invalid key `$keyElement` at ${filename ?: "unknown"}")
}

private fun parseValueOrNull(
    valueElement: YamlElement,
    key: String,
    filename: String?,
    strict: Boolean,
): MessageDefinition? {
    if (valueElement is YamlPrimitive) {
        val valueString = valueElement.content.orEmpty()

        return MessageStringDefinition(valueString)
    }

    if (valueElement is YamlMap) {
        return MessageObjectDefinition(
            language = extractLanguage(
                metadata = valueElement,
                key = key,
                filename = filename,
                strict = strict
            ),
            gender = extractGenderModifier(
                metadata = valueElement,
                key = key,
                filename = filename,
                strict = strict
            ),
            count = extractCountModifier(
                metadata = valueElement,
                key = key,
                filename = filename,
                strict = strict
            ),
            attributes = extractAttributes(
                metadata = valueElement,
                key = key,
                filename = filename,
                strict = strict
            ),
            template = extractTemplate(
                metadata = valueElement,
                key = key,
                filename = filename,
                strict = strict
            )
        )
    }

    if (!strict)
        return null

    throw IllegalArgumentException("invalid value for `$key` on ${filename ?: "unknown"}")
}

private fun extractLanguage(
    metadata: YamlMap,
    key: String,
    filename: String?,
    strict: Boolean,
): String? {
    val value = metadata["language"] ?: return null

    if (value is YamlPrimitive)
        return value.content.orEmpty().trim()

    if (!strict)
        return null

    throw IllegalArgumentException("invalid language field for $key at ${filename ?: "unknown"}")
}

private fun extractAttributes(
    metadata: YamlMap,
    key: String,
    filename: String?,
    strict: Boolean,
): Map<String, String>? {
    val value = metadata["attributes"] ?: return null

    if (value is YamlMap) {
        return value.entries.associate {
            val k = it.key.literalContentOrNull.orEmpty()
            val v = it.value.literalContentOrNull.orEmpty()

            k to v
        }
    }

    if (!strict)
        return null

    throw IllegalArgumentException("invalid attributes element for $key at ${filename ?: "unknown"}")
}

private fun extractGenderModifier(
    metadata: YamlMap,
    key: String,
    filename: String?,
    strict: Boolean,
): TranslationGender? {
    val value = metadata["gender"] ?: return null

    if (value is YamlLiteral) {
        @OptIn(I18nerInternalApi::class)
        val genderModifier = value.content.trim().parseGenderModifierOrNull()

        if (genderModifier != null)
            return genderModifier
    }

    if (!strict)
        return null

    throw IllegalArgumentException("invalid gender modifier `$value` for $key at ${filename ?: "unknown"}")
}

private fun extractCountModifier(
    metadata: YamlMap,
    key: String,
    filename: String?,
    strict: Boolean,
): LongRange? {
    val value = metadata["count"] ?: return null

    if (value is YamlLiteral) {
        @OptIn(I18nerInternalApi::class)
        val countModifier = value.content.trim().parseLongRangeString()

        if (countModifier != null)
            return countModifier
    }

    if (!strict)
        return null

    throw IllegalArgumentException("invalid count modifier `$value` for $key at ${filename ?: "unknown"}")
}

private fun extractTemplate(
    metadata: YamlMap,
    key: String,
    filename: String?,
    strict: Boolean,
): String? {
    val value = metadata["template"] ?: return null

    if (value is YamlPrimitive) {
        return value.literalContentOrNull.orEmpty()
    }

    if (!strict)
        return null

    throw IllegalArgumentException("invalid template for $key at ${filename ?: "unknown"}")
}
