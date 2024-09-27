package com.example.v5rules

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.v5rules.ui.compose.screen.HomeScreen
import com.example.v5rules.ui.compose.screen.InputScreen
import com.example.v5rules.ui.compose.screen.clan.ClanDetailScreen
import com.example.v5rules.ui.compose.screen.clan.ClanListScreen
import com.example.v5rules.ui.compose.screen.discipline.DisciplineDetailScreen
import com.example.v5rules.ui.compose.screen.discipline.DisciplinePowerScreen
import com.example.v5rules.ui.compose.screen.discipline.DisciplineScreen
import com.example.v5rules.ui.compose.screen.discipline.RitualScreen
import com.example.v5rules.ui.compose.screen.lore.LoreDetailsScreen
import com.example.v5rules.ui.compose.screen.lore.LoreListScreen
import com.example.v5rules.ui.compose.screen.lore.SubLoreDetail
import com.example.v5rules.ui.compose.screen.predator.PredatorTypeDetailsScreen
import com.example.v5rules.ui.compose.screen.predator.PredatorTypeListScreen
import com.example.v5rules.ui.compose.screen.rule.RuleListScreen
import com.example.v5rules.ui.compose.screen.rule.RulesDetailsScreen
import com.example.v5rules.ui.compose.screen.rule.SubRuleDetail
import com.example.v5rules.ui.viewModel.ClanViewModel
import com.example.v5rules.ui.viewModel.DisciplineViewModel
import com.example.v5rules.ui.viewModel.LoreViewModel
import com.example.v5rules.ui.viewModel.NPCGeneratorViewModel
import com.example.v5rules.ui.viewModel.PredatorTypeViewModel
import com.example.v5rules.ui.viewModel.RulesViewModel
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
data class DisciplineDetailsNav(val disciplineId: String)

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

@Composable
fun CustomNavHost(
    navController: NavHostController,
    disciplineViewModel: DisciplineViewModel,
    clanViewModel: ClanViewModel,
    predatorTypeViewModel: PredatorTypeViewModel,
    rulesViewModel: RulesViewModel,
    loreViewModel: LoreViewModel,
    npcGeneratorViewModel: NPCGeneratorViewModel
) {
    NavHost(navController = navController, startDestination = HomeNav) {
        composable<HomeNav> {
            HomeScreen(navController)
        }
        composable<DisciplinesNav> {
            DisciplineScreen(disciplineViewModel, navController)
        }
        composable<DisciplineDetailsNav> { backStackEntry ->
            val entry = backStackEntry.toRoute<DisciplineDetailsNav>()
            DisciplineDetailScreen(
                disciplineId = entry.disciplineId,
                disciplineViewModel,
                navController
            )
        }
        composable<DisciplinePowerNav> { backStackEntry ->
            val entry = backStackEntry.toRoute<DisciplinePowerNav>()
            DisciplinePowerScreen(
                disciplineId = entry.disciplineId,
                disciplinePowerId = entry.subDisciplineId,
                viewModel = disciplineViewModel,
                navController = navController
            )
        }
        composable<RitualNav> { backStackEntry ->
            val entry = backStackEntry.toRoute<RitualNav>()
            RitualScreen(
                disciplineId = entry.disciplineId,
                ritualId = entry.ritualId,
                viewModel = disciplineViewModel,
                navController = navController
            )
        }
        composable<PredatorTypesNav> {
            PredatorTypeListScreen(
                viewModel = predatorTypeViewModel,
                navController = navController
            )
        }
        composable<PredatorTypeDetailsNav> { backStackEntry ->
            val entry = backStackEntry.toRoute<PredatorTypeDetailsNav>()
            PredatorTypeDetailsScreen(
                predatorTypeViewModel,
                navController,
                entry.predatorName
            )
        }
        composable<ClansNav> {
            ClanListScreen(viewModel = clanViewModel, navController = navController)
        }
        composable<ClanDetailsNav> { backStackEntry ->
            val entry = backStackEntry.toRoute<ClanDetailsNav>()
            ClanDetailScreen(
                clanViewModel = clanViewModel,
                navController = navController,
                clanName = entry.clanName
            )
        }
        composable<LoreNav> {
            LoreListScreen(
                loreViewModel = loreViewModel, navController = navController
            )
        }
        composable<LoreDetailsNav> { backStackEntry ->
            val entry = backStackEntry.toRoute<LoreDetailsNav>()
            LoreDetailsScreen(
                loreViewModel = loreViewModel,
                navController = navController,
                title = entry.title
            )
        }
        composable<SubLoreNav> { backStackEntry ->
            val entry = backStackEntry.toRoute<SubLoreNav>()
            SubLoreDetail(
                loreViewModel = loreViewModel,
                chapterTitle = entry.title,
                sectionTitle = entry.section,
                navController = navController
            )
        }
        composable<NPCGeneratorNav> {
            InputScreen(
                modifier = Modifier,
                viewModel = npcGeneratorViewModel,
                navController = navController
            )
        }
        composable<RulesNav> {
            RuleListScreen(viewModel = rulesViewModel, navController = navController)
        }
        composable<RulesDetailsNav> { backStackEntry ->
            val entry = backStackEntry.toRoute<RulesDetailsNav>()
            RulesDetailsScreen(
                rulesViewModel = rulesViewModel,
                navController = navController,
                title = entry.title
            )
        }
        composable<SubRuleNav> { backStackEntry ->
            val entry = backStackEntry.toRoute<SubRuleNav>()
            SubRuleDetail(
                rulesViewModel = rulesViewModel,
                chapterTitle = entry.title,
                sectionTitle = entry.section,
                navController = navController
            )
        }
    }
}