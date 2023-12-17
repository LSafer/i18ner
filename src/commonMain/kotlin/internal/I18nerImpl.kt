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

    override var defaultLanguages = mutableListOf<String>()

    override val templateEngines: MutableMap<String, TranslationTemplateEngine> =
        mutableMapOf("basic" to BasicTranslationTemplateEngine)

    override var languageResolution: TranslationLanguageResolution =
        BasicTranslationLanguageResolution

    override operator fun get(specifier: TranslationSpecifier): TranslationMessage? {
        val messages = this._messages[specifier.name] ?: return null

        val choices = messages.asSequence()
            .filter { it.gender == null || specifier.gender == it.gender }
            .filter { it.countRange == null || specifier.count in it.countRange }
            .filter { it.attributes.all { (k, v) -> v == specifier.attributes[k] } }
            .groupBy { it.language }

        if (choices.isEmpty())
            return null

        val choicesLanguages = choices.keys.filterNotNullTo(mutableSetOf())

        var choice = languageResolution(
            ranges = specifier.languages,
            languages = choicesLanguages
        )

        if (choice == null) {
            choice = languageResolution(
                ranges = defaultLanguages,
                languages = choicesLanguages
            )
        }

        val filtered = choices[choice] ?: choices.values.first()

        return filtered.maxBy {
            var score = it.attributes.size
            score -= score - specifier.attributes.size

            if (it.countRange != null) score++
            if (it.gender != null) score++

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
