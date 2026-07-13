package com.example.v5rules.data.local.reader

import android.content.Context
import android.content.res.Resources
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Test
import java.io.ByteArrayInputStream
import java.util.Locale

class PredatorTypeReaderTest {

    private val context = mockk<Context>()
    private val resources = mockk<Resources>()

    @Test
    fun `readPredatorType should return list of predator types from JSON`() {
        val json = """
            [
              {
                "name": "Alleycat",
                "description": "Hunt in alleys."
              }
            ]
        """.trimIndent()
        val inputStream = ByteArrayInputStream(json.toByteArray())

        every { context.resources } returns resources
        every { resources.openRawResource(any()) } returns inputStream

        val reader = PredatorTypeReader(context)
        val result = reader.readPredatorType(Locale.ITALIAN)

        assertEquals(1, result.size)
        assertEquals("Alleycat", result[0].name)
    }
}
