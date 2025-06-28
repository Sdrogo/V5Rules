package com.example.v5rules.ui.compose.screen.sheet

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.example.v5rules.R
import com.example.v5rules.data.Character
import com.example.v5rules.ui.compose.component.ClanImage
import com.example.v5rules.utils.CharacterSheetEvent
import com.example.v5rules.viewModel.CharacterSheetViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GeneralInfoSection(character: Character, viewModel: CharacterSheetViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    val clans by viewModel.clans.collectAsState()
    val predatorType by viewModel.predator.collectAsState()
    var generation by remember { mutableFloatStateOf(character.generation.toFloat()) }
    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current // Per controllare la tastiera

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
                        1.dp, MaterialTheme.colorScheme.tertiary, RoundedCornerShape(8.dp)
                    ),
            ) {
                Column ( modifier = Modifier.padding(8.dp)){
                    OutlinedTextField(modifier = Modifier.fillMaxWidth(),
                        value = character.name,
                        onValueChange = { viewModel.onEvent(CharacterSheetEvent.NameChanged(it)) },
                        label = { Text(stringResource(R.string.character_screen_name)) })


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
                        }
                    ) {
                        val selectedClan = uiState.character.clan
                        OutlinedTextField(
                            value = selectedClan?.name ?: "",
                            onValueChange = {},
                            readOnly = true,
                            label = { Text(stringResource(R.string.clan)) },
                            leadingIcon = {
                                selectedClan?.name?.let {
                                    ClanImage(
                                        it,
                                        tintColor = MaterialTheme.colorScheme.tertiary,
                                        width = 24.dp,
                                    )
                                }
                            },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = clanExpanded) },
                            colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth()
                                .focusRequester(focusRequester)
                                .onKeyEvent {
                                    if (it.key == Key.Escape) {
                                        clanExpanded = false
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
                    }
                    DropdownMenu(
                        expanded = clanExpanded,
                        onDismissRequest = { clanExpanded = false },
                        modifier = Modifier
                            .focusable(false)
                            .align(Alignment.Start)

                    ) {
                        clans.forEach { clan ->
                            DropdownMenuItem(onClick = {
                                viewModel.onEvent(CharacterSheetEvent.ClanChanged(clan))
                                clanExpanded = false

                            }, text = {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    ClanImage(
                                        clanName = clan.name,
                                        tintColor = MaterialTheme.colorScheme.tertiary,
                                        width = 30.dp
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(clan.name)
                                }
                            })
                        }
                    }
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
                        }
                    ) {
                        val selectedPredator = uiState.character.predator
                        OutlinedTextField(
                            value = selectedPredator?.name ?: "",
                            onValueChange = {},
                            readOnly = true,
                            label = { Text(stringResource(R.string.predator)) },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = predatorExpanded) },
                            colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth()
                                .focusRequester(focusRequester)
                                .onKeyEvent {
                                    if (it.key == Key.Escape) {
                                        predatorExpanded = false
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
                    }
                    DropdownMenu(
                        expanded = predatorExpanded,
                        onDismissRequest = { predatorExpanded = false },
                        modifier = Modifier
                            .focusable(false)
                            .align(Alignment.Start)

                    ) {
                        predatorType.forEach { predator ->
                            DropdownMenuItem(onClick = {
                                viewModel.onEvent(CharacterSheetEvent.PredatorChanged(predator))
                                predatorExpanded = false
                            }, text = { Text(predator.name) }
                            )
                        }
                    }


                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(8.dp)
                    ) {
                        Text(
                            stringResource(R.string.character_screen_generation),
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Spacer(Modifier.weight(0.7f))
                        Text("${generation.toInt()}°")
                    }
                    Slider(
                        value = generation,
                        onValueChange = { newValue ->
                            generation = newValue
                            viewModel.onEvent(CharacterSheetEvent.GenerationChanged(newValue.toInt()))
                        },
                        valueRange = 1f..16f,
                        steps = 14,
                        modifier = Modifier.fillMaxWidth(),
                        colors = SliderDefaults.colors(
                            activeTrackColor = MaterialTheme.colorScheme.tertiary, // Colore della parte piena
                        )
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