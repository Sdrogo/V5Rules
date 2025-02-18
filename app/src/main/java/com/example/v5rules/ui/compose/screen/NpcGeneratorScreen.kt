package com.example.v5rules.ui.compose.screen

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowColumn
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension
import androidx.compose.ui.layout.layoutId
import androidx.navigation.NavHostController
import com.example.v5rules.R
import com.example.v5rules.data.FavoriteNpc
import com.example.v5rules.data.Npc
import com.example.v5rules.data.RegenerationType
import com.example.v5rules.ui.compose.component.CommonScaffold
import com.example.v5rules.ui.compose.component.GenderSelection
import com.example.v5rules.ui.compose.component.GenerateButton
import com.example.v5rules.ui.compose.component.IncludeSecondNameCheckbox
import com.example.v5rules.ui.compose.component.NationalityDropdown
import com.example.v5rules.ui.compose.component.RegenerationCheckbox
import com.example.v5rules.viewModel.NPCGeneratorViewModel

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun NPCGeneratorScreen(
    modifier: Modifier = Modifier, // Teniamo questo modifier, ma con un default
    viewModel: NPCGeneratorViewModel,
    navController: NavHostController
) {
    val uiState by viewModel.uiState.collectAsState()
    val favoriteNpcs by viewModel.favoriteNpcs.collectAsState()
    val isLandscape = LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE

    CommonScaffold(
        navController = navController,
        title = stringResource(id = R.string.npc_generator_title)
    ) {
        val constraintSet = ConstraintSet {
            val topSection = createRefFor("topSection")
            val generatedName = createRefFor("generatedName")
            val bottomSection = createRefFor("bottomSection")

            constrain(topSection) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                width = Dimension.fillToConstraints
                height = Dimension.percent(0.3f)
            }
            constrain(generatedName) {
                top.linkTo(topSection.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                width = Dimension.fillToConstraints
                height = Dimension.percent(0.3f)
            }
            constrain(bottomSection) {
                top.linkTo(generatedName.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                bottom.linkTo(parent.bottom)
                width = Dimension.fillToConstraints
                height = Dimension.fillToConstraints // Importante per lo scrolling
            }
        }
        ConstraintLayout(
            constraintSet,
            modifier = Modifier
                .fillMaxSize()
        ) {
            FlowRow(
                modifier = Modifier
                    .layoutId("topSection")
                    .fillMaxWidth()
            ) {
                FavoritesDropdown(favoriteNpcs = favoriteNpcs)
                Row(
                    modifier = Modifier
                        .fillMaxWidth(if (isLandscape) 0.5f else 1f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(stringResource(id = R.string.nationality_selector_label))

                    Spacer(modifier = Modifier.width(16.dp))
                    NationalityDropdown(
                        nationalities = viewModel.nationalities,
                        onNationalitySelected = { viewModel.setSelectedNationality(it) }
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
                        onGenderSelected = { viewModel.setSelectedGender(it) }
                    )
                } else {
                    GenderSelection(
                        selectedGender = uiState.selectedGender,
                        widthOfFlow = 0.4f,
                        isLandscape = false,
                        onGenderSelected = { viewModel.setSelectedGender(it) }
                    )
                    IncludeSecondNameCheckbox(
                        includeSecondName = uiState.includeSecondName,
                        width = 0.4f,
                        onIncludeSecondNameChange = { viewModel.setIncludeSecondName(it) }
                    )
                }
            }

            GeneratedName(  // No modifier passato qui
                npc = uiState.npc,
                widthFloat = if (isLandscape) 0.45f else 1f,
                viewModel = viewModel,
                modifier = Modifier.layoutId("generatedName") // Solo layoutId
            )

            FlowRow(
                modifier = Modifier.layoutId("bottomSection"),
                horizontalArrangement = Arrangement.Center,
                maxItemsInEachRow = 2,

                ) {
                RegenerationCheckbox(
                    text = stringResource(id = R.string.generate_name_button_label),
                    width = 0.4f,
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
                RegenerationCheckbox(
                    text = stringResource(id = R.string.generate_second_name_button_label),
                    width = 0.4f,
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
                RegenerationCheckbox(
                    text = stringResource(id = R.string.generate_family_name_button_label),
                    width = 0.4f,
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
                RegenerationCheckbox(
                    text = stringResource(id = R.string.generate_all_button_label),
                    checked = uiState.selectedRegenerationTypes.contains(RegenerationType.ALL),
                    width = 0.4f,
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

@Composable
fun GeneratedName(
    npc: Npc?,
    widthFloat: Float = 1f,
    viewModel: NPCGeneratorViewModel,
    modifier: Modifier = Modifier // Aggiunto Modifier
) {
    Box(
        modifier = modifier // Usa il Modifier passato
            .fillMaxWidth(widthFloat)
            .padding(vertical = 8.dp)
    ) {
        if (npc != null) {
            Text(
                text = buildString {
                    append(npc.nome)
                    if (!npc.secondName.isNullOrEmpty()) append(" ${npc.secondName}")
                    append(" ${npc.cognome}")
                },
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.align(Alignment.Center) // Centra il testo
            )

            FavoriteHeartIcon(
                isFavorite = npc.isFavorite,
                onToggleFavorite = { viewModel.toggleFavorite(npc) },
                modifier = Modifier //passo il modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp)
            )
        } else {
            Text(
                text = "Genera un NPC",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}

@Composable
fun FavoriteHeartIcon(isFavorite: Boolean, onToggleFavorite: () -> Unit, modifier: Modifier = Modifier) {
    Icon(
        imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
        contentDescription = if (isFavorite) "Remove from favorites" else "Add to favorites",
        modifier = modifier.clickable { onToggleFavorite() },
        tint = if (isFavorite) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesDropdown(
    favoriteNpcs: List<FavoriteNpc>,
    onFavoriteSelected: (FavoriteNpc) -> Unit = {}
) {
    var expanded by remember { mutableStateOf(false) }

    if (favoriteNpcs.isNotEmpty()) {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            TextField(
                readOnly = true,
                value = "Preferiti (${favoriteNpcs.size})",
                onValueChange = {},
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                colors = ExposedDropdownMenuDefaults.textFieldColors(),
                modifier = Modifier
                    .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                    .fillMaxWidth()
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                favoriteNpcs.forEach { favorite ->
                    DropdownMenuItem(
                        text = {
                            Text(buildString {
                                append(favorite.name)
                                if (!favorite.secondName.isNullOrEmpty()) append(" ${favorite.secondName}")
                                append(" ${favorite.familyName}")
                                append(" (${favorite.nationality})")
                            })
                        },
                        onClick = {
                            expanded = false
                            onFavoriteSelected(favorite)
                        },
                    )
                }
            }
        }
    }
}