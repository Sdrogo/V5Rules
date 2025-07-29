package com.example.v5rules.ui.compose.screen.sheet.visualization

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.v5rules.R
import com.example.v5rules.data.Ability
import com.example.v5rules.data.Character
import com.example.v5rules.ui.compose.component.DotsForAttribute

@Composable
fun AbilitySectionVisualization(character: Character) {

    val pagerState = rememberPagerState(pageCount = { 3 })
    Column {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.padding(horizontal = 8.dp),
            contentPadding = PaddingValues(horizontal = 16.dp)
        ) { page ->
            when (page) {
                0 -> {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .border(
                                width = 1.dp,
                                color = MaterialTheme.colorScheme.secondary,
                                shape = RoundedCornerShape(8.dp)
                            )
                    ) {
                        Column(modifier = Modifier.padding(8.dp)) {
                            Text(
                                text = stringResource(R.string.character_screen_physical),
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .wrapContentWidth(Alignment.CenterHorizontally)
                                    .padding(bottom = 8.dp)
                            )

                            val physicalAbilities = listOf(
                                stringResource(R.string.character_screen_abilities_athletics),
                                stringResource(R.string.character_screen_abilities_brawl),
                                stringResource(R.string.character_screen_abilities_craft),
                                stringResource(R.string.character_screen_abilities_drive),
                                stringResource(R.string.character_screen_abilities_firearms),
                                stringResource(R.string.character_screen_abilities_larceny),
                                stringResource(R.string.character_screen_abilities_melee),
                                stringResource(R.string.character_screen_abilities_stealth),
                                stringResource(R.string.character_screen_abilities_survival),
                            ).sorted()
                            physicalAbilities.forEach { abilityName ->
                                val ability =
                                    character.abilities.firstOrNull { it.name == abilityName }
                                        ?: Ability(
                                            name = abilityName, level = 0
                                        )
                                AbilityItem(ability)
                            }
                        }
                    }

                }

                1 -> {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .border(
                                width = 1.dp,
                                color = MaterialTheme.colorScheme.secondary,
                                shape = RoundedCornerShape(8.dp)
                            )
                    ) {
                        Column(modifier = Modifier.padding(8.dp)) {
                            Text(
                                text = stringResource(R.string.character_screen_social),
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .wrapContentWidth(Alignment.CenterHorizontally)
                                    .padding(bottom = 8.dp)
                            )

                            val socialAbilities = listOf(
                                stringResource(R.string.character_screen_abilities_animal_ken),
                                stringResource(R.string.character_screen_abilities_etiquette),
                                stringResource(R.string.character_screen_abilities_insight),
                                stringResource(R.string.character_screen_abilities_intimidation),
                                stringResource(R.string.character_screen_abilities_leadership),
                                stringResource(R.string.character_screen_abilities_performance),
                                stringResource(R.string.character_screen_abilities_persuasion),
                                stringResource(R.string.character_screen_abilities_streetwise),
                                stringResource(R.string.character_screen_abilities_subterfuge),
                            ).sorted()
                            socialAbilities.forEach { abilityName ->
                                val ability =
                                    character.abilities.firstOrNull { it.name == abilityName }
                                        ?: Ability(
                                            name = abilityName, level = 0
                                        )
                                AbilityItem(ability)
                            }
                        }
                    }
                }

                2 -> {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .border(
                                width = 1.dp,
                                color = MaterialTheme.colorScheme.secondary,
                                shape = RoundedCornerShape(8.dp)
                            )
                    ) {
                        Column(modifier = Modifier.padding(8.dp)) {
                            Text(
                                text = stringResource(R.string.character_screen_mental),
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .wrapContentWidth(Alignment.CenterHorizontally)
                                    .padding(bottom = 8.dp)
                            )

                            val mentalAbilities = listOf(
                                stringResource(R.string.character_screen_abilities_academics),
                                stringResource(R.string.character_screen_abilities_awareness),
                                stringResource(R.string.character_screen_abilities_finance),
                                stringResource(R.string.character_screen_abilities_investigation),
                                stringResource(R.string.character_screen_abilities_medicine),
                                stringResource(R.string.character_screen_abilities_occult),
                                stringResource(R.string.character_screen_abilities_politics),
                                stringResource(R.string.character_screen_abilities_science),
                                stringResource(R.string.character_screen_abilities_technology),
                            ).sorted()
                            mentalAbilities.forEach { abilityName ->
                                val ability =
                                    character.abilities.firstOrNull { it.name == abilityName }
                                        ?: Ability(
                                            name = abilityName, level = 0
                                        )
                                AbilityItem(ability)
                            }
                        }
                    }
                }
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(pagerState.pageCount) { iteration ->
                val color = if (pagerState.currentPage == iteration) {
                    MaterialTheme.colorScheme.secondary
                } else {
                    Color.LightGray
                }
                Box(
                    modifier = Modifier
                        .padding(8.dp)
                        .clip(CircleShape)
                        .background(color)
                        .size(8.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
    }
}

@Composable
fun AbilityItem(
    ability: Ability,
) {
    Column {
        DotsForAttribute(
            label = ability.name,
            level = ability.level,
            textStyle = MaterialTheme.typography.bodyMedium
        )
        ability.specialization?.let {
            Text(
                stringResource(R.string.specialization, it),
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 16.dp)
            )
        }
    }
}
