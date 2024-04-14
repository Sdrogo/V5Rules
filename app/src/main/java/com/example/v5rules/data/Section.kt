package com.example.v5rules.data

data class Section(
    val title: String,
    val content: String = "",
    val keywords: List<String> = emptyList(),
    val paragraphs: List<Paragraph>
)