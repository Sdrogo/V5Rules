package com.example.v5rules.ui.compose.screen.sheet.visualization

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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


@Composable
fun HealthWillpowerHungerSection(
    character: Character, // O singoli parametri per health, willpower, hunger
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceVariant) // Sfondo per distinguerlo
            .padding(8.dp)
    ) {
        // Sezione Fame
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        ) {
            Text(
                text = stringResource(R.string.hunger) + ": ",
                style = MaterialTheme.typography.titleMedium
            )
            HungerDisplay(currentHunger = (character.hunger ?: 1))
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        ) {
            Text(
                text = stringResource(R.string.health) + ": ",
                style = MaterialTheme.typography.titleMedium
            )
            Text(text = "Placeholder Salute")
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = stringResource(R.string.willpower) + ": ",
                style = MaterialTheme.typography.titleMedium
            )
            Text(text = "Placeholder Volontà")
        }
    }
}
@Preview(showBackground = true)
@Composable
fun HealthWillpowerHungerSectionPreview() {
    // TODO: Provide a sample Character for preview
    // HealthWillpowerHungerSection(character = Character())
}


@Composable
fun HungerDisplay(
    modifier: Modifier = Modifier,
    currentHunger: Int,
    maxHunger: Int = 5
) {
    Row(modifier = modifier) {
        for (i in 1..maxHunger) {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .padding(2.dp)
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
