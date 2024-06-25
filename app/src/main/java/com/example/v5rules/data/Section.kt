package com.example.v5rules.data

data class Section(
    val title: String,
    val content: String = "",
    val imageUrl: String? = null,
    val paragraphs: List<Paragraph>? = null,
)