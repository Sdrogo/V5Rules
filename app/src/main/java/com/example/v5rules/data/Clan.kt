package com.example.v5rules.data

import kotlinx.serialization.Serializable

@Serializable
data class Clan(
    val name: String = "",
    val description: String = "",
    val disciplines: List<Paragraph> = emptyList(),
    val weakness: String = "",
    val paragraphs: List<Paragraph> = emptyList(),
    val compulsion: List<Compulsion> = emptyList()
)

@Serializable
data class Compulsion(
    val name: String = "",
    val description: String = ""
)