package com.example.v5rules.data

import kotlinx.serialization.Serializable

@Serializable
data class Discipline(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val type: String = "",
    val masquerade: String = "",
    val resonance: String = "",
    val clanAffinity: List<String> = emptyList(),
    val disciplinePowers: List<DisciplinePower> = emptyList(),
    val ritual: Ritual? = null,
    val level: Int = 0,
    val ritualLevel: Int = 0,
    val selectedDisciplinePowers: List<DisciplinePower> = emptyList()
)

@Serializable
data class DisciplinePower(
    val id: String = "",
    val title: String = "",
    val level: Int = 0,
    val amalgama: String? = null,
    val exclusiveClan: String? = null,
    val prerequisite: String? = null,
    val cost: String? = null,
    val dicePool: String? = null,
    val duration: String? = null,
    val description: String = "",
    val ingredients: String? = null,
    val system: String = "",
    val table: Table? = null
)

@Serializable
data class Ritual(
    val id: String = "",
    val level: Int = 0,
    val title: String = "",
    val ritualsPowers: List<RitualPower> = emptyList()
)

@Serializable
data class RitualPower(
    val id: String = "",
    val level: Int = 0,
    val title: String = "",
    val description: String = "",
    val prerequisite: String? = null,
    val ingredients: String = "",
    val execution: String? = null,
    val system: String? = null,
    val table: Table? = null
)
@Serializable
data class Table(
    val headers: List<String> = emptyList(),
    val columns: List<List<String>> = emptyList()
)
