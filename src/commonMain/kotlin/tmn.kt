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

// I18ner.tmn(map, specifier)

/**
 * Return the translation for the given [specifier]
 * from the given [map] with the given arguments.
 */
fun <T> I18ner.tmn(
    map: Map<String, T>,
    specifier: LocalizationSpecifier,
): T? {
    return tmpn(map, specifier)?.second
}

/**
 * Return the translation for the given [specifier]
 * from the given [map] with the given arguments.
 */
fun <T> tmn(
    map: Map<String, T>,
    specifier: LocalizationSpecifier,
): T? {
    return I18ner.tmpn(map, specifier)?.second
}

// I18ner.tmn(map, name, languages[] block)

/**
 * Return the translation for the given [name]
 * from the given [map] with the given arguments.
 */
fun <T> I18ner.tmn(
    map: Map<String, T>,
    name: String,
    languages: List<String>,
    block: (LocalizationSpecifier).() -> Unit = {},
): T? {
    return tmpn(map, name, languages, block)?.second
}

/**
 * Return the translation for the given [name]
 * from the given [map] with the given arguments.
 */
fun <T> tmn(
    map: Map<String, T>,
    name: String,
    languages: List<String>,
    block: (LocalizationSpecifier).() -> Unit = {},
): T? {
    return I18ner.tmpn(map, name, languages, block)?.second
}

// I18ner.tmpn(map, name, ...languages[], block)

/**
 * Return the translation for the given [name]
 * from the given [map] with the given arguments.
 */
fun <T> I18ner.tmn(
    map: Map<String, T>,
    name: String,
    vararg languages: String,
    block: (LocalizationSpecifier).() -> Unit = {},
): T? {
    return tmpn(map, name, *languages, block = block)?.second
}

/**
 * Return the translation for the given [name]
 * from the given [map] with the given arguments.
 */
fun <T> tmn(
    map: Map<String, T>,
    name: String,
    vararg languages: String,
    block: (LocalizationSpecifier).() -> Unit = {},
): T? {
    return tmpn(map, name, *languages, block = block)?.second
}
