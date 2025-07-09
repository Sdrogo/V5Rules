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
import com.example.v5rules.data.Character
import com.example.v5rules.ui.compose.component.DotsForAttribute

@Composable
fun AttributeSectionVisualization(
    character: Character
) {
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
                                color = MaterialTheme.colorScheme.tertiary,
                                shape = RoundedCornerShape(8.dp)
                            ),
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp)
                        ) {
                            Text(
                                text = stringResource(R.string.character_screen_physical),
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier
                                    .wrapContentWidth()
                                    .align(Alignment.CenterHorizontally)
                                    .padding(top = 8.dp)
                            )
                            DotsForAttribute(
                                stringResource(R.string.character_screen_attributes_strength),
                                character.attributes.strength,
                                textStyle = MaterialTheme.typography.headlineSmall
                            )
                            DotsForAttribute(
                                stringResource(R.string.character_screen_attributes_dexterity),
                                character.attributes.dexterity,
                                textStyle = MaterialTheme.typography.headlineSmall
                            )
                            DotsForAttribute(
                                stringResource(R.string.character_screen_attributes_stamina),
                                character.attributes.stamina,
                                textStyle = MaterialTheme.typography.headlineSmall
                            )
                            Spacer(modifier = Modifier.height(8.dp))
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
                                color = MaterialTheme.colorScheme.tertiary,
                                shape = RoundedCornerShape(8.dp)
                            ),
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp)
                        ) {
                            Text(
                                text = stringResource(R.string.character_screen_social),
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier
                                    .wrapContentWidth()
                                    .align(Alignment.CenterHorizontally)
                                    .padding(top = 8.dp)
                            )
                            DotsForAttribute(
                                stringResource(R.string.character_screen_attributes_charisma),
                                character.attributes.charisma,
                                textStyle = MaterialTheme.typography.headlineSmall
                            )
                            DotsForAttribute(
                                stringResource(R.string.character_screen_attributes_manipulation),
                                character.attributes.manipulation,
                                textStyle = MaterialTheme.typography.headlineSmall
                            )
                            DotsForAttribute(
                                stringResource(R.string.character_screen_attributes_composure),
                                character.attributes.composure,
                                textStyle = MaterialTheme.typography.headlineSmall
                            )

                            Spacer(modifier = Modifier.height(8.dp))
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
                                color = MaterialTheme.colorScheme.tertiary,
                                shape = RoundedCornerShape(8.dp)
                            ),
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp)
                        ) {
                            Text(
                                text = stringResource(R.string.character_screen_mental),
                                style = MaterialTheme.typography.titleMedium, // Usa uno stile più piccolo
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier
                                    .wrapContentWidth() // Wrap content per centrare
                                    .align(Alignment.CenterHorizontally) // Centra orizzontalmente
                                    .padding(top = 8.dp)
                            )
                            DotsForAttribute(
                                stringResource(R.string.character_screen_attributes_intelligence),
                                character.attributes.intelligence,
                                textStyle = MaterialTheme.typography.headlineSmall
                            )
                            DotsForAttribute(
                                stringResource(R.string.character_screen_attributes_wits),
                                character.attributes.wits,
                                textStyle = MaterialTheme.typography.headlineSmall
                            )
                            DotsForAttribute(
                                stringResource(R.string.character_screen_attributes_resolve),
                                character.attributes.resolve,
                                textStyle = MaterialTheme.typography.headlineSmall
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                        }
                    }
                }

            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(pagerState.pageCount) { iteration ->
                val color = if (pagerState.currentPage == iteration) {
                    MaterialTheme.colorScheme.tertiary
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
    }
}
