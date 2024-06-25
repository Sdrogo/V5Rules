package com.example.v5rules.ui.compose.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.v5rules.R
import com.example.v5rules.ui.compose.component.CommonScaffold
import com.example.v5rules.ui.viewModel.PredatorTypeUiState
import com.example.v5rules.ui.viewModel.PredatorTypeViewModel

@Composable
fun PredatorTypeListScreen(viewModel: PredatorTypeViewModel, navController: NavHostController){

    val uiState by viewModel.predatorTypeUiState.collectAsState()

    CommonScaffold(navController = navController, title = stringResource(id = R.string.predator_type_screen_title))
    { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 16.dp)
                .padding(innerPadding)
        ) {
            when (uiState) {
                is PredatorTypeUiState.Loading -> Text("Loading...")
                is PredatorTypeUiState.Success -> {
                    val predators = (uiState as PredatorTypeUiState.Success).clans
                    LazyColumn {
                        items(predators) { predator ->
                            Text(
                                text = predator.name,
                                style = MaterialTheme.typography.headlineSmall,
                                modifier = Modifier
                                    .clickable { navController.navigate("predator_type_screen/${predator.name}") }
                                    .padding(8.dp)
                            )
                        }
                    }
                }
                is PredatorTypeUiState.Error -> Text("Error: ${(uiState as PredatorTypeUiState.Error).message}")
            }
        }
    }
}


