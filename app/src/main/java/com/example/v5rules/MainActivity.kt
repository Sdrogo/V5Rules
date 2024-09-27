package com.example.v5rules

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.v5rules.ui.viewModel.LoreViewModel
import com.example.v5rules.ui.compose.screen.clan.ClanDetailScreen
import com.example.v5rules.ui.compose.screen.clan.ClanListScreen
import com.example.v5rules.ui.viewModel.DisciplineViewModel
import com.example.v5rules.ui.compose.screen.discipline.DisciplineDetailScreen
import com.example.v5rules.ui.compose.screen.discipline.DisciplineScreen
import com.example.v5rules.ui.compose.screen.HomeScreen
import com.example.v5rules.ui.compose.screen.discipline.RitualScreen
import com.example.v5rules.ui.compose.screen.discipline.DisciplinePowerScreen
import com.example.v5rules.ui.compose.screen.InputScreen
import com.example.v5rules.ui.compose.screen.lore.LoreDetailsScreen
import com.example.v5rules.ui.compose.screen.lore.LoreListScreen
import com.example.v5rules.ui.compose.screen.predator.PredatorTypeDetailsScreen
import com.example.v5rules.ui.compose.screen.predator.PredatorTypeListScreen
import com.example.v5rules.ui.compose.screen.rule.RuleListScreen
import com.example.v5rules.ui.compose.screen.rule.RulesDetailsScreen
import com.example.v5rules.ui.compose.screen.lore.SubLoreDetail
import com.example.v5rules.ui.compose.screen.rule.SubRuleDetail
import com.example.v5rules.ui.theme.V5RulesTheme
import com.example.v5rules.ui.viewModel.ClanViewModel
import com.example.v5rules.ui.viewModel.NPCGeneratorViewModel
import com.example.v5rules.ui.viewModel.PredatorTypeViewModel
import com.example.v5rules.ui.viewModel.RulesViewModel
import com.example.v5rules.utils.ClanDetailsNav
import com.example.v5rules.utils.ClansNav
import com.example.v5rules.utils.DisciplineDetailsNav
import com.example.v5rules.utils.DisciplinePowerNav
import com.example.v5rules.utils.DisciplinesNav
import com.example.v5rules.utils.HomeNav
import com.example.v5rules.utils.LoreNav
import com.example.v5rules.utils.LoreDetailsNav
import com.example.v5rules.utils.NPCGeneratorNav
import com.example.v5rules.utils.PredatorTypeDetailsNav
import com.example.v5rules.utils.PredatorTypesNav
import com.example.v5rules.utils.RitualNav
import com.example.v5rules.utils.RulesNav
import com.example.v5rules.utils.RulesDetailsNav
import com.example.v5rules.utils.SubLoreNav
import com.example.v5rules.utils.SubRuleNav
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("MainActivity", "onCreate called")
        setContent {
            V5RulesApp()
        }
    }
}

@Composable
fun V5RulesApp(
) {
    val disciplineViewModel: DisciplineViewModel = hiltViewModel<DisciplineViewModel>()
    val clanViewModel: ClanViewModel = hiltViewModel<ClanViewModel>()
    val predatorTypeViewModel: PredatorTypeViewModel = hiltViewModel<PredatorTypeViewModel>()
    val rulesViewModel: RulesViewModel = hiltViewModel<RulesViewModel>()
    val loreViewModel: LoreViewModel = hiltViewModel<LoreViewModel>()
    val npcGeneratorViewModel: NPCGeneratorViewModel = hiltViewModel<NPCGeneratorViewModel>()

    val navController = rememberNavController()
    V5RulesTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.secondary
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
    }
}
