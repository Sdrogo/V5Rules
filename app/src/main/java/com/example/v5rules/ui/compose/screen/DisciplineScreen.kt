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
import com.example.v5rules.ui.compose.component.RemoteIcon
import com.example.v5rules.ui.viewModel.DisciplineViewModel
import com.example.v5rules.ui.viewModel.DisciplineUiState

@Composable
fun DisciplineScreen(
    viewModel: DisciplineViewModel,
    navController: NavHostController
) {
    var searchQuery by remember { mutableStateOf("") }
    val uiState by viewModel.disciplineUiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Row {
            Button(
                onClick = { navController.navigateUp() },
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                Text("Back")
            }
            Spacer(modifier = Modifier.width(8.dp))
            OutlinedTextField(
                value = searchQuery,
                onValueChange = {
                    searchQuery = it
                    viewModel.search(it)
                },
                label = { Text("Search") },
                modifier = Modifier.fillMaxWidth()
            )
        }

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
