package com.example.v5rules.ui.compose.screen.sheet

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.v5rules.R
import com.example.v5rules.data.Character
import com.example.v5rules.utils.CharacterSheetEvent
import com.example.v5rules.viewModel.CharacterSheetViewModel

@Composable
fun AttributeSection(
    character: Character, viewModel: CharacterSheetViewModel
) {
    // Le variabili di stato locali vengono mantenute per gestire l'UI in tempo reale
    // e vengono inizializzate con i valori del character.
    var forza by remember(character.attributes.strength) { mutableIntStateOf(character.attributes.strength) }
    var destrezza by remember(character.attributes.dexterity) { mutableIntStateOf(character.attributes.dexterity) }
    var costituzione by remember(character.attributes.stamina) { mutableIntStateOf(character.attributes.stamina) }
    var charisma by remember(character.attributes.charisma) { mutableIntStateOf(character.attributes.charisma) }
    var persuasione by remember(character.attributes.manipulation) { mutableIntStateOf(character.attributes.manipulation) }
    var autocontrollo by remember(character.attributes.composure) { mutableIntStateOf(character.attributes.composure) }
    var intelligenza by remember(character.attributes.intelligence) { mutableIntStateOf(character.attributes.intelligence) }
    var prontezza by remember(character.attributes.wits) { mutableIntStateOf(character.attributes.wits) }
    var fermezza by remember(character.attributes.resolve) { mutableIntStateOf(character.attributes.resolve) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        // Attributi Fisici
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.secondary,
                    shape = RoundedCornerShape(8.dp)
                ),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(
                    text = stringResource(R.string.character_screen_physical),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .wrapContentWidth()
                        .align(Alignment.CenterHorizontally)
                        .padding(bottom = 8.dp)
                )
                InteractiveAttributeRow(
                    label = stringResource(R.string.character_screen_attributes_strength),
                    currentValue = forza,
                    onValueChange = { newValue ->
                        forza = newValue
                        viewModel.onEvent(CharacterSheetEvent.StrengthChanged(newValue))
                    }
                )
                InteractiveAttributeRow(
                    label = stringResource(R.string.character_screen_attributes_dexterity),
                    currentValue = destrezza,
                    onValueChange = { newValue ->
                        destrezza = newValue
                        viewModel.onEvent(CharacterSheetEvent.DexterityChanged(newValue))
                    }
                )
                InteractiveAttributeRow(
                    label = stringResource(R.string.character_screen_attributes_stamina),
                    currentValue = costituzione,
                    onValueChange = { newValue ->
                        costituzione = newValue
                        viewModel.onEvent(CharacterSheetEvent.StaminaChanged(newValue))
                    }
                )
            }
        }

        // Attributi Sociali
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.secondary,
                    shape = RoundedCornerShape(8.dp)
                ),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(
                    text = stringResource(R.string.character_screen_social),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .wrapContentWidth()
                        .align(Alignment.CenterHorizontally)
                        .padding(bottom = 8.dp)
                )
                InteractiveAttributeRow(
                    label = stringResource(R.string.character_screen_attributes_charisma),
                    currentValue = charisma,
                    onValueChange = { newValue ->
                        charisma = newValue
                        viewModel.onEvent(CharacterSheetEvent.CharismaChanged(newValue))
                    }
                )
                InteractiveAttributeRow(
                    label = stringResource(R.string.character_screen_attributes_manipulation),
                    currentValue = persuasione,
                    onValueChange = { newValue ->
                        persuasione = newValue
                        viewModel.onEvent(CharacterSheetEvent.ManipulationChanged(newValue))
                    }
                )
                InteractiveAttributeRow(
                    label = stringResource(R.string.character_screen_attributes_composure),
                    currentValue = autocontrollo,
                    onValueChange = { newValue ->
                        autocontrollo = newValue
                        viewModel.onEvent(CharacterSheetEvent.ComposureChanged(newValue))
                    }
                )
            }
        }

        // Attributi Mentali
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.secondary,
                    shape = RoundedCornerShape(8.dp)
                ),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(
                    text = stringResource(R.string.character_screen_mental),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .wrapContentWidth()
                        .align(Alignment.CenterHorizontally)
                        .padding(bottom = 8.dp)
                )
                InteractiveAttributeRow(
                    label = stringResource(R.string.character_screen_attributes_intelligence),
                    currentValue = intelligenza,
                    onValueChange = { newValue ->
                        intelligenza = newValue
                        viewModel.onEvent(CharacterSheetEvent.IntelligenceChanged(newValue))
                    }
                )
                InteractiveAttributeRow(
                    label = stringResource(R.string.character_screen_attributes_wits),
                    currentValue = prontezza,
                    onValueChange = { newValue ->
                        prontezza = newValue
                        viewModel.onEvent(CharacterSheetEvent.WitsChanged(newValue))
                    }
                )
                InteractiveAttributeRow(
                    label = stringResource(R.string.character_screen_attributes_resolve),
                    currentValue = fermezza,
                    onValueChange = { newValue ->
                        fermezza = newValue
                        viewModel.onEvent(CharacterSheetEvent.ResolveChanged(newValue))
                    }
                )
            }
        }
    }
}

@Composable
private fun InteractiveAttributeRow(
    label: String,
    currentValue: Int,
    onValueChange: (Int) -> Unit,
    maxDots: Int = 5,
    textStyle: TextStyle = MaterialTheme.typography.bodyLarge,
    dotSize: Dp = 20.dp
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, style = textStyle, modifier = Modifier.weight(1f))
        Row(
            horizontalArrangement = Arrangement.End
        ) {
            for (i in 1..maxDots) {
                Box(
                    modifier = Modifier
                        .size(dotSize)
                        .padding(2.dp)
                        .clip(CircleShape)
                        .background(
                            if (i <= currentValue) MaterialTheme.colorScheme.secondary else Color.Transparent
                        )
                        .border(
                            width = 1.dp,
                            color = if (i <= currentValue) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(
                                alpha = 0.3f
                            ),
                            shape = CircleShape
                        )
                        .clickable { onValueChange(i) }
                )
            }
        }
    }
}
