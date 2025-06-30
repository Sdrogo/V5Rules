package com.example.v5rules.ui.compose.screen.sheet.visualization

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults.SecondaryIndicator
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavHostController
import com.example.v5rules.R
import com.example.v5rules.ui.compose.component.CommonScaffold
import com.example.v5rules.viewModel.CharacterSheetViewModel

@Composable
fun CharacterSheetScreenVisualization(
    viewModel: CharacterSheetViewModel,
    navController: NavHostController,
    id: Int? = null
) {
    val uiState by viewModel.uiState.collectAsState()
    val character = uiState.character
    val selectedTabIndex by viewModel.selectedTabIndex.collectAsState()

    LaunchedEffect(key1 = id) {
        if (id != null) {
            viewModel.setCharacter(id)
        }
    }

    val tabs = listOf("Info Generali", "Attributi", "Abilità", "Discipline", "Background")

    CommonScaffold(
        navController = navController, title = stringResource(R.string.character_screen_title)
    ) { _ ->
        Column {
            ScrollableTabRow(
                selectedTabIndex = selectedTabIndex,
                modifier = Modifier
                    .zIndex(1f),
                edgePadding = 0.dp,
                containerColor = MaterialTheme.colorScheme.surface,
                indicator = { tabPositions ->
                    SecondaryIndicator(
                        Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                        color = MaterialTheme.colorScheme.tertiary
                    )
                },
                divider = { }
            ) {
                tabs.forEachIndexed { index, tab ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { viewModel.selectTab(index) },
                        modifier = Modifier.widthIn(min = 80.dp),
                        text = {
                            Text(
                                tab,
                                style = MaterialTheme.typography.bodySmall,
                                maxLines = 1,
                                color = MaterialTheme.colorScheme.primary,
                                overflow = TextOverflow.Ellipsis
                            )
                        })
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                when (selectedTabIndex) {
                    0 -> GeneralInfoSectionVisualization(character)
                    1 -> AttributeSectionVisualization(character)
                    2 -> AbilitySectionVisualization(character)
                    3 -> DisciplineSelectionVisualization(viewModel, navController)
                    4 -> BackgroundSectionVisualization(viewModel)
                }
            }
        }
    }
}
