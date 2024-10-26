package com.example.v5rules.data

data class Chapter(
    val title: String,
    val content: String = "",
    val table: Table? = null,
    val sections: List<Paragraph>? = null
)

data class Paragraph(
    val title: String,
    val content: String,
    val table: Table? = null,
    val subParagraphs: List<Paragraph>? = null,
)
