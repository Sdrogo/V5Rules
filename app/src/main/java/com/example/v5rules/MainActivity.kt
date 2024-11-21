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
import androidx.navigation.compose.rememberNavController
import com.example.v5rules.ui.theme.V5RulesTheme
import com.example.v5rules.viewModel.LoreViewModel
import com.example.v5rules.viewModel.DisciplineViewModel
import com.example.v5rules.viewModel.ClanViewModel
import com.example.v5rules.viewModel.KindredViewModel
import com.example.v5rules.viewModel.LoresheetViewModel
import com.example.v5rules.viewModel.NPCGeneratorViewModel
import com.example.v5rules.viewModel.PgViewModel
import com.example.v5rules.viewModel.PredatorTypeViewModel
import com.example.v5rules.viewModel.RulesViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("MainActivity", "onCreate called")
        setContent {
            V5RulesTheme {
                V5RulesApp()
            }
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
    val loresheetViewModel: LoresheetViewModel = hiltViewModel<LoresheetViewModel>()
    val kindredViewModel: KindredViewModel = hiltViewModel<KindredViewModel>()
    val pgViewModel: PgViewModel = hiltViewModel<PgViewModel>()

    val navController = rememberNavController()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.secondary
    ) {
        CustomNavHost(
            navController = navController,
            disciplineViewModel = disciplineViewModel,
            clanViewModel = clanViewModel,
            predatorTypeViewModel = predatorTypeViewModel,
            rulesViewModel = rulesViewModel,
            loreViewModel = loreViewModel,
            loresheetViewModel = loresheetViewModel,
            npcGeneratorViewModel = npcGeneratorViewModel,
            kindredViewModel = kindredViewModel,
            pgViewModel = pgViewModel
        )
    }
}
