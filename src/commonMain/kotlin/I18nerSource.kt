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

import net.lsafer.i18ner.internal.consumeStatements
import net.lsafer.i18ner.internal.createStatementConsumer

/**
 * The configuration needed to obtain
 * translation messages from a raw source.
 */
class I18nSourceOptions {
    /**
     * The language of the message if no language tag where specified.
     */
    var defaultLanguage: String? = null

    /**
     * The raw source.
     */
    var source: String = ""
}

/**
 * Obtain a translation messages list from the
 * source in the given configuration [block].
 *
 * The base syntax is following the `.properties` syntax
 * with custom key and value interpretations.
 *
 * The following is are examples of the syntax:
 *
 * ```properties
 * # Girl messages
 * my_message_name#en-US[F,0]=There is no girls here. No one to ask about {0}!
 * my_message_name#en-US[F,1]=Hey girl. Do you like {0}?
 * my_message_name#en-US[F,2..10]=Hey girls. Do you like {0}?
 * my_message_name#en-US[F,11..infinity]=Hey y'all girls. Do you like {0}?
 * # Boy messages
 * my_message_name#en-US[M,0]=There is no boys here. No one to ask about {0}!
 * my_message_name#en-US[M,1]=Hey man. Do you like {0}?
 * my_message_name#en-US[M,2..10]=Hey guys. Do you like {0}?
 * my_message_name#en-US[M,11..infinity]=Hey y'all men. Do you like {0}?
 * # An example of defining message attributes; `-` serves as an assignment operator
 * my_message_name_2[my_attr-my_attr_val]=Hello World
 * # An example of using named parameters
 * my_message_name_3=Oops, {first_person} just called {second_person} by mistake!
 * ```
 */
fun i18nerSource(
    source: String = "",
    block: I18nSourceOptions.() -> Unit = {},
): List<TranslationMessage> {
    val configuration = I18nSourceOptions()
    configuration.source = source
    configuration.apply(block)
    return i18nerSource(configuration)
}

/**
 * Obtain a translation messages list from the
 * source in the given [options].
 *
 * The base syntax is following the `.properties` syntax
 * with custom key and value interpretations.
 *
 * The following is are examples of the syntax:
 *
 * ```properties
 * # Girl messages
 * my_message_name#en-US[F,0]=There is no girls here. No one to ask about {0}!
 * my_message_name#en-US[F,1]=Hey girl. Do you like {0}?
 * my_message_name#en-US[F,2..10]=Hey girls. Do you like {0}?
 * my_message_name#en-US[F,11..infinity]=Hey y'all girls. Do you like {0}?
 * # Boy messages
 * my_message_name#en-US[M,0]=There is no boys here. No one to ask about {0}!
 * my_message_name#en-US[M,1]=Hey man. Do you like {0}?
 * my_message_name#en-US[M,2..10]=Hey guys. Do you like {0}?
 * my_message_name#en-US[M,11..infinity]=Hey y'all men. Do you like {0}?
 * # An example of defining message attributes; `-` serves as an assignment operator
 * my_message_name_2[my_attr-my_attr_val]=Hello World
 * # An example of using named parameters
 * my_message_name_3=Oops, {first_person} just called {second_person} by mistake!
 * ```
 */
fun i18nerSource(options: I18nSourceOptions): List<TranslationMessage> {
    val source = options.source
    val output = mutableListOf<TranslationMessage>()
    val consumer = createStatementConsumer(null, true) {
        output += TranslationMessage(
            name = it.keyName,
            language = it.keyTag ?: options.defaultLanguage,
            countRange = it.keyMetadataCountRange,
            gender = it.keyMetadataGender,
            attributes = it.keyMetadataAttributes ?: emptyMap(),
            template = BasicTranslationTemplate(
                id = it.key,
                source = it.value
            )
        )
    }
    source.lineSequence().consumeStatements(consumer)
    return output
}
