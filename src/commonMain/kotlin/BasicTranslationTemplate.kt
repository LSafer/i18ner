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

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * A basic implementation of [TranslationTemplate].
 *
 * TODO syntax specification
 *
 * @author LSafer
 * @since 1.0.0
 */
@Serializable
@SerialName("basic")
data class BasicTranslationTemplate(val id: String, val source: String) : TranslationTemplate {
    override fun invoke(parameters: Map<String, Any?>): String {
        return buildString {
            var previousBraceIndex = 0

            while (true) {
                val openBraceIndex = source.indexOf('{', previousBraceIndex)

                // no more injections
                if (openBraceIndex == -1)
                    break

                // ignore escapes
                if (source.getOrNull(openBraceIndex - 1) == '\\')
                    continue

                val closeBraceIndex = source.indexOf('}', openBraceIndex)

                require(closeBraceIndex != -1) {
                    "Unclosed template injection at `${id}`"
                }

                val parameterQuery = source.substring(openBraceIndex + 1, closeBraceIndex)
                val parameter = parameters[parameterQuery]

                require(parameter != null) {
                    "Injection parameter {$parameterQuery} not provided at `${id}`"
                }

                append(source, previousBraceIndex, openBraceIndex)
                append(parameter)

                previousBraceIndex = closeBraceIndex + 1
            }

            append(source, previousBraceIndex, source.length)
        }
    }
}
