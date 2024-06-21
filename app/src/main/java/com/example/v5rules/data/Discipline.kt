package com.example.v5rules.data

import com.example.v5rules.utils.Clan

data class Discipline(
    val id: String,
    val title: String,
    val description: String,
    val imageUrl: String,
    val type: String,
    val masquerade: String,
    val resonance: String,
    val clanAffinity: List<String>,
    val subDisciplines: List<SubDiscipline>,
    val rituals: List<Rituals>? = null
)

data class SubDiscipline(
    val id: String,
    val title: String,
    val level: Int,
    val amalgama: String? = null,
    val prerequisite: String? = null,
    val cost: String,
    val dicePool: String? = null,
    val duration: String,
    val description: String,
    val system: String,
    val table: Table? = null,
)

data class Rituals(
    val id: String,
    val title: String,
    val description: String,
    val ingredients: String,
    val execution: String,
    val system: String
)
data class Table(
    val numberOfColumns: Int,
    val headers: List<String>,
    val columns: List<List<String>>
)