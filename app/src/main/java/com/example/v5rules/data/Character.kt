package com.example.v5rules.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "characters")
data class Character(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String = "",
    val clan: Clan? = null,
    val generation: Int = 12,
    val sire: String = "",
    val concept: String = "",
    val ambition: String = "",
    val desire: String = "",
    val predator: PredatorType? = null,
    val attributes: Attributes = Attributes(),
    val abilities: List<Ability> = emptyList(),
    val disciplines: List<Discipline> = emptyList(),
    val advantages: List<Background> = emptyList(),
    val backgrounds: List<Background> = emptyList(),
    val directFlaws: List<Advantage>? = emptyList(),
    val loresheets: List<Loresheet> = emptyList(),
    val health: Health = Health(),
    val willpower: Willpower = Willpower(),
    val humanity: Humanity = Humanity(),
    val experience: Experience = Experience(),
)

@Serializable
data class Attributes(
    val strength: Int = 1,
    val dexterity: Int= 1,
    val stamina: Int= 1,
    val charisma: Int= 1,
    val manipulation: Int= 1,
    val composure: Int= 1,
    val intelligence: Int= 1,
    val wits: Int= 1,
    val resolve: Int= 1
)

@Serializable
data class Ability(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val level: Int = 0, // Valore predefinito per il livello
    val specialization: String? = null // Specializzazione opzionale
)

@Serializable
data class Health(
    val max: Int = 0,
    var current: Int = 0 // Usiamo var perché può cambiare
)

@Serializable
data class Willpower(
    val max: Int = 0,
    var current: Int = 0 // Usiamo var perché può cambiare
)

@Serializable
data class Humanity(
    val current: Int = 0,
    var stains: Int = 0 // Usiamo var perché può cambiare
)

@Serializable
data class Experience(
    val total: Int = 0,
    val spent: Int = 0
)