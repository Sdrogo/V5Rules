package com.example.v5rules.ui.compose.screen.sheet

import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.example.v5rules.data.Discipline
import com.example.v5rules.data.DisciplinePower
import com.example.v5rules.ui.compose.component.CustomContentExpander
import com.example.v5rules.ui.compose.component.DisciplineIcon
import com.example.v5rules.ui.compose.component.DotsForAttribute
import com.example.v5rules.utils.CharacterSheetEvent
import com.example.v5rules.viewModel.CharacterSheetViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DisciplineSection(viewModel: CharacterSheetViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    val disciplines by viewModel.disciplines.collectAsState() // Tutte le discipline
    val characterDisciplines = uiState.character.disciplines

    // Stato per il dialogo
    val (showAddPowerDialog, setShowAddPowerDialog) = remember { mutableStateOf(false) }
    val (selectedDisciplineForPower, setSelectedDisciplineForPower) = remember {
        mutableStateOf<Discipline?>(
            null
        )
    }

    // Mappa per gestire l'espansione (nome disciplina -> espanso/non espanso)
    val expandedStates = remember { mutableStateMapOf<String, Boolean>() }

    // Inizializza lo stato di espansione, *solo* se non è già stato inizializzato
    LaunchedEffect(characterDisciplines) {
        characterDisciplines.forEach { discipline ->
            if (!expandedStates.containsKey(discipline.title)) { // Controlla se la chiave esiste già
                expandedStates[discipline.title] = false // Inizialmente chiuso
            }
        }
        val disciplineNames = characterDisciplines.map { it.title }.toSet()
        expandedStates.keys.retainAll(disciplineNames)
    }

    var expanded by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

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
                        .menuAnchor(MenuAnchorType.PrimaryNotEditable)
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
                            text = { Text(discipline.title) } //usa text, non content
                        )
                    }
                }
            }
        }
        item {
            selectedDisciplineForPower?.disciplinePowers?.filter { it.title != null }
            if (showAddPowerDialog && selectedDisciplineForPower != null) {
                AddPowerDialog(
                    discipline = selectedDisciplineForPower,
                    allDisciplines = disciplines,
                    onDismiss = { setShowAddPowerDialog(false) },
                    onPowerSelected = { power ->
                        viewModel.onEvent(
                            CharacterSheetEvent.DisciplinePowerAdded(
                                selectedDisciplineForPower.title,
                                power
                            )
                        )
                        setShowAddPowerDialog(false)
                    }
                )
            }
        }
        items(characterDisciplines) { discipline ->
            Column {
                CustomContentExpander(
                    useFullWidth = true,
                    header = {
                        DisciplineHeaderItem(discipline)
                    },
                    content = {
                        Column(modifier = Modifier.padding(8.dp)) {
                            Slider(
                                value = discipline.level.toFloat(),
                                onValueChange = { newValue ->
                                    viewModel.onEvent(
                                        CharacterSheetEvent.DisciplineLevelChanged(
                                            discipline.title,
                                            newValue.toInt()
                                        )
                                    )
                                },
                                valueRange = 0f..5f,
                                steps = 4,
                                colors = SliderDefaults.colors(
                                    activeTrackColor = MaterialTheme.colorScheme.tertiary
                                )
                            )
                        }
                    }
                )
                val disciplinePowers =
                    discipline.selectedDisciplinePowers.filter { it.level > 0 }.orEmpty()
                disciplinePowers.forEach { power ->
                    DisciplinePowerItem(power, discipline, viewModel)
                }
                if (disciplinePowers.size < discipline.level) {
                    TextButton(
                        onClick = {
                            setSelectedDisciplineForPower(discipline)
                            setShowAddPowerDialog(true)
                        },
                        modifier = Modifier
                            .padding(8.dp)
                    ) {
                        Text("Aggiungi Potere")
                    }
                }
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
    viewModel: CharacterSheetViewModel
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth() // Aggiunto fillMaxWidth()
            .padding(start = 16.dp, end = 8.dp, top = 4.dp, bottom = 4.dp)
    ) {
        Text(
            text = "${power.title} (Livello ${power.level})",
            modifier = Modifier.fillMaxWidth() // Invece di .weight(1f)
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
                imageVector = Icons.Default.Clear,
                contentDescription = "Rimuovi Potere",
                tint = MaterialTheme.colorScheme.error
            )
        }
    }
}

@Composable
fun AddPowerDialog(
    discipline: Discipline,
    allDisciplines: List<Discipline>,
    onDismiss: () -> Unit,
    onPowerSelected: (DisciplinePower) -> Unit
) {
    val fullDiscipline = allDisciplines.firstOrNull { it.id == discipline.id }
    val selectedPowers = discipline.selectedDisciplinePowers
    val availablePowers = fullDiscipline?.disciplinePowers?.filter { power ->
        power.level <= discipline.level && !selectedPowers.contains(power)
    }?.sortedBy { it.level } ?: emptyList()

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Aggiungi Potere a ${discipline.title}") },
        text = {
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
                                    .clickable { onPowerSelected(power) }
                                    .padding(8.dp)
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Annulla")
            }
        },
    )
}