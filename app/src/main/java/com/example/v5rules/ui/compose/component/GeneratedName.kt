package com.example.v5rules.ui.compose.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.v5rules.data.Npc
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import com.example.v5rules.viewModel.NPCGeneratorViewModel


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
                modifier = Modifier
                    .align(Alignment.BottomEnd) // Allinea in basso a destra
                    .padding(16.dp) // Aggiungi padding
            )
        } else {
            // Placeholder o messaggio "Nessun NPC generato"
            Text(
                text = "Genera un NPC", // Testo di esempio
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f), // Colore più tenue
            )
        }
    }
}

@Composable
fun FavoriteHeartIcon(isFavorite: Boolean, onToggleFavorite: () -> Unit, modifier: Modifier = Modifier) {
    Icon(
        imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
        contentDescription = if (isFavorite) "Remove from favorites" else "Add to favorites",
        modifier = Modifier.clickable { onToggleFavorite() },
        tint = if (isFavorite) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.primary.copy(alpha = 0.6f) // Usa il colore del tema, ma più tenue se non favorito

    )
}