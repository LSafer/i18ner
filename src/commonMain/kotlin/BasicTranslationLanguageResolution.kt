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
 * A basic implementation of [TranslationLanguageResolution].
 *
 * TODO behaviour specification
 *
 * @author LSafer
 * @since 1.0.0
 */
@Serializable
@SerialName("basic")
data object BasicTranslationLanguageResolution : TranslationLanguageResolution {
    override fun invoke(ranges: List<LanguageRange>, languages: Set<String>): String? {
        if (ranges.isEmpty() || languages.isEmpty())
            return null

        for (range in ranges.asSequence().sortedBy { it.weight }) {
            // Special language range ("*") is ignored in lookup.
            if (range.language == "*")
                continue

            var normalRangeLanguage = range.language.lowercase()

            while (normalRangeLanguage.isNotEmpty()) {
                val regex = normalRangeLanguage
                    .replace("*", "[a-z0-9]*")
                    .toRegex()

                for (language in languages) {
                    val normalLocale = language.lowercase()

                    if (normalLocale.matches(regex))
                        return language
                }

                // Truncate from the end....
                normalRangeLanguage = truncateRange(normalRangeLanguage)
            }
        }

        return null
    }

    /* Truncate the range from end during the lookup match; copy-paste from java LocaleMatcher.truncateRange */
    private fun truncateRange(rangeForRegex: String): String {
        var rangeForRegexVar = rangeForRegex
        var index = rangeForRegexVar.lastIndexOf('-')
        if (index >= 0) {
            rangeForRegexVar = rangeForRegexVar.substring(0, index)

            // if range ends with an extension key, truncate it.
            index = rangeForRegexVar.lastIndexOf('-')
            if (index >= 0 && index == rangeForRegexVar.length - 2) {
                rangeForRegexVar = rangeForRegexVar.substring(0, rangeForRegexVar.length - 2)
            }
        } else {
            rangeForRegexVar = ""
        }
        return rangeForRegexVar
    }
}
