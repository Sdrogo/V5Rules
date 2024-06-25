package com.example.v5rules

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.v5rules.ui.compose.screen.ClanDetailScreen
import com.example.v5rules.ui.compose.screen.ClanListScreen
import com.example.v5rules.ui.viewModel.DisciplineViewModel
import com.example.v5rules.ui.viewModel.DisciplineViewModelFactory
import com.example.v5rules.ui.compose.screen.DisciplineDetailScreen
import com.example.v5rules.ui.compose.screen.DisciplineScreen
import com.example.v5rules.ui.compose.screen.HomeScreen
import com.example.v5rules.ui.compose.screen.RitualScreen
import com.example.v5rules.ui.compose.screen.DisciplinePowerScreen
import com.example.v5rules.ui.theme.V5RulesTheme
import com.example.v5rules.ui.viewModel.ClanViewModel
import com.example.v5rules.ui.viewModel.ClanViewModelFactory

class MainActivity : ComponentActivity() {

    private val disciplineViewModel: DisciplineViewModel by viewModels {
        DisciplineViewModelFactory(this)
    }
    private val clanViewModel: ClanViewModel by viewModels {
        ClanViewModelFactory(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            V5RulesApp(disciplineViewModel, clanViewModel)
        }
    }
}

@Composable
fun V5RulesApp(disciplineViewModel: DisciplineViewModel,
               clanViewModel: ClanViewModel
) {
    V5RulesTheme {
        val navController = rememberNavController()
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
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
                    DisciplineDetailScreen(disciplineId = disciplineId ?: "", disciplineViewModel, navController)
                }
                composable("discipline_detail_screen/{disciplineId}/{subDisciplineId}") { backStackEntry ->
                    val disciplineId = backStackEntry.arguments?.getString("disciplineId")
                    val subDisciplineId = backStackEntry.arguments?.getString("subDisciplineId")
                    DisciplinePowerScreen(disciplineId = disciplineId ?: "",
                        disciplinePowerId = subDisciplineId?: "",
                        viewModel = disciplineViewModel,
                        navController = navController)
                }
                composable("discipline_ritual_screen/{disciplineId}/{ritualId}") { backStackEntry ->
                    val disciplineId = backStackEntry.arguments?.getString("disciplineId")
                    val ritualId = backStackEntry.arguments?.getString("ritualId")
                    RitualScreen(disciplineId = disciplineId ?: "",
                        ritualId = ritualId?: "",
                        viewModel = disciplineViewModel,
                        navController = navController)
                }

                composable("clan_screen") {
                    ClanListScreen(viewModel = clanViewModel, navController = navController)
                }
                composable("clan_screen/{clanName}") { backStackEntry ->
                    val clanName = backStackEntry.arguments?.getString("clanName")
                    ClanDetailScreen(clanViewModel = clanViewModel, navController = navController, clanName = clanName ?: "")
                }
            }
        }
    }
}
