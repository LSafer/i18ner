package net.lsafer.i18ner.test

import net.lsafer.i18ner.*
import net.lsafer.i18ner.Gender.Female
import net.lsafer.i18ner.Gender.Male
import kotlin.test.Test
import kotlin.test.assertEquals

class ExampleTest {
    @Suppress("LocalVariableName")
    @Test
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

        val AR_SA_M_OSAMA = i18ner.t("welcome", "name" to "Osama") {
            languages += "ar-SA"
            gender = Male
        }

        assertEquals("AR SA M Osama", AR_SA_M_OSAMA)

        val AR_SA_F_FATIMA = i18ner.t("welcome", "name" to "Fatima") {
            languages += "ar-SA"
            gender = Female
        }

        assertEquals("AR SA F Fatima", AR_SA_F_FATIMA)

        val NO_GENDER = i18ner.tn("welcome", "name" to "Fatima") {
            languages += "ar-SA"
        }

        assertEquals(null, NO_GENDER)
    }

    @Test
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
                template = TranslationTemplate(
                    id = "WELCOME_IMPLICIT_2",
                    source = "This is an example of an implicit\n" +
                            "multiline message definition with two lines"
                )
            ),
            TranslationMessage(
                name = "WELCOME_IMPLICIT_3",
                template = TranslationTemplate(
                    id = "WELCOME_IMPLICIT_3",
                    source = "This is an example of an implicit\n" +
                            "multiline message definition with\n" +
                            "three lines"
                )
            ),
            TranslationMessage(
                name = "WELCOME_EXPLICIT_2",
                template = TranslationTemplate(
                    id = "WELCOME_EXPLICIT_2",
                    source = "This is an example of an explicit\n" +
                            "multiline message definition with two lines"
                )
            ),
            TranslationMessage(
                name = "WELCOME_EXPLICIT_3",
                template = TranslationTemplate(
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
