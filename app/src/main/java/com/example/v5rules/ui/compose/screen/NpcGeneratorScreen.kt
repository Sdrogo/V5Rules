package com.example.v5rules.ui.compose.screen

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.v5rules.CharacterSheetEditNav
import com.example.v5rules.R
import com.example.v5rules.data.FavoriteNpc
import com.example.v5rules.data.Npc
import com.example.v5rules.ui.compose.component.GenderSelection
import com.example.v5rules.ui.compose.component.IncludeSecondNameCheckbox
import com.example.v5rules.ui.compose.component.NationalityDropdown
import com.example.v5rules.viewModel.NPCGeneratorViewModel
import com.example.v5rules.viewModel.NpcNationalityUiState
import com.example.v5rules.viewModel.NpcNavigationEvent
import kotlinx.coroutines.flow.collectLatest

@Composable
fun NPCGeneratorScreen(
    modifier: Modifier,
    viewModel: NPCGeneratorViewModel,
    navController: NavHostController,
    onTitleChanged: (String) -> Unit
) {
    val loadingState by viewModel.nationalityState.collectAsState()
    val uiState by viewModel.uiState.collectAsState()
    val orientation = LocalConfiguration.current.orientation
    val title = stringResource(id = R.string.npc_generator_title)
    LaunchedEffect(Unit) {
        onTitleChanged(title)
    }
    // Handle navigation
    LaunchedEffect(Unit) {
        viewModel.navigationEvent.collectLatest { event ->
            when (event) {
                is NpcNavigationEvent.ToCharacterSheet -> {
                    navController.navigate(CharacterSheetEditNav(event.characterId))
                }
            }
        }
    }

    when (loadingState) {
        is NpcNationalityUiState.Error -> Text(
            "Error: ${(loadingState as NpcNationalityUiState.Error).message}",
            color = MaterialTheme.colorScheme.primary
        )

        NpcNationalityUiState.Loading -> Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }

        is NpcNationalityUiState.Success -> {
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item {
                        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                            Row {
                                Column(modifier = Modifier.weight(1f)) {
                                    SettingsCard(viewModel = viewModel)
                                }
                                Column(modifier = Modifier.weight(1f)) {
                                    Box(
                                        modifier = Modifier.padding(8.dp)
                                    ) {
                                        if (uiState.favoriteNpcs.isNotEmpty()) {
                                            FavoritesDropdown(
                                                favoriteNpcs = uiState.favoriteNpcs,
                                                onFavoriteSelected = { viewModel.selectFavorite(it) }
                                            )
                                        }
                                    }
                                    GeneratedNameSection(
                                        npc = uiState.npc,
                                        includeSecondName = uiState.includeSecondName,
                                        onRegenerateName = viewModel::regenerateName,
                                        onRegenerateSecondName = viewModel::regenerateSecondName,
                                        onRegenerateFamilyName = viewModel::regenerateFamilyName,
                                        onToggleFavorite = {
                                            uiState.npc?.let {
                                                viewModel.toggleFavorite()
                                            }
                                        }
                                    )
                                }
                            }
                        } else {
                            Column {
                                if (uiState.favoriteNpcs.isNotEmpty()) {
                                    FavoritesDropdown(
                                        favoriteNpcs = uiState.favoriteNpcs,
                                        onFavoriteSelected = { viewModel.selectFavorite(it) }
                                    )
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                SettingsCard(viewModel = viewModel)
                                Spacer(modifier = Modifier.height(16.dp))
                                GeneratedNameSection(
                                    npc = uiState.npc,
                                    includeSecondName = uiState.includeSecondName,
                                    onRegenerateName = viewModel::regenerateName,
                                    onRegenerateSecondName = viewModel::regenerateSecondName,
                                    onRegenerateFamilyName = viewModel::regenerateFamilyName,
                                    onToggleFavorite = {
                                        uiState.npc?.let {
                                            viewModel.toggleFavorite()
                                        }
                                    }
                                )
                            }
                        }
                    }
                }
                ActionButtons(
                    onGenerateClick = viewModel::generateAll,
                    onCreateClick = viewModel::createCharacterFromNpc,
                    isCreateEnabled = uiState.npc != null
                )
            }
        }
    }
}

@Composable
private fun ActionButtons(
    onGenerateClick: () -> Unit,
    onCreateClick: () -> Unit,
    isCreateEnabled: Boolean
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(
            8.dp,
            Alignment.CenterHorizontally
        )
    ) {
        Button(
            onClick = onGenerateClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.tertiary,
                contentColor = MaterialTheme.colorScheme.secondary
            )
        ) {
            Text(stringResource(R.string.generate_all_button_label))
        }
        Button(
            onClick = onCreateClick,
            enabled = isCreateEnabled,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.tertiary,
                contentColor = MaterialTheme.colorScheme.secondary
            )
        ) {
            Text(stringResource(R.string.create_character))
        }
    }
}

@Composable
private fun GeneratedNameSection(
    npc: Npc?,
    includeSecondName: Boolean,
    onRegenerateName: () -> Unit,
    onRegenerateSecondName: () -> Unit,
    onRegenerateFamilyName: () -> Unit,
    onToggleFavorite: () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        if (npc == null) {
            Text(
                text = stringResource(R.string.generate_an_npc_to_start),
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(32.dp)
                    .fillMaxWidth()
            )
        } else {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = npc.nome,
                        style = MaterialTheme.typography.headlineMedium,
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(onClick = onRegenerateName) {
                        Icon(
                            Icons.Default.Refresh,
                            contentDescription = stringResource(R.string.regenerate_name)
                        )
                    }
                }

                if (includeSecondName && npc.secondName != null) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = npc.secondName,
                            style = MaterialTheme.typography.headlineMedium,
                            modifier = Modifier.weight(1f)
                        )
                        IconButton(onClick = onRegenerateSecondName) {
                            Icon(
                                Icons.Default.Refresh,
                                contentDescription = stringResource(R.string.regenerate_second_name)
                            )
                        }
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = npc.cognome,
                        style = MaterialTheme.typography.headlineMedium,
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(onClick = onRegenerateFamilyName) {
                        Icon(
                            Icons.Default.Refresh,
                            contentDescription = stringResource(R.string.regenerate_family_name)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                IconButton(onClick = onToggleFavorite) {
                    Icon(
                        imageVector = if (npc.isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                        contentDescription = "Favorite",
                        tint = if (npc.isFavorite) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}

@Composable
private fun SettingsCard(viewModel: NPCGeneratorViewModel) {
    val uiState by viewModel.uiState.collectAsState()

    OutlinedCard(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .padding(top = 8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = stringResource(R.string.settings),
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    stringResource(id = R.string.nationality_selector_label),
                    modifier = Modifier.weight(1f)
                )
                NationalityDropdown(
                    nationalities = viewModel.nationalities,
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
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesDropdown(
    favoriteNpcs: List<FavoriteNpc>,
    onFavoriteSelected: (FavoriteNpc) -> Unit,
    modifier: Modifier = Modifier,
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier
    ) {
        TextField(
            readOnly = true,
            value = stringResource(R.string.favorites_count, favoriteNpcs.size),
            onValueChange = {},
            label = { Text(stringResource(R.string.favorites)) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            colors = ExposedDropdownMenuDefaults.textFieldColors(),
            modifier = Modifier
                .menuAnchor()
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
