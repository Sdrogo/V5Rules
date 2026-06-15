package com.example.v5rules.reader

import android.content.Context
import android.content.res.Resources
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import java.io.ByteArrayInputStream
import java.util.Locale

class RulesReaderTest {

    private lateinit var context: Context
    private lateinit var resources: Resources
    private lateinit var reader: RulesReader

    @Before
    fun setUp() {
        context = mockk()
        resources = mockk()
        every { context.resources } returns resources
        reader = RulesReader(context)
    }

    @Test
    fun `readRules returns list of chapters from JSON`() {
        val json = """
            [
              {
                "title": "Chapter 1",
                "paragraphs": []
              }
            ]
        """.trimIndent()
        val inputStream = ByteArrayInputStream(json.toByteArray())
        
        every { resources.openRawResource(any()) } returns inputStream

        val result = reader.readRules(Locale.ITALIAN)

        assertNotNull(result)
        assertEquals(1, result.size)
        assertEquals("Chapter 1", result[0].title)
    }

    @Test
    fun `readLore returns list of chapters from JSON`() {
        val json = """
            [
              {
                "title": "Lore Chapter",
                "paragraphs": []
              }
            ]
        """.trimIndent()
        val inputStream = ByteArrayInputStream(json.toByteArray())
        
        every { resources.openRawResource(any()) } returns inputStream

        val result = reader.readLore(Locale.ENGLISH)

        assertEquals(1, result.size)
        assertEquals("Lore Chapter", result[0].title)
    }
}
