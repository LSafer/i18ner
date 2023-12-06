package net.lsafer.i18ner.test

import net.lsafer.i18ner.*
import net.lsafer.i18ner.TranslationGender.FEMALE
import net.lsafer.i18ner.TranslationGender.MALE
import kotlin.js.JsName
import kotlin.test.Test
import kotlin.test.assertEquals

class ExampleTest {
    @Suppress("LocalVariableName")
    @Test
    @JsName("_20628350")
    fun `simple format and resolution test`() {
        val i18ner = I18ner()

        i18ner += i18nerSource {
            //language=properties
            source = """
                welcome#ar[F]=AR F {name}
                welcome#ar-SA[F]=AR SA F {name}
                welcome#en[F]=EN F {name}
                welcome#ar[M]=AR M {name}
                welcome#ar-SA[M]=AR SA M {name}
                welcome#en[M]=EN M {name}
            """
        }

        println(i18ner.messages.toString())

        val AR_SA_M_OSAMA = i18ner.t("welcome", "name" to "Osama") {
            languages += LanguageRange("ar-SA")
            gender = MALE
        }

        assertEquals("AR SA M Osama", AR_SA_M_OSAMA)

        val AR_SA_F_FATIMA = i18ner.t("welcome", "name" to "Fatima") {
            languages += LanguageRange("ar-SA")
            gender = FEMALE
        }

        assertEquals("AR SA F Fatima", AR_SA_F_FATIMA)
    }

    @Test
    @JsName("_53357355")
    fun `implicit and explicit multiline parsing`() {
        val messages = i18nerSource {
            //language=dotenv
            source = """
                WELCOME_IMPLICIT_2='This is an example of an implicit
                multiline message definition with two lines'
                WELCOME_IMPLICIT_3='This is an example of an implicit
                multiline message definition with
                three lines'
                WELCOME_EXPLICIT_2='This is an example of an explicit\
                multiline message definition with two lines'
                WELCOME_EXPLICIT_3='This is an example of an explicit\
                multiline message definition with
                three lines'
            """.trimIndent()
        }

        val expectedMessages = setOf(
            TranslationMessage(
                name = "WELCOME_IMPLICIT_2",
                template = BasicTranslationTemplate(
                    id = "WELCOME_IMPLICIT_2",
                    source = "This is an example of an implicit\n" +
                            "multiline message definition with two lines"
                )
            ),
            TranslationMessage(
                name = "WELCOME_IMPLICIT_3",
                template = BasicTranslationTemplate(
                    id = "WELCOME_IMPLICIT_3",
                    source = "This is an example of an implicit\n" +
                            "multiline message definition with\n" +
                            "three lines"
                )
            ),
            TranslationMessage(
                name = "WELCOME_EXPLICIT_2",
                template = BasicTranslationTemplate(
                    id = "WELCOME_EXPLICIT_2",
                    source = "This is an example of an explicit\n" +
                            "multiline message definition with two lines"
                )
            ),
            TranslationMessage(
                name = "WELCOME_EXPLICIT_3",
                template = BasicTranslationTemplate(
                    id = "WELCOME_EXPLICIT_3",
                    source = "This is an example of an explicit\n" +
                            "multiline message definition with\n" +
                            "three lines"
                )
            ),
        )

        assertEquals(expectedMessages, messages.toSet())
    }
}
