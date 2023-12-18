package net.lsafer.i18ner

// I18ner.tmn(specifier, parameters{})

/**
 * Return the translation for the given [specifier]
 * from the given [source] with the given arguments.
 */
fun <T> I18ner.tmn(
    source: Map<String, T>,
    specifier: LocalizationSpecifier,
): T? {
    val choices = mutableMapOf<String?, T>()

    for ((itk, itv) in source) {
        val itkSplits = itk.split("#", limit = 2)
        val itn = itkSplits.first()
        val itt = itkSplits.getOrNull(1)

        if (itn != specifier.name)
            continue

        choices[itt] = itv
    }

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

    return choices[choice] ?: choices.values.first()
}

/**
 * Return the translation for the given [specifier]
 * with the given arguments.
 */
fun <T> tmn(
    source: Map<String, T>,
    specifier: LocalizationSpecifier,
): T? {
    return I18ner.tmn(source, specifier)
}

// I18ner.tmn(name, parameters{}, block)

/**
 * Return the translation for the given [name]
 * with the given arguments.
 */
fun <T> I18ner.tmn(
    source: Map<String, T>,
    name: String,
    block: (LocalizationSpecifier).() -> Unit = {},
): T? {
    val specifier = LocalizationSpecifier(
        name = name,
        languages = mutableListOf(),
    )

    specifier.apply(block)

    return tmn(source, specifier)
}

/**
 * Return the translation for the given [name]
 * with the given arguments.
 */
fun <T> tmn(
    source: Map<String, T>,
    name: String,
    block: (LocalizationSpecifier).() -> Unit = {},
): T? {
    return I18ner.tmn(source, name, block)
}
