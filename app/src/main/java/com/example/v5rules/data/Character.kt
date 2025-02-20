package com.example.v5rules.data

import androidx.room.Entity
import androidx.room.PrimaryKey

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
    val attributes: Attributes = Attributes(),
    val abilities: List<Ability> = emptyList(),
    val disciplines: List<Discipline> = emptyList(),
    val advantages: List<Advantage> = emptyList(),
    val backgrounds: List<Background> = emptyList(),
    val loresheets: List<Loresheet> = emptyList(),
    val health: Health = Health(),
    val willpower: Willpower = Willpower(),
    val humanity: Humanity = Humanity(),
    val experience: Experience = Experience(),
)

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

data class SelectedClan(
    val name: String,
)

data class Ability(
    val name: String,
    val level: Int = 0, // Valore predefinito per il livello
    val specialization: String? = null // Specializzazione opzionale
)

data class Advantage(
    val name: String,
    val description: String,
    val level: Int,
    val type: AdvantageType // Enum: Positive, Negative
)

data class Background(
    val name: String,
    val description: String,
    val level: Int,
    val type: AdvantageType // Enum: Positive, Negative
)

enum class AdvantageType {
    POSITIVE,
    NEGATIVE
}

data class Health(
    val max: Int = 0,
    var current: Int = 0 // Usiamo var perché può cambiare
)

data class Willpower(
    val max: Int = 0,
    var current: Int = 0 // Usiamo var perché può cambiare
)
data class Humanity(
    val current: Int = 7,
    var stains: Int = 0 // Usiamo var perché può cambiare
)
data class Experience(
    val total: Int = 0,
    val spent: Int = 0
)