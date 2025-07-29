package com.example.v5rules.data

import kotlinx.serialization.Serializable

@Serializable
data class Loresheet(
    val id: String = "",
    val title: String = "",
    val content: String = "",
    val limitation: String? = null,
    val powers: List<LoresheetPower> = emptyList(),
    val level: Int = 0,
)

@Serializable
data class LoresheetPower(
    val title: String  = "",
    val level: Int  = 1,
    val limitation: String? = null,
    val content: String = ""
)
