package com.example.v5rules.data

import kotlinx.serialization.Serializable

@Serializable
data class Clan(
    val name: String,
    val description: String,
    val disciplines: List<Paragraph>,
    val weakness: String? = null,
    val paragraphs: List<Paragraph>? = null,
    val compulsion: List<Compulsion>? = null
)

@Serializable
data class Compulsion(
    val name: String,
    val description: String
)
