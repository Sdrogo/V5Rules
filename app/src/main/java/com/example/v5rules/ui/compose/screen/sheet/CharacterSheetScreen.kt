package com.example.v5rules.ui.compose.screen.sheet

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults.SecondaryIndicator
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.NavHostController
import com.example.v5rules.R
import com.example.v5rules.data.Ability
import com.example.v5rules.data.Character
import com.example.v5rules.ui.compose.component.ClanImage
import com.example.v5rules.ui.compose.component.CommonScaffold
import com.example.v5rules.ui.compose.component.CustomContentExpander
import com.example.v5rules.ui.compose.component.DotsForAttribute
import com.example.v5rules.utils.CharacterSheetEvent
import com.example.v5rules.viewModel.CharacterSheetViewModel

@Composable
fun CharacterSheetScreen(
    modifier: Modifier,
    viewModel: CharacterSheetViewModel,
    navController: NavHostController,
    id: Int? = null
) {
    val scrollState = rememberScrollState()
    val uiState by viewModel.uiState.collectAsState()
    val character = uiState.character
    val isLandscape = LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE
    val selectedTabIndex by viewModel.selectedTabIndex.collectAsState()


    // LaunchedEffect per caricare il personaggio se l'ID è presente
    LaunchedEffect(key1 = id) {
        if (id != null) {
            viewModel.setCharacter(id)
        }
    }

    // Indice della tab selezionata
    val tabs = listOf(
        "Info Generali", "Attributi", "Abilità", "Discipline", "Background" /*... altre tab... */
    )

    CommonScaffold(
        navController = navController, title = stringResource(R.string.character_screen_title)
    ) { paddingValues ->

        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .padding()
        ) {
            val (tabRow, content, buttons) = createRefs()

            // TabRow scorrevole
            ScrollableTabRow(selectedTabIndex = selectedTabIndex,
                modifier = Modifier
                    .constrainAs(tabRow) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        width = Dimension.fillToConstraints
                    }
                    .zIndex(1f),
                edgePadding = 0.dp, // Rimuovi il padding predefinito di TabRow
                containerColor = MaterialTheme.colorScheme.surface, // Colore di sfondo di TabRow
                indicator = { tabPositions ->
                    SecondaryIndicator(
                        Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                        color = MaterialTheme.colorScheme.tertiary // Colore dell'indicatore
                    )
                },
                divider = { } // Nascondi il divider tra le tab
            ) {
                tabs.forEachIndexed { index, tab ->
                    Tab(selected = selectedTabIndex == index,
                        onClick = { viewModel.selectTab(index) },
                        modifier = Modifier.widthIn(min = 80.dp), // Larghezza minima per ogni tab
                        text = {
                            Text(
                                tab,
                                style = MaterialTheme.typography.bodySmall,
                                maxLines = 1,
                                color = MaterialTheme.colorScheme.primary,
                                overflow = TextOverflow.Ellipsis
                            )
                        })
                }
            }

            // Contenuto scrollabile
            Column(modifier = Modifier
                .constrainAs(content) {
                    top.linkTo(tabRow.bottom) // Collega al bottom di TabRow
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(buttons.top)
                    width = Dimension.fillToConstraints // Aggiungi vincolo per la larghezza
                    height = Dimension.fillToConstraints
                }
                .verticalScroll(scrollState)) {
                //... (Contenuto come prima, ma usa when per selezionare la sezione)...
                when (selectedTabIndex) {
                    0 -> GeneralInfoSection(character, viewModel)
                    1 -> AttributiSection(character, viewModel, isLandscape)
                    2 -> AbilitaSection(character, viewModel)
                    3 -> DisciplineSection(character, viewModel)
                    //... altre sezioni...
                }
            }

            Row(modifier = Modifier
                .constrainAs(buttons) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                }
                .fillMaxWidth()
                .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween) {
                Button(
                    onClick = { viewModel.onEvent(CharacterSheetEvent.SaveClicked) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.tertiary,
                        contentColor = MaterialTheme.colorScheme.secondary
                    )
                ) {
                    Text("Save")
                }
                Button(
                    onClick = { viewModel.onEvent(CharacterSheetEvent.CleanupClicked) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.tertiary,
                        contentColor = MaterialTheme.colorScheme.secondary
                    )
                ) {
                    Text("Cleanup")
                }
                Button(
                    onClick = { viewModel.onEvent(CharacterSheetEvent.DeleteClicked) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.tertiary,
                        contentColor = MaterialTheme.colorScheme.secondary
                    )
                ) {
                    Text("Delete")
                }
            }
        }
    }
}

@Composable
fun DisciplineSection(character: Character, viewModel: CharacterSheetViewModel) {

}

@Composable
fun AbilitaSection(character: Character, viewModel: CharacterSheetViewModel) {

    val expandedStates = remember { mutableStateMapOf<String, Boolean>() }
    val allAbilities = viewModel.allAbilities // Prendi la lista completa dal ViewModel

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
    // Abilità Fisiche
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
                val ability = character.abilities.firstOrNull { it.name == abilityName } ?: Ability(
                    name = abilityName, level = 0
                )
                AbilityItem(ability, viewModel, expandedStates)
            }
        }
    }

    // Abilità Sociali (struttura simile a quelle Fisiche)
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

    // Abilità Mentali (struttura simile)
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

                OutlinedTextField(value = ability.specialization ?: "",
                    onValueChange = { newSpecialization ->
                        viewModel.onEvent(CharacterSheetEvent.AbilitySpecializationChanged(ability.name, // Usa il nome dell'abilità
                            newSpecialization.ifBlank { null }))
                    },
                    label = { Text("Specializzazione (opzionale)") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        })

    // LaunchedEffect per aggiornare lo stato di espansione *nella mappa*
    LaunchedEffect(isExpanded) {
        expandedStates[ability.name] = isExpanded // Aggiorna lo stato *nella mappa*
        if (!isExpanded) {
            //Resetta specializzazione
            viewModel.onEvent(CharacterSheetEvent.AbilitySpecializationChanged(ability.name, null))
        }
    }
}

@Composable
fun AttributiSection(
    character: Character, viewModel: CharacterSheetViewModel, isLandscape: Boolean
) {
    var showSliderForAttribute by remember { mutableStateOf<String?>(null) }

    // CHIAVE per forzare la ricomposizione
    var refreshKey by remember { mutableStateOf(0) }

    // LaunchedEffect che osserva la refreshKey
    LaunchedEffect(refreshKey) {
        // Questa parte viene eseguita ogni volta che refreshKey cambia
        // Non è necessario fare nulla qui, la ricomposizione è sufficiente
    }


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {

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
                var forza by remember(character.attributes.strength) { mutableStateOf(character.attributes.strength) }
                CustomContentExpander(maxWith = if (isLandscape) 0.45f else 1f,
                    padding = 0.dp,
                    header = {
                        DotsForAttribute(
                            stringResource(R.string.character_screen_attributes_strength), forza
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
                var destrezza by remember(character.attributes.dexterity) { mutableStateOf(character.attributes.dexterity) }
                CustomContentExpander(maxWith = if (isLandscape) 0.45f else 1f,
                    padding = 0.dp,
                    header = {
                        DotsForAttribute(
                            stringResource(R.string.character_screen_attributes_dexterity),
                            destrezza
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
                    mutableStateOf(
                        character.attributes.stamina
                    )
                }
                CustomContentExpander(maxWith = if (isLandscape) 0.45f else 1f,
                    padding = 0.dp,
                    header = {
                        DotsForAttribute(
                            stringResource(R.string.character_screen_attributes_stamina),
                            costituzione
                        )
                    },
                    content = {
                        Slider(
                            value = costituzione.toFloat(), onValueChange = { newValue ->
                                costituzione = newValue.toInt()
                                viewModel.onEvent(CharacterSheetEvent.StaminaChanged(costituzione))
                            }, valueRange = 1f..5f, steps = 3, colors = SliderDefaults.colors(
                                activeTrackColor = MaterialTheme.colorScheme.primary,
                            ), modifier = Modifier.fillMaxWidth()
                        )
                    })
                Spacer(modifier = Modifier.height(8.dp))
            }
        }

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
                var charisma by remember(character.attributes.charisma) { mutableStateOf(character.attributes.charisma) }
                CustomContentExpander(maxWith = if (isLandscape) 0.45f else 1f,
                    padding = 0.dp,
                    header = {
                        DotsForAttribute(
                            stringResource(R.string.character_screen_attributes_charisma), charisma
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
                    mutableStateOf(
                        character.attributes.manipulation
                    )
                }
                CustomContentExpander(maxWith = if (isLandscape) 0.45f else 1f,
                    padding = 0.dp,
                    header = {
                        DotsForAttribute(
                            stringResource(R.string.character_screen_attributes_manipulation),
                            persuasione
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
                    mutableStateOf(
                        character.attributes.composure
                    )
                }
                CustomContentExpander(maxWith = if (isLandscape) 0.45f else 1f,
                    padding = 0.dp,
                    header = {
                        DotsForAttribute(
                            stringResource(R.string.character_screen_attributes_composure),
                            autocontrollo
                        )
                    },
                    content = {
                        Slider(
                            value = autocontrollo.toFloat(), onValueChange = { newValue ->
                                autocontrollo = newValue.toInt()
                                viewModel.onEvent(CharacterSheetEvent.ComposureChanged(autocontrollo))
                            }, valueRange = 1f..5f, steps = 3, colors = SliderDefaults.colors(
                                activeTrackColor = MaterialTheme.colorScheme.primary, // Colore della parte piena
                            ), modifier = Modifier.fillMaxWidth()
                        )
                    })
                Spacer(modifier = Modifier.height(8.dp))
            }
        }

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
                    mutableStateOf(
                        character.attributes.intelligence
                    )
                }
                CustomContentExpander(maxWith = if (isLandscape) 0.45f else 1f,
                    padding = 0.dp,
                    header = {
                        DotsForAttribute(
                            stringResource(R.string.character_screen_attributes_intelligence),
                            intelligenza
                        )
                    },
                    content = {
                        Slider(
                            value = intelligenza.toFloat(), onValueChange = { newValue ->
                                intelligenza = newValue.toInt()
                                viewModel.onEvent(
                                    CharacterSheetEvent.IntelligenceChanged(
                                        intelligenza
                                    )
                                )
                            }, colors = SliderDefaults.colors(
                                activeTrackColor = MaterialTheme.colorScheme.primary, // Colore della parte piena
                            ), valueRange = 1f..5f, steps = 3, modifier = Modifier.fillMaxWidth()
                        )
                    })

                // Prontezza
                var prontezza by remember(character.attributes.wits) { mutableStateOf(character.attributes.wits) }
                CustomContentExpander(maxWith = if (isLandscape) 0.45f else 1f,
                    padding = 0.dp,
                    header = {
                        DotsForAttribute(
                            stringResource(R.string.character_screen_attributes_wits), prontezza
                        )
                    },
                    content = {
                        Slider(
                            value = prontezza.toFloat(), onValueChange = { newValue ->
                                prontezza = newValue.toInt()
                                viewModel.onEvent(CharacterSheetEvent.WitsChanged(prontezza))
                            }, colors = SliderDefaults.colors(
                                activeTrackColor = MaterialTheme.colorScheme.primary, // Colore della parte piena
                            ), valueRange = 1f..5f, steps = 3, modifier = Modifier.fillMaxWidth()
                        )
                    })

                // Fermezza
                var fermezza by remember(character.attributes.resolve) { mutableStateOf(character.attributes.resolve) }
                CustomContentExpander(maxWith = if (isLandscape) 0.45f else 1f,
                    padding = 0.dp,
                    header = {
                        DotsForAttribute(
                            stringResource(R.string.character_screen_attributes_resolve), fermezza
                        )
                    },
                    content = {
                        Slider(
                            value = fermezza.toFloat(), onValueChange = { newValue ->
                                fermezza = newValue.toInt()
                                viewModel.onEvent(CharacterSheetEvent.ComposureChanged(fermezza))
                            }, colors = SliderDefaults.colors(
                                activeTrackColor = MaterialTheme.colorScheme.primary, // Colore della parte piena
                            ), valueRange = 1f..5f, steps = 3, modifier = Modifier.fillMaxWidth()
                        )
                    })
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
    // Osserva i cambiamenti nel personaggio e aggiorna la chiave se necessario
    LaunchedEffect(character) {
        refreshKey++
    }
}

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
                OutlinedTextField(value = selectedClan?.name ?: searchQuery,
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
                    }))

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
            var generation by remember { mutableStateOf(character.generation.toFloat()) }
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