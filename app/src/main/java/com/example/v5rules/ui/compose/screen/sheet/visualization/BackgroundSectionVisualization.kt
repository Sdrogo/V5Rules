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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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

    val pagerState = rememberPagerState(pageCount = { 3 })
    val pagerStateNoLoresheet = rememberPagerState(pageCount = { 2 })

    if (characterLoresheets.isEmpty()) {
        Column {
            HorizontalPager(
                state = pagerStateNoLoresheet,
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
                                    1.dp,
                                    MaterialTheme.colorScheme.tertiary,
                                    RoundedCornerShape(8.dp)
                                ),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(text = stringResource(R.string.background), style = MaterialTheme.typography.headlineSmall, modifier = Modifier.weight(1f))
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                if (characterBackgrounds.isNotEmpty()) {
                                    BackgroundListVisualization(
                                        backgrounds = characterBackgrounds
                                    )
                                }
                            }
                        }
                    }

                    1 -> {
                        if (characterDirectFlaws.isNotEmpty()) {
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp)
                                    .border(
                                        1.dp,
                                        MaterialTheme.colorScheme.tertiary,
                                        RoundedCornerShape(8.dp)
                                    ),
                                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = stringResource(R.string.flaws),
                                            style = MaterialTheme.typography.headlineSmall,
                                            modifier = Modifier.weight(1f)
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(8.dp))

                                    if (characterDirectFlaws.isNotEmpty()) {
                                        DirectFlawsListVisualization(flaws = characterDirectFlaws)
                                    } else {
                                        Text(
                                            stringResource(R.string.no_flaw_selected),
                                            style = MaterialTheme.typography.bodyMedium
                                        )
                                    }
                                }
                            }
                        } else {
                            Spacer(modifier = Modifier.width(8.dp))
                        }
                    }

                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                repeat(pagerStateNoLoresheet.pageCount) { iteration ->
                    val color = if (pagerStateNoLoresheet.currentPage == iteration) {
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
            Spacer(modifier = Modifier.height(4.dp))

        }
    } else {
        Column {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.padding(horizontal = 8.dp),
                contentPadding = PaddingValues(horizontal = 16.dp)
            ) { page ->
                when (page) {
                    0 -> {
                        if (characterBackgrounds.isNotEmpty()) {
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp)
                                    .border(
                                        1.dp,
                                        MaterialTheme.colorScheme.tertiary,
                                        RoundedCornerShape(8.dp)
                                    ),
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
                    }

                    1 -> {
                        if (characterDirectFlaws.isNotEmpty()) {
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp)
                                    .border(
                                        1.dp,
                                        MaterialTheme.colorScheme.tertiary,
                                        RoundedCornerShape(8.dp)
                                    ),
                                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = stringResource(R.string.flaws),
                                            style = MaterialTheme.typography.headlineSmall,
                                            modifier = Modifier.weight(1f)
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(8.dp))

                                    if (characterDirectFlaws.isNotEmpty()) {
                                        DirectFlawsListVisualization(flaws = characterDirectFlaws)
                                    } else {
                                        Text(
                                            stringResource(R.string.no_flaw_selected),
                                            style = MaterialTheme.typography.bodyMedium
                                        )
                                    }
                                }
                            }
                        } else {
                            Spacer(modifier = Modifier.width(8.dp))
                        }
                    }

                    2 -> {
                        if (characterLoresheets.isNotEmpty()) {
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp)
                                    .border(
                                        1.dp,
                                        MaterialTheme.colorScheme.tertiary,
                                        RoundedCornerShape(8.dp)
                                    ),
                                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                            )
                            {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = stringResource(R.string.lore_screen_title),
                                            style = MaterialTheme.typography.headlineSmall,
                                            modifier = Modifier.weight(1f)
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(8.dp))

                                    if (characterLoresheets.isNotEmpty()) {
                                        LoresheetListVisualization(loresheets = characterLoresheets)
                                    } else {
                                        Text(
                                            stringResource(R.string.no_loresheet_selected),
                                            style = MaterialTheme.typography.bodyMedium
                                        )
                                    }
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
            Spacer(modifier = Modifier.height(4.dp))
        }
    }

}
