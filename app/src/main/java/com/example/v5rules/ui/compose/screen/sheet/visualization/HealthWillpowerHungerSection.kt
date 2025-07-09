package com.example.v5rules.ui.compose.screen.sheet.visualization

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable // NUOVO IMPORT
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.v5rules.R
import com.example.v5rules.data.Character
import com.example.v5rules.data.DamageType
import com.example.v5rules.data.Health
import com.example.v5rules.data.Willpower
import com.example.v5rules.ui.compose.component.DamageTrackDisplay
import com.example.v5rules.utils.CharacterSheetEvent
import com.example.v5rules.ui.theme.V5RulesTheme
import kotlin.math.max // NUOVO IMPORT

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun HealthWillpowerHungerSection(
    character: Character,
    onEvent: (CharacterSheetEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    FlowRow(modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 8.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        maxItemsInEachRow = 4
    )
    {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .wrapContentSize()
                .padding(bottom = 16.dp)
        ) {
            Text(
                text = stringResource(R.string.hunger) + ": ",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.align(Alignment.CenterVertically)
            )
            HungerDisplay(
                currentHunger = (character.hunger ?: 0), // Default a 0 se null
                onHungerChange = { newHunger ->
                    onEvent(CharacterSheetEvent.HungerChanged(newHunger))
                }
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .wrapContentSize()
                .padding(bottom = 16.dp) // Aumentato padding
        ) {
            Text(
                text = stringResource(R.string.health) + ": ",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.align(Alignment.CenterVertically)
            )
            DamageTrackDisplay(
                damageTrack = character.health.boxes,
                onBoxClick = { index ->
                    onEvent(CharacterSheetEvent.HealthBoxClicked(index))
                },
                allowsAggravated = true
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.wrapContentSize()
        ) {
            Text(
                text = stringResource(R.string.willpower) + ": ",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.align(Alignment.CenterVertically)
            )
            DamageTrackDisplay(
                damageTrack = character.willpower.boxes,
                onBoxClick = { index ->
                    onEvent(CharacterSheetEvent.WillpowerBoxClicked(index))
                },
                allowsAggravated = true, // Questo dovrebbe essere false per Willpower tipicamente
                superficialColor = Color.Cyan,
                aggravatedColor = Color.Blue
            )
        }
    }
}

@Composable
fun HungerDisplay(
    modifier: Modifier = Modifier,
    currentHunger: Int,
    maxHunger: Int = 5,
    onHungerChange: (Int) -> Unit
) {
    Row(modifier = modifier) {
        for (i in 1..maxHunger) {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .padding(2.dp)
                    .clickable {
                        val newHunger = if (i == currentHunger) {
                            max(0, i - 1)
                        } else {
                            i
                        }
                        onHungerChange(newHunger)
                    }
                    .then(
                        if (i <= currentHunger) {
                            Modifier.background(Color.Red)
                        } else {
                            Modifier.border(
                                1.dp,
                                Color.Gray
                            )
                        }
                    )
            )
        }
    }
}


@Preview(showBackground = true, widthDp = 360)
@Composable
fun HealthWillpowerHungerSectionPreview() {
    V5RulesTheme {
        HealthWillpowerHungerSection(
            character = Character(
                hunger = 2,
                health = Health(
                    boxes = listOf(
                        DamageType.AGGRAVATED,
                        DamageType.SUPERFICIAL,
                        DamageType.EMPTY,
                        DamageType.EMPTY,
                        DamageType.SUPERFICIAL,
                        DamageType.EMPTY,
                        DamageType.EMPTY,
                        DamageType.EMPTY,
                        DamageType.EMPTY,
                        DamageType.EMPTY
                    )
                ),
                willpower = Willpower(
                    boxes = listOf(
                        DamageType.SUPERFICIAL,
                        DamageType.EMPTY,
                        DamageType.SUPERFICIAL,
                        DamageType.EMPTY,
                        DamageType.EMPTY,
                        DamageType.EMPTY,
                        DamageType.EMPTY
                    )
                )
            ),
            onEvent = {}
        )
    }
}
