package com.example.v5rules.data

data class Chapter(
    val title: String,
    val content: String = "",
    val sections: List<Paragraph>? = null
)

data class Paragraph(
    val title: String,
    val content: String,
    val subParagraphs: List<Paragraph>? = null,
)
