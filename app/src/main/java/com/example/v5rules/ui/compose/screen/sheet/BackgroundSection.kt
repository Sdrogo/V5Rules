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
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetState
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
    val characterLoresheets = uiState.character.loresheets  // Character's selected loresheets

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()
    var showAddLoresheetSheet by remember { mutableStateOf(false) } // Control bottom sheet visibility

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

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Loresheets",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.weight(1f) // Take up available space
                )
                IconButton(onClick = { showAddLoresheetSheet = true }) { // Show the bottom sheet
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add Loresheet",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Display the character's selected loresheets
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
        Box(modifier = Modifier
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
            Row ( verticalAlignment = Alignment.CenterVertically )  {
                Slider(
                    value = loresheet.level.toFloat(),
                    onValueChange = { newValue ->
                        if(newValue > 0 ){
                            viewModel.onEvent(
                                CharacterSheetEvent.LoresheetLevelChanged(
                                    loresheet.title,
                                    newValue.toInt()
                                )
                            )
                        }else{
                            viewModel.onEvent(
                                CharacterSheetEvent.LoresheetRemoved( loresheet)
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

    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 4.dp)
        .clickable { expanded = !expanded })
    {
        Row(verticalAlignment = Alignment.CenterVertically){
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