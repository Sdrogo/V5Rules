package com.example.v5rules.ui.compose.screen.sheet.visualization

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.v5rules.R
import com.example.v5rules.ui.compose.component.background.BackgroundListVisualization
import com.example.v5rules.ui.compose.component.background.DirectFlawsListVisualization
import com.example.v5rules.ui.compose.component.loresheet.LoresheetListVisualization
import com.example.v5rules.viewModel.CharacterSheetViewModel

@Composable
fun BackgroundSectionVisualization(
    viewModel: CharacterSheetViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    val characterLoresheets = uiState.character.loresheets
    val characterBackgrounds = uiState.character.backgrounds
    val characterDirectFlaws = uiState.character.directFlaws

    LazyColumn(
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
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    if (characterLoresheets.isNotEmpty()) {
                        LoresheetListVisualization(
                            loresheets = characterLoresheets
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
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    if (characterBackgrounds.isNotEmpty()) {
                        BackgroundListVisualization(
                            backgrounds = characterBackgrounds
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
                            text = stringResource(R.string.flaw),
                            style = MaterialTheme.typography.headlineSmall,
                            modifier = Modifier.weight(1f) // Take up available space
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    if (characterDirectFlaws.orEmpty().isNotEmpty()) {
                        DirectFlawsListVisualization( flaws = characterDirectFlaws.orEmpty() )
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
