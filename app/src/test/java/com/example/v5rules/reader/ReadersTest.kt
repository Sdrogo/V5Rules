package com.example.v5rules.reader

import android.content.Context
import android.content.res.Resources
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.io.ByteArrayInputStream
import java.util.Locale

class ReadersTest {

    private lateinit var context: Context
    private lateinit var resources: Resources

    @Before
    fun setUp() {
        context = mockk()
        resources = mockk()
        every { context.resources } returns resources
    }

    @Test
    fun `BackgroundReader returns list from JSON`() {
        val json = "[{\"title\": \"Resources\"}]"
        every { resources.openRawResource(any()) } returns ByteArrayInputStream(json.toByteArray())
        val reader = BackgroundReader(context)
        val result = reader.readBackground(Locale.ITALIAN)
        assertEquals(1, result.size)
        assertEquals("Resources", result[0].title)
    }

    @Test
    fun `ClanReader returns list from JSON`() {
        val json = "[{\"name\": \"Ventrue\"}]"
        every { resources.openRawResource(any()) } returns ByteArrayInputStream(json.toByteArray())
        val reader = ClanReader(context)
        val result = reader.readClans(Locale.ITALIAN)
        assertEquals(1, result.size)
        assertEquals("Ventrue", result[0].name)
    }

    @Test
    fun `DisciplineReader returns list from JSON`() {
        val json = "[{\"title\": \"Presence\"}]"
        every { resources.openRawResource(any()) } returns ByteArrayInputStream(json.toByteArray())
        val reader = DisciplineReader(context)
        val result = reader.readDisciplines(Locale.ITALIAN)
        assertEquals(1, result.size)
        assertEquals("Presence", result[0].title)
    }

//    @Test
//    fun `LoresheetReader returns list from JSON`() {
//        val json = "[{\"title\": \"Descendant of Tyler\"}]"
//        every { resources.openRawResource(any()) } returns ByteArrayInputStream(json.toByteArray())
//        val reader = LoresheetReader(context)
//        val result = reader.readLoresheet(Locale.ITALIAN)
//        assertEquals(1, result.size)
//        assertEquals("Descendant of Tyler", result[0].title)
//    }

    @Test
    fun `PredatorTypeReader returns list from JSON`() {
        val json = "[{\"name\": \"Alleycat\"}]"
        every { resources.openRawResource(any()) } returns ByteArrayInputStream(json.toByteArray())
        val reader = PredatorTypeReader(context)
        val result = reader.readPredatorType(Locale.ITALIAN)
        assertEquals(1, result.size)
        assertEquals("Alleycat", result[0].name)
    }
}
