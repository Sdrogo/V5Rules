package com.example.v5rules.ui.compose.screen.sheet.visualization

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.v5rules.data.Discipline
import com.example.v5rules.data.DisciplinePower
import com.example.v5rules.ui.compose.component.CustomContentExpander
import com.example.v5rules.ui.compose.component.DisciplineIcon
import com.example.v5rules.ui.compose.component.DotsForAttribute
import com.example.v5rules.utils.CharacterSheetEvent
import com.example.v5rules.viewModel.CharacterSheetViewModel
import com.example.v5rules.DisciplinePowerNav

@Composable
fun DisciplineSelectionVisualization(viewModel: CharacterSheetViewModel, navController: NavHostController) {
    val uiState by viewModel.uiState.collectAsState()
    val characterDisciplines = uiState.character.disciplines

    Column(modifier = Modifier
        .fillMaxWidth()
        .padding( horizontal = 8.dp)) {
        characterDisciplines.forEach {  discipline ->
            Column {
                CustomContentExpander(
                    useFullWidth = true,
                    header = {
                        DisciplineHeaderItem(discipline)
                    },
                    content = {
                        Column(modifier = Modifier.padding(8.dp)) {
                            val disciplinePowers =
                                discipline.selectedDisciplinePowers.filter { it.level > 0 }
                            disciplinePowers.forEach { power ->
                                DisciplinePowerItem(power, discipline, viewModel, navController)
                            }
                        }
                    }
                )

            }
        }
    }
}

@Composable
fun DisciplineHeaderItem(discipline: Discipline) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        DisciplineIcon(
            disciplineId = discipline.id,
            contentDescription = discipline.title,
            size = 32.dp
        )

        DotsForAttribute(label = discipline.title, level = discipline.level)
    }
}

@Composable
fun DisciplinePowerItem(
    power: DisciplinePower,
    discipline: Discipline,
    viewModel: CharacterSheetViewModel,
    navController: NavHostController
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                viewModel.onEvent(CharacterSheetEvent.SaveClicked)
                navController.navigate(
                    DisciplinePowerNav(disciplineId = discipline.id, subDisciplineId = power.id)
                )
            }
            .padding(start = 16.dp, end = 8.dp, top = 4.dp, bottom = 4.dp)
    ) {
        Text(
            text = "${power.title} (Livello ${power.level})",
            modifier = Modifier.weight(1f)
        )
    }
}