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
 * A basic implementation of [TranslationTemplate].
 *
 * TODO syntax specification
 *
 * @author LSafer
 * @since 1.0.0
 */
data object BasicTranslationTemplateEngine : TranslationTemplateEngine {
    override fun invoke(template: TranslationTemplate, parameters: Map<String, Any?>): List<Any?> {
        return buildList {
            var previousBraceIndex = 0

            while (true) {
                val openBraceIndex = template.source.indexOf('{', previousBraceIndex)

                // no more injections
                if (openBraceIndex == -1)
                    break

                // ignore escapes
                if (template.source.getOrNull(openBraceIndex - 1) == '\\')
                    continue

                val closeBraceIndex = template.source.indexOf('}', openBraceIndex)

                require(closeBraceIndex != -1) {
                    "Unclosed template injection at `${template.id}`"
                }

                val parameterQuery = template.source.substring(openBraceIndex + 1, closeBraceIndex)
                val parameter = parameters[parameterQuery]

                require(parameter != null || parameterQuery in parameters) {
                    "Injection parameter {$parameterQuery} not provided at `${template.id}`"
                }

                add(template.source.substring(previousBraceIndex, openBraceIndex))
                add(parameter)

                previousBraceIndex = closeBraceIndex + 1
            }

            add(template.source.substring(previousBraceIndex))
        }
    }
}
