package com.example.v5rules.data.local.reader

import android.content.Context
import android.content.res.Resources
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Test
import java.io.ByteArrayInputStream
import java.util.Locale

class BackgroundReaderTest {

    private val context = mockk<Context>()
    private val resources = mockk<Resources>()

    @Test
    fun `readBackground should return list of backgrounds from JSON`() {
        val json = """
            [
              {
                "id": "b1",
                "title": "Allies",
                "description": "Mortal associates."
              }
            ]
        """.trimIndent()
        val inputStream = ByteArrayInputStream(json.toByteArray())

        every { context.resources } returns resources
        every { resources.openRawResource(any()) } returns inputStream

        val reader = BackgroundReader(context)
        val result = reader.readBackground(Locale.ITALIAN)

        assertEquals(1, result.size)
        assertEquals("Allies", result[0].title)
    }
}
