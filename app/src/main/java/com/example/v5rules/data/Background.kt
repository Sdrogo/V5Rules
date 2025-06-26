
package com.example.v5rules.data

import kotlinx.serialization.Serializable

@Serializable
data class Background(
    val id: Int,
    val title: String,
    val description: String,
    val prerequisites: String? = null,
    val isAdept: Boolean = false,
    val directFlaws: List<Advantage>? = null,
    val level: Int? = null,
    val merits: List<Advantage>? = null,
    val flaws: List<Advantage>? = null,
    val minLevel: Int?= null,
    val maxLevel: Int? = null,
)


