package com.example.v5rules.data

import kotlinx.serialization.Serializable

@Serializable
data class Chapter(
    val title: String,
    val content: String = "",
    val table: Table? = null,
    val sections: List<Paragraph>? = null
)
@Serializable
data class Paragraph(
    val title: String,
    val content: String,
    val table: Table? = null,
    val subParagraphs: List<Paragraph>? = null,
)
