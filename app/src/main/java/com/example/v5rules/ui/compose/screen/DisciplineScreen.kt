package com.example.v5rules.ui.compose.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.v5rules.R
import com.example.v5rules.data.Discipline
import com.example.v5rules.ui.compose.component.CommonScaffold
import com.example.v5rules.ui.compose.component.RemoteIcon
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
                .padding(start = 16.dp)
                .padding(it)
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
    ) {
        Row {
            RemoteIcon(
                imageUrl = discipline.imageUrl,
                contentDescription = discipline.title,
                size = 40.dp
            )
            Text(
                text = discipline.title,
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier
                    .clickable { navController.navigate("discipline_detail_screen/${discipline.id}") }
                    .padding(horizontal = 8.dp)
            )
        }

    }
}