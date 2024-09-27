package com.example.v5rules.utils

import kotlinx.serialization.Serializable

@Serializable
object HomeNav
@Serializable
object DisciplinesNav
@Serializable
object PredatorTypesNav
@Serializable
object ClansNav
@Serializable
object LoreNav
@Serializable
object NPCGeneratorNav
@Serializable
object RulesNav
@Serializable
data class DisciplineDetailsNav(val disciplineId:String)
@Serializable
data class DisciplinePowerNav(val disciplineId: String, val subDisciplineId: String)
@Serializable
data class RitualNav(val disciplineId: String, val ritualId: String)
@Serializable
data class PredatorTypeDetailsNav(val predatorName: String)
@Serializable
data class ClanDetailsNav(val clanName: String)
@Serializable
data class LoreDetailsNav(val title: String)
@Serializable
data class SubLoreNav(val title: String, val section: String)
@Serializable
data class RulesDetailsNav(val title: String)
@Serializable
data class SubRuleNav(val title: String, val section: String)
