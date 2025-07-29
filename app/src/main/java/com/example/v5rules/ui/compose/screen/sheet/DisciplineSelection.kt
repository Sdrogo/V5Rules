package com.example.v5rules.ui.compose.screen.sheet

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.v5rules.DisciplinePowerNav
import com.example.v5rules.data.Discipline
import com.example.v5rules.data.DisciplinePower
import com.example.v5rules.ui.compose.component.CustomContentExpander
import com.example.v5rules.ui.compose.component.DisciplineIcon
import com.example.v5rules.utils.CharacterSheetEvent
import com.example.v5rules.viewModel.CharacterSheetViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DisciplineSection(viewModel: CharacterSheetViewModel, navController: NavHostController) {
    val uiState by viewModel.uiState.collectAsState()
    val disciplines by viewModel.disciplines.collectAsState()
    val characterDisciplines = uiState.character.disciplines

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val coroutineScope = rememberCoroutineScope()
    var selectedDisciplineForPower by remember { mutableStateOf<Discipline?>(null) }

    var showAddPowerSheet by remember { mutableStateOf(false) }

    var expanded by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    if (showAddPowerSheet) {
        ModalBottomSheet(
            onDismissRequest = {
                showAddPowerSheet = false
            },
            sheetState = sheetState
        ) {
            Column(modifier = Modifier.navigationBarsPadding().imePadding().padding(bottom = 16.dp)) {
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


    LazyColumn(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
        item {
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = {
                    expanded = !expanded
                    if (expanded) {
                        focusRequester.requestFocus()
                    } else {
                        focusManager.clearFocus()
                    }
                }
            ) {
                OutlinedTextField(
                    value = "",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Aggiungi Disciplina") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                        .focusRequester(focusRequester)
                        .onKeyEvent {
                            if (it.key == Key.Escape) {
                                expanded = false
                                true
                            } else {
                                false
                            }
                        },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            keyboardController?.hide()
                            focusManager.clearFocus()
                        }
                    )
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.focusable(false)
                ) {
                    disciplines.filter { discipline ->
                        characterDisciplines.none { it.title == discipline.title }
                    }.sortedBy { it.title }.forEach { discipline ->
                        DropdownMenuItem(
                            onClick = {
                                viewModel.onEvent(CharacterSheetEvent.DisciplineChanged(discipline))
                                expanded = false
                                keyboardController?.hide()
                                focusManager.clearFocus()
                            },
                            leadingIcon = {
                                DisciplineIcon(
                                    disciplineId = discipline.id,
                                    contentDescription = discipline.title,
                                    size = 24.dp
                                )
                            },
                            text = { Text(discipline.title) }
                        )
                    }
                }
            }
        }

        items(characterDisciplines) { discipline ->
            Column {
                CustomContentExpander(
                    useFullWidth = true,
                    header = {
                        DisciplineHeaderItem(discipline, viewModel) // Pass viewModel
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
                        }
                    }
                )

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
        Text(text = discipline.title, style = MaterialTheme.typography.titleMedium, modifier = Modifier.weight(1f))
        Spacer(modifier = Modifier.width(8.dp))
        InteractiveDisciplineDots(
            currentValue = discipline.level,
            onValueChange = { newLevel ->
                viewModel.onEvent(
                    CharacterSheetEvent.DisciplineLevelChanged(
                        discipline.title,
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
                        color = if (i <= currentValue) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
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
fun AddPowerBottomSheet(
    discipline: Discipline,
    allDisciplines: List<Discipline>,
    onPowerSelected: (DisciplinePower) -> Unit,
) {
    val fullDiscipline = allDisciplines.firstOrNull { it.id == discipline.id }
    val selectedPowers = discipline.selectedDisciplinePowers
    val availablePowers = fullDiscipline?.disciplinePowers?.filter { power ->
        power.level <= discipline.level && !selectedPowers.any{it.title == power.title}
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