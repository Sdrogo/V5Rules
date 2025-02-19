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
    val abilities: Abilities = Abilities(),
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
    //val imageUrl: String? = null, // Potresti usare un URL dell'immagine se le avessi
)

data class Abilities(
    val athletics: Int= 0,
    val brawl: Int= 0,
    val craft: Int= 0,
    val drive: Int= 0,
    val firearms: Int= 0,
    val melee: Int= 0,
    val larceny: Int= 0,
    val stealth: Int= 0,
    val survival: Int= 0,

    val animalken: Int= 0,
    val etiquette: Int= 0,
    val insight: Int= 0,
    val intimidation: Int= 0,
    val leadership: Int= 0,
    val performance: Int= 0,
    val persuasion: Int= 0,
    val streetwise: Int= 0,
    val subterfuge: Int= 0,

    val academics: Int= 0,
    val awareness: Int= 0,
    val finance: Int= 0,
    val investigation: Int= 0,
    val medicine: Int= 0,
    val occult: Int= 0,
    val politics: Int= 0,
    val science: Int= 0,
    val technology: Int= 0,
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