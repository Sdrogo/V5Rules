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
import com.example.v5rules.ui.viewModel.LoreViewModel
import com.example.v5rules.ui.compose.screen.ClanDetailScreen
import com.example.v5rules.ui.compose.screen.ClanListScreen
import com.example.v5rules.ui.viewModel.DisciplineViewModel
import com.example.v5rules.ui.compose.screen.DisciplineDetailScreen
import com.example.v5rules.ui.compose.screen.DisciplineScreen
import com.example.v5rules.ui.compose.screen.HomeScreen
import com.example.v5rules.ui.compose.screen.RitualScreen
import com.example.v5rules.ui.compose.screen.DisciplinePowerScreen
import com.example.v5rules.ui.compose.screen.InputScreen
import com.example.v5rules.ui.compose.screen.LoreDetailsScreen
import com.example.v5rules.ui.compose.screen.LoreListScreen
import com.example.v5rules.ui.compose.screen.PredatorTypeDetailsScreen
import com.example.v5rules.ui.compose.screen.PredatorTypeListScreen
import com.example.v5rules.ui.compose.screen.RuleListScreen
import com.example.v5rules.ui.compose.screen.RulesDetailsScreen
import com.example.v5rules.ui.compose.screen.SubLoreDetail
import com.example.v5rules.ui.compose.screen.SubRuleDetail
import com.example.v5rules.ui.theme.V5RulesTheme
import com.example.v5rules.ui.viewModel.ClanViewModel
import com.example.v5rules.ui.viewModel.NPCGeneratorViewModel
import com.example.v5rules.ui.viewModel.PredatorTypeViewModel
import com.example.v5rules.ui.viewModel.RulesViewModel
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
            NavHost(navController = navController, startDestination = "home_screen") {
                composable("home_screen") {
                    HomeScreen(navController)
                }
                composable("discipline_screen") {
                    DisciplineScreen(disciplineViewModel, navController)
                }
                composable("discipline_detail_screen/{disciplineId}") { backStackEntry ->
                    val disciplineId = backStackEntry.arguments?.getString("disciplineId")
                    DisciplineDetailScreen(
                        disciplineId = disciplineId ?: "",
                        disciplineViewModel,
                        navController
                    )
                }
                composable("discipline_detail_screen/{disciplineId}/{subDisciplineId}") { backStackEntry ->
                    val disciplineId = backStackEntry.arguments?.getString("disciplineId")
                    val subDisciplineId = backStackEntry.arguments?.getString("subDisciplineId")
                    DisciplinePowerScreen(
                        disciplineId = disciplineId ?: "",
                        disciplinePowerId = subDisciplineId ?: "",
                        viewModel = disciplineViewModel,
                        navController = navController
                    )
                }
                composable("discipline_ritual_screen/{disciplineId}/{ritualId}") { backStackEntry ->
                    val disciplineId = backStackEntry.arguments?.getString("disciplineId")
                    val ritualId = backStackEntry.arguments?.getString("ritualId")
                    RitualScreen(
                        disciplineId = disciplineId ?: "",
                        ritualId = ritualId ?: "",
                        viewModel = disciplineViewModel,
                        navController = navController
                    )
                }
                composable("predator_type_screen") {
                    PredatorTypeListScreen(
                        viewModel = predatorTypeViewModel,
                        navController = navController
                    )
                }
                composable("predator_type_screen/{predatorName}") { backStackEntry ->
                    val predatorNameId = backStackEntry.arguments?.getString("predatorName")
                    PredatorTypeDetailsScreen(
                        predatorTypeViewModel,
                        navController,
                        predatorNameId ?: ""
                    )
                }
                composable("clan_screen") {
                    ClanListScreen(viewModel = clanViewModel, navController = navController)
                }
                composable("clan_screen/{clanName}") { backStackEntry ->
                    val clanName = backStackEntry.arguments?.getString("clanName")
                    ClanDetailScreen(
                        clanViewModel = clanViewModel,
                        navController = navController,
                        clanName = clanName ?: ""
                    )
                }
                composable("lore_screen") {
                    LoreListScreen(
                        loreViewModel = loreViewModel, navController = navController)
                }
                composable("lore_screen/{title}") { backStackEntry ->
                    val chapter = backStackEntry.arguments?.getString("title")
                    LoreDetailsScreen(
                        loreViewModel = loreViewModel,
                        navController = navController,
                        title = chapter ?: ""
                    )
                }
                composable("lore_screen/{title}/{section}") { backStackEntry ->
                    val chapter = backStackEntry.arguments?.getString("title")
                    val section = backStackEntry.arguments?.getString("section")
                    SubLoreDetail(
                        loreViewModel = loreViewModel,
                        chapterTitle = chapter ?: "",
                        sectionTitle = section ?: "",
                        navController = navController
                    )
                }
                composable("npc_generator") {
                    InputScreen(modifier = Modifier, viewModel = npcGeneratorViewModel, navController = navController)
                }
                composable("rules_screen") {
                    RuleListScreen(viewModel = rulesViewModel, navController = navController)
                }
                composable("rules_screen/{title}") { backStackEntry ->
                    val chapter = backStackEntry.arguments?.getString("title")
                    RulesDetailsScreen(
                        rulesViewModel = rulesViewModel,
                        navController = navController,
                        title = chapter ?: ""
                    )
                }
                composable("rules_screen/{title}/{section}") { backStackEntry ->
                    val chapter = backStackEntry.arguments?.getString("title")
                    val section = backStackEntry.arguments?.getString("section")
                    SubRuleDetail(
                        rulesViewModel = rulesViewModel,
                        chapterTitle = chapter ?: "",
                        sectionTitle = section ?: "",
                        navController = navController
                    )
                }
            }
        }
    }

}
