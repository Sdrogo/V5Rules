package com.example.v5rules.data

data class Discipline(
    val id: String,
    val title: String,
    val description: String,
    val type: String,
    val masquerade: String,
    val resonance: String,
    val clanAffinity: List<String>,
    val disciplinePowers: List<DisciplinePower>,
    val rituals: List<Ritual>? = null,
    val level: Int = 0, // Aggiungi il livello
    val selectedDisciplinePowers: List<DisciplinePower> = emptyList()//Poteri selezionati
)

data class DisciplinePower(
    val id: String,
    val title: String,
    val level: Int,
    val amalgama: String? = null,
    val exclusiveClan: String? = null,
    val prerequisite: String? = null,
    val cost: String? = null,
    val dicePool: String? = null,
    val duration: String? = null,
    val description: String,
    val ingredients: String? = null,
    val system: String,
    val table: Table? = null,
)

data class Ritual(
    val id: String,
    val level: Int,
    val title: String,
    val description: String,
    val prerequisite: String? = null,
    val ingredients: String,
    val execution: String? = null,
    val system: String? = null,
    val table: Table? = null
)
data class Table(
    val headers: List<String>,
    val columns: List<List<String>>
)