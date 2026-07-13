package com.example.v5rules.data.local.reader

import android.content.Context
import android.content.res.Resources
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Test
import java.io.ByteArrayInputStream
import java.util.Locale

class RulesReaderTest {

    private val context = mockk<Context>()
    private val resources = mockk<Resources>()

    @Test
    fun `readRules should return list of chapters from JSON`() {
        val json = """
            [
              {
                "title": "Basic Rules",
                "content": "How to play",
                "sections": [
                  { "title": "Dice", "content": "Roll 10s" }
                ]
              }
            ]
        """.trimIndent()
        val inputStream = ByteArrayInputStream(json.toByteArray())

        every { context.resources } returns resources
        every { resources.openRawResource(any()) } returns inputStream

        val reader = RulesReader(context)
        val result = reader.readRules(Locale.ITALIAN)

        assertEquals(1, result.size)
        assertEquals("Basic Rules", result[0].title)
        assertEquals(1, result[0].sections?.size)
        assertEquals("Dice", result[0].sections?.get(0)?.title)
    }

    @Test
    fun `readLore should return list of chapters from JSON`() {
        val json = "[]"
        val inputStream = ByteArrayInputStream(json.toByteArray())

        every { context.resources } returns resources
        every { resources.openRawResource(any()) } returns inputStream

        val reader = RulesReader(context)
        val result = reader.readLore(Locale.ITALIAN)

        assertEquals(0, result.size)
    }
}
