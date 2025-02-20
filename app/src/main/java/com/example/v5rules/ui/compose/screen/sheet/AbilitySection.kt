package com.example.v5rules.ui.compose.screen.sheet

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.v5rules.R
import com.example.v5rules.data.Ability
import com.example.v5rules.data.Character
import com.example.v5rules.ui.compose.component.CustomContentExpander
import com.example.v5rules.ui.compose.component.DotsForAttribute
import com.example.v5rules.utils.CharacterSheetEvent
import com.example.v5rules.viewModel.CharacterSheetViewModel

@Composable
fun AbilitySection(character: Character, viewModel: CharacterSheetViewModel) {

    val expandedStates = remember { mutableStateMapOf<String, Boolean>() }
    val allAbilities = viewModel.allAbilities // Prendi la lista completa dal ViewModel
    val pagerState = rememberPagerState(pageCount = { 3 })

    //Inizializza expandedState, adesso per TUTTE le abilità
    LaunchedEffect(character.abilities, allAbilities) {
        allAbilities.forEach { abilityName ->
            expandedStates.putIfAbsent(
                abilityName, false
            ) // Inizializza a false SOLO se non esiste già
        }

        // Rimuovi gli stati di espansione per abilità che non esistono più nel personaggio *e* non sono nella lista completa
        val validAbilityNames = character.abilities.map { it.name }.toSet() + allAbilities.toSet()
        expandedStates.keys.retainAll(validAbilityNames)
    }

    LazyColumn{
        item {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.padding(horizontal = 8.dp),
                contentPadding = PaddingValues(horizontal = 16.dp)
            ){ page ->
                when (page) {
                    0 -> {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                                .border(
                                    width = 1.dp,
                                    color = MaterialTheme.colorScheme.tertiary,
                                    shape = RoundedCornerShape(8.dp)
                                )
                        ) {
                            Column(modifier = Modifier.padding(8.dp)) {
                                Text(
                                    text = stringResource(R.string.character_screen_physical),
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .wrapContentWidth(Alignment.CenterHorizontally)
                                        .padding(bottom = 8.dp)
                                )

                                val physicalAbilities = listOf(
                                    stringResource(R.string.character_screen_abilities_athletics),
                                    stringResource(R.string.character_screen_abilities_brawl),
                                    stringResource(R.string.character_screen_abilities_craft),
                                    stringResource(R.string.character_screen_abilities_drive),
                                    stringResource(R.string.character_screen_abilities_firearms),
                                    stringResource(R.string.character_screen_abilities_larceny),
                                    stringResource(R.string.character_screen_abilities_melee),
                                    stringResource(R.string.character_screen_abilities_stealth),
                                    stringResource(R.string.character_screen_abilities_survival),
                                ).sorted()

                                // Usa la lista *completa* delle abilità fisiche
                                physicalAbilities.forEach { abilityName ->
                                    // Cerca l'abilità nel personaggio.  Se non c'è, crea un'abilità temporanea con livello 0.
                                    val ability =
                                        character.abilities.firstOrNull { it.name == abilityName } ?: Ability(
                                            name = abilityName, level = 0
                                        )
                                    AbilityItem(ability, viewModel, expandedStates)
                                }
                            }
                        }

                    }
                    1 -> {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                                .border(
                                    width = 1.dp,
                                    color = MaterialTheme.colorScheme.tertiary,
                                    shape = RoundedCornerShape(8.dp)
                                )
                        ) {
                            Column(modifier = Modifier.padding(8.dp)) {
                                Text(
                                    text = stringResource(R.string.character_screen_social), // Usa una stringa, o meglio ancora, una risorsa stringa
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .wrapContentWidth(Alignment.CenterHorizontally)
                                        .padding(bottom = 8.dp)
                                )

                                val socialAbilities = listOf(
                                    stringResource(R.string.character_screen_abilities_animal_ken),
                                    stringResource(R.string.character_screen_abilities_etiquette),
                                    stringResource(R.string.character_screen_abilities_insight),
                                    stringResource(R.string.character_screen_abilities_intimidation),
                                    stringResource(R.string.character_screen_abilities_leadership),
                                    stringResource(R.string.character_screen_abilities_performance),
                                    stringResource(R.string.character_screen_abilities_persuasion),
                                    stringResource(R.string.character_screen_abilities_streetwise),
                                    stringResource(R.string.character_screen_abilities_subterfuge),
                                ).sorted()
                                socialAbilities.forEach { abilityName ->
                                    val ability = character.abilities.firstOrNull { it.name == abilityName } ?: Ability(
                                        name = abilityName, level = 0
                                    )
                                    AbilityItem(ability, viewModel, expandedStates)
                                }
                            }
                        }
                    }
                    2 ->{
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                                .border(
                                    width = 1.dp,
                                    color = MaterialTheme.colorScheme.tertiary,
                                    shape = RoundedCornerShape(8.dp)
                                )
                        ) {
                            Column(modifier = Modifier.padding(8.dp)) {
                                Text(
                                    text = stringResource(R.string.character_screen_mental), // Usa una stringa, o meglio ancora, una risorsa stringa
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .wrapContentWidth(Alignment.CenterHorizontally)
                                        .padding(bottom = 8.dp)
                                )

                                val mentalAbilities = listOf(
                                    stringResource(R.string.character_screen_abilities_academics),
                                    stringResource(R.string.character_screen_abilities_awareness),
                                    stringResource(R.string.character_screen_abilities_finance),
                                    stringResource(R.string.character_screen_abilities_investigation),
                                    stringResource(R.string.character_screen_abilities_medicine),
                                    stringResource(R.string.character_screen_abilities_occult),
                                    stringResource(R.string.character_screen_abilities_politics),
                                    stringResource(R.string.character_screen_abilities_science),
                                    stringResource(R.string.character_screen_abilities_technology),
                                ).sorted()
                                mentalAbilities.forEach { abilityName ->
                                    val ability = character.abilities.firstOrNull { it.name == abilityName } ?: Ability(
                                        name = abilityName, level = 0
                                    )
                                    AbilityItem(ability, viewModel, expandedStates)
                                }
                            }
                        }
                    }
                }
            }
        }
        item {
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                repeat(pagerState.pageCount) { iteration ->
                    val color = if (pagerState.currentPage == iteration) {
                        MaterialTheme.colorScheme.tertiary
                    } else {
                        Color.LightGray
                    }
                    Box(
                        modifier = Modifier
                            .padding(8.dp)
                            .clip(CircleShape)
                            .background(color)
                            .size(8.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun AbilityItem(
    ability: Ability,
    viewModel: CharacterSheetViewModel,
    expandedStates: MutableMap<String, Boolean>
) {
    // Usa *direttamente* lo stato di espansione dalla mappa
    val isExpanded = expandedStates[ability.name] ?: false

    CustomContentExpander(maxWith = 1f,
        initialState = isExpanded, // Inizializza con lo stato corretto
        header = {
            DotsForAttribute(
                label = "${ability.name} - ${ability.specialization.orEmpty()}",
                level = ability.level,
                textStyle = MaterialTheme.typography.bodyMedium
            )
        },
        content = {
            Column(modifier = Modifier.padding(8.dp)) {
                Slider(
                    value = ability.level.toFloat(), onValueChange = { newValue ->
                        viewModel.onEvent(
                            CharacterSheetEvent.AbilityChanged(
                                ability.name, // Usa il nome dell'abilità
                                newValue.toInt()
                            )
                        )
                    }, valueRange = 0f..5f, steps = 4, colors = SliderDefaults.colors(
                        activeTrackColor = MaterialTheme.colorScheme.tertiary
                    )
                )

                OutlinedTextField(
                    value = ability.specialization ?: "",
                    onValueChange = { newSpecialization ->
                        viewModel.onEvent(
                            CharacterSheetEvent.AbilitySpecializationChanged(ability.name, // Usa il nome dell'abilità
                                newSpecialization.ifBlank { null })
                        )
                    },
                    label = { Text("Specializzazione (opzionale)") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        })

    LaunchedEffect(isExpanded) {
        expandedStates[ability.name] = isExpanded
        if (!isExpanded) {
            viewModel.onEvent(CharacterSheetEvent.AbilitySpecializationChanged(ability.name, null))
        }
    }
}
