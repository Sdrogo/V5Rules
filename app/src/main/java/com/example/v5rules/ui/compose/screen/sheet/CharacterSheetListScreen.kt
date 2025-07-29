package com.example.v5rules.ui.compose.screen.sheet

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.v5rules.CharacterSheetCreationNav
import com.example.v5rules.R
import com.example.v5rules.data.Character
import com.example.v5rules.ui.compose.component.ClanImage
import com.example.v5rules.viewModel.CharacterSheetListViewModel
import com.example.v5rules.CharacterSheetEditNav
import com.example.v5rules.CharacterSheetVisualizationNav


@Composable
fun CharacterSheetListScreen(
    navController: NavHostController,
    viewModel: CharacterSheetListViewModel = hiltViewModel(),
    onTitleChanged: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val title = stringResource(R.string.character_list_title)
    LaunchedEffect(Unit) {
        onTitleChanged(title)
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
    )
    {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.tertiary)
            } else if (uiState.error != null) {
                Text(text = stringResource(R.string.error_loading_data, uiState.error ?: ""))
            } else if (uiState.characterList.isEmpty()) {
                Text(text = stringResource(R.string.no_characters_found_create_new))
            } else {
                CharacterList(
                    characters = uiState.characterList,
                    navController = navController
                )
            }
        }
        FloatingActionButton(
            onClick = { navController.navigate(CharacterSheetCreationNav) },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 16.dp, bottom = 56.dp),
            shape = CircleShape,
            containerColor = MaterialTheme.colorScheme.secondary,
            elevation = FloatingActionButtonDefaults.elevation(),
            interactionSource = remember { MutableInteractionSource() }
        ) {
            Icon(
                Icons.Filled.Add,
                tint = MaterialTheme.colorScheme.primary,
                contentDescription = stringResource(R.string.create_new_sheet)
            )
        }
    }
}

@Composable
fun CharacterList(
    characters: List<Character>,
    navController: NavHostController
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
    ) {
        items(characters) { char ->
            Column {
                CharacterCard(character = char, navController = navController)
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun CharacterCard(character: Character, navController: NavHostController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.secondary,
                shape = RoundedCornerShape(8.dp)
            )
            .clickable { navController.navigate(CharacterSheetVisualizationNav(character.id)) },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(8.dp),
    ) {
        Box(modifier = Modifier.background(MaterialTheme.colorScheme.primary)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                ClanImage(
                    clanName = character.clan?.name
                        ?: "Unknown",
                    tintColor = MaterialTheme.colorScheme.onSecondary,
                    width = 48.dp
                )

                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(
                        text = character.name,
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onSecondary
                    )
                    Text(
                        text = character.concept,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSecondary
                    )
                }
            }
            IconButton(
                onClick = { navController.navigate(CharacterSheetEditNav(character.id)) },
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 16.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.secondary)
            ) {
                Icon(
                    Icons.Filled.Edit,
                    contentDescription = stringResource(R.string.edit_sheet),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}