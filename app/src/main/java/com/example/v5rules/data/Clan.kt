package com.example.v5rules.data
data class Clan(
    val name: String,
    val description: String,
    val disciplines: List<Paragraph>,
    val weakness: String? = null,
    val paragraphs: List<Paragraph>? = null,
    val compulsion: List<Compulsion>? = null
)
data class Compulsion(
    val name: String,
    val description: String
)
