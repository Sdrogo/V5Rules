package com.example.v5rules.data.local.reader

import android.content.Context
import android.content.res.Resources
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Test
import java.io.ByteArrayInputStream
import java.util.Locale

class NpcReaderTest {

    private val context = mockk<Context>()
    private val resources = mockk<Resources>()

    @Test
    fun `readNpc should return list of nationalities from JSON`() {
        val json = """
            [
              {
                "nationality": "Italian",
                "nomiMaschili": ["Mario"],
                "nomiFemminili": ["Maria"],
                "cognomi": ["Rossi"]
              }
            ]
        """.trimIndent()
        val inputStream = ByteArrayInputStream(json.toByteArray())

        every { context.resources } returns resources
        every { resources.openRawResource(any()) } returns inputStream

        val reader = NpcReader(context)
        val result = reader.readNpc(Locale.ITALIAN)

        assertEquals(1, result.size)
        assertEquals("Italian", result[0].nationality)
        assertEquals(1, result[0].nomiMaschili.size)
    }
}
