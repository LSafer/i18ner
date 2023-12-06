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

/**
 * Return the translation for the given [specifier]
 * with the given arguments.
 */
fun I18ner.t(
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
fun t(
    specifier: TranslationSpecifier,
    parameters: Map<String, Any?>,
) = I18ner.t(specifier, parameters)

/**
 * Return the translation for the given [name]
 * with the given arguments.
 */
fun I18ner.t(
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

    return t(specifier, parameters)
}

/**
 * Return the translation for the given [name]
 * with the given arguments.
 */
fun t(
    name: String,
    parameters: Map<String, Any?>,
    block: (MutableTranslationSpecifier).() -> Unit = {},
) = I18ner.t(name, parameters, block)

/**
 * Return the translation for the given [name]
 * with the given arguments.
 */
fun I18ner.t(
    name: String,
    block: (MutableTranslationSpecifier).() -> Unit = {},
): String? {
    return t(name, emptyMap(), block)
}

/**
 * Return the translation for the given [name]
 * with the given arguments.
 */
fun t(
    name: String,
    block: (MutableTranslationSpecifier).() -> Unit = {},
) = I18ner.t(name, block)

/**
 * Return the translation for the given [name]
 * with the given arguments.
 */
fun I18ner.t(
    name: String,
    parameters: List<Any?>,
    block: (MutableTranslationSpecifier).() -> Unit = {},
): String? {
    val parametersMap = parameters
        .asSequence()
        .mapIndexed { i, v -> "$i" to v }
        .toMap()

    return t(name, parametersMap, block)
}

/**
 * Return the translation for the given [name]
 * with the given arguments.
 */
fun t(
    name: String,
    parameters: List<Any?>,
    block: (MutableTranslationSpecifier).() -> Unit = {},
) = I18ner.t(name, parameters, block)

/**
 * Return the translation for the given [name]
 * with the given arguments.
 */
fun I18ner.t(
    name: String,
    vararg parameters: Pair<String, Any?>,
    block: (MutableTranslationSpecifier).() -> Unit = {},
): String? {
    return t(name, parameters.toMap(), block)
}

/**
 * Return the translation for the given [name]
 * with the given arguments.
 */
fun t(
    name: String,
    vararg parameters: Pair<String, Any?>,
    block: (MutableTranslationSpecifier).() -> Unit = {},
) = I18ner.t(name, *parameters, block = block)

/**
 * Return the translation for the given [name]
 * with the given arguments.
 */
fun I18ner.t(
    name: String,
    vararg parameters: Any?,
    block: (MutableTranslationSpecifier).() -> Unit = {},
): String? {
    return t(name, parameters.asList(), block)
}

/**
 * Return the translation for the given [name]
 * with the given arguments.
 */
fun t(
    name: String,
    vararg parameters: Any?,
    block: (MutableTranslationSpecifier).() -> Unit = {},
) = I18ner.t(name, *parameters, block = block)
