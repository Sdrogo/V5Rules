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
import com.example.v5rules.utils.ClanDetailsScreen
import com.example.v5rules.utils.ClansScreen
import com.example.v5rules.utils.DisciplineDetailsScreen
import com.example.v5rules.utils.DisciplinePowerScreen
import com.example.v5rules.utils.DisciplinesScreen
import com.example.v5rules.utils.HomeScreen
import com.example.v5rules.utils.LoreScreen
import com.example.v5rules.utils.LoreDetailsScreen
import com.example.v5rules.utils.NPCGeneratorScreen
import com.example.v5rules.utils.PredatorTypeDetailsScreen
import com.example.v5rules.utils.PredatorTypesScreen
import com.example.v5rules.utils.RitualScreen
import com.example.v5rules.utils.RulesScreen
import com.example.v5rules.utils.RulesDetailsScreen
import com.example.v5rules.utils.SubLoreScreen
import com.example.v5rules.utils.SubRuleScreen
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
    V5RulesTheme{
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.secondary
        ) {
            NavHost(navController = navController, startDestination = HomeScreen) {
                composable<HomeScreen> {
                    HomeScreen(navController)
                }
                composable<DisciplinesScreen> {
                    DisciplineScreen(disciplineViewModel, navController)
                }
                composable<DisciplineDetailsScreen> { backStackEntry ->
                    val entry = backStackEntry.toRoute<DisciplineDetailsScreen>()
                    DisciplineDetailScreen(
                        disciplineId = entry.disciplineId,
                        disciplineViewModel,
                        navController
                    )
                }
                composable<DisciplinePowerScreen> { backStackEntry ->
                    val entry = backStackEntry.toRoute<DisciplinePowerScreen>()
                    DisciplinePowerScreen(
                        disciplineId = entry.disciplineId,
                        disciplinePowerId = entry.subDisciplineId,
                        viewModel = disciplineViewModel,
                        navController = navController
                    )
                }
                composable<RitualScreen> { backStackEntry ->
                    val entry = backStackEntry.toRoute<RitualScreen>()
                    RitualScreen(
                        disciplineId = entry.disciplineId,
                        ritualId = entry.ritualId,
                        viewModel = disciplineViewModel,
                        navController = navController
                    )
                }
                composable<PredatorTypesScreen> {
                    PredatorTypeListScreen(
                        viewModel = predatorTypeViewModel,
                        navController = navController
                    )
                }
                composable<PredatorTypeDetailsScreen>{ backStackEntry ->
                    val entry = backStackEntry.toRoute<PredatorTypeDetailsScreen>()
                    PredatorTypeDetailsScreen(
                        predatorTypeViewModel,
                        navController,
                        entry.predatorName
                    )
                }
                composable<ClansScreen> {
                    ClanListScreen(viewModel = clanViewModel, navController = navController)
                }
                composable<ClanDetailsScreen> { backStackEntry ->
                    val entry = backStackEntry.toRoute<ClanDetailsScreen>()
                    ClanDetailScreen(
                        clanViewModel = clanViewModel,
                        navController = navController,
                        clanName = entry.clanName
                    )
                }
                composable<LoreScreen> {
                    LoreListScreen(
                        loreViewModel = loreViewModel, navController = navController)
                }
                composable<LoreDetailsScreen> { backStackEntry ->
                    val entry = backStackEntry.toRoute<LoreDetailsScreen>()
                      LoreDetailsScreen(
                        loreViewModel = loreViewModel,
                        navController = navController,
                        title = entry.title
                    )
                }
                composable<SubLoreScreen> { backStackEntry ->
                    val entry = backStackEntry.toRoute<SubLoreScreen>()
                    SubLoreDetail(
                        loreViewModel = loreViewModel,
                        chapterTitle = entry.title,
                        sectionTitle = entry.section,
                        navController = navController
                    )
                }
                composable<NPCGeneratorScreen> {
                    InputScreen(modifier = Modifier, viewModel = npcGeneratorViewModel, navController = navController)
                }
                composable<RulesScreen> {
                    RuleListScreen(viewModel = rulesViewModel, navController = navController)
                }
                composable<RulesDetailsScreen> { backStackEntry ->
                    val entry = backStackEntry.toRoute<RulesDetailsScreen>()
                    RulesDetailsScreen(
                        rulesViewModel = rulesViewModel,
                        navController = navController,
                        title = entry.title
                    )
                }
                composable<SubRuleScreen> { backStackEntry ->
                    val entry = backStackEntry.toRoute<SubRuleScreen>()
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
