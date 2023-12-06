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

// I18ner.tn(TranslationSpecifier, Map)

/**
 * Return the translation for the given [specifier]
 * with the given arguments.
 */
fun I18ner.tn(
    specifier: TranslationSpecifier,
    parameters: Map<String, Any?>,
): String? {
    val message = this[specifier] ?: return null

    return message.template(parameters)
}

/**
 * Return the translation for the given [specifier]
 * with the given arguments.
 */
fun tn(
    specifier: TranslationSpecifier,
    parameters: Map<String, Any?>,
): String? {
    return I18ner.tn(specifier, parameters)
}

// I18ner.tn(String, Map, Function)

/**
 * Return the translation for the given [name]
 * with the given arguments.
 */
fun I18ner.tn(
    name: String,
    parameters: Map<String, Any?>,
    block: (MutableTranslationSpecifier).() -> Unit = {},
): String? {
    val builder = MutableTranslationSpecifier(
        name = name,
        languages = mutableListOf(),
        count = null,
        gender = null,
        attributes = mutableMapOf()
    )

    builder.apply(block)

    val specifier = TranslationSpecifier(
        name = builder.name,
        languages = builder.languages,
        count = builder.count,
        gender = builder.gender,
        attributes = builder.attributes
    )

    return tn(specifier, parameters)
}

/**
 * Return the translation for the given [name]
 * with the given arguments.
 */
fun tn(
    name: String,
    parameters: Map<String, Any?>,
    block: (MutableTranslationSpecifier).() -> Unit = {},
): String? {
    return I18ner.tn(name, parameters, block)
}

// I18ner.tn(String, Function)

/**
 * Return the translation for the given [name]
 * with the given arguments.
 */
fun I18ner.tn(
    name: String,
    block: (MutableTranslationSpecifier).() -> Unit = {},
): String? {
    return tn(name, emptyMap(), block)
}

/**
 * Return the translation for the given [name]
 * with the given arguments.
 */
fun tn(
    name: String,
    block: (MutableTranslationSpecifier).() -> Unit = {},
): String? {
    return I18ner.tn(name, block)
}

// I18ner.tn(String, List, Function)

/**
 * Return the translation for the given [name]
 * with the given arguments.
 */
fun I18ner.tn(
    name: String,
    parameters: List<Any?>,
    block: (MutableTranslationSpecifier).() -> Unit = {},
): String? {
    val parametersMap = parameters
        .asSequence()
        .mapIndexed { i, v -> "$i" to v }
        .toMap()

    return tn(name, parametersMap, block)
}

/**
 * Return the translation for the given [name]
 * with the given arguments.
 */
fun tn(
    name: String,
    parameters: List<Any?>,
    block: (MutableTranslationSpecifier).() -> Unit = {},
): String? {
    return I18ner.tn(name, parameters, block)
}

// I18ner.tn(String, ...Pair, Function)

/**
 * Return the translation for the given [name]
 * with the given arguments.
 */
fun I18ner.tn(
    name: String,
    vararg parameters: Pair<String, Any?>,
    block: (MutableTranslationSpecifier).() -> Unit = {},
): String? {
    return tn(name, parameters.toMap(), block)
}

/**
 * Return the translation for the given [name]
 * with the given arguments.
 */
fun tn(
    name: String,
    vararg parameters: Pair<String, Any?>,
    block: (MutableTranslationSpecifier).() -> Unit = {},
): String? {
    return I18ner.tn(name, *parameters, block = block)
}

// I18ner.tn(String, ...Any?, Function)

/**
 * Return the translation for the given [name]
 * with the given arguments.
 */
fun I18ner.tn(
    name: String,
    vararg parameters: Any?,
    block: (MutableTranslationSpecifier).() -> Unit = {},
): String? {
    return tn(name, parameters.asList(), block)
}

/**
 * Return the translation for the given [name]
 * with the given arguments.
 */
fun tn(
    name: String,
    vararg parameters: Any?,
    block: (MutableTranslationSpecifier).() -> Unit = {},
): String? {
    return I18ner.tn(name, *parameters, block = block)
}
