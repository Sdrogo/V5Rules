package com.example.v5rules.ui.compose.screen.sheet

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.v5rules.R
import com.example.v5rules.data.Character
import com.example.v5rules.ui.compose.component.CustomContentExpander
import com.example.v5rules.ui.compose.component.DotsForAttribute
import com.example.v5rules.utils.CharacterSheetEvent
import com.example.v5rules.viewModel.CharacterSheetViewModel

@Composable
fun AttributeSection(
    character: Character, viewModel: CharacterSheetViewModel, isLandscape: Boolean
) {
    // CHIAVE per forzare la ricomposizione
    var refreshKey by remember { mutableIntStateOf(0) }

    // LaunchedEffect che osserva la refreshKey
    LaunchedEffect(refreshKey) {
        // Questa parte viene eseguita ogni volta che refreshKey cambia
        // Non è necessario fare nulla qui, la ricomposizione è sufficiente
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        item {
            // Attributi Fisici
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.tertiary,
                        shape = RoundedCornerShape(8.dp)
                    ),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp)
                ) {
                    Text(
                        text = stringResource(R.string.character_screen_physical),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .wrapContentWidth()
                            .align(Alignment.CenterHorizontally)
                            .padding(top = 8.dp)
                    )

                    // Forza
                    // Usa remember con character.attributes.strength come chiave
                    var forza by remember(character.attributes.strength) { mutableIntStateOf(character.attributes.strength) }
                    CustomContentExpander(maxWith = if (isLandscape) 0.45f else 1f,
                        padding = 0.dp,
                        header = {
                            DotsForAttribute(
                                stringResource(R.string.character_screen_attributes_strength), forza,
                                textStyle = MaterialTheme.typography.headlineSmall
                            )
                        },
                        content = {
                            Slider(
                                value = forza.toFloat(), onValueChange = { newValue ->
                                    forza = newValue.toInt()
                                    viewModel.onEvent(CharacterSheetEvent.StrengthChanged(forza))
                                }, valueRange = 1f..5f, steps = 3, colors = SliderDefaults.colors(
                                    activeTrackColor = MaterialTheme.colorScheme.primary,
                                ), modifier = Modifier.fillMaxWidth()
                            )
                        })

                    // Destrezza
                    // Usa remember con character.attributes.dexterity come chiave
                    var destrezza by remember(character.attributes.dexterity) {
                        mutableIntStateOf(
                            character.attributes.dexterity
                        )
                    }
                    CustomContentExpander(maxWith = if (isLandscape) 0.45f else 1f,
                        padding = 0.dp,
                        header = {
                            DotsForAttribute(
                                stringResource(R.string.character_screen_attributes_dexterity),
                                destrezza,
                                textStyle = MaterialTheme.typography.headlineSmall
                            )
                        },
                        content = {
                            Slider(
                                value = destrezza.toFloat(), onValueChange = { newValue ->
                                    destrezza = newValue.toInt()
                                    viewModel.onEvent(CharacterSheetEvent.DexterityChanged(destrezza))
                                }, valueRange = 1f..5f, steps = 3, colors = SliderDefaults.colors(
                                    activeTrackColor = MaterialTheme.colorScheme.primary,
                                ), modifier = Modifier.fillMaxWidth()
                            )
                        })

                    // Costituzione
                    // Usa remember con character.attributes.stamina come chiave
                    var costituzione by remember(character.attributes.stamina) {
                        mutableIntStateOf(
                            character.attributes.stamina
                        )
                    }
                    CustomContentExpander(maxWith = if (isLandscape) 0.45f else 1f,
                        padding = 0.dp,
                        header = {
                            DotsForAttribute(
                                stringResource(R.string.character_screen_attributes_stamina),
                                costituzione,
                                textStyle = MaterialTheme.typography.headlineSmall
                            )
                        },
                        content = {
                            Slider(
                                value = costituzione.toFloat(), onValueChange = { newValue ->
                                    costituzione = newValue.toInt()
                                    viewModel.onEvent(
                                        CharacterSheetEvent.StaminaChanged(
                                            costituzione
                                        )
                                    )
                                }, valueRange = 1f..5f, steps = 3, colors = SliderDefaults.colors(
                                    activeTrackColor = MaterialTheme.colorScheme.primary,
                                ), modifier = Modifier.fillMaxWidth()
                            )
                        })
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
        item {
            // Attributi Mentali
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.tertiary,
                        shape = RoundedCornerShape(8.dp)
                    ),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp)
                ) {
                    Text(
                        text = stringResource(R.string.character_screen_social),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .wrapContentWidth()
                            .align(Alignment.CenterHorizontally)
                            .padding(top = 8.dp)
                    )

                    // Charisma
                    var charisma by remember(character.attributes.charisma) {
                        mutableIntStateOf(
                            character.attributes.charisma
                        )
                    }
                    CustomContentExpander(maxWith = if (isLandscape) 0.45f else 1f,
                        padding = 0.dp,
                        header = {
                            DotsForAttribute(
                                stringResource(R.string.character_screen_attributes_charisma),
                                charisma,
                                textStyle = MaterialTheme.typography.headlineSmall
                            )
                        },
                        content = {
                            Slider(
                                value = charisma.toFloat(), onValueChange = { newValue ->
                                    charisma = newValue.toInt()
                                    viewModel.onEvent(CharacterSheetEvent.CharismaChanged(charisma))
                                }, valueRange = 1f..5f, steps = 3, colors = SliderDefaults.colors(
                                    activeTrackColor = MaterialTheme.colorScheme.primary,
                                ), modifier = Modifier.fillMaxWidth()
                            )
                        })
                    // Persuasione
                    var persuasione by remember(character.attributes.manipulation) {
                        mutableIntStateOf(
                            character.attributes.manipulation
                        )
                    }
                    CustomContentExpander(maxWith = if (isLandscape) 0.45f else 1f,
                        padding = 0.dp,
                        header = {
                            DotsForAttribute(
                                stringResource(R.string.character_screen_attributes_manipulation),
                                persuasione,
                                textStyle = MaterialTheme.typography.headlineSmall
                            )
                        },
                        content = {
                            Slider(
                                value = persuasione.toFloat(), onValueChange = { newValue ->
                                    persuasione = newValue.toInt()
                                    viewModel.onEvent(
                                        CharacterSheetEvent.ManipulationChanged(
                                            persuasione
                                        )
                                    )
                                }, valueRange = 1f..5f, steps = 3, colors = SliderDefaults.colors(
                                    activeTrackColor = MaterialTheme.colorScheme.primary, // Colore della parte piena
                                ), modifier = Modifier.fillMaxWidth()
                            )
                        })

                    // Autocontrollo
                    var autocontrollo by remember(character.attributes.composure) {
                        mutableIntStateOf(
                            character.attributes.composure
                        )
                    }
                    CustomContentExpander(maxWith = if (isLandscape) 0.45f else 1f,
                        padding = 0.dp,
                        header = {
                            DotsForAttribute(
                                stringResource(R.string.character_screen_attributes_composure),
                                autocontrollo,
                                textStyle = MaterialTheme.typography.headlineSmall
                            )
                        },
                        content = {
                            Slider(
                                value = autocontrollo.toFloat(), onValueChange = { newValue ->
                                    autocontrollo = newValue.toInt()
                                    viewModel.onEvent(
                                        CharacterSheetEvent.ComposureChanged(
                                            autocontrollo
                                        )
                                    )
                                }, valueRange = 1f..5f, steps = 3, colors = SliderDefaults.colors(
                                    activeTrackColor = MaterialTheme.colorScheme.primary, // Colore della parte piena
                                ), modifier = Modifier.fillMaxWidth()
                            )
                        })
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
        item {
            // Attributi Mentali
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.tertiary,
                        shape = RoundedCornerShape(8.dp)
                    ),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp)
                ) {
                    Text(
                        text = stringResource(R.string.character_screen_mental),
                        style = MaterialTheme.typography.titleMedium, // Usa uno stile più piccolo
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .wrapContentWidth() // Wrap content per centrare
                            .align(Alignment.CenterHorizontally) // Centra orizzontalmente
                            .padding(top = 8.dp)
                    )

                    // Intelligenza
                    var intelligenza by remember(character.attributes.intelligence) {
                        mutableIntStateOf(
                            character.attributes.intelligence
                        )
                    }
                    CustomContentExpander(maxWith = if (isLandscape) 0.45f else 1f,
                        padding = 0.dp,
                        header = {
                            DotsForAttribute(
                                stringResource(R.string.character_screen_attributes_intelligence),
                                intelligenza,
                                textStyle = MaterialTheme.typography.headlineSmall
                            )
                        },
                        content = {
                            Slider(
                                value = intelligenza.toFloat(),
                                onValueChange = { newValue ->
                                    intelligenza = newValue.toInt()
                                    viewModel.onEvent(
                                        CharacterSheetEvent.IntelligenceChanged(
                                            intelligenza
                                        )
                                    )
                                },
                                colors = SliderDefaults.colors(
                                    activeTrackColor = MaterialTheme.colorScheme.primary, // Colore della parte piena
                                ),
                                valueRange = 1f..5f,
                                steps = 3,
                                modifier = Modifier.fillMaxWidth()
                            )
                        })

                    // Prontezza
                    var prontezza by remember(character.attributes.wits) { mutableIntStateOf(character.attributes.wits) }
                    CustomContentExpander(maxWith = if (isLandscape) 0.45f else 1f,
                        padding = 0.dp,
                        header = {
                            DotsForAttribute(
                                stringResource(R.string.character_screen_attributes_wits), prontezza,
                                textStyle = MaterialTheme.typography.headlineSmall
                            )
                        },
                        content = {
                            Slider(
                                value = prontezza.toFloat(),
                                onValueChange = { newValue ->
                                    prontezza = newValue.toInt()
                                    viewModel.onEvent(CharacterSheetEvent.WitsChanged(prontezza))
                                },
                                colors = SliderDefaults.colors(
                                    activeTrackColor = MaterialTheme.colorScheme.primary, // Colore della parte piena
                                ),
                                valueRange = 1f..5f,
                                steps = 3,
                                modifier = Modifier.fillMaxWidth()
                            )
                        })

                    // Fermezza
                    var fermezza by remember(character.attributes.resolve) {
                        mutableIntStateOf(
                            character.attributes.resolve
                        )
                    }
                    CustomContentExpander(maxWith = if (isLandscape) 0.45f else 1f,
                        padding = 0.dp,
                        header = {
                            DotsForAttribute(
                                stringResource(R.string.character_screen_attributes_resolve),
                                fermezza,
                                textStyle = MaterialTheme.typography.headlineSmall
                            )
                        },
                        content = {
                            Slider(
                                value = fermezza.toFloat(),
                                onValueChange = { newValue ->
                                    fermezza = newValue.toInt()
                                    viewModel.onEvent(CharacterSheetEvent.ComposureChanged(fermezza))
                                },
                                colors = SliderDefaults.colors(
                                    activeTrackColor = MaterialTheme.colorScheme.primary, // Colore della parte piena
                                ),
                                valueRange = 1f..5f,
                                steps = 3,
                                modifier = Modifier.fillMaxWidth()
                            )
                        })
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
    // Osserva i cambiamenti nel personaggio e aggiorna la chiave se necessario
    LaunchedEffect(character) {
        refreshKey++
    }
}
