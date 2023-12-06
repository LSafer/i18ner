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
 * A language range is an identifier which is used to
 * select language tag(s) meeting specific requirements.
 */
data class LanguageRange(
    val language: String,
    val weight: Double = 1.0,
)

/**
 * A function for selecting the best matching language
 * from a set of languages given some language ranges.
 */
fun interface TranslationLanguageResolution {
    operator fun invoke(ranges: List<LanguageRange>, languages: Set<String>): String?
}

/**
 * A function that produces a localized string from some parameters.
 */
fun interface TranslationTemplate {
    operator fun invoke(parameters: Map<String, Any?>): String
}

/**
 * Enumeration over the possible genders to help
 * produce the best matching translation message
 * based on gender.
 */
enum class TranslationGender {
    MALE, FEMALE
}

/**
 * A class holding the information about some translation message.
 *
 * @param name the message name (identifier)
 * @param language the language of the message (e.g. ar-SA)
 * @param countRange how many entities this message is valid for.
 * @param gender is this message targeting men or women.
 * @param attributes additional attributes assigned for the message.
 * @param template a function to render the message content with parameters.
 */
data class TranslationMessage(
    val name: String,
    val language: String? = null,
    val countRange: LongRange? = null,
    val gender: TranslationGender? = null,
    val attributes: Map<String, String> = emptyMap(),
    val template: TranslationTemplate,
)

/**
 * The necessary data for selecting a translation message.
 *
 * @param name the target message name (identifier)
 * @param languages the preferred language ranges of the message.
 * @param count the number of entities the target message is for.
 * @param gender the gender the target message is for.
 * @param attributes preferred attributes of the target message.
 */
data class TranslationSpecifier(
    val name: String,
    val languages: List<LanguageRange> = emptyList(),
    val count: Long? = null,
    val gender: TranslationGender? = null,
    val attributes: Map<String, String> = emptyMap(),
)

/**
 * The necessary data for selecting a translation message.
 *
 * @param name the target message name (identifier)
 * @param languages the preferred language ranges of the message.
 * @param count the number of entities the target message is for.
 * @param gender the gender the target message is for.
 * @param attributes preferred attributes of the target message.
 */
data class MutableTranslationSpecifier(
    var name: String,
    val languages: MutableList<LanguageRange>,
    var count: Long?,
    var gender: TranslationGender?,
    val attributes: MutableMap<String, String>,
)

/**
 * Helper variable for accessing the first element
 * of [MutableTranslationSpecifier.languages].
 *
 * Attempting to assign `null` will result to all the
 * elements of [MutableTranslationSpecifier.languages]
 * being removed.
 */
var MutableTranslationSpecifier.language: String?
    get() = languages.first().language
    set(value) = when {
        value == null -> languages.clear()
        languages.isEmpty() -> languages += LanguageRange(value)
        else -> languages[0] = LanguageRange(value)
    }
