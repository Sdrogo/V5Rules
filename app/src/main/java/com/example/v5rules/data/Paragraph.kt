package com.example.v5rules.data

data class Paragraph(
    val title: String,
    val content: String = "",
    val keywords: List<String> = emptyList()
)
