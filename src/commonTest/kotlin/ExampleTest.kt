package net.lsafer.i18ner.test

import net.lsafer.i18ner.I18ner
import net.lsafer.i18ner.LanguageRange
import net.lsafer.i18ner.TranslationGender.FEMALE
import net.lsafer.i18ner.TranslationGender.MALE
import net.lsafer.i18ner.i18nerSource
import net.lsafer.i18ner.t
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
}
