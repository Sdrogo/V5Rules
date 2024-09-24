package com.example.v5rules.ui.compose.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.v5rules.data.RegenerationType
import com.example.myapplication.util.Nationalities
import com.example.v5rules.R
import com.example.v5rules.ui.compose.component.CommonScaffold
import com.example.v5rules.ui.compose.component.GenderSelection
import com.example.v5rules.ui.compose.component.GenerateButton
import com.example.v5rules.ui.compose.component.GeneratedName
import com.example.v5rules.ui.compose.component.IncludeSecondNameCheckbox
import com.example.v5rules.ui.compose.component.NationalityDropdown
import com.example.v5rules.ui.compose.component.RegenerationCheckbox
import com.example.v5rules.ui.viewModel.NPCGeneratorViewModel

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
        Column(
            modifier = modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Selezione della nazionalità
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = modifier.width(16.dp))
                Text(stringResource(id = R.string.nationality_selector_label))

                Spacer(modifier = modifier.width(16.dp))
                NationalityDropdown(
                    nationalities = Nationalities.list,
                    onNationalitySelected = { viewModel.setSelectedNationality(it) }
                )
            }
            GenderSelection(
                selectedGender = uiState.selectedGender,
                onGenderSelected = { viewModel.setSelectedGender(it) }
            )
            IncludeSecondNameCheckbox(
                includeSecondName = uiState.includeSecondName,
                onIncludeSecondNameChange = { viewModel.setIncludeSecondName(it) }
            )
            Spacer(
                modifier = modifier
                    .height(16.dp)
                    .weight(0.5f)
            )
            GeneratedName(
                modifier = modifier,
                npc = uiState.npc
            )
            Spacer(
                    modifier = modifier
                        .height(16.dp)
                        .weight(0.5f)
                    )
            Spacer(
                modifier = modifier
                    .height(16.dp)
                    .weight(1f)
            )
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RegenerationCheckbox(
                        text = stringResource(id = R.string.generate_name_button_label),
                        checked = uiState.selectedRegenerationTypes.contains(RegenerationType.NAME),
                        onCheckedChange = { checked ->
                            if (checked) {
                                viewModel.addSelectedRegenerationType(RegenerationType.NAME)
                            } else {
                                viewModel.removeSelectedRegenerationType(RegenerationType.NAME)
                            }
                        },
                        enabled = uiState.firstGeneration,
                        firstGeneration = uiState.firstGeneration
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    RegenerationCheckbox(
                        text = stringResource(id = R.string.generate_second_name_button_label),
                        checked = uiState.selectedRegenerationTypes.contains(RegenerationType.SECOND_NAME),
                        onCheckedChange = { checked ->
                            if (checked) {
                                viewModel.addSelectedRegenerationType(RegenerationType.SECOND_NAME)
                            } else {
                                viewModel.removeSelectedRegenerationType(RegenerationType.SECOND_NAME)
                            }
                        },
                        enabled = uiState.firstGeneration && uiState.includeSecondName,
                        firstGeneration = uiState.firstGeneration
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RegenerationCheckbox(
                        text = stringResource(id = R.string.generate_family_name_button_label),
                        checked = uiState.selectedRegenerationTypes.contains(RegenerationType.FAMILY_NAME),
                        onCheckedChange = { checked ->
                            if (checked) {
                                viewModel.addSelectedRegenerationType(RegenerationType.FAMILY_NAME)
                            } else {
                                viewModel.removeSelectedRegenerationType(RegenerationType.FAMILY_NAME)
                            }
                        },
                        enabled = uiState.firstGeneration,
                        firstGeneration = uiState.firstGeneration
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    RegenerationCheckbox(
                        text = stringResource(id = R.string.generate_all_button_label),
                        checked = uiState.selectedRegenerationTypes.contains(RegenerationType.ALL),
                        onCheckedChange = { checked ->
                            if (checked) {
                                viewModel.addSelectedRegenerationType(RegenerationType.ALL)
                            } else {
                                viewModel.removeSelectedRegenerationType(RegenerationType.ALL)
                            }
                        },
                        enabled = uiState.firstGeneration,
                        firstGeneration = uiState.firstGeneration
                    )
                }
                GenerateButton(
                    selectedNationality = uiState.selectedNationality,
                    firstGeneration = uiState.firstGeneration,
                    isListEmpty = uiState.selectedRegenerationTypes.isEmpty(),
                    onGenerateNPC = { viewModel.generateNPC() }
                )
            }
            Spacer(
                modifier = modifier
                    .height(8.dp)
            )
        }

    }

}
