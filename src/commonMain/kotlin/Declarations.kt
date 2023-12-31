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

import kotlinx.serialization.Serializable
import net.lsafer.i18ner.internal.LongRangeArraySerializer

/**
 * A function that produces a localized string from some parameters.
 */
@Serializable
data class TranslationTemplate(
    val id: String,
    val source: String,
    val engineId: String = "basic",
)

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
@Serializable
data class TranslationMessage(
    val name: String,
    val language: String? = null,
    @OptIn(I18nerInternalApi::class)
    @Serializable(LongRangeArraySerializer::class)
    val countRange: LongRange? = null,
    val gender: Gender? = null,
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
@Serializable
data class TranslationSpecifier(
    var name: String,
    val languages: MutableList<String> = mutableListOf(),
    var count: Long? = null,
    var gender: Gender? = null,
    val attributes: MutableMap<String, String> = mutableMapOf(),
)

/**
 * The necessary data for selecting a localized entry.
 *
 * @param name the target entry name (identifier)
 * @param languages the preferred language ranges of the entry.
 */
@Serializable
data class LocalizationSpecifier(
    val name: String,
    val languages: MutableList<String> = mutableListOf(),
)

/**
 * A function for selecting the best matching language
 * from a set of languages given some language ranges.
 */
fun interface TranslationLanguageResolution {
    operator fun invoke(ranges: List<String>, languages: Set<String>): String?
}

/**
 * A function that renders templates with parameters.
 */
fun interface TranslationTemplateEngine {
    operator fun invoke(template: TranslationTemplate, parameters: Map<String, Any?>): List<Any?>
}
