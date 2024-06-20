package com.example.v5rules.ui.compose.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.v5rules.data.Discipline
import com.example.v5rules.ui.ViewModel.DisciplineViewModel
import com.example.v5rules.ui.ViewModel.UiState

@Composable
fun DisciplineScreen(
    viewModel: DisciplineViewModel,
    navController: NavHostController
) {
    var searchQuery by remember { mutableStateOf("") }
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        OutlinedTextField(
            value = searchQuery,
            onValueChange = {
                searchQuery = it
                viewModel.search(it)
            },
            label = { Text("Search") },
            modifier = Modifier.fillMaxWidth()
        )

        when (uiState) {
            is UiState.Loading -> Text("Loading...")
            is UiState.Success -> {
                val disciplines = (uiState as UiState.Success).disciplines
                LazyColumn {
                    items(disciplines) { discipline ->
                        DisciplineItem(discipline, navController)
                    }
                }
            }
            is UiState.Error -> Text("Error: ${(uiState as UiState.Error).message}")
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
        Text(
            text = discipline.title,
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier
                .clickable { navController.navigate("discipline_detail_screen/${discipline.id}") }
                .padding(8.dp)
        )
    }
}
