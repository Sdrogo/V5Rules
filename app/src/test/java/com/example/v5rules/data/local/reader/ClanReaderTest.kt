package com.example.v5rules.data.local.reader

import android.content.Context
import android.content.res.Resources
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Test
import java.io.ByteArrayInputStream
import java.util.Locale

class ClanReaderTest {

    private val context = mockk<Context>()
    private val resources = mockk<Resources>()

    @Test
    fun `readClans should return list of clans from JSON`() {
        val json = """
            [
              {
                "name": "Ventrue",
                "description": "The Kings.",
                "disciplines": [
                  { "title": "Dominate", "content": "Control minds" }
                ],
                "weakness": "Rare blood",
                "compulsion": [
                   { "name": "Arrogance", "description": "Too proud" }
                ]
              }
            ]
        """.trimIndent()
        val inputStream = ByteArrayInputStream(json.toByteArray())

        every { context.resources } returns resources
        every { resources.openRawResource(any()) } returns inputStream

        val reader = ClanReader(context)
        val result = reader.readClans(Locale.ITALIAN)

        assertEquals(1, result.size)
        assertEquals("Ventrue", result[0].name)
        assertEquals(1, result[0].disciplines.size)
        assertEquals("Dominate", result[0].disciplines[0].title)
        assertEquals(1, result[0].compulsion.size)
        assertEquals("Arrogance", result[0].compulsion[0].name)
    }
}
