package com.example.v5rules.ui.compose.component.bottomSheet

import androidx.compose.foundation.clickable
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
import com.example.v5rules.data.Loresheet

@Composable
fun LoresheetSelectionBottomSheet(
    allLoresheets: List<Loresheet>,
    characterLoresheets: List<Loresheet>,
    onLoresheetSelected: (Loresheet) -> Unit,
    onDismiss: () -> Unit
) {
    var searchText by remember { mutableStateOf("") }
    val filteredLoresheets = allLoresheets.filter { loresheet ->
        loresheet.title.contains(searchText, ignoreCase = true) &&
                characterLoresheets.none { it.id == loresheet.id }
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
                label = { Text(stringResource(R.string.search_loresheet)) },
                modifier = Modifier.weight(1f),
                singleLine = true,
                trailingIcon = {
                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = stringResource(R.string.close)
                        )
                    }
                }
            )
        }
        LazyColumn(modifier = Modifier.padding(bottom = 16.dp)) {
            items(filteredLoresheets) { loresheet ->
                LoresheetSelectionBottomSheetItem(
                    loresheet = loresheet,
                    onLoresheetSelected = { onLoresheetSelected(it) }
                )
            }
        }
    }
}

@Composable
fun LoresheetSelectionBottomSheetItem(
    loresheet: Loresheet,
    onLoresheetSelected: (Loresheet) -> Unit
) {
    Text(
        text = loresheet.title,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onLoresheetSelected(loresheet) }
            .padding(16.dp)
    )
}
