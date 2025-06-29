package com.example.v5rules.ui.compose.component.background

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.v5rules.R
import com.example.v5rules.data.Advantage
import com.example.v5rules.ui.compose.component.ContentExpander
import com.example.v5rules.ui.compose.component.DotsWithMinMax


@Composable
fun DirectFlawsList(
    flaws: List<Advantage>,
    onRemove: (Advantage) -> Unit,
    onAddNote: (Advantage, String) -> Unit,
    onRemoveNote: (Advantage) -> Unit,
    onDirectFlawLevelChanged: (Advantage, Int) -> Unit
) {
    LazyColumn {
        items(flaws) { characterDirectFlaw ->
            AdvantageItem(
                characterDirectFlaw = characterDirectFlaw,
                onAddNote = onAddNote,
                onRemove = onRemove,
                onRemoveNote = onRemoveNote,
                onDirectFlawLevelChanged = onDirectFlawLevelChanged
            )
        }
    }
}

@Composable
fun AdvantageItem(
    characterDirectFlaw: Advantage,
    onAddNote: (Advantage, String) -> Unit,
    onRemoveNote: (Advantage) -> Unit,
    onDirectFlawLevelChanged: (Advantage, Int) -> Unit,
    onRemove: (Advantage) -> Unit// Lista di tutti i background disponibili nel gioco
) {
    var expanded by remember { mutableStateOf(false) }
    val minLevel = characterDirectFlaw.minLevel ?: 1
    val maxLevel = characterDirectFlaw.maxLevel ?: 5
    var noteText by remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = !expanded }) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = characterDirectFlaw.title,
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.weight(1f)
                )
                if ((characterDirectFlaw.level ?: 1) > 0 &&
                    characterDirectFlaw.minLevel == null ||
                    (characterDirectFlaw.level ?: 1) <= (characterDirectFlaw.maxLevel ?: 5)
                ) {
                    DotsWithMinMax(
                        level = (characterDirectFlaw.level ?: 1),
                        maxLevel = (characterDirectFlaw.maxLevel ?: 5)
                    )
                    Spacer(Modifier.width(8.dp))
                }
                Icon(
                    imageVector = if (expanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                    contentDescription = if (expanded) stringResource(
                        R.string.collapse,
                        characterDirectFlaw.title
                    ) else stringResource(R.string.expand_stuff, characterDirectFlaw.title)
                )
                IconButton(onClick = { onRemove(characterDirectFlaw) }) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = stringResource(
                            R.string.remove_stuff,
                            characterDirectFlaw.title
                        ),
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
        if (expanded) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = !expanded })
            {
                Column {
                    if (minLevel != maxLevel) {
                        Slider(
                            value = (characterDirectFlaw.level ?: 1).toFloat(),
                            onValueChange = { newValue ->
                                onDirectFlawLevelChanged(characterDirectFlaw, newValue.toInt())
                            },
                            valueRange = (characterDirectFlaw.minLevel?.toFloat()
                                ?: 0f)..(characterDirectFlaw.maxLevel?.toFloat() ?: 5f),
                            steps = ((characterDirectFlaw.maxLevel
                                ?: 5) - (characterDirectFlaw.minLevel
                                ?: 0) - 1).coerceAtLeast(0),
                            colors = SliderDefaults.colors(
                                activeTrackColor = MaterialTheme.colorScheme.tertiary
                            )
                        )
                    }
                    characterDirectFlaw.note?.let {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = it)
                            IconButton(onClick = { onRemoveNote(characterDirectFlaw) }) {
                                Icon(
                                    imageVector = Icons.Default.Delete, // Usa AutoMirrored se l'icona deve supportare RTL
                                    contentDescription = stringResource(R.string.delete_note)
                                )
                            }
                        }
                    }
                    if (characterDirectFlaw.note == null) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            TextField(
                                value = noteText,
                                onValueChange = { noteText = it },
                                label = { Text(stringResource(R.string.add_note)) },
                                modifier = Modifier.weight(1f), // Occupa lo spazio disponibile nella Row
                                singleLine = true,
                                trailingIcon = {
                                    IconButton(onClick = {
                                        onAddNote(
                                            characterDirectFlaw,
                                            noteText
                                        )
                                        keyboardController?.hide()
                                        focusManager.clearFocus()
                                    }) {
                                        Icon(
                                            imageVector = Icons.AutoMirrored.Filled.Send, // Usa AutoMirrored se l'icona deve supportare RTL
                                            contentDescription = stringResource(R.string.add_note)
                                        )
                                    }
                                }
                            )
                        }
                    }
                    ContentExpander(stringResource(R.string.discipline_description)) {
                        Text(
                            text = characterDirectFlaw.description,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}