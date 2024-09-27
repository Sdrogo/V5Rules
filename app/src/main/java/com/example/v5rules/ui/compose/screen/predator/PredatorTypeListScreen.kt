package com.example.v5rules.ui.compose.screen.predator

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.v5rules.R
import com.example.v5rules.ui.compose.component.CommonScaffold
import com.example.v5rules.ui.viewModel.PredatorTypeUiState
import com.example.v5rules.ui.viewModel.PredatorTypeViewModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PredatorTypeListScreen(viewModel: PredatorTypeViewModel, navController: NavHostController) {

    val uiState by viewModel.predatorTypeUiState.collectAsState()

    CommonScaffold(
        navController = navController,
        title = stringResource(id = R.string.predator_type_screen_title)
    )
    {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 16.dp)
                .background(color = MaterialTheme.colorScheme.secondary)
        ) {
            when (uiState) {
                is PredatorTypeUiState.Loading -> Text("Loading...")
                is PredatorTypeUiState.Success -> {
                    val predators = (uiState as PredatorTypeUiState.Success).clans
                    LazyColumn(modifier = Modifier.fillMaxWidth()) {
                        item {
                            val orientation = LocalConfiguration.current.orientation
                            val widthByOrientation =
                                if (orientation == Configuration.ORIENTATION_LANDSCAPE) 0.3f else 1f
                            FlowRow(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(8.dp),
                                horizontalArrangement = Arrangement.Center,
                            ) {
                                predators.forEach {
                                    Text(
                                        text = it.name,
                                        style = MaterialTheme.typography.headlineSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier
                                            .fillMaxWidth(widthByOrientation)
                                            .clickable { navController.navigate("predator_type_screen/${it.name}") }
                                            .padding(8.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                is PredatorTypeUiState.Error -> Text(
                    "Error: ${(uiState as PredatorTypeUiState.Error).message}",
                    color = MaterialTheme.colorScheme.primary,
                )
            }
        }
    }
}


