package com.example.v5rules.ui.compose.component.background.bottomSheet

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.v5rules.R
import com.example.v5rules.data.Advantage
import com.example.v5rules.data.Background


@Composable
fun MeritsSelectionBottomSheet(
    background: Background,
    onMeritSelected: (Advantage) -> Unit,
    onDismiss: () -> Unit
) {
    var searchText by remember { mutableStateOf("") }
    val filteredMerits = background.merits.orEmpty().sortedBy { it.title }.filter { flaw ->
        flaw.title.contains(searchText, ignoreCase = true)
    }
    Column(
        modifier = Modifier
            .navigationBarsPadding()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = searchText,
                onValueChange = { searchText = it },
                label = { Text(stringResource(R.string.search_merits)) },
                modifier = Modifier.weight(1f), // Occupa lo spazio disponibile
                singleLine = true, // Opzionale: per un aspetto più compatto
                trailingIcon = {
                    IconButton(onClick = onDismiss) { // Azione: pulisce il testo
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = stringResource(R.string.close) // Per l'accessibilità
                        )
                    }
                }
            )
        }
        LazyColumn(modifier = Modifier) {
            items(filteredMerits) { merit ->
                AdvantageSelectionBottomSheetItem(
                    advantage = merit,
                    onAdvantageSelected = {
                        onMeritSelected(
                            merit
                        )
                    }
                )
            }
        }
    }
}
