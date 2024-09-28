package com.example.v5rules.data

import kotlinx.serialization.Serializable

@Serializable
data class Loresheet(
    val id: Int,
    val title: String,
    val content: String,
    val limitation: String? = null,
    val powers: List<LoresheetPower>
)

@Serializable
data class LoresheetPower(
    val title: String,
    val level: Int,
    val limitation: String? = null,
    val content: String
)
