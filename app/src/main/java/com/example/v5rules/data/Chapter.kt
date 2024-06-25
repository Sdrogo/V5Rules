package com.example.v5rules.data

data class Chapter(
    val title: String,
    val content: String = "",
    val imageUrl: String? = null,
    val sections: List<Section>
)
