package com.example.v5rules.data

import kotlinx.serialization.Serializable


@Serializable
data class PredatorType(
    val name: String,
    val description: String,
    val huntPool: String,
    val paragraphs: List<String>,
)
