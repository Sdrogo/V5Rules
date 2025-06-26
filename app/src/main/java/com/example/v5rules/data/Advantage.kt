package com.example.v5rules.data

import kotlinx.serialization.Serializable

@Serializable
data class Advantage(
    val id: Int,
    val title: String,
    val description: String,
    val isFlaw: Boolean? = false,
    val minLevel: Int?= null,
    val maxLevel: Int? = null,
    val prerequisites: String? = null
)
