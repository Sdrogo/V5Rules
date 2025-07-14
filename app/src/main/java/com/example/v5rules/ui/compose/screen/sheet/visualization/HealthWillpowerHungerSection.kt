package com.example.v5rules.ui.compose.screen.sheet.visualization

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.v5rules.R
import com.example.v5rules.data.Character
import com.example.v5rules.data.DamageType
import com.example.v5rules.data.Health
import com.example.v5rules.data.Humanity
import com.example.v5rules.data.Willpower
import com.example.v5rules.ui.compose.component.DamageTrackDisplay
import com.example.v5rules.ui.theme.V5RulesTheme
import com.example.v5rules.utils.CharacterSheetEvent
import kotlin.math.max

private enum class HumanityBoxState {
    HUMANITY,
    STAINED,
    EMPTY
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun HealthWillpowerHungerSection(
    character: Character,
    onEvent: (CharacterSheetEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    FlowRow(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        maxItemsInEachRow = 4
    ) {
        // Sezione Fame
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .wrapContentSize()
                .padding(bottom = 8.dp, end = 8.dp)
        ) {
            Text(
                text = stringResource(R.string.hunger) + ": ",
                style = MaterialTheme.typography.titleMedium
            )
            HungerDisplay(
                currentHunger = (character.hunger ?: 0),
                onHungerChange = { newHunger ->
                    onEvent(CharacterSheetEvent.HungerChanged(newHunger))
                }
            )
        }

        // Sezione Salute
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .wrapContentSize()
                .padding(bottom = 8.dp, end = 8.dp)
        ) {
            Text(
                text = stringResource(R.string.health) + ": ",
                style = MaterialTheme.typography.titleMedium
            )
            DamageTrackDisplay(
                damageTrack = character.health.boxes,
                onBoxClick = { index ->
                    onEvent(CharacterSheetEvent.HealthBoxClicked(index))
                },
                allowsAggravated = true
            )
        }

        // Sezione Volontà
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .wrapContentSize()
                .padding(bottom = 8.dp, end = 8.dp)
        ) {
            Text(
                text = stringResource(R.string.willpower) + ": ",
                style = MaterialTheme.typography.titleMedium
            )
            DamageTrackDisplay(
                damageTrack = character.willpower.boxes,
                onBoxClick = { index ->
                    onEvent(CharacterSheetEvent.WillpowerBoxClicked(index))
                },
                allowsAggravated = true
            )
        }

        // Sezione Umanità
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .wrapContentSize()
                .padding(bottom = 8.dp)
        ) {
            Text(
                text = stringResource(R.string.humanity) + ": ",
                style = MaterialTheme.typography.titleMedium
            )
            HumanityDisplay(
                currentHumanity = character.humanity.current,
                stains = character.humanity.stains,
                onHumanityChange = { newHumanityLevel ->
                    onEvent(CharacterSheetEvent.HumanityChanged(newHumanityLevel))
                }
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
                    .border(1.dp, MaterialTheme.colorScheme.onTertiary  )
                    .then(
                        if (i <= currentHunger) {
                            Modifier.background(MaterialTheme.colorScheme.tertiary)
                        } else {
                            Modifier.background(MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f))
                        }
                    )
            )
        }
    }
}

@Composable
fun HumanityDisplay(
    modifier: Modifier = Modifier,
    currentHumanity: Int,
    stains: Int,
    maxHumanity: Int = 10,
    onHumanityChange: (Int) -> Unit
) {
    Row(modifier = modifier) {
        for (i in 1..maxHumanity) {
            val boxState = when {
                i <= currentHumanity -> HumanityBoxState.HUMANITY
                i <= currentHumanity + stains -> HumanityBoxState.STAINED
                else -> HumanityBoxState.EMPTY
            }
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .padding(2.dp)
                    .border(1.dp, MaterialTheme.colorScheme.primary)
                    .clickable { onHumanityChange(i) }
                    .padding(2.dp)
            ) {
                Canvas(modifier = Modifier.matchParentSize()) {
                    when (boxState) {
                        HumanityBoxState.HUMANITY -> {
                            drawRect(color = Color(0XFF76031A), size = this.size)
                        }
                        HumanityBoxState.STAINED -> {
                            val strokeWidth = size.minDimension / 5f
                            drawLine(
                                color = Color.Red,
                                start = Offset(strokeWidth / 2, strokeWidth / 2),
                                end = Offset(size.width - strokeWidth / 2, size.height - strokeWidth / 2),
                                strokeWidth = strokeWidth,
                                cap = StrokeCap.Round
                            )
                        }
                        HumanityBoxState.EMPTY -> {

                        }
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true, widthDp = 400)
@Composable
fun HealthWillpowerHungerSectionPreview() {
    V5RulesTheme {
        HealthWillpowerHungerSection(
            character = Character(
                hunger = 2,
                health = Health(
                    boxes = List(7) { if (it < 2) DamageType.SUPERFICIAL else DamageType.EMPTY }
                ),
                willpower = Willpower(
                    boxes = List(7) { if (it < 3) DamageType.SUPERFICIAL else DamageType.EMPTY }
                ),
                humanity = Humanity(current = 7, stains = 2) // Aggiunto humanity alla preview
            ),
            onEvent = {}
        )
    }
}

@Preview(showBackground = true, widthDp = 200) // Preview più stretta
@Composable
fun HealthWillpowerHungerSectionNarrowPreview() {
    V5RulesTheme {
        HealthWillpowerHungerSection(
            character = Character(
                hunger = 3,
                health = Health(
                    boxes = List(7) { DamageType.EMPTY }
                ),
                willpower = Willpower(
                    boxes = List(7) { DamageType.EMPTY }
                ),
                humanity = Humanity(current = 5, stains = 3)
            ),
            onEvent = {}
        )
    }
}
