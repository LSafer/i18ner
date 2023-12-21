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

import net.lsafer.i18ner.internal.parseStatementKey

/**
 * The configuration needed to obtain
 * translation messages from message definitions.
 *
 * @param defaultLanguage the language of the message if no language tag where specified.
 * @param defaultTemplateEngineId the default engine id.
 * @param element the message definitions.
 * @param strict true, to enable stricter parsing; Some issues might not be reported.
 * @param filename optional; Helps to track syntax issues.
 */
data class I18nerMessageDefinitionsOptions(
    var defaultLanguage: String? = null,
    var defaultTemplateEngineId: String = "basic",
    var element: Map<String, MessageDefinition> = emptyMap(),
    var strict: Boolean = false,
    var filename: String? = null,
)

/**
 * Transform the given message definitions into
 * translation messages.
 */
fun i18nerMessageDefinitions(
    element: Map<String, MessageDefinition> = emptyMap(),
    block: I18nerMessageDefinitionsOptions.() -> Unit,
): List<TranslationMessage> {
    val configuration = I18nerMessageDefinitionsOptions()
    configuration.element = element
    configuration.apply(block)
    return i18nerMessageDefinitions(configuration)
}

/**
 * Transform the given message definitions into
 * translation messages.
 */
fun i18nerMessageDefinitions(
    options: I18nerMessageDefinitionsOptions,
): List<TranslationMessage> {
    return options.element.map { (key, definition) ->
        val consumedKey = key.parseStatementKey(
            filename = options.filename,
            line = null,
            lineNumber = null,
            strict = options.strict
        )

        when (definition) {
            is MessageStringDefinition -> {
                TranslationMessage(
                    name = consumedKey.name,
                    language = consumedKey.language
                        ?: options.defaultLanguage,
                    countRange = consumedKey.countModifier,
                    gender = consumedKey.genderModifier,
                    attributes = consumedKey.attributes.orEmpty(),
                    template = TranslationTemplate(
                        id = key,
                        source = definition.template,
                        engineId = options.defaultTemplateEngineId
                    )
                )
            }

            is MessageObjectDefinition -> {
                TranslationMessage(
                    name = consumedKey.name,
                    language = definition.language
                        ?: consumedKey.language
                        ?: options.defaultLanguage,
                    countRange = definition.count
                        ?: consumedKey.countModifier,
                    gender = definition.gender
                        ?: consumedKey.genderModifier,
                    attributes = consumedKey.attributes.orEmpty() +
                            definition.attributes.orEmpty(),
                    template = TranslationTemplate(
                        id = key,
                        source = definition.template.orEmpty(),
                        engineId = options.defaultTemplateEngineId
                    )
                )
            }
        }
    }
}
