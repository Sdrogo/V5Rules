package com.example.v5rules.ui.compose.component.loresheet

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.v5rules.R
import com.example.v5rules.data.Loresheet
import com.example.v5rules.data.LoresheetPower
import com.example.v5rules.ui.compose.component.ContentExpander
import com.example.v5rules.ui.compose.component.DotsOnlyForLevel
import com.example.v5rules.ui.compose.component.background.InteractiveBackgroundDots
import com.example.v5rules.utils.CharacterSheetEvent
import com.example.v5rules.viewModel.CharacterSheetViewModel

@Composable
fun LoresheetList(
    loresheets: List<Loresheet>,
    viewModel: CharacterSheetViewModel
) {
   Column {
       loresheets.forEach { loresheet ->
           LoresheetItem(
               loresheet = loresheet,
               viewModel = viewModel,
               onRemove = {viewModel.onEvent(CharacterSheetEvent.LoresheetRemoved(loresheet)) }
           )
       }
    }
}

@Composable
fun LoresheetItem(
    loresheet: Loresheet,
    viewModel: CharacterSheetViewModel,
    onRemove: (Loresheet) -> Unit
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
                Text(text = loresheet.title, style = MaterialTheme.typography.titleMedium, modifier = Modifier.weight(1f))

                InteractiveBackgroundDots(
                    currentValue = loresheet.level,
                    minValue = 1,
                    maxValue = 5,
                    onValueChange = { newLevel -> viewModel.onEvent(
                        CharacterSheetEvent.LoresheetLevelChanged(
                            loresheet.title,
                            newLevel
                        )
                    ) }
                )
                IconButton(onClick = {onRemove(loresheet)}) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Remove ${loresheet.title}",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
                Icon(imageVector = if (expanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                    contentDescription = if (expanded) "Collapse ${loresheet.title}" else "Expand ${loresheet.title}"
                )
            }
        }
        if (expanded) {
            ContentExpander(stringResource(R.string.discipline_description)) {
                Text(text = loresheet.content, style = MaterialTheme.typography.bodyMedium)
            }
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
