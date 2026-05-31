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
import com.example.v5rules.navigation.DisciplinePowerNav
import com.example.v5rules.R
import com.example.v5rules.navigation.RitualNav
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
    
    DisciplineContent(
        characterDisciplines = uiState.character.disciplines,
        characterRituals = uiState.character.learnedRituals,
        allDisciplines = disciplines,
        onEvent = { viewModel.onEvent(it) },
        onNavigateToPower = { disciplineId, powerId ->
            navController.navigate(DisciplinePowerNav(disciplineId, powerId))
        },
        onNavigateToRitual = { disciplineId, powerId ->
            viewModel.onEvent(CharacterSheetEvent.SaveClicked)
            navController.navigate(RitualNav(disciplineId, powerId))
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DisciplineContent(
    characterDisciplines: List<Discipline>,
    characterRituals: List<Ritual>,
    allDisciplines: List<Discipline>,
    onEvent: (CharacterSheetEvent) -> Unit,
    onNavigateToPower: (String, String) -> Unit,
    onNavigateToRitual: (String, String) -> Unit
) {
    val rituals = allDisciplines.filter { it.ritual != null }.mapNotNull { it.ritual }
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
        ModalBottomSheet(onDismissRequest = { showAddPowerSheet = false }, sheetState = sheetState) {
            selectedDisciplineForPower?.let { discipline ->
                AddPowerBottomSheet(
                    discipline = discipline,
                    allDisciplines = allDisciplines,
                    onPowerSelected = { power ->
                        onEvent(CharacterSheetEvent.DisciplinePowerAdded(discipline.title, power))
                        coroutineScope.launch { sheetState.hide(); showAddPowerSheet = false }
                    }
                )
            }
        }
    }

    if (showAddRitualSheet) {
        ModalBottomSheet(onDismissRequest = { showAddRitualSheet = false }, sheetState = ritualSheetState) {
            selectedRitualsForPower?.let { ritual ->
                AddRitualPowerBottomSheet(
                    ritual = ritual,
                    allRituals = rituals,
                    onPowerSelected = { power ->
                        onEvent(CharacterSheetEvent.RitualPowerAdded(ritual, power))
                        coroutineScope.launch { ritualSheetState.hide(); showAddRitualSheet = false }
                    }
                )
            }
        }
    }

    Column(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
        // Dropdown per aggiungere discipline
        Box(modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)) {
            OutlinedTextField(
                value = "",
                onValueChange = {},
                label = { Text("Aggiungi Disciplina") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                readOnly = true,
                enabled = false,
                colors = OutlinedTextFieldDefaults.colors(
                    disabledTextColor = MaterialTheme.colorScheme.onSurface,
                    disabledBorderColor = MaterialTheme.colorScheme.outline
                ),
                modifier = Modifier.fillMaxWidth()
            )
            Box(modifier = Modifier.matchParentSize().clickable { expanded = !expanded })
            
            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }, modifier = Modifier.fillMaxWidth()) {
                val available = allDisciplines.filter { d -> characterDisciplines.none { it.title == d.title } }.sortedBy { it.title }
                if (available.isEmpty()) {
                    DropdownMenuItem(text = { Text("Nessuna disciplina disponibile") }, enabled = false, onClick = {})
                } else {
                    available.forEach { discipline ->
                        DropdownMenuItem(
                            text = { Text(discipline.title) },
                            leadingIcon = { DisciplineIcon(discipline.id, discipline.title, 24.dp) },
                            onClick = {
                                onEvent(CharacterSheetEvent.DisciplineChanged(discipline))
                                expanded = false
                                focusManager.clearFocus()
                            }
                        )
                    }
                }
            }
        }

        LazyColumn(modifier = Modifier.weight(1f)) {
            items(characterDisciplines) { discipline ->
                CustomContentExpander(
                    useFullWidth = true,
                    header = {
                        DisciplineHeaderItem(
                            discipline = discipline,
                            onLevelChange = { newLevel -> onEvent(CharacterSheetEvent.DisciplineLevelChanged(discipline, newLevel)) }
                        )
                    },
                    content = {
                        Column(modifier = Modifier.padding(8.dp)) {
                            // Poteri della disciplina
                            val powers = discipline.selectedDisciplinePowers.filter { it.level > 0 }
                            powers.forEach { power ->
                                DisciplinePowerItem(
                                    power = power,
                                    disciplineId = discipline.id,
                                    onNavigate = { onNavigateToPower(discipline.id, power.id) },
                                    onRemove = { onEvent(CharacterSheetEvent.DisciplinePowerRemoved(discipline.title, power)) }
                                )
                            }
                            
                            if (powers.size < discipline.level) {
                                TextButton(onClick = { selectedDisciplineForPower = discipline; showAddPowerSheet = true }) {
                                    Text("Aggiungi Potere")
                                }
                            }

                            // Rituali associati
                            characterRituals.filter { it.title == discipline.title }.forEach { ritual ->
                                Spacer(modifier = Modifier.height(12.dp))
                                RitualSection(
                                    ritual = ritual,
                                    disciplineId = discipline.id,
                                    onLevelChange = { onEvent(CharacterSheetEvent.UpdateRitualLevel(discipline.title, ritual, it)) },
                                    onNavigateToRitual = onNavigateToRitual,
                                    onRemovePower = { onEvent(CharacterSheetEvent.RitualPowerRemoved(ritual, it)) },
                                    onAddRitualClick = { selectedRitualsForPower = ritual; showAddRitualSheet = true }
                                )
                            }
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun DisciplineHeaderItem(discipline: Discipline, onLevelChange: (Int) -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        DisciplineIcon(discipline.id, discipline.title, 32.dp)
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = discipline.title, style = MaterialTheme.typography.titleMedium, modifier = Modifier.weight(1f))
        InteractiveDisciplineDots(currentValue = discipline.level, onValueChange = onLevelChange)
    }
}

@Composable
fun RitualSection(
    ritual: Ritual,
    disciplineId: String,
    onLevelChange: (Int) -> Unit,
    onNavigateToRitual: (String, String) -> Unit,
    onRemovePower: (RitualPower) -> Unit,
    onAddRitualClick: () -> Unit
) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        Text(text = stringResource(id = R.string.discipline_rituals), style = MaterialTheme.typography.titleLarge)
        InteractiveDisciplineDots(currentValue = ritual.level, onValueChange = onLevelChange)
    }
    
    ritual.ritualsPowers.forEach { power ->
        RitualPowerItem(
            power = power,
            onNavigate = { onNavigateToRitual(disciplineId, power.id) },
            onRemove = { onRemovePower(power) }
        )
    }
    
    if (ritual.ritualsPowers.size < ritual.level) {
        TextButton(onClick = onAddRitualClick) { Text("Aggiungi Rituale") }
    }
}

@Composable
private fun InteractiveDisciplineDots(currentValue: Int, onValueChange: (Int) -> Unit) {
    Row {
        for (i in 1..5) {
            Box(
                modifier = Modifier.size(22.dp).padding(2.dp).clip(CircleShape)
                    .background(if (i <= currentValue) MaterialTheme.colorScheme.secondary else Color.Transparent)
                    .border(1.dp, if (i <= currentValue) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f), CircleShape)
                    .clickable { onValueChange(if (i == currentValue) 0 else i) }
            )
        }
    }
}

@Composable
fun DisciplinePowerItem(power: DisciplinePower, disciplineId: String, onNavigate: () -> Unit, onRemove: () -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth().padding(start = 16.dp, end = 8.dp).padding(vertical = 4.dp).clickable { onNavigate() }) {
        Text(text = "${power.title} (Livello ${power.level})", modifier = Modifier.weight(1f))
        IconButton(onClick = onRemove) {
            Icon(Icons.Default.Delete, "Rimuovi", tint = MaterialTheme.colorScheme.error)
        }
    }
}

@Composable
fun RitualPowerItem(power: RitualPower, onNavigate: () -> Unit, onRemove: () -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth().clickable { onNavigate() }.padding(start = 16.dp, end = 8.dp).padding(vertical = 4.dp)) {
        Text(text = "${power.title} (Livello ${power.level})", modifier = Modifier.weight(1f))
        IconButton(onClick = onRemove) {
            Icon(Icons.Default.Delete, "Rimuovi", tint = MaterialTheme.colorScheme.error)
        }
    }
}

@Composable
fun AddPowerBottomSheet(discipline: Discipline, allDisciplines: List<Discipline>, onPowerSelected: (DisciplinePower) -> Unit) {
    val fullDiscipline = allDisciplines.firstOrNull { it.id == discipline.id }
    val available = fullDiscipline?.disciplinePowers?.filter { p -> p.level <= discipline.level && discipline.selectedDisciplinePowers.none { it.title == p.title } }?.sortedBy { it.level } ?: emptyList()

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Aggiungi Potere a ${discipline.title}", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(16.dp))
        Box(modifier = Modifier.heightIn(max = 300.dp)) {
            if (available.isEmpty()) {
                Text("Nessun potere disponibile")
            } else {
                LazyColumn(modifier = Modifier.fillMaxWidth()) {
                    items(available) { power ->
                        Text(text = "${power.title} (Lv ${power.level})", modifier = Modifier.fillMaxWidth().clickable { onPowerSelected(power) }.padding(8.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun AddRitualPowerBottomSheet(ritual: Ritual, allRituals: List<Ritual>, onPowerSelected: (RitualPower) -> Unit) {
    val fullRitual = allRituals.firstOrNull { it.id == ritual.id }
    val available = fullRitual?.ritualsPowers?.filter { p -> p.level <= ritual.level && ritual.ritualsPowers.none { it.title == p.title } }?.sortedBy { it.level } ?: emptyList()

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Aggiungi a ${ritual.title}", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(16.dp))
        Box(modifier = Modifier.heightIn(max = 300.dp)) {
            if (available.isEmpty()) {
                Text("Nessun rituale disponibile")
            } else {
                LazyColumn(modifier = Modifier.fillMaxWidth()) {
                    items(available) { power ->
                        Text(text = "${power.title} (Lv ${power.level})", modifier = Modifier.fillMaxWidth().clickable { onPowerSelected(power) }.padding(8.dp))
                    }
                }
            }
        }
    }
}
