package com.example.v5rules.data

import kotlinx.serialization.Serializable

@Serializable
data class Background(
    val id: String = "",
    val identifier: String? = null,
    val title: String = "",
    val description: String = "",
    val prerequisites: String? = null,
    val isAdept: Boolean = false,
    val directFlaws: List<Advantage> = emptyList(),
    val level: Int = 0,
    val merits: List<Advantage> = emptyList(),
    val flaws: List<Advantage> = emptyList(),
    val minLevel: Int = 0,
    val maxLevel: Int = 5,
    val note: String? = null
)