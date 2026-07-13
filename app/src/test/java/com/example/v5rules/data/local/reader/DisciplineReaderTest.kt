package com.example.v5rules.data.local.reader

import android.content.Context
import android.content.res.Resources
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Test
import java.io.ByteArrayInputStream
import java.util.Locale

class DisciplineReaderTest {

    private val context = mockk<Context>()
    private val resources = mockk<Resources>()

    @Test
    fun `readDisciplines should return list of disciplines from JSON`() {
        val json = """
            [
              {
                "id": "d1",
                "title": "Auspex",
                "description": "The power of sensing things.",
                "disciplinePowers": []
              }
            ]
        """.trimIndent()
        val inputStream = ByteArrayInputStream(json.toByteArray())

        every { context.resources } returns resources
        every { resources.openRawResource(any()) } returns inputStream

        val reader = DisciplineReader(context)
        val result = reader.readDisciplines(Locale.ITALIAN)

        assertEquals(1, result.size)
        assertEquals("Auspex", result[0].title)
        assertEquals("d1", result[0].id)
    }

    @Test
    fun `readDisciplines should return empty list when JSON is empty array`() {
        val json = "[]"
        val inputStream = ByteArrayInputStream(json.toByteArray())

        every { context.resources } returns resources
        every { resources.openRawResource(any()) } returns inputStream

        val reader = DisciplineReader(context)
        val result = reader.readDisciplines(Locale.ITALIAN)

        assertEquals(0, result.size)
    }
}
