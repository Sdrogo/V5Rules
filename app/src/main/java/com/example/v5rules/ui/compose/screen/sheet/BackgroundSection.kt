package com.example.v5rules.ui.compose.screen.sheet

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.v5rules.R
import com.example.v5rules.data.Background
import com.example.v5rules.ui.compose.component.bottomSheet.BackgroundFlawsSelectionBottomSheet
import com.example.v5rules.ui.compose.component.background.BackgroundList
import com.example.v5rules.ui.compose.component.bottomSheet.BackgroundSelectionBottomSheet
import com.example.v5rules.ui.compose.component.bottomSheet.DirectFlawSelectionBottomSheet
import com.example.v5rules.ui.compose.component.background.DirectFlawsList
import com.example.v5rules.ui.compose.component.bottomSheet.MeritsSelectionBottomSheet
import com.example.v5rules.ui.compose.component.loresheet.LoresheetList
import com.example.v5rules.ui.compose.component.bottomSheet.LoresheetSelectionBottomSheet
import com.example.v5rules.utils.CharacterSheetEvent
import com.example.v5rules.viewModel.CharacterSheetViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BackgroundSection(
    viewModel: CharacterSheetViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    val allLoresheets by viewModel.loreSheets.collectAsState() // All available loresheets
    val allBackgrounds by viewModel.backgrounds.collectAsState() // All available backgrounds
    val allDirectFlaws by viewModel.directFlaws.collectAsState() // All available direct flaws
    val characterLoresheets = uiState.character.loresheets
    val characterBackgrounds = uiState.character.backgrounds
    val characterDirectFlaws = uiState.character.directFlaws

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()
    var showAddLoresheetSheet by remember { mutableStateOf(false) }
    var showAddBackgroundSheet by remember { mutableStateOf(false) }
    var showAddDirectFlawsSheet by remember { mutableStateOf(false) }
    var showAddMeritsSheet by remember { mutableStateOf(false) }
    var showAddFlawsToBackgroundSheet by remember { mutableStateOf(false) }
    var selectedBackground by remember { mutableStateOf<Background?>(null) }

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
    } else if (showAddBackgroundSheet) {
        BackgroundSelectionBottomSheet(
            allBackgrounds = allBackgrounds ,
            onBackgroundSelected = { background ->
                viewModel.onEvent(
                    CharacterSheetEvent.BackgroundAdded(
                        background,
                        background.minLevel
                    )
                )
                scope.launch { sheetState.hide() }
                showAddBackgroundSheet = false
            },
            onDismiss = {
                showAddBackgroundSheet = false
            }
        )
    } else if (showAddMeritsSheet && selectedBackground != null) {
        allBackgrounds.find { it.id == (selectedBackground!!.id) }?.let {
            characterBackgrounds.find { characterBg -> characterBg.identifier == selectedBackground!!.identifier }
                ?.let { characterBackground ->
                    MeritsSelectionBottomSheet(
                        background = it,
                        onMeritSelected = { merit ->
                            viewModel.onEvent(
                                CharacterSheetEvent.BackgroundMeritAdded(
                                    characterBackground,
                                    merit,
                                    merit.minLevel ?: 1
                                )
                            )
                            scope.launch { sheetState.hide() }
                            showAddMeritsSheet = false
                        },
                        onDismiss = {
                            showAddMeritsSheet = false
                        }
                    )
                }

        }
    } else if (showAddFlawsToBackgroundSheet && selectedBackground != null) {
        allBackgrounds.find { it.id == selectedBackground!!.id }?.let {
            BackgroundFlawsSelectionBottomSheet(
                background = it,
                onFlawSelected = { flaw ->
                    viewModel.onEvent(
                        CharacterSheetEvent.BackgroundFlawAdded(
                            selectedBackground!!,
                            flaw,
                            flaw.minLevel ?: 1
                        )
                    )
                    scope.launch { sheetState.hide() }
                    showAddFlawsToBackgroundSheet = false
                },
                onDismiss = {
                    showAddFlawsToBackgroundSheet = false
                }
            )
        }
    } else if (showAddDirectFlawsSheet) {
        DirectFlawSelectionBottomSheet(
            allDirectFlaws = allDirectFlaws,
            onDirectFlawSelected = { flaw ->
                viewModel.onEvent(
                    CharacterSheetEvent.CharacterDirectFlawAdded(
                        flaw,
                        flaw.minLevel ?: 1
                    )
                )
                scope.launch { sheetState.hide() }
                showAddDirectFlawsSheet = false
            },
            onDismiss = {
                showAddDirectFlawsSheet = false
            }
        )
    } else LazyColumn(
        modifier = Modifier.padding(16.dp)
    ) {
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
                    .border(
                        1.dp, MaterialTheme.colorScheme.tertiary, RoundedCornerShape(8.dp)
                    ),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            )
            {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = stringResource(R.string.lore_screen_title),
                            style = MaterialTheme.typography.headlineSmall,
                            modifier = Modifier.weight(1f) // Take up available space
                        )
                        IconButton(onClick = {
                            showAddLoresheetSheet = true
                        }) { // Show the bottom sheet
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = stringResource(R.string.add_loresheet),
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
                        Text(
                            stringResource(R.string.no_loresheet_selected),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
                    .border(1.dp, MaterialTheme.colorScheme.tertiary, RoundedCornerShape(8.dp)),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {

                        Text(
                            text = stringResource(R.string.background),
                            style = MaterialTheme.typography.headlineSmall,
                            modifier = Modifier.weight(1f)
                        )
                        IconButton(onClick = {
                            showAddBackgroundSheet = true
                        }) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = stringResource(R.string.add_background),
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    if (characterBackgrounds.isNotEmpty()) {
                        BackgroundList(
                            backgrounds = characterBackgrounds,
                            allGameBackgrounds = allBackgrounds,
                            onAddMeritClick = { background ->
                                selectedBackground = background
                                showAddMeritsSheet = true
                            },
                            onAddFlawClick = { background ->
                                selectedBackground = background
                                showAddFlawsToBackgroundSheet = true
                            },
                            onEvent = {event -> viewModel.onEvent(event)}
                        )
                    } else {
                        Text(
                            stringResource(R.string.no_background_selected),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
                    .border(1.dp, MaterialTheme.colorScheme.tertiary, RoundedCornerShape(8.dp)),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = stringResource(R.string.flaws),
                            style = MaterialTheme.typography.headlineSmall,
                            modifier = Modifier.weight(1f) // Take up available space
                        )
                        IconButton(onClick = {
                            showAddDirectFlawsSheet = true
                        }) { // Show the bottom sheet
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = stringResource(R.string.add_flaw),
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    if (characterDirectFlaws.isNotEmpty()) {
                        DirectFlawsList(
                            flaws = characterDirectFlaws,
                            onEvent = {event -> viewModel.onEvent(event)}
                        )
                    } else {
                        Text(
                            stringResource(R.string.no_flaw_selected),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
    }
}