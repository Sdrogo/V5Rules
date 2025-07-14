package com.example.v5rules.data

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

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

@Entity(tableName = "favorite_npcs")
data class FavoriteNpc(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String = "",
    val secondName: String? = null,
    val familyName: String = "",
    val nationality: String = ""
) {
    @Ignore
    constructor(
        name: String,
        secondName: String?,
        familyName: String,
        nationality: String
    ) : this(0, name, secondName, familyName, nationality)
}