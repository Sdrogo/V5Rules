package com.example.v5rules.ui.compose.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.v5rules.R
import com.example.v5rules.data.Discipline
import com.example.v5rules.ui.compose.component.CommonScaffold
import com.example.v5rules.ui.compose.component.DisciplineIcon
import com.example.v5rules.ui.viewModel.DisciplineViewModel
import com.example.v5rules.ui.viewModel.DisciplineUiState

@Composable
fun DisciplineScreen(
    viewModel: DisciplineViewModel,
    navController: NavHostController
) {
    val uiState by viewModel.disciplineUiState.collectAsState()

    CommonScaffold(navController = navController, title = stringResource(id = R.string.discipline_screen_title)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .background(color = MaterialTheme.colorScheme.secondary)

        ) {
            when (uiState) {
                is DisciplineUiState.Loading -> Text("Loading...")
                is DisciplineUiState.Success -> {
                    val disciplines = (uiState as DisciplineUiState.Success).disciplines
                    LazyColumn {
                        items(disciplines) { discipline ->
                            DisciplineItem(discipline, navController)
                        }
                    }
                }
                is DisciplineUiState.Error -> Text("Error: ${(uiState as DisciplineUiState.Error).message}")
            }
        }
    }
}


@Composable
fun DisciplineItem(discipline: Discipline, navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.secondary)
    ) {
        Row(modifier = Modifier
            .fillMaxWidth()
            .clickable { navController.navigate("discipline_detail_screen/${discipline.id}") }) {
            DisciplineIcon(
                disciplineId = discipline.id,
                contentDescription = discipline.title,
                size = 40.dp
            )
            Text(
                text = discipline.title,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
            )
        }

    }
}