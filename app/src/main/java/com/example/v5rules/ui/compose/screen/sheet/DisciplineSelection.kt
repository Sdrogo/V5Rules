package com.example.v5rules.ui.compose.screen.sheet

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.v5rules.DisciplinePowerNav
import com.example.v5rules.R
import com.example.v5rules.RitualNav
import com.example.v5rules.data.Discipline
import com.example.v5rules.data.DisciplinePower
import com.example.v5rules.data.Ritual
import com.example.v5rules.data.RitualPower
import com.example.v5rules.ui.compose.component.CustomContentExpander
import com.example.v5rules.ui.compose.component.DisciplineIcon
import com.example.v5rules.utils.CharacterSheetEvent
import com.example.v5rules.viewModel.CharacterSheetViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DisciplineSection(
    viewModel: CharacterSheetViewModel,
    navController: NavHostController,
) {
    val uiState by viewModel.uiState.collectAsState()
    val disciplines by viewModel.disciplines.collectAsState()
    val rituals = disciplines.filter { it.ritual != null }.mapNotNull { it.ritual }
    val characterDisciplines = uiState.character.disciplines
    val characterRituals = uiState.character.learnedRituals
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val ritualSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val coroutineScope = rememberCoroutineScope()
    var selectedDisciplineForPower by remember { mutableStateOf<Discipline?>(null) }
    var selectedRitualsForPower by remember { mutableStateOf<Ritual?>(null) }

    var showAddPowerSheet by remember { mutableStateOf(false) }
    var showAddRitualSheet by remember { mutableStateOf(false) }
    var expanded by remember { mutableStateOf(false) }

    val focusManager = LocalFocusManager.current

    if (showAddPowerSheet) {
        ModalBottomSheet(
            onDismissRequest = {
                showAddPowerSheet = false
            },
            sheetState = sheetState
        ) {
            Column(
                modifier = Modifier
                    .navigationBarsPadding()
                    .imePadding()
                    .padding(bottom = 16.dp)
            ) {
                selectedDisciplineForPower?.let { discipline ->
                    AddPowerBottomSheet(
                        discipline = discipline,
                        allDisciplines = disciplines,
                        onPowerSelected = { power ->
                            viewModel.onEvent(
                                CharacterSheetEvent.DisciplinePowerAdded(
                                    discipline.title,
                                    power
                                )
                            )
                            coroutineScope.launch {
                                sheetState.hide()
                                showAddPowerSheet = false
                            }
                        }
                    )
                }
            }
        }
    }

    if (showAddRitualSheet) {
        ModalBottomSheet(
            onDismissRequest = {
                showAddRitualSheet = false
            },
            sheetState = ritualSheetState
        ) {
            Column(
                modifier = Modifier
                    .navigationBarsPadding()
                    .imePadding()
                    .padding(bottom = 16.dp)
            ) {
                selectedRitualsForPower?.let { ritual ->
                    AddRitualPowerBottomSheet(
                        ritual = ritual,
                        allRituals = rituals,
                        onPowerSelected = { power ->
                            viewModel.onEvent(
                                CharacterSheetEvent.RitualPowerAdded(
                                    ritual,
                                    power
                                )
                            )
                            coroutineScope.launch {
                                sheetState.hide()
                                showAddRitualSheet = false
                            }
                        }
                    )
                }
            }
        }
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        ) {
            OutlinedTextField(
                value = "",
                onValueChange = {},
                label = { Text("Aggiungi Disciplina") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                readOnly = true,
                enabled = false,
                colors = OutlinedTextFieldDefaults.colors(
                    disabledTextColor = MaterialTheme.colorScheme.onSurface,
                    disabledBorderColor = MaterialTheme.colorScheme.outline,
                    disabledPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    disabledTrailingIconColor = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                modifier = Modifier.fillMaxWidth()
            )
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .clickable(onClick = { expanded = !expanded })
            )
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.fillMaxWidth()
            ) {
                val availableDisciplines = disciplines.filter { discipline ->
                    characterDisciplines.none { it.title == discipline.title }
                }.sortedBy { it.title }

                if (availableDisciplines.isEmpty()) {
                    DropdownMenuItem(
                        text = { Text("Nessuna disciplina disponibile") },
                        enabled = false,
                        onClick = {}
                    )
                } else {
                    availableDisciplines.forEach { discipline ->
                        DropdownMenuItem(
                            text = { Text(discipline.title) },
                            leadingIcon = {
                                DisciplineIcon(
                                    disciplineId = discipline.id,
                                    contentDescription = discipline.title,
                                    size = 24.dp
                                )
                            },
                            onClick = {
                                viewModel.onEvent(CharacterSheetEvent.DisciplineChanged(discipline))
                                expanded = false
                                focusManager.clearFocus()
                            }
                        )
                    }
                }
            }
        }


        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {
            items(characterDisciplines) { discipline ->
                Column {
                    CustomContentExpander(
                        useFullWidth = true,
                        header = {
                            DisciplineHeaderItem(discipline, viewModel)
                        },
                        content = {
                            Column(modifier = Modifier.padding(8.dp)) {
                                val disciplinePowers =
                                    discipline.selectedDisciplinePowers.filter { it.level > 0 }
                                disciplinePowers.forEach { power ->
                                    DisciplinePowerItem(power, discipline, viewModel, navController)
                                }
                                if (disciplinePowers.size < discipline.level) {
                                    TextButton(
                                        onClick = {
                                            selectedDisciplineForPower = discipline
                                            showAddPowerSheet = true
                                        },
                                        modifier = Modifier
                                            .padding(8.dp)
                                    ) {
                                        Text("Aggiungi Potere")
                                    }
                                }
                                characterRituals.filter { it.title == discipline.title }
                                    .forEach { ritual ->
                                        Spacer(modifier = Modifier.height(12.dp))
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(vertical = 4.dp)
                                        ) {
                                            Text(
                                                text = stringResource(id = R.string.discipline_rituals),
                                                style = MaterialTheme.typography.titleLarge
                                            )
                                            InteractiveDisciplineDots(
                                                currentValue = ritual.level,
                                                onValueChange = { newLevel ->
                                                    viewModel.onEvent(
                                                        CharacterSheetEvent.updateRitualLevel(
                                                            discipline.title,
                                                            ritual,
                                                            newLevel
                                                        )
                                                    )
                                                }
                                            )
                                        }
                                        ritual.ritualsPowers.forEach { ritualPower ->
                                            RitualPowerItem(
                                                power = ritualPower,
                                                ritual = ritual,
                                                discipline = discipline,
                                                viewModel = viewModel,
                                                navController = navController
                                            )
                                        }
                                        if (ritual.ritualsPowers.size < ritual.level) {
                                            TextButton(
                                                onClick = {
                                                    selectedRitualsForPower = ritual
                                                    showAddRitualSheet = true
                                                },
                                                modifier = Modifier
                                                    .padding(8.dp)
                                            ) {
                                                Text("Aggiungi Rituale")
                                            }
                                        }
                                    }
                            }

                        }
                    )
                }
            }
        }
    }

}

@Composable
fun DisciplineHeaderItem(discipline: Discipline, viewModel: CharacterSheetViewModel) {
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
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = discipline.title,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.weight(1f)
        )
        Spacer(modifier = Modifier.width(8.dp))
        InteractiveDisciplineDots(
            currentValue = discipline.level,
            onValueChange = { newLevel ->
                viewModel.onEvent(
                    CharacterSheetEvent.DisciplineLevelChanged(
                        discipline,
                        newLevel
                    )
                )
            }
        )
    }
}

@Composable
private fun InteractiveDisciplineDots(
    currentValue: Int,
    onValueChange: (Int) -> Unit
) {
    Row {
        for (i in 1..5) {
            Box(
                modifier = Modifier
                    .size(22.dp)
                    .padding(2.dp)
                    .clip(CircleShape)
                    .background(
                        if (i <= currentValue) MaterialTheme.colorScheme.secondary else Color.Transparent
                    )
                    .border(
                        width = 1.dp,
                        color = if (i <= currentValue) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(
                            alpha = 0.3f
                        ),
                        shape = CircleShape
                    )
                    .clickable {
                        val newLevel = if (i == currentValue) 0 else i
                        onValueChange(newLevel)
                    }
            )
        }
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
        IconButton(onClick = {
            viewModel.onEvent(
                CharacterSheetEvent.DisciplinePowerRemoved(
                    discipline.title,
                    power
                )
            )
        }) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Rimuovi Potere",
                tint = MaterialTheme.colorScheme.error
            )
        }
    }
}

@Composable
fun RitualPowerItem(
    power: RitualPower,
    ritual: Ritual,
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
                    RitualNav(disciplineId = discipline.id, ritualId = power.id)
                )
            }
            .padding(start = 16.dp, end = 8.dp, top = 4.dp, bottom = 4.dp)
    ) {
        Text(
            text = "${power.title} (Livello ${power.level})",
            modifier = Modifier.weight(1f)
        )
        IconButton(onClick = {
            viewModel.onEvent(
                CharacterSheetEvent.RitualPowerRemoved(
                    ritual,
                    power
                )
            )
        }) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Rimuovi Potere",
                tint = MaterialTheme.colorScheme.error
            )
        }
    }
}

@Composable
fun AddPowerBottomSheet(
    discipline: Discipline,
    allDisciplines: List<Discipline>,
    onPowerSelected: (DisciplinePower) -> Unit,
) {
    val fullDiscipline = allDisciplines.firstOrNull { it.id == discipline.id }
    val selectedPowers = discipline.selectedDisciplinePowers
    val availablePowers = fullDiscipline?.disciplinePowers?.filter { power ->
        power.level <= discipline.level && !selectedPowers.any { it.title == power.title }
    }?.sortedBy { it.level } ?: emptyList()

    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        Text("Aggiungi Potere a ${discipline.title}", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(16.dp))
        Box(modifier = Modifier.heightIn(max = 300.dp)) {
            if (availablePowers.isEmpty()) {
                Text("Non ci sono poteri disponibili per questa disciplina e livello")
            } else {
                LazyColumn(modifier = Modifier.fillMaxWidth()) {
                    items(availablePowers) { power ->
                        Text(
                            text = "${power.title} (Livello ${power.level})",
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onPowerSelected(power)
                                }
                                .padding(8.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AddRitualPowerBottomSheet(
    ritual: Ritual,
    allRituals: List<Ritual>,
    onPowerSelected: (RitualPower) -> Unit,
) {
    val fullRituals = allRituals.firstOrNull { it.id == ritual.id }
    val selectedPowers = ritual.ritualsPowers
    val availablePowers = fullRituals?.ritualsPowers?.filter { power ->
        power.level <= ritual.level && !selectedPowers.any { it.title == power.title }
    }?.sortedBy { it.level } ?: emptyList()

    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        Text("Aggiungi Potere a ${ritual.title}", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(16.dp))
        Box(modifier = Modifier.heightIn(max = 300.dp)) {
            if (availablePowers.isEmpty()) {
                Text("Non ci sono poteri disponibili per questo Rituale e livello")
            } else {
                LazyColumn(modifier = Modifier.fillMaxWidth()) {
                    items(availablePowers) { power ->
                        Text(
                            text = "${power.title} (Livello ${power.level})",
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onPowerSelected(power)
                                }
                                .padding(8.dp)
                        )
                    }
                }
            }
        }
    }
}
