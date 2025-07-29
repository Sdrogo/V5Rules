package com.example.v5rules.data


data class Npc(
    val nome: String = "",
    val secondName: String? = "",
    val cognome: String = "",
    var isFavorite: Boolean = false
) {
    override fun toString(): String {
        return "$nome $secondName $cognome"
    }
}

enum class Gender {
    MALE,
    FEMALE
}

data class FavoriteNpc(
    val id: Int = 0,
    val name: String = "",
    val secondName: String? = "",
    val familyName: String = "",
    val nationality: String = ""
) {
    constructor(
        name: String,
        secondName: String?,
        familyName: String,
        nationality: String
    ) : this(0, name, secondName, familyName, nationality)
}