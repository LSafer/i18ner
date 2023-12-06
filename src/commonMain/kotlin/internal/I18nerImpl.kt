package net.lsafer.i18ner.internal

import net.lsafer.i18ner.*

internal class I18nerImpl : I18ner {
    private val _messages = mutableMapOf<String, MutableList<TranslationMessage>>()

    override val messages = object : Iterable<TranslationMessage> {
        private val sequence = _messages.values
            .asSequence()
            .flatten()

        override fun iterator(): Iterator<TranslationMessage> {
            return sequence.asIterable().iterator()
        }

        override fun toString(): String {
            return sequence.joinToString(", ", "[", "]")
        }
    }

    override var defaultLanguage: String? =
        null

    override var languageResolution: TranslationLanguageResolution =
        BasicTranslationLanguageResolution

    override operator fun get(specifier: TranslationSpecifier): TranslationMessage? {
        val messages = this._messages[specifier.name] ?: return null

        val choices = messages.asSequence()
            .filter { it.gender == null || specifier.gender == it.gender }
            .filter { it.countRange == null || specifier.count in it.countRange }
            .groupBy { it.language }

        if (choices.isEmpty())
            return null

        val choice = languageResolution(
            ranges = specifier.languages,
            languages = choices.keys.filterNotNullTo(mutableSetOf())
        )

        val filtered = choices[choice]
            ?: choices[defaultLanguage]
            ?: choices.values.first()

        return filtered.maxBy {
            var score = 0

            if (it.countRange != null) score++
            if (it.gender != null) score++

            for (k in it.attributes.keys + specifier.attributes.keys) {
                val v0 = it.attributes[k]
                val v1 = specifier.attributes[k]

                when {
                    v0 == v1 -> score++
                    v0 == null || v1 == null -> continue
                    else -> score--
                }
            }

            score
        }
    }

    override fun plusAssign(message: TranslationMessage) {
        val groupList = this._messages.getOrPut(message.name) { mutableListOf() }

        groupList.removeAll {
            it.language == message.language &&
                    it.gender == message.gender &&
                    it.countRange == message.countRange &&
                    it.attributes == message.attributes
        }
        groupList += message
    }

    override fun plusAssign(messages: List<TranslationMessage>) {
        for ((group, groupMessages) in messages.groupBy { it.name }) {
            val groupList = this._messages.getOrPut(group) { mutableListOf() }

            for (message in groupMessages) {
                groupList.removeAll {
                    it.language == message.language &&
                            it.gender == message.gender &&
                            it.countRange == message.countRange &&
                            it.attributes == message.attributes
                }
                groupList += message
            }
        }
    }
}
