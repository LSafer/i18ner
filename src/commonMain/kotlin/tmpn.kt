/*
 *	Copyright 2023 cufy.org
 *
 *	Licensed under the Apache License, Version 2.0 (the "License");
 *	you may not use this file except in compliance with the License.
 *	You may obtain a copy of the License at
 *
 *	    http://www.apache.org/licenses/LICENSE-2.0
 *
 *	Unless required by applicable law or agreed to in writing, software
 *	distributed under the License is distributed on an "AS IS" BASIS,
 *	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *	See the License for the specific language governing permissions and
 *	limitations under the License.
 */
package net.lsafer.i18ner

// I18ner.tmpn(map, specifier)

/**
 * Return the translation for the given [specifier]
 * from the given [map] with the given arguments.
 */
fun <T> I18ner.tmpn(
    map: Map<String, T>,
    specifier: LocalizationSpecifier,
): Pair<String, T>? {
    val choices = mutableMapOf<String?, T>()

    for ((itk, itv) in map) {
        val itkSplits = itk.split("#", limit = 2)
        val itn = itkSplits.first()
        val itt = itkSplits.getOrNull(1)

        if (itn != specifier.name)
            continue

        choices[itt] = itv
    }

    if (choices.isEmpty())
        return null

    val choicesLanguages = choices.keys.filterNotNullTo(mutableSetOf())

    var choice = languageResolution(
        ranges = specifier.languages,
        languages = choicesLanguages
    )

    if (choice == null) {
        choice = languageResolution(
            ranges = defaultLanguages,
            languages = choicesLanguages
        )
    }

    val chosen = choices.entries.firstOrNull { it.key == choice }
        ?: choices.entries.first()

    return chosen.key.orEmpty() to chosen.value
}

/**
 * Return the translation for the given [specifier]
 * from the given [map] with the given arguments.
 */
fun <T> tmpn(
    source: Map<String, T>,
    specifier: LocalizationSpecifier,
): Pair<String, T>? {
    return I18ner.tmpn(source, specifier)
}

// I18ner.tmpn(map, name, languages[], block)

/**
 * Return the translation for the given [name]
 * from the given [map] with the given arguments.
 */
fun <T> I18ner.tmpn(
    source: Map<String, T>,
    name: String,
    languages: List<String>,
    block: (LocalizationSpecifier).() -> Unit = {},
): Pair<String, T>? {
    val specifier = LocalizationSpecifier(
        name = name,
        languages = languages.toMutableList(),
    )

    specifier.apply(block)

    return tmpn(source, specifier)
}

/**
 * Return the translation for the given [name]
 * from the given [map] with the given arguments.
 */
fun <T> tmpn(
    source: Map<String, T>,
    name: String,
    languages: List<String>,
    block: (LocalizationSpecifier).() -> Unit = {},
): Pair<String, T>? {
    return I18ner.tmpn(source, name, languages, block)
}

// I18ner.tmpn(map, name, ...languages[], block)

/**
 * Return the translation for the given [name]
 * from the given [map] with the given arguments.
 */
fun <T> I18ner.tmpn(
    source: Map<String, T>,
    name: String,
    vararg languages: String,
    block: (LocalizationSpecifier).() -> Unit = {},
): Pair<String, T>? {
    return tmpn(source, name, languages.asList(), block)
}

/**
 * Return the translation for the given [name]
 * from the given [map] with the given arguments.
 */
fun <T> tmpn(
    source: Map<String, T>,
    name: String,
    vararg languages: String,
    block: (LocalizationSpecifier).() -> Unit = {},
): Pair<String, T>? {
    return I18ner.tmpn(source, name, *languages, block = block)
}
