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
import net.lsafer.i18ner.internal.LongRangeStringSerializer
import kotlin.jvm.JvmInline

@Serializable
sealed interface MessageDefinition

@JvmInline
@Serializable
value class MessageStringDefinition(
    val template: String,
) : MessageDefinition

@Serializable
data class MessageObjectDefinition(
    val language: String? = null,
    val gender: Gender? = null,
    @OptIn(I18nerInternalApi::class)
    @Serializable(LongRangeStringSerializer::class)
    val count: LongRange? = null,
    val attributes: Map<String, String>? = null,
    val template: String? = null,
) : MessageDefinition
