package com.example.v5rules.data.local.model

import org.junit.Assert.assertEquals
import org.junit.Test

class DataClassesTest {

    @Test
    fun `Compulsion data class should hold data correctly`() {
        val compulsion = Compulsion(name = "Test Compulsion", description = "Test Description")
        assertEquals("Test Compulsion", compulsion.name)
        assertEquals("Test Description", compulsion.description)
    }

    @Test
    fun `Paragraph data class should hold data correctly`() {
        val paragraph = Paragraph(title = "Test Title", content = "Test Content")
        assertEquals("Test Title", paragraph.title)
        assertEquals("Test Content", paragraph.content)
    }

    @Test
    fun `LoresheetPower data class should hold data correctly`() {
        val power = LoresheetPower(title = "Test Power", level = 3, content = "Test Content")
        assertEquals("Test Power", power.title)
        assertEquals(3, power.level)
        assertEquals("Test Content", power.content)
    }
    
    @Test
    fun `Chapter data class should hold data correctly`() {
        val paragraph = Paragraph(title = "Sub Title")
        val chapter = Chapter(title = "Main Title", sections = listOf(paragraph))
        assertEquals("Main Title", chapter.title)
        assertEquals(1, chapter.sections?.size)
        assertEquals("Sub Title", chapter.sections?.first()?.title)
    }
}
