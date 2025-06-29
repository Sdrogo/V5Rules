package com.example.v5rules.ui.compose.component.background.bottomSheet

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
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
import com.example.v5rules.ui.compose.component.RangeDots


@Composable
fun DirectFlawSelectionBottomSheet(
    allDirectFlaws: List<Advantage>,
    characterDirectFlaws: List<Advantage>,
    onDirectFlawSelected: (Advantage) -> Unit,
    onDismiss: () -> Unit
) {
    var searchText by remember { mutableStateOf("") }
    val filteredFlaws = allDirectFlaws.filter { flaw ->
        flaw.title.contains(searchText, ignoreCase = true)
    }
    Column(
        modifier = Modifier
            .navigationBarsPadding()

    ) {
        Row( // Puoi mantenere la Row se vuoi altri elementi accanto al TextField, o rimuoverla se il TextField è l'unico elemento orizzontale
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp), // Aggiustato padding per un look migliore
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = searchText,
                onValueChange = { searchText = it },
                label = { Text(stringResource(R.string.search_flaws)) },
                modifier = Modifier.weight(1f), // Occupa lo spazio disponibile
                singleLine = true, // Opzionale: per un aspetto più compatto
                trailingIcon = {
                        IconButton(onClick = onDismiss) { // Azione: pulisce il testo
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = stringResource(R.string.close)// Per l'accessibilità
                            )
                        }
                }
            )
        }
        LazyColumn(modifier = Modifier) { // Added padding for FAB
            items(filteredFlaws) { flaw ->
                AdvantageSelectionBottomSheetItem(
                    advantage = flaw,
                    onAdvantageSelected = { onDirectFlawSelected(flaw) }
                )
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AdvantageSelectionBottomSheetItem(
    advantage: Advantage,
    onAdvantageSelected: (Advantage) -> Unit
) {
    FlowRow(modifier = Modifier
            .fillMaxSize()
        .clickable { onAdvantageSelected(advantage) },
        verticalArrangement = Arrangement.Center,
    horizontalArrangement = Arrangement.Start ){
        Text(
            text = advantage.title,
            modifier = Modifier
                .wrapContentWidth()
                .padding(horizontal = 16.dp)
        )
        RangeDots(min = advantage.minLevel ?: 1, max = advantage.maxLevel ?: 5)
    }
}
