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
    val clanAffinity: List<Clan>,
    val subDisciplines: List<SubDiscipline>
)

data class SubDiscipline(
    val id: String,
    val title: String,
    val level: Int,
    val amalgama: Pair<String, Int>? = null,
    val cost: String,
    val dicePool: String,
    val duration: String,
    val description: String,
    val system: String,
    val table: Table? = null,
)

data class Table(
    val numberOfColumns: Int,
    val headers: List<String>,
    val columns: List<List<String>>
)