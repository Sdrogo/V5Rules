package com.example.v5rules.data.local.reader

import android.content.Context
import android.content.res.Resources
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Test
import java.io.ByteArrayInputStream
import java.util.Locale

class LoresheetReaderTest {

    private val context = mockk<Context>()
    private val resources = mockk<Resources>()

    @Test
    fun `readLoresheets should return list of loresheets from JSON`() {
        val json = """
            [
              {
                "id": "l1",
                "title": "Descendant of Zelios",
                "content": "A famous architect.",
                "powers": [
                  { "title": "Sense the Pattern", "level": 1, "content": "You can see things." }
                ]
              }
            ]
        """.trimIndent()
        val inputStream = ByteArrayInputStream(json.toByteArray())

        every { context.resources } returns resources
        every { resources.openRawResource(any()) } returns inputStream

        val reader = LoresheetReader(context)
        val result = reader.readLoresheets(Locale.ITALIAN)

        assertEquals(1, result.size)
        assertEquals("Descendant of Zelios", result[0].title)
        assertEquals(1, result[0].powers.size)
        assertEquals("Sense the Pattern", result[0].powers[0].title)
    }
}
