package com.example.v5rules.ui.compose.screen.sheet

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.v5rules.R
import com.example.v5rules.data.Character
import com.example.v5rules.ui.compose.component.ClanImage
import com.example.v5rules.utils.CharacterSheetEvent
import com.example.v5rules.viewModel.CharacterSheetViewModel
import kotlin.collections.forEach


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GeneralInfoSection(character: Character, viewModel: CharacterSheetViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    val clans by viewModel.clans.collectAsState()
    val predatorType by viewModel.predator.collectAsState()
    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }
    val generations = remember { (1..16).toList() }

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.background)
                    .border(
                        1.dp, MaterialTheme.colorScheme.secondary, RoundedCornerShape(8.dp)
                    ),
            ) {
                Column ( modifier = Modifier.padding(8.dp)){
                    OutlinedTextField(modifier = Modifier.fillMaxWidth(),
                        value = character.name,
                        onValueChange = { viewModel.onEvent(CharacterSheetEvent.NameChanged(it)) },
                        label = { Text(stringResource(R.string.character_screen_name)) }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    ClanSelector(
                        selectedClan = uiState.character.clan,
                        clans = clans,
                        onClanSelected = { viewModel.onEvent(CharacterSheetEvent.ClanChanged(it)) },
                        focusRequester = focusRequester,
                        focusManager = focusManager
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    PredatorSelector(
                        selectedPredator = uiState.character.predator,
                        predators = predatorType,
                        onPredatorSelected = { viewModel.onEvent(CharacterSheetEvent.PredatorChanged(it)) },
                        focusRequester = focusRequester,
                        focusManager = focusManager
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    GenerationSelector(
                        selectedGeneration = character.generation,
                        generations = generations,
                        onGenerationSelected = { viewModel.onEvent(CharacterSheetEvent.GenerationChanged(it)) },
                        focusRequester = focusRequester,
                        focusManager = focusManager
                    )

                    // Sire
                    OutlinedTextField(modifier = Modifier.fillMaxWidth(),
                        value = character.sire,
                        onValueChange = { viewModel.onEvent(CharacterSheetEvent.SireChanged(it)) },
                        label = { Text(stringResource(R.string.character_screen_sire)) })

                    // Concept
                    OutlinedTextField(modifier = Modifier.fillMaxWidth(),
                        value = character.concept,
                        onValueChange = { viewModel.onEvent(CharacterSheetEvent.ConceptChanged(it)) },
                        label = { Text(stringResource(R.string.character_screen_concept)) })

                    // Ambition
                    OutlinedTextField(modifier = Modifier.fillMaxWidth(),
                        value = character.ambition,
                        onValueChange = { viewModel.onEvent(CharacterSheetEvent.AmbitionChanged(it)) },
                        label = { Text(stringResource(R.string.character_screen_ambition)) })

                    // Desire
                    OutlinedTextField(modifier = Modifier.fillMaxWidth(),
                        value = character.desire,
                        onValueChange = { viewModel.onEvent(CharacterSheetEvent.DesireChanged(it)) },
                        label = { Text(stringResource(R.string.character_screen_desire)) })
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClanSelector(
    selectedClan: com.example.v5rules.data.Clan?,
    clans: List<com.example.v5rules.data.Clan>,
    onClanSelected: (com.example.v5rules.data.Clan) -> Unit,
    focusRequester: FocusRequester,
    focusManager: androidx.compose.ui.focus.FocusManager,
    modifier: Modifier = Modifier
) {
    var clanExpanded by remember { mutableStateOf(false) }
    
    ExposedDropdownMenuBox(
        expanded = clanExpanded,
        onExpandedChange = {
            clanExpanded = !clanExpanded
            if (clanExpanded) {
                focusRequester.requestFocus()
            } else {
                focusManager.clearFocus()
            }
        },
        modifier = modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(4.dp))
                .padding(12.dp)
                .clickable { clanExpanded = !clanExpanded }
                .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                Box(modifier = Modifier.weight(1f)) {
                    selectedClan?.name?.let {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            ClanImage(
                                it,
                                tintColor = MaterialTheme.colorScheme.secondary,
                                width = 24.dp,
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(it)
                        }
                    } ?: run {
                        Text(
                            stringResource(R.string.clan),
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                Icon(
                    imageVector = if (clanExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = null,
                    tint = if (!clanExpanded) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant

                )
            }
        }
        
        DropdownMenu(
            expanded = clanExpanded,
            onDismissRequest = { 
                clanExpanded = false
                focusManager.clearFocus()
            }
        ) {
            clans.forEach { clan ->
                DropdownMenuItem(
                    onClick = {
                        onClanSelected(clan)
                        clanExpanded = false
                    },
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            ClanImage(
                                clanName = clan.name,
                                tintColor = MaterialTheme.colorScheme.secondary,
                                width = 30.dp
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(clan.name)
                        }
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PredatorSelector(
    selectedPredator: com.example.v5rules.data.PredatorType?,
    predators: List<com.example.v5rules.data.PredatorType>,
    onPredatorSelected: (com.example.v5rules.data.PredatorType) -> Unit,
    focusRequester: FocusRequester,
    focusManager: androidx.compose.ui.focus.FocusManager,
    modifier: Modifier = Modifier
) {
    var predatorExpanded by remember { mutableStateOf(false) }
    
    ExposedDropdownMenuBox(
        expanded = predatorExpanded,
        onExpandedChange = {
            predatorExpanded = !predatorExpanded
            if (predatorExpanded) {
                focusRequester.requestFocus()
            } else {
                focusManager.clearFocus()
            }
        },
        modifier = modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(4.dp))
                .padding(12.dp)
                .clickable { predatorExpanded = !predatorExpanded }
                .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                Box(modifier = Modifier.weight(1f)) {
                    selectedPredator?.name?.let {
                        Text(it)
                    } ?: run {
                        Text(
                            stringResource(R.string.predator),
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                Icon(
                    imageVector = if (predatorExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = null,
                    tint = if (!predatorExpanded) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        
        DropdownMenu(
            expanded = predatorExpanded,
            onDismissRequest = { 
                predatorExpanded = false
                focusManager.clearFocus()
            }
        ) {
            predators.forEach { predator ->
                DropdownMenuItem(
                    onClick = {
                        onPredatorSelected(predator)
                        predatorExpanded = false
                    },
                    text = { Text(predator.name) }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GenerationSelector(
    selectedGeneration: Int,
    generations: List<Int>,
    onGenerationSelected: (Int) -> Unit,
    focusRequester: FocusRequester,
    focusManager: androidx.compose.ui.focus.FocusManager,
    modifier: Modifier = Modifier
) {
    var generationExpanded by remember { mutableStateOf(false) }
    
    ExposedDropdownMenuBox(
        expanded = generationExpanded,
        onExpandedChange = {
            generationExpanded = !generationExpanded
            if (generationExpanded) {
                focusRequester.requestFocus()
            } else {
                focusManager.clearFocus()
            }
        },
        modifier = modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(4.dp))
                .padding(12.dp)
                .clickable { generationExpanded = !generationExpanded }
                .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                Box(modifier = Modifier.weight(1f)) {
                    Text(selectedGeneration.toString().plus("°"))
                }
                Icon(
                    imageVector = if (generationExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = null,
                    tint = if (!generationExpanded) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        
        DropdownMenu(
            expanded = generationExpanded,
            onDismissRequest = { 
                generationExpanded = false
                focusManager.clearFocus()
            }
        ) {
            generations.forEach { gen ->
                DropdownMenuItem(
                    onClick = {
                        onGenerationSelected(gen)
                        generationExpanded = false
                    },
                    text = { Text(gen.toString()) }
                )
            }
        }
    }
}




