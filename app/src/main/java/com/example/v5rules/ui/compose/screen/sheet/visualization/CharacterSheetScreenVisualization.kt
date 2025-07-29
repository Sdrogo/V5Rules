package com.example.v5rules.ui.compose.screen.sheet.visualization

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.v5rules.R
import com.example.v5rules.viewModel.CharacterSheetViewModel

@Composable
fun CharacterSheetScreenVisualization(
    viewModel: CharacterSheetViewModel,
    navController: NavHostController,
    id: String? = null,
    onTitleChanged: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val character = uiState.character
    var isHealthSectionExpanded by remember { mutableStateOf(false) }
    var isGeneralSectionExpanded by remember { mutableStateOf(true) }
    var isAttributeSectionExpanded by remember { mutableStateOf(true) }
    var isAbilitySectionExpanded by remember { mutableStateOf(true) }
    var isBackgroundSectionExpanded by remember { mutableStateOf(true) }
    var isDisciplineSectionExpanded by remember { mutableStateOf(true) }

    LaunchedEffect(key1 = id) {
        if (id != null) {
            viewModel.setCharacter(id)
        }
    }

    LaunchedEffect(character.name) {
        if (character.name.isNotEmpty()) {
            onTitleChanged(character.name)
        }
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding()
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            isGeneralSectionExpanded = !isGeneralSectionExpanded
                        }
                        .background(MaterialTheme.colorScheme.secondary)
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = stringResource(R.string.sheet_general_info_title),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Icon(
                        imageVector = if (isGeneralSectionExpanded) Icons.Filled.KeyboardArrowDown else Icons.Filled.KeyboardArrowUp,
                        tint = MaterialTheme.colorScheme.primary,
                        contentDescription = if (isGeneralSectionExpanded) stringResource(
                            R.string.collapse
                        ) else stringResource(R.string.expand)
                    )
                }
                if (isGeneralSectionExpanded) {
                    GeneralInfoSectionVisualization(character)
                }
            }
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            isAttributeSectionExpanded = !isAttributeSectionExpanded
                        }
                        .background(MaterialTheme.colorScheme.secondary)
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = stringResource(R.string.sheet_attribute_title),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Icon(
                        imageVector = if (isAttributeSectionExpanded) Icons.Filled.KeyboardArrowDown else Icons.Filled.KeyboardArrowUp,
                        tint = MaterialTheme.colorScheme.primary,
                        contentDescription = if (isAttributeSectionExpanded) stringResource(
                            R.string.collapse
                        ) else stringResource(R.string.expand)
                    )
                }
                if (isAttributeSectionExpanded) {
                    AttributeSectionVisualization(character)
                }
            }
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            isAbilitySectionExpanded = !isAbilitySectionExpanded
                        }
                        .background(MaterialTheme.colorScheme.secondary)
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = stringResource(R.string.sheet_ability_title),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Icon(
                        imageVector = if (isAbilitySectionExpanded) Icons.Filled.KeyboardArrowDown else Icons.Filled.KeyboardArrowUp,
                        tint = MaterialTheme.colorScheme.primary,
                        contentDescription = if (isAbilitySectionExpanded) stringResource(
                            R.string.collapse
                        ) else stringResource(R.string.expand)
                    )
                }
                if (isAbilitySectionExpanded) {
                    AbilitySectionVisualization(character)
                }
            }
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            isDisciplineSectionExpanded = !isDisciplineSectionExpanded
                        }
                        .background(MaterialTheme.colorScheme.secondary)
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = stringResource(R.string.sheet_discipline_title),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Icon(
                        imageVector = if (isDisciplineSectionExpanded) Icons.Filled.KeyboardArrowDown else Icons.Filled.KeyboardArrowUp,
                        tint = MaterialTheme.colorScheme.primary,
                        contentDescription = if (isDisciplineSectionExpanded) stringResource(
                            R.string.collapse
                        ) else stringResource(R.string.expand)
                    )
                }
                if (isDisciplineSectionExpanded) {
                    DisciplineSelectionVisualization(viewModel, navController)
                }
            }
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            isBackgroundSectionExpanded = !isBackgroundSectionExpanded
                        }
                        .background(MaterialTheme.colorScheme.secondary)
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = stringResource(R.string.sheet_background_title),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Icon(
                        imageVector = if (isBackgroundSectionExpanded) Icons.Filled.KeyboardArrowDown else Icons.Filled.KeyboardArrowUp,
                        tint = MaterialTheme.colorScheme.primary,
                        contentDescription = if (isBackgroundSectionExpanded) stringResource(
                            R.string.collapse
                        ) else stringResource(R.string.expand)
                    )
                }

                if (isBackgroundSectionExpanded) {
                    BackgroundSectionVisualization(viewModel)
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { isHealthSectionExpanded = !isHealthSectionExpanded }
                .background(MaterialTheme.colorScheme.secondary)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(R.string.vital_stats_header),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Icon(
                imageVector = if (isHealthSectionExpanded) Icons.Filled.KeyboardArrowDown else Icons.Filled.KeyboardArrowUp,
                tint = MaterialTheme.colorScheme.primary,
                contentDescription = if (isHealthSectionExpanded) stringResource(R.string.collapse) else stringResource(
                    R.string.expand
                )
            )
        }

        if (isHealthSectionExpanded) {
            HealthWillpowerHungerSection(
                character = character,
                onEvent = viewModel::onEvent,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )
        }
    }
}
