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
 * A mutable instance holding i18n manually
 * loaded messages.
 *
 * Every instance of this class is initialized
 * with nothing and the user is responsible for
 * populating it using [plusAssign].
 *
 * @author LSafer
 * @since 1.0.0
 */
interface I18ner {
    /**
     * The global [I18ner] instance.
     */
    companion object : I18ner by I18ner()

    /**
     * A view of all the messages of this instance.
     */
    val messages: Iterable<TranslationMessage>

    /**
     * The default (fallback) languages.
     */
    val defaultLanguages: MutableList<LanguageRange>

    /**
     * The language resolution algorithm to be used.
     */
    var languageResolution: TranslationLanguageResolution

    /**
     * Return the best matching translation message for the
     * given [specifier].
     */
    operator fun get(specifier: TranslationSpecifier): TranslationMessage?

    /**
     * Add the given [message].
     */
    operator fun plusAssign(message: TranslationMessage)

    /**
     * Add the given [messages].
     */
    operator fun plusAssign(messages: List<TranslationMessage>)
}
