package com.example.v5rules.ui.compose.screen.sheet.visualization

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import com.example.v5rules.R
import com.example.v5rules.ui.compose.component.CommonScaffold
import com.example.v5rules.ui.compose.component.ContentExpander
import com.example.v5rules.viewModel.CharacterSheetViewModel

@Composable
fun CharacterSheetScreenVisualization(
    viewModel: CharacterSheetViewModel,
    navController: NavHostController,
    id: Int? = null
) {
    val uiState by viewModel.uiState.collectAsState()
    val character = uiState.character

    LaunchedEffect(key1 = id) {
        if (id != null) {
            viewModel.setCharacter(id)
        }
    }

    CommonScaffold(
        navController = navController, title = stringResource(R.string.character_screen_title)
    ) { _ ->
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            item {
                ContentExpander(
                    stringResource(R.string.sheet_general_info_title),
                    style = MaterialTheme.typography.bodyMedium,
                    initialState = true,
                    content = { GeneralInfoSectionVisualization(character) }
                )
            }
            item {
                ContentExpander(
                    stringResource(R.string.sheet_attribute_title),
                    style = MaterialTheme.typography.bodyMedium,
                    initialState = true,
                    content = { AttributeSectionVisualization(character) }
                )
            }
            item {
                ContentExpander(
                    stringResource(R.string.sheet_ability_title),
                    style = MaterialTheme.typography.bodyMedium,
                    initialState = true,
                    content = { AbilitySectionVisualization(character) }
                )
            }
            item {
                ContentExpander(
                    stringResource(R.string.sheet_discipline_title),
                    style = MaterialTheme.typography.bodyMedium,
                    initialState = true,
                    content = { DisciplineSelectionVisualization(viewModel, navController) }
                )
            }
            item {
                ContentExpander(
                    stringResource(R.string.sheet_background_title),
                    style = MaterialTheme.typography.bodyMedium,
                    initialState = true,
                    content = { BackgroundSectionVisualization(viewModel) }
                )
            }
        }
    }
}
