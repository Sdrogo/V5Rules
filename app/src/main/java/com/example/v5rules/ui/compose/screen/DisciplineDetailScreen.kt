package com.example.v5rules.ui.compose.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.v5rules.data.SubDiscipline
import com.example.v5rules.ui.ViewModel.DisciplineViewModel

@Composable
fun DisciplineDetailScreen(
    disciplineId: String,
    viewModel: DisciplineViewModel,
    navController: NavHostController
) {
    val discipline = viewModel.allDisciplines.find { it.id == disciplineId }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Button(
            onClick = { navController.navigateUp() },
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            Text("Back")
        }

        if (discipline != null) {
            LazyColumn(modifier = Modifier.padding(16.dp)) {
                item {
                    Text(
                        text = discipline.title,
                        style = MaterialTheme.typography.headlineMedium,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    Text(
                        text = discipline.description,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                }

                items(discipline.subDisciplines.groupBy { it.level }.entries.toList()) { (level, subDisciplines) ->
                    var expanded by remember { mutableStateOf(false) }

                    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
                        Text(
                            text = "Level $level",
                            style = MaterialTheme.typography.headlineSmall,
                            modifier = Modifier
                                .clickable { expanded = !expanded }
                                .padding(8.dp)
                        )

                        AnimatedVisibility(visible = expanded) {
                            Column(modifier = Modifier.padding(8.dp)) {
                                subDisciplines.forEach { subDiscipline ->
                                    SubDisciplineItem(subDiscipline)
                                }
                            }
                        }
                    }
                }
            }
        } else {
            Text(text = "Discipline not found", style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
fun SubDisciplineItem(subDiscipline: SubDiscipline) {
    Column(modifier = Modifier.padding(8.dp)) {
        Text(
            text = subDiscipline.title,
            style = MaterialTheme.typography.bodyLarge,
            fontSize = 18.sp,
            modifier = Modifier.padding(vertical = 4.dp)
        )
        Text(
            text = "Cost: ${subDiscipline.cost}",
            style = MaterialTheme.typography.bodyMedium,
            fontSize = 16.sp
        )
        Text(
            text = "Dice Pool: ${subDiscipline.dicePool}",
            style = MaterialTheme.typography.bodyMedium,
            fontSize = 16.sp
        )
        Text(
            text = "Duration: ${subDiscipline.duration}",
            style = MaterialTheme.typography.bodyMedium,
            fontSize = 16.sp
        )
        Text(
            text = subDiscipline.description,
            style = MaterialTheme.typography.bodySmall,
            fontSize = 14.sp,
            modifier = Modifier.padding(vertical = 4.dp)
        )
    }
}
