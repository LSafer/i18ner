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

// I18ner.t(specifier, parameters{}, default)

/**
 * Return the translation for the given [specifier]
 * with the given arguments.
 */
fun I18ner.t(
    specifier: TranslationSpecifier,
    parameters: Map<String, Any?>,
    default: String = specifier.name,
): String {
    return tn(specifier, parameters) ?: default
}

/**
 * Return the translation for the given [specifier]
 * with the given arguments.
 */
fun t(
    specifier: TranslationSpecifier,
    parameters: Map<String, Any?>,
    default: String = specifier.name,
): String {
    return tn(specifier, parameters) ?: default
}

// I18ner.t(name, parameters{}, default, block)

/**
 * Return the translation for the given [name]
 * with the given arguments.
 */
fun I18ner.t(
    name: String,
    parameters: Map<String, Any?>,
    default: String = name,
    block: (MutableTranslationSpecifier).() -> Unit = {},
): String {
    return tn(name, parameters, block) ?: default
}

/**
 * Return the translation for the given [name]
 * with the given arguments.
 */
fun t(
    name: String,
    parameters: Map<String, Any?>,
    default: String = name,
    block: (MutableTranslationSpecifier).() -> Unit = {},
): String {
    return tn(name, parameters, block) ?: default
}

// I18ner.t(name, parameters[], default, block)

/**
 * Return the translation for the given [name]
 * with the given arguments.
 */
fun I18ner.t(
    name: String,
    parameters: List<Any?>,
    default: String = name,
    block: (MutableTranslationSpecifier).() -> Unit = {},
): String {
    return tn(name, parameters, block) ?: default
}

/**
 * Return the translation for the given [name]
 * with the given arguments.
 */
fun t(
    name: String,
    parameters: List<Any?>,
    default: String = name,
    block: (MutableTranslationSpecifier).() -> Unit = {},
): String {
    return tn(name, parameters, block) ?: default
}

// I18ner.t(name,, default, block)

/**
 * Return the translation for the given [name]
 * with the given arguments.
 */
fun I18ner.t(
    name: String,
    dummy: Unit = Unit,
    default: String = name,
    block: (MutableTranslationSpecifier).() -> Unit = {},
): String {
    return tn(name, block) ?: default
}

/**
 * Return the translation for the given [name]
 * with the given arguments.
 */
fun t(
    name: String,
    dummy: Unit = Unit,
    default: String = name,
    block: (MutableTranslationSpecifier).() -> Unit = {},
): String {
    return tn(name, block) ?: default
}

// I18ner.t(name, ...parameters{}, default, block)

/**
 * Return the translation for the given [name]
 * with the given arguments.
 */
fun I18ner.t(
    name: String,
    vararg parameters: Pair<String, Any?>,
    default: String = name,
    block: (MutableTranslationSpecifier).() -> Unit = {},
): String {
    return tn(name, *parameters, block = block) ?: default
}

/**
 * Return the translation for the given [name]
 * with the given arguments.
 */
fun t(
    name: String,
    vararg parameters: Pair<String, Any?>,
    default: String = name,
    block: (MutableTranslationSpecifier).() -> Unit = {},
): String {
    return tn(name, *parameters, block = block) ?: default
}

// I18ner.t(name, ...parameters[], default, block)

/**
 * Return the translation for the given [name]
 * with the given arguments.
 */
fun I18ner.t(
    name: String,
    vararg parameters: Any?,
    default: String = name,
    block: (MutableTranslationSpecifier).() -> Unit = {},
): String {
    return tn(name, *parameters, block = block) ?: default
}

/**
 * Return the translation for the given [name]
 * with the given arguments.
 */
fun t(
    name: String,
    vararg parameters: Any?,
    default: String = name,
    block: (MutableTranslationSpecifier).() -> Unit = {},
): String {
    return tn(name, *parameters, block = block) ?: default
}
