package com.example.v5rules.ui.compose.screen.sheet

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.v5rules.R
import com.example.v5rules.data.Character
import com.example.v5rules.ui.compose.component.ClanImage
import com.example.v5rules.ui.compose.component.CommonScaffold
import com.example.v5rules.viewModel.CharacterSheetListViewModel
import com.example.v5rules.CharacterSheetEditNav


@Composable
fun CharacterSheetListScreen(
    navController: NavHostController,
    viewModel: CharacterSheetListViewModel = hiltViewModel() // Usa hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    CommonScaffold(
        navController = navController,
        title = stringResource(R.string.character_list_title)
    ) { _ ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(),
            contentAlignment = Alignment.Center
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator()
            } else if (uiState.error != null) {
                Text(text = "Errore: ${uiState.error}")
            } else {
                CharacterList(
                    characters = uiState.characterList,
                    navController = navController
                )
            }
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
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        items(characters) { char ->
            CharacterCard(character = char, navController = navController)
        }
    }
}

@Composable
fun CharacterCard(character: Character, navController: NavHostController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.tertiary,
                shape = RoundedCornerShape(8.dp)
            )
            .clickable {
                // Naviga alla schermata di dettaglio del personaggio, passando l'ID
                navController.navigate(CharacterSheetEditNav(character.id))
            },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Immagine del clan (usa il tuo componente ClanImage)
            ClanImage(
                clanName = character.clan?.name ?: "Unknown", // Gestisci il caso in cui il clan è null
                tintColor = MaterialTheme.colorScheme.tertiary, // o un altro colore appropriato
                width = 48.dp // Imposta una dimensione appropriata
            )

            Spacer(modifier = Modifier.width(16.dp))

            // Nome del personaggio e altre info
            Column {
                Text(
                    text = character.name,
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.primary
                )
                // Aggiungi altre info qui, se vuoi (es. concept, sire, ecc.)
                Text(
                    text = "Clan: ${character.clan?.name ?: "Sconosciuto"}", // Mostra il nome del clan
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}