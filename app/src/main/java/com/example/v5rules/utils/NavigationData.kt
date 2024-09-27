package com.example.v5rules.utils

import kotlinx.serialization.Serializable

@Serializable
object HomeScreen
@Serializable
object DisciplinesScreen
@Serializable
object PredatorTypesScreen
@Serializable
object ClansScreen
@Serializable
object LoreScreen
@Serializable
object NPCGeneratorScreen
@Serializable
object RulesScreen
@Serializable
data class DisciplineDetailsScreen(val disciplineId:String)
@Serializable
data class DisciplinePowerScreen(val disciplineId: String, val subDisciplineId: String)
@Serializable
data class RitualScreen(val disciplineId: String, val ritualId: String)
@Serializable
data class PredatorTypeDetailsScreen(val predatorName: String)
@Serializable
data class ClanDetailsScreen(val clanName: String)
@Serializable
data class LoreDetailsScreen(val title: String)
@Serializable
data class SubLoreScreen(val title: String, val section: String)
@Serializable
data class RulesDetailsScreen(val title: String)
@Serializable
data class SubRuleScreen(val title: String, val section: String)
