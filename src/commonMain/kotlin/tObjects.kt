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

// I18ner.tObjects(specifier, parameters{}, default)

/**
 * Return the translation for the given [specifier]
 * with the given arguments.
 */
fun I18ner.tObjects(
    specifier: TranslationSpecifier,
    parameters: Map<String, Any?>,
    default: List<Any?> = listOf(specifier.name),
): List<Any?> {
    return tnObjects(specifier, parameters) ?: default
}

/**
 * Return the translation for the given [specifier]
 * with the given arguments.
 */
fun tObjects(
    specifier: TranslationSpecifier,
    parameters: Map<String, Any?>,
    default: List<Any?> = listOf(specifier.name),
): List<Any?> {
    return tnObjects(specifier, parameters) ?: default
}

// I18ner.tObjects(name, parameters{}, default, block)

/**
 * Return the translation for the given [name]
 * with the given arguments.
 */
fun I18ner.tObjects(
    name: String,
    parameters: Map<String, Any?>,
    default: List<Any?> = listOf(name),
    block: (TranslationSpecifier).() -> Unit = {},
): List<Any?> {
    return tnObjects(name, parameters, block) ?: default
}

/**
 * Return the translation for the given [name]
 * with the given arguments.
 */
fun tObjects(
    name: String,
    parameters: Map<String, Any?>,
    default: List<Any?> = listOf(name),
    block: (TranslationSpecifier).() -> Unit = {},
): List<Any?> {
    return tnObjects(name, parameters, block) ?: default
}

// I18ner.tObjects(name, parameters[], default, block)

/**
 * Return the translation for the given [name]
 * with the given arguments.
 */
fun I18ner.tObjects(
    name: String,
    parameters: List<Any?>,
    default: List<Any?> = listOf(name),
    block: (TranslationSpecifier).() -> Unit = {},
): List<Any?> {
    return tnObjects(name, parameters, block) ?: default
}

/**
 * Return the translation for the given [name]
 * with the given arguments.
 */
fun tObjects(
    name: String,
    parameters: List<Any?>,
    default: List<Any?> = listOf(name),
    block: (TranslationSpecifier).() -> Unit = {},
): List<Any?> {
    return tnObjects(name, parameters, block) ?: default
}

// I18ner.tObjects(name,, default, block)

/**
 * Return the translation for the given [name]
 * with the given arguments.
 */
fun I18ner.tObjects(
    name: String,
    @Suppress("UNUSED_PARAMETER")
    dummy: Unit = Unit,
    default: List<Any?> = listOf(name),
    block: (TranslationSpecifier).() -> Unit = {},
): List<Any?> {
    return tnObjects(name, block) ?: default
}

/**
 * Return the translation for the given [name]
 * with the given arguments.
 */
fun tObjects(
    name: String,
    @Suppress("UNUSED_PARAMETER")
    dummy: Unit = Unit,
    default: List<Any?> = listOf(name),
    block: (TranslationSpecifier).() -> Unit = {},
): List<Any?> {
    return tnObjects(name, block) ?: default
}

// I18ner.tObjects(name, ...parameters{}, default, block)

/**
 * Return the translation for the given [name]
 * with the given arguments.
 */
fun I18ner.tObjects(
    name: String,
    vararg parameters: Pair<String, Any?>,
    default: List<Any?> = listOf(name),
    block: (TranslationSpecifier).() -> Unit = {},
): List<Any?> {
    return tnObjects(name, *parameters, block = block) ?: default
}

/**
 * Return the translation for the given [name]
 * with the given arguments.
 */
fun tObjects(
    name: String,
    vararg parameters: Pair<String, Any?>,
    default: List<Any?> = listOf(name),
    block: (TranslationSpecifier).() -> Unit = {},
): List<Any?> {
    return tnObjects(name, *parameters, block = block) ?: default
}

// I18ner.tObjects(name, ...parameters[], default, block)

/**
 * Return the translation for the given [name]
 * with the given arguments.
 */
fun I18ner.tObjects(
    name: String,
    vararg parameters: Any?,
    default: List<Any?> = listOf(name),
    block: (TranslationSpecifier).() -> Unit = {},
): List<Any?> {
    return tnObjects(name, *parameters, block = block) ?: default
}

/**
 * Return the translation for the given [name]
 * with the given arguments.
 */
fun tObjects(
    name: String,
    vararg parameters: Any?,
    default: List<Any?> = listOf(name),
    block: (TranslationSpecifier).() -> Unit = {},
): List<Any?> {
    return tnObjects(name, *parameters, block = block) ?: default
}
