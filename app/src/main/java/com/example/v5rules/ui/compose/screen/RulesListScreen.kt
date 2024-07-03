package com.example.v5rules.ui.compose.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.v5rules.R
import com.example.v5rules.ui.compose.component.CommonScaffold
import com.example.v5rules.ui.viewModel.RulesUiState
import com.example.v5rules.ui.viewModel.RulesViewModel

@Composable
fun RuleListScreen(viewModel: RulesViewModel, navController: NavHostController) {

    val uiState by viewModel.rulesUiState.collectAsState()

    CommonScaffold(
        navController = navController,
        title = stringResource(id = R.string.rules)
    )
    {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.secondary)
                .padding(start = 16.dp)
        ) {
            when (uiState) {
                is RulesUiState.Loading -> Text(
                    "Loading...",
                    color = MaterialTheme.colorScheme.primary,
                )

                is RulesUiState.Success -> {
                    val chapters = (uiState as RulesUiState.Success).chapters
                    LazyColumn(modifier = Modifier.fillMaxWidth()) {
                        items(chapters) { chapter ->
                            Text(
                                text = chapter.title,
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { navController.navigate("rules_screen/${chapter.title}") }
                                    .padding(8.dp)
                            )
                        }
                    }
                }

                is RulesUiState.Error -> Text(
                    "Error: ${(uiState as RulesUiState.Error).message}",
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}


