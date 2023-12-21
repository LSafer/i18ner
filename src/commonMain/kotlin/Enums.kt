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

/**
 * Enumeration over the possible genders to help
 * produce the best matching translation message
 * based on gender.
 */
@Serializable
enum class Gender {
    Male, Female
}

@Serializable
enum class LanguageDirection {
    LeftToRight, RightToLeft
}

/**
 * Return the direction of the given [language].
 */
fun directionOfLanguage(language: String): LanguageDirection {
    return when (language.substringBefore('-')) {
        "ae", /* Avestan */
        "ar", /* 'العربية', Arabic */
        "arc",  /* Aramaic */
        "bcc",  /* 'بلوچی مکرانی', Southern Balochi */
        "bqi",  /* 'بختياري', Bakthiari */
        "ckb",  /* 'Soranî / کوردی', Sorani */
        "dv",   /* Dhivehi */
        "fa",   /* 'فارسی', Persian */
        "glk",  /* 'گیلکی', Gilaki */
        "ku",   /* 'Kurdî / كوردی', Kurdish */
        "mzn",  /* 'مازِرونی', Mazanderani */
        "nqo",  /* N'Ko */
        "pnb",  /* 'پنجابی', Western Punjabi */
        "prs",  /* 'دری', Darī */
        "ps",   /* 'پښتو', Pashto, */
        "sd",   /* 'سنڌي', Sindhi */
        "ug",   /* 'Uyghurche / ئۇيغۇرچە', Uyghur */
        "ur",    /* 'اردو', Urdu */
        "yi",    /* 'ייִדיש', Yiddish */
        -> LanguageDirection.RightToLeft

        else
        -> LanguageDirection.LeftToRight
    }
}
