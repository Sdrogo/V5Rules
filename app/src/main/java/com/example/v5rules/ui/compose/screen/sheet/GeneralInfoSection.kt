package com.example.v5rules.ui.compose.screen.sheet

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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


@Composable
fun GeneralInfoSection(character: Character, viewModel: CharacterSheetViewModel) {
    val clans by viewModel.clans.collectAsState()
    val searchQuery by viewModel.clanSearchQuery.collectAsState()
    val selectedClan by viewModel.selectedClan.collectAsState()

    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current // Per controllare la tastiera

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(MaterialTheme.colorScheme.background)
            .border(
                1.dp, MaterialTheme.colorScheme.tertiary, RoundedCornerShape(8.dp)
            ),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Nome
            OutlinedTextField(modifier = Modifier.fillMaxWidth(),
                value = character.name,
                onValueChange = { viewModel.onEvent(CharacterSheetEvent.NameChanged(it)) },
                label = { Text(stringResource(R.string.character_screen_name)) })

            // Clan (esempio con DropdownMenu)
            var expanded by remember { mutableStateOf(false) }

            Box { // Usa un Box per sovrapporre il DropdownMenu
                OutlinedTextField(
                    value = selectedClan?.name ?: searchQuery,
                    onValueChange = {
                        viewModel.setClanSearchQuery(it)
                        expanded = it.isNotEmpty() // Apri SOLO se la query NON è vuota
                    },
                    label = { Text(stringResource(R.string.character_screen_clan)) },
                    trailingIcon = {
                        IconButton(onClick = {
                            if (searchQuery.isNotEmpty() || selectedClan != null) {
                                viewModel.setClanSearchQuery("") // Pulisci
                            } else {
                                expanded =
                                    true // Apri se la query è vuota e nessun clan è selezionato
                                focusRequester.requestFocus() // Richiedi il focus SOLO se apri il dropdown

                            }
                        }) {
                            //Usa l'icona clear se c'è del testo.
                            if (searchQuery.isNotEmpty() || selectedClan != null) {
                                Icon(Icons.Filled.Clear, "Clear")
                            } else {
                                Icon(Icons.Filled.ArrowDropDown, "Open Clan dropdown")
                            }

                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(focusRequester)
                        .onKeyEvent {
                            if (it.key == Key.Escape) { //Chiudi il dropdown quando premi esc.
                                expanded = false
                                true // Evento gestito
                            } else {
                                false // Lascia gestire l'evento al sistema
                            }
                        },
                    leadingIcon = {
                        if (selectedClan != null) {
                            ClanImage(
                                clanName = selectedClan!!.name,
                                tintColor = MaterialTheme.colorScheme.tertiary,
                                width = 24.dp
                            )
                        }
                    },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done), // Imposta l'azione "Done"
                    keyboardActions = KeyboardActions(onDone = {
                        keyboardController?.hide() // Nascondi la tastiera quando si preme "Done"
                        focusManager.clearFocus() // Rilascia il focus
                    })
                )

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = {
                        expanded = false
                        //Non richiedere il focus qui.
                    },
                    modifier = Modifier
                        .focusable(false)
                        .align(Alignment.TopStart) // Allinea il DropdownMenu sotto il TextField

                ) {
                    clans.forEach { clan ->
                        DropdownMenuItem(onClick = {
                            viewModel.onEvent(CharacterSheetEvent.ClanChanged(clan))
                            expanded = false
                            keyboardController?.hide() // Nascondi la tastiera
                            focusManager.clearFocus()   // Rilascia il focus

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
            }
            // Generazione
            var generation by remember { mutableFloatStateOf(character.generation.toFloat()) }
            Column {
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
            }

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