// In ui/compose/screen/sheet/BackgroundSection.kt
package com.example.v5rules.ui.compose.screen.sheet

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.v5rules.data.Advantage
import com.example.v5rules.data.Background
import com.example.v5rules.data.Loresheet
import com.example.v5rules.data.LoresheetPower
import com.example.v5rules.ui.compose.component.DotsForAttribute
import com.example.v5rules.ui.compose.component.DotsOnlyForLevel
import com.example.v5rules.utils.CharacterSheetEvent
import com.example.v5rules.viewModel.CharacterSheetViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BackgroundSection(
    viewModel: CharacterSheetViewModel,
    navController: NavHostController
) {
    val uiState by viewModel.uiState.collectAsState()
    val allLoresheets by viewModel.loreSheets.collectAsState() // All available loresheets
    val allBackgrounds by viewModel.backgrounds.collectAsState() // All available backgrounds
    val characterLoresheets = uiState.character.loresheets
    val characterBackgrounds = uiState.character.backgrounds

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()
    var showAddLoresheetSheet by remember { mutableStateOf(false) } // Control bottom sheet visibility
    var showAddBackgroundSheet by remember { mutableStateOf(false) } // Control bottom sheet visibility

    if (showAddLoresheetSheet) {
        LoresheetSelectionBottomSheet(
            allLoresheets = allLoresheets,
            characterLoresheets = characterLoresheets,
            onLoresheetSelected = { loresheet ->
                viewModel.onEvent(CharacterSheetEvent.LoresheetAdded(loresheet, 1))
                scope.launch { sheetState.hide() }
                showAddLoresheetSheet = false
            },
            onDismiss = {
                showAddLoresheetSheet = false
            }
        )
    }

    if (showAddBackgroundSheet) {
        BackgroundSelectionBottomSheet(
            allBackgrounds = allBackgrounds,
            characterBackgrounds = characterBackgrounds,
            onBackgroundSelected = { background ->
                viewModel.onEvent(
                    CharacterSheetEvent.BackgroundAdded(
                        background,
                        background.minLevel ?: 1
                    )
                )
                scope.launch { sheetState.hide() }
                showAddBackgroundSheet = false
            },
            onDismiss = {
                showAddBackgroundSheet = false
            }
        )
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Loresheet",
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier.weight(1f) // Take up available space
                    )
                    IconButton(onClick = {
                        showAddLoresheetSheet = true
                    }) { // Show the bottom sheet
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add Loresheet",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                if (characterLoresheets.isNotEmpty()) {
                    LoresheetList(
                        loresheets = characterLoresheets,
                        viewModel = viewModel
                    )
                } else {
                    Text("No Loresheets selected.", style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Background",
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier.weight(1f) // Take up available space
                    )
                    IconButton(onClick = {
                        showAddBackgroundSheet = true
                    }) { // Show the bottom sheet
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add Background",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                if (characterBackgrounds.isNotEmpty()) {
                    BackgroundList(
                        backgrounds = characterBackgrounds,
                        viewModel = viewModel,
                        allGameBackgrounds = allBackgrounds
                    )
                } else {
                    Text("No Background selected.", style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
    }
}

@Composable
fun LoresheetList(
    loresheets: List<Loresheet>,
    viewModel: CharacterSheetViewModel
) {
    LazyColumn {
        items(loresheets) { loresheet ->
            LoresheetItem(
                loresheet = loresheet,
                viewModel = viewModel
            )
        }
    }
}

@Composable
fun BackgroundList(
    backgrounds: List<Background>, // Lista dei background del personaggio
    viewModel: CharacterSheetViewModel,
    allGameBackgrounds: List<Background> // Lista di tutti i background disponibili nel gioco
    // (per ottenere le definizioni complete di merits/flaws potenziali)
) {
    LazyColumn {
        items(backgrounds) { characterBackground -> // characterBackground è il background con i dati scelti dal PG
            BackgroundItem(
                characterBackground = characterBackground,
                viewModel = viewModel,
                allGameBackgrounds = allGameBackgrounds // Passala a BackgroundItem
            )
        }
    }
}

@Composable
fun BackgroundItem(
    characterBackground: Background, // Il Background specifico del personaggio con i suoi dati (livello, merits/flaws scelti)
    viewModel: CharacterSheetViewModel,
    allGameBackgrounds: List<Background> // Lista di tutti i background disponibili nel gioco
) {
    var expanded by remember { mutableStateOf(false) }

    // Trova la definizione completa del background dal gioco
    // Questo serve per accedere alla lista completa dei merits/flaws *potenziali* definiti per questo tipo di background
    val gameBackgroundDefinition = remember(characterBackground.id, allGameBackgrounds) {
        allGameBackgrounds.find { it.id == characterBackground.id }
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = !expanded }) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                DotsForAttribute(
                    label = characterBackground.title,
                    level = characterBackground.level,
                )
            }
        }
        if (expanded) {
            Spacer(modifier = Modifier.height(8.dp))
            Slider(
                value = characterBackground.level.toFloat(),
                onValueChange = { newValue ->
                    val newIntValue = newValue.toInt()
                    if (newIntValue >= (characterBackground.minLevel
                            ?: 0)
                    ) { // Usa minLevel del background se definito
                        viewModel.onEvent(
                            CharacterSheetEvent.BackgroundLevelChanged(
                                characterBackground, // Passa l'intero oggetto o solo l'ID/nome come hai definito nell'evento
                                newIntValue
                            )
                        )
                    } else if (newIntValue == 0) { // Opzione per rimuovere se il livello è 0
                        viewModel.onEvent(
                            CharacterSheetEvent.BackgroundRemoved(characterBackground)
                        )
                    }
                },
                valueRange = (characterBackground.minLevel?.toFloat()
                    ?: 0f)..(characterBackground.maxLevel?.toFloat() ?: 5f),
                steps = ((characterBackground.maxLevel ?: 5) - (characterBackground.minLevel
                    ?: 0) - 1).coerceAtLeast(0),
                colors = SliderDefaults.colors(
                    activeTrackColor = MaterialTheme.colorScheme.tertiary
                )
            )
            Spacer(modifier = Modifier.height(8.dp))
            // --- Sezione Merits ---
            Text("Pregi (Merits)", style = MaterialTheme.typography.titleMedium)
            characterBackground.merits?.forEach { merit ->
                AdvantageDisplayItem(
                    advantage = merit,
                    onLevelChange = { newLevel ->
                        viewModel.onEvent(
                            CharacterSheetEvent.BackgroundMeritLevelChanged(
                                characterBackground.id,
                                merit.id,
                                newLevel
                            )
                        )
                    },
                    onRemove = {
                        viewModel.onEvent(
                            CharacterSheetEvent.BackgroundMeritRemoved(
                                characterBackground.id,
                                merit
                            )
                        )
                    }
                )
            }
            Button(onClick = {
                // TODO: Aprire BottomSheet per selezionare un MERIT
                // Dovrai passare gameBackgroundDefinition?.merits (filtrati per quelli non ancora scelti)
                // e characterBackground.id
                println("TODO: Apri BottomSheet per aggiungere Pregio al Background ${characterBackground.title}")
            }) {
                Text("Aggiungi Pregio")
            }
            Spacer(modifier = Modifier.height(16.dp))

            // --- Sezione Flaws ---
            Text("Difetti (Flaws)", style = MaterialTheme.typography.titleMedium)
            characterBackground.flaws?.forEach { flaw ->
                AdvantageDisplayItem(
                    advantage = flaw,
                    onLevelChange = { newLevel ->
                        viewModel.onEvent(
                            CharacterSheetEvent.BackgroundFlawLevelChanged(
                                characterBackground.id,
                                flaw.id,
                                newLevel
                            )
                        )
                    },
                    onRemove = {
                        viewModel.onEvent(
                            CharacterSheetEvent.BackgroundFlawRemoved(
                                characterBackground.id,
                                flaw
                            )
                        )
                    }
                )
            }
            Button(onClick = {
                // TODO: Aprire BottomSheet per selezionare un FLAW
                // Dovrai passare gameBackgroundDefinition?.flaws (filtrati per quelli non ancora scelti)
                // e characterBackground.id
                println("TODO: Apri BottomSheet per aggiungere Difetto al Background ${characterBackground.title}")
            }) {
                Text("Aggiungi Difetto")
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = characterBackground.description, style = MaterialTheme.typography.bodySmall)
            Spacer(modifier = Modifier.height(4.dp))

        }
    }

}

@Composable
fun AdvantageDisplayItem(
    advantage: Advantage,
    onLevelChange: (Int) -> Unit,
    onRemove: () -> Unit,
    // showLevelSlider: Boolean = true, // Rimosso per ora, il range del slider gestisce la non modificabilità
    isCharacterFlaw: Boolean = false // Flag per differenziare se è un difetto del personaggio o di un background
) {
    var itemExpanded by remember { mutableStateOf(false) }
    val canModifyLevel = (advantage.minLevel ?: 1) < (advantage.maxLevel ?: 5) || (advantage.level
        ?: 1) > 0 // O una logica più specifica se necessario
    Column(modifier = Modifier.padding(8.dp)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { itemExpanded = !itemExpanded }
        ) {
            Text(
                text = advantage.title,
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.weight(1f)
            )
            if ((advantage.level ?: 1) > 0 && (advantage.minLevel == null || (advantage.level
                    ?: 1) <= (advantage.maxLevel ?: 5))
            ) { // Mostra i dots solo se c'è un livello
                DotsOnlyForLevel(level = (advantage.level ?: 1))
                Spacer(Modifier.width(8.dp))
            }
            Icon(
                imageVector = if (itemExpanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                contentDescription = if (itemExpanded) "Collapse ${advantage.title}" else "Expand ${advantage.title}"
            )
            IconButton(onClick = onRemove) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Remove ${advantage.title}",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }

        if (itemExpanded) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = advantage.description,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Slider per il livello dell'Advantage (solo se modificabile)
            // Il livello 0 spesso significa "non posseduto" o è usato per la rimozione.
            // minLevel e maxLevel nell'advantage dovrebbero guidare questo.
            val currentMinLevel =
                advantage.minLevel ?: 0 // Se null, assumi 0 per rimozione o 1 se già a 1
            val currentMaxLevel = advantage.maxLevel ?: 5 // Default a 5 se non specificato

            // Mostra lo slider solo se c'è un range di livelli possibile o se si può settare a 0 per rimuovere (logica gestita onValueChange)
            if (currentMinLevel < currentMaxLevel || (advantage.level ?: 1) > 0) {
                Text(
                    "Livello ${advantage.title}: ${advantage.level}",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(start = 8.dp)
                )
                advantage.level?.toFloat()?.let {
                    Slider(
                        value = it,
                        onValueChange = { newValue ->
                            val newIntValue = newValue.toInt()
                            // Permetti di cambiare livello solo se è diverso dall'attuale
                            // e rispetta min/max intrinseci dell'advantage (se specificati)
                            if (newIntValue != advantage.level) {
                                if (newIntValue == 0 && currentMinLevel == 0) { // Se 0 è un'opzione valida (es. per rimozione implicita)
                                    onRemove() // O onLevelChange(0) se gestisci la rimozione a livello 0 nel ViewModel
                                } else if (newIntValue >= (advantage.minLevel
                                        ?: 1) && newIntValue <= (advantage.maxLevel ?: 5)
                                ) {
                                    onLevelChange(newIntValue)
                                } else if (newIntValue >= 1 && newIntValue <= (advantage.maxLevel
                                        ?: 5)
                                ) {
                                    onLevelChange(newIntValue) // Caso in cui minLevel non è definito ma vogliamo > 0
                                }
                            }
                        },
                        valueRange = (currentMinLevel.toFloat())..(currentMaxLevel.toFloat()),
                        steps = (currentMaxLevel - currentMinLevel - 1).coerceAtLeast(0), // steps è il numero di divisioni *tra* i punti
                        colors = SliderDefaults.colors(
                            thumbColor = MaterialTheme.colorScheme.secondary,
                            activeTrackColor = MaterialTheme.colorScheme.secondaryContainer,
                            inactiveTrackColor = MaterialTheme.colorScheme.surfaceVariant,
                        ),
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun LoresheetItem(
    loresheet: Loresheet,
    viewModel: CharacterSheetViewModel
) {
    var expanded by remember { mutableStateOf(false) }
    val characterLoresheet =
        viewModel.uiState.collectAsState().value.character.loresheets.firstOrNull { it.id == loresheet.id }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = !expanded }) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                DotsForAttribute(
                    label = loresheet.title,
                    level = loresheet.level,
                )
            }
        }
        if (expanded) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Slider(
                    value = loresheet.level.toFloat(),
                    onValueChange = { newValue ->
                        if (newValue > 0) {
                            viewModel.onEvent(
                                CharacterSheetEvent.LoresheetLevelChanged(
                                    loresheet.title,
                                    newValue.toInt()
                                )
                            )
                        } else {
                            viewModel.onEvent(
                                CharacterSheetEvent.LoresheetRemoved(loresheet)
                            )
                        }
                    },
                    valueRange = 0f..5f,
                    steps = 4,
                    colors = SliderDefaults.colors(
                        activeTrackColor = MaterialTheme.colorScheme.tertiary
                    )
                )
            }
            Text(text = loresheet.content, style = MaterialTheme.typography.bodyMedium)
        }
        LoresheetPowerSection(
            powers = characterLoresheet?.powers?.subList(0, loresheet.level) ?: emptyList()
        )
    }
}

@Composable
fun LoresheetPowerSection(powers: List<LoresheetPower>) {
    Column(modifier = Modifier.padding(start = 16.dp)) {
        powers.forEach { power ->
            LoresheetPowerItem(power = power)
        }
    }
}

@Composable
fun LoresheetPowerItem(power: LoresheetPower) {
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { expanded = !expanded })
    {
        Row(verticalAlignment = Alignment.CenterVertically) {
            DotsOnlyForLevel(level = power.level)
            Spacer(Modifier.width(8.dp))
            Text(
                text = power.title,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(start = 8.dp)
            )
        }

        if (expanded) {
            Text(
                text = power.content,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}

@Composable
fun LoresheetSelectionBottomSheet(
    allLoresheets: List<Loresheet>,
    characterLoresheets: List<Loresheet>,
    onLoresheetSelected: (Loresheet) -> Unit,
    onDismiss: () -> Unit
) {
    var searchText by remember { mutableStateOf("") }
    val filteredLoresheets = allLoresheets.filter { loresheet ->
        loresheet.title.contains(searchText, ignoreCase = true) &&
                characterLoresheets.none { it.id == loresheet.id } // Exclude already selected loresheets
    }
    Column(
        modifier = Modifier
            .navigationBarsPadding()
            .imePadding()
    ) {
        TextField(
            value = searchText,
            onValueChange = { searchText = it },
            label = { Text("Search Loresheets") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )

        LazyColumn(modifier = Modifier.padding(bottom = 56.dp)) { // Added padding for FAB
            items(filteredLoresheets) { loresheet ->
                LoresheetSelectionBottomSheetItem(
                    loresheet = loresheet,
                    onLoresheetSelected = { onLoresheetSelected(it) }
                )
            }
        }
        Button(onClick = onDismiss) {
            Text(text = "Close")
        }
    }
}

@Composable
fun LoresheetSelectionBottomSheetItem(
    loresheet: Loresheet,
    onLoresheetSelected: (Loresheet) -> Unit
) {
    Text(
        text = loresheet.title,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onLoresheetSelected(loresheet) }
            .padding(16.dp)
    )
}

@Composable
fun BackgroundSelectionBottomSheet(
    allBackgrounds: List<Background>,
    characterBackgrounds: List<Background>,
    onBackgroundSelected: (Background) -> Unit,
    onDismiss: () -> Unit
) {
    var searchText by remember { mutableStateOf("") }
    val filteredBackgrounds = allBackgrounds.filter { background ->
        background.title.contains(searchText, ignoreCase = true) &&
                characterBackgrounds.none { it.id == background.id }
    }
    Column(
        modifier = Modifier
            .navigationBarsPadding()
            .imePadding()
    ) {
        TextField(
            value = searchText,
            onValueChange = { searchText = it },
            label = { Text("Search Loresheets") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )

        LazyColumn(modifier = Modifier.padding(bottom = 56.dp)) { // Added padding for FAB
            items(filteredBackgrounds) { background ->
                BackgroundSelectionBottomSheetItem(
                    background = background,
                    ononBackgroundSelected = { onBackgroundSelected(it) }
                )
            }
        }
        Button(onClick = onDismiss) {
            Text(text = "Close")
        }
    }
}

@Composable
fun BackgroundSelectionBottomSheetItem(
    background: Background,
    ononBackgroundSelected: (Background) -> Unit
) {

    Text(
        text = background.title,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { ononBackgroundSelected(background) }
            .padding(16.dp)
    )
}