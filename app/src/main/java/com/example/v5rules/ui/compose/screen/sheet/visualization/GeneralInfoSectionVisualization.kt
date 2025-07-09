package com.example.v5rules.ui.compose.screen.sheet.visualization

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.v5rules.R
import com.example.v5rules.data.Character
import com.example.v5rules.ui.compose.component.ClanImage


@Composable
fun GeneralInfoSectionVisualization(character: Character) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(Color.Yellow)
    ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.background)
                    .border(
                        1.dp, MaterialTheme.colorScheme.tertiary, RoundedCornerShape(8.dp)
                    ),
            ) {
                Column(modifier = Modifier.padding(8.dp)) {
                    Row {
                        Text(
                            text = stringResource(R.string.character_name),
                            style = MaterialTheme.typography.titleMedium
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(text = character.name)
                    }
                    Row {
                        Text(
                            text = stringResource(R.string.clan).plus(": "),
                            style = MaterialTheme.typography.titleMedium
                        )
                        character.clan?.name?.let {
                            Spacer(modifier = Modifier.width(16.dp))
                            ClanImage(
                                clanName = it,
                                tintColor = MaterialTheme.colorScheme.tertiary,
                                width = 24.dp
                            )
                            Text(text = it)
                        }
                    }
                    character.predator?.name?.let {
                        Row {
                            Text(
                                text = stringResource(R.string.predator).plus(": "),
                                style = MaterialTheme.typography.titleMedium
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            Text(text = it)
                        }
                    }
                    Row {
                        Text(
                            text = stringResource(R.string.character_screen_generation).plus(": "),
                            style = MaterialTheme.typography.titleMedium
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(text = character.generation.toString().plus("°"))
                    }
                    Row {
                        Text(
                            text = stringResource(R.string.character_screen_sire).plus(": "),
                            style = MaterialTheme.typography.titleMedium
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(text = character.sire)
                    }
                    Row {
                        Text(
                            text = stringResource(R.string.character_screen_concept).plus(": "),
                            style = MaterialTheme.typography.titleMedium
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(text = character.concept)
                    }
                    Row {
                        Text(
                            text = stringResource(R.string.character_screen_ambition).plus(": "),
                            style = MaterialTheme.typography.titleMedium
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(text = character.ambition)
                    }
                    Row {
                        Text(
                            text = stringResource(R.string.character_screen_desire).plus(": "),
                            style = MaterialTheme.typography.titleMedium
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(text = character.desire)
                    }
                }
            }
        }
}