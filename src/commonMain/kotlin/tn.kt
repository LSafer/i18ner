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

// I18ner.tn(specifier, parameters{})

/**
 * Return the translation for the given [specifier]
 * with the given arguments.
 */
fun I18ner.tn(
    specifier: TranslationSpecifier,
    parameters: Map<String, Any?>,
): String? {
    return tnObjects(specifier, parameters)?.joinToString("")
}

/**
 * Return the translation for the given [specifier]
 * with the given arguments.
 */
fun tn(
    specifier: TranslationSpecifier,
    parameters: Map<String, Any?>,
): String? {
    return tnObjects(specifier, parameters)?.joinToString("")
}

// I18ner.tn(name, parameters{}, block)

/**
 * Return the translation for the given [name]
 * with the given arguments.
 */
fun I18ner.tn(
    name: String,
    parameters: Map<String, Any?>,
    block: (TranslationSpecifier).() -> Unit = {},
): String? {
    return tnObjects(name, parameters, block)?.joinToString("")
}

/**
 * Return the translation for the given [name]
 * with the given arguments.
 */
fun tn(
    name: String,
    parameters: Map<String, Any?>,
    block: (TranslationSpecifier).() -> Unit = {},
): String? {
    return tnObjects(name, parameters, block)?.joinToString("")
}

// I18ner.tn(name, parameters[], block)

/**
 * Return the translation for the given [name]
 * with the given arguments.
 */
fun I18ner.tn(
    name: String,
    parameters: List<Any?>,
    block: (TranslationSpecifier).() -> Unit = {},
): String? {
    return tnObjects(name, parameters, block)?.joinToString("")
}

/**
 * Return the translation for the given [name]
 * with the given arguments.
 */
fun tn(
    name: String,
    parameters: List<Any?>,
    block: (TranslationSpecifier).() -> Unit = {},
): String? {
    return tnObjects(name, parameters, block)?.joinToString("")
}

// I18ner.tn(name, block)

/**
 * Return the translation for the given [name]
 * with the given arguments.
 */
fun I18ner.tn(
    name: String,
    block: (TranslationSpecifier).() -> Unit = {},
): String? {
    return tnObjects(name, block)?.joinToString("")
}

/**
 * Return the translation for the given [name]
 * with the given arguments.
 */
fun tn(
    name: String,
    block: (TranslationSpecifier).() -> Unit = {},
): String? {
    return tnObjects(name, block)?.joinToString("")
}

// I18ner.tn(name, ...parameters{}, block)

/**
 * Return the translation for the given [name]
 * with the given arguments.
 */
fun I18ner.tn(
    name: String,
    vararg parameters: Pair<String, Any?>,
    block: (TranslationSpecifier).() -> Unit = {},
): String? {
    return tnObjects(name, *parameters, block = block)?.joinToString("")
}

/**
 * Return the translation for the given [name]
 * with the given arguments.
 */
fun tn(
    name: String,
    vararg parameters: Pair<String, Any?>,
    block: (TranslationSpecifier).() -> Unit = {},
): String? {
    return tnObjects(name, *parameters, block = block)?.joinToString("")
}

// I18ner.tn(name, ...parameters[], block)

/**
 * Return the translation for the given [name]
 * with the given arguments.
 */
fun I18ner.tn(
    name: String,
    vararg parameters: Any?,
    block: (TranslationSpecifier).() -> Unit = {},
): String? {
    return tnObjects(name, *parameters, block = block)?.joinToString("")
}

/**
 * Return the translation for the given [name]
 * with the given arguments.
 */
fun tn(
    name: String,
    vararg parameters: Any?,
    block: (TranslationSpecifier).() -> Unit = {},
): String? {
    return tnObjects(name, *parameters, block = block)?.joinToString("")
}
