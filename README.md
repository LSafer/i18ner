# I18ner

Basic I18n library with no code generation or automatic translations' population.

### Localization File Example

```properties
# Girl messages
my_message_name#en-US[F,0]=There is no girls here. No one to ask about {0}!
my_message_name#en-US[F,1]=Hey girl. Do you like {0}?
my_message_name#en-US[F,2..10]=Hey girls. Do you like {0}?
my_message_name#en-US[F,11..infinity]=Hey y'all girls. Do you like {0}?
# Boy messages
my_message_name#en-US[M,0]=There is no boys here. No one to ask about {0}!
my_message_name#en-US[M,1]=Hey man. Do you like {0}?
my_message_name#en-US[M,2..10]=Hey guys. Do you like {0}?
my_message_name#en-US[M,11..infinity]=Hey y'all men. Do you like {0}?
# An example of defining message attributes; `-` serves as an assignment operator
my_message_name_2[my_attr-my_attr_val]=Hello World
# An example of using named parameters
my_message_name_3=Oops, {first_person} just called {second_person} by mistake!
```

### Runtime Configuration Example

```kotlin
val i18ner = I18ner() // or use the companion object I18ner 
i18ner.defaultLanguages += LanguageRange("ar-SA") // optional
i18ner.defaultLanguages += LanguageRange("en-US") // optional
i18ner.languageResolution = BasicTranslationLanguageResolution // optional
i18ner += i18nerSource {
    defaultLanguage = "ar-SA" // optional
    source = MyLocalizationFileSource
}
```

### Runtime Usage Example

```kotlin
val i18ner = I18ner() // or use the companion object I18ner 

// using named parameters
i18ner.t("welcome", "name" to "Osama", "age" to 25) {
    // all optional
    languages += LanguageRange("ar-SA")
    languages += LanguageRange("en-US")
    count = 1
    gender = TranslationGender.MALE
    attributes["my_attr"] = "my_attr_val"
}
// using positional parameters
i18ner.t("welcome", "Osama", "age") {
    // all optional
    languages += LanguageRange("ar-SA")
    languages += LanguageRange("en-US")
    count = 1
    gender = TranslationGender.MALE
    attributes["my_attr"] = "my_attr_val"
}
```
