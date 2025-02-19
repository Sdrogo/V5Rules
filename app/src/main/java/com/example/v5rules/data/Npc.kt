package com.example.v5rules.data

import java.util.UUID

data class Npc(
    val nome: String,
    val secondName: String?,
    val cognome: String,
    var isFavorite: Boolean = false
) {
    override fun toString(): String {
        return "${nome} ${secondName.orEmpty() + " "}${cognome}"
    }
}

enum class Gender {
    MALE,
    FEMALE
}

enum class RegenerationType {
    NAME,
    SECOND_NAME,
    FAMILY_NAME,
    ALL
}

data class FavoriteNpc(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val secondName: String? = null,
    val familyName: String,
    val nationality: String,
    var isFavorite: Boolean = false // Inizia con cuore vuoto
)