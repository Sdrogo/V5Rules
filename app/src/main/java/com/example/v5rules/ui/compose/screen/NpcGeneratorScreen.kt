package com.example.v5rules.ui.compose.screen

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.v5rules.data.RegenerationType
import com.example.v5rules.utils.Nationalities
import com.example.v5rules.R
import com.example.v5rules.ui.compose.component.CommonScaffold
import com.example.v5rules.ui.compose.component.GenderSelection
import com.example.v5rules.ui.compose.component.GenerateButton
import com.example.v5rules.ui.compose.component.GeneratedName
import com.example.v5rules.ui.compose.component.IncludeSecondNameCheckbox
import com.example.v5rules.ui.compose.component.NationalityDropdown
import com.example.v5rules.ui.compose.component.RegenerationCheckbox
import com.example.v5rules.ui.viewModel.NPCGeneratorViewModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun InputScreen(
    modifier: Modifier,
    viewModel: NPCGeneratorViewModel,
    navController: NavHostController

) {
    val uiState by viewModel.uiState.collectAsState()
    CommonScaffold(
        navController = navController,
        title = stringResource(id = R.string.npc_generator_title)
    ) {
        Box(
            modifier = modifier
                .fillMaxSize()
        ) {
            val isLandscape =
                LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE

            FlowRow(
                modifier = modifier
                    .matchParentSize(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalArrangement = Arrangement.SpaceAround
            ) {
                FlowRow(
                    modifier = modifier
                        .fillMaxWidth(1f),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    maxItemsInEachRow = 2,
                    maxLines = 2
                ) {
                    Row(
                        modifier = modifier
                            .fillMaxWidth(if (isLandscape) 0.5f else 1f),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Spacer(modifier = modifier.width(16.dp))
                        Text(stringResource(id = R.string.nationality_selector_label))

                        Spacer(modifier = modifier.width(16.dp))
                        NationalityDropdown(
                            nationalities = Nationalities.list,
                            onNationalitySelected = {
                                viewModel.setSelectedNationality(
                                    it
                                )
                            }
                        )
                    }
                    if (isLandscape) {
                        IncludeSecondNameCheckbox(
                            includeSecondName = uiState.includeSecondName,
                            width = 0.4f,
                            onIncludeSecondNameChange = { viewModel.setIncludeSecondName(it) }
                        )
                        GenderSelection(
                            selectedGender = uiState.selectedGender,
                            widthOfFlow = 1f,
                            isLandscape = isLandscape,
                            onGenderSelected = { viewModel.setSelectedGender(it) }
                        )
                    } else {
                        GenderSelection(
                            selectedGender = uiState.selectedGender,
                            widthOfFlow = 0.5f,
                            isLandscape = isLandscape,
                            onGenderSelected = { viewModel.setSelectedGender(it) }
                        )
                        IncludeSecondNameCheckbox(
                            includeSecondName = uiState.includeSecondName,
                            width = 0.5f,
                            onIncludeSecondNameChange = { viewModel.setIncludeSecondName(it) }
                        )
                    }
                }
                GeneratedName(
                    npc = uiState.npc,
                    widthFloat = if (isLandscape) 0.45f else 1f,
                    isLandscape = isLandscape
                )
                FlowRow(
                    modifier = modifier
                        .fillMaxWidth(if (isLandscape) 0.5f else 1f),
                    horizontalArrangement = Arrangement.Center,
                    maxItemsInEachRow = 2,
                    maxLines = 3
                ) {
                    RegenerationCheckbox(
                        text = stringResource(id = R.string.generate_name_button_label),
                        width = 0.4f,
                        checked = uiState.selectedRegenerationTypes.contains(
                            RegenerationType.NAME
                        ),
                        onCheckedChange = { checked ->
                            if (checked) {
                                viewModel.addSelectedRegenerationType(
                                    RegenerationType.NAME
                                )
                            } else {
                                viewModel.removeSelectedRegenerationType(
                                    RegenerationType.NAME
                                )
                            }
                        },
                        enabled = uiState.firstGeneration,
                        firstGeneration = uiState.firstGeneration
                    )
                    RegenerationCheckbox(
                        text = stringResource(id = R.string.generate_second_name_button_label),
                        width = 0.4f,
                        checked = uiState.selectedRegenerationTypes.contains(
                            RegenerationType.SECOND_NAME
                        ),
                        onCheckedChange = { checked ->
                            if (checked) {
                                viewModel.addSelectedRegenerationType(
                                    RegenerationType.SECOND_NAME
                                )
                            } else {
                                viewModel.removeSelectedRegenerationType(
                                    RegenerationType.SECOND_NAME
                                )
                            }
                        },
                        enabled = uiState.firstGeneration && uiState.includeSecondName,
                        firstGeneration = uiState.firstGeneration
                    )
                    RegenerationCheckbox(
                        text = stringResource(id = R.string.generate_family_name_button_label),
                        width = 0.4f,
                        checked = uiState.selectedRegenerationTypes.contains(
                            RegenerationType.FAMILY_NAME
                        ),
                        onCheckedChange = { checked ->
                            if (checked) {
                                viewModel.addSelectedRegenerationType(
                                    RegenerationType.FAMILY_NAME
                                )
                            } else {
                                viewModel.removeSelectedRegenerationType(
                                    RegenerationType.FAMILY_NAME
                                )
                            }
                        },
                        enabled = uiState.firstGeneration,
                        firstGeneration = uiState.firstGeneration
                    )
                    RegenerationCheckbox(
                        text = stringResource(id = R.string.generate_all_button_label),
                        checked = uiState.selectedRegenerationTypes.contains(
                            RegenerationType.ALL
                        ),
                        width = 0.4f,
                        onCheckedChange = { checked ->
                            if (checked) {
                                viewModel.addSelectedRegenerationType(
                                    RegenerationType.ALL
                                )
                            } else {
                                viewModel.removeSelectedRegenerationType(
                                    RegenerationType.ALL
                                )
                            }
                        },
                        enabled = uiState.firstGeneration,
                        firstGeneration = uiState.firstGeneration
                    )
                    GenerateButton(
                        selectedNationality = uiState.selectedNationality,
                        firstGeneration = uiState.firstGeneration,
                        isListEmpty = uiState.selectedRegenerationTypes.isEmpty(),
                        width = if (isLandscape) 0.5f else 1f,
                        onGenerateNPC = { viewModel.generateNPC() }
                    )
                }
            }
        }
    }
}
