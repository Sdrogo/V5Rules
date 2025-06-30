package com.example.v5rules.ui.compose.component.background

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Add
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
import com.example.v5rules.data.Background
import com.example.v5rules.ui.compose.component.ContentExpander
import com.example.v5rules.ui.compose.component.DotsWithMinMax
import com.example.v5rules.utils.CharacterSheetEvent
import com.example.v5rules.viewModel.CharacterSheetViewModel

@Composable
fun BackgroundList(
    backgrounds: List<Background>,
    viewModel: CharacterSheetViewModel,
    allGameBackgrounds: List<Background>,
    onAddMeritClick: (Background) -> Unit,
    onAddFlawClick: (Background) -> Unit,
    onAddNoteToBackground: (Background, String) -> Unit,
    onRemove: (Background) -> Unit,
    onRemoveNote: (Background) -> Unit
) {
    Column {
        backgrounds.forEach { characterBackground ->
            BackgroundItem(
                characterBackground = characterBackground,
                viewModel = viewModel,
                allGameBackgrounds = allGameBackgrounds,
                onAddMeritClick = onAddMeritClick,
                onAddFlawClick = onAddFlawClick,
                onAddNoteToBackground = onAddNoteToBackground,
                onRemove = onRemove,
                onRemoveNote = onRemoveNote
            )
        }
    }
}

@Composable
fun BackgroundItem(
    characterBackground: Background,
    viewModel: CharacterSheetViewModel,
    allGameBackgrounds: List<Background>,
    onAddMeritClick: (Background) -> Unit,
    onAddFlawClick: (Background) -> Unit,
    onAddNoteToBackground: (Background, String) -> Unit,
    onRemove: (Background) -> Unit,
    onRemoveNote: (Background) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
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
                    text = characterBackground.title,
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.weight(1f)
                )
                if (characterBackground.level > 0 && characterBackground.minLevel == null || characterBackground.level
                    <= (characterBackground.maxLevel ?: 5)
                ) {
                    DotsWithMinMax(
                        level = (characterBackground.level),
                        maxLevel = (characterBackground.maxLevel ?: 5)
                    )
                    Spacer(Modifier.width(8.dp))
                }
                Icon(
                    imageVector = if (expanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                    contentDescription = if (expanded) stringResource(
                        R.string.collapse,
                        characterBackground.title
                    ) else stringResource(R.string.expand_stuff, characterBackground.title)
                )
                IconButton(onClick = { onRemove(characterBackground) }) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = stringResource(
                            R.string.remove_stuff,
                            characterBackground.title
                        ),
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
        if (expanded) {
            Column {
                Slider(
                    value = characterBackground.level.toFloat(),
                    onValueChange = { newValue ->
                        val newIntValue = newValue.toInt()
                        if (newIntValue >= (characterBackground.minLevel
                                ?: 0)
                        ) { // Usa minLevel del background se definito
                            viewModel.onEvent(
                                CharacterSheetEvent.BackgroundLevelChanged(
                                    characterBackground, // Passa l'intero oggetto o solo l'ID/nome come hai definito nell'evento
                                    newIntValue
                                )
                            )
                        } else if (newIntValue == 0) { // Opzione per rimuovere se il livello è 0
                            viewModel.onEvent(
                                CharacterSheetEvent.BackgroundRemoved(characterBackground)
                            )
                        }
                    },
                    valueRange = (characterBackground.minLevel?.toFloat()
                        ?: 0f)..(characterBackground.maxLevel?.toFloat() ?: 5f),
                    steps = ((characterBackground.maxLevel ?: 5) - (characterBackground.minLevel
                        ?: 0) - 1).coerceAtLeast(0),
                    colors = SliderDefaults.colors(
                        activeTrackColor = MaterialTheme.colorScheme.tertiary
                    )
                )
                characterBackground.note?.let {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = it)
                        IconButton(onClick = { onRemoveNote(characterBackground) }) {
                            Icon(
                                imageVector = Icons.Default.Delete, // Usa AutoMirrored se l'icona deve supportare RTL
                                contentDescription = stringResource(R.string.delete_note)
                            )
                        }
                    }
                }
                if (characterBackground.note == null) {
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
                                    onAddNoteToBackground(
                                        characterBackground,
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

                ContentExpander(title = stringResource(R.string.discipline_description)) {
                    Text(
                        text = characterBackground.description,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                if (allGameBackgrounds.find { it.id == characterBackground.id }?.merits?.isNotEmpty() == true) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        Text(
                            modifier = Modifier.wrapContentWidth(),
                            text = stringResource(R.string.merit),
                            style = MaterialTheme.typography.titleMedium
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        IconButton(onClick = { onAddMeritClick(characterBackground) }) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = stringResource(R.string.add_merit)
                            )
                        }
                    }
                    characterBackground.merits?.forEach { merit ->
                        AdvantageDisplayItem(
                            advantage = merit,
                            onLevelChange = { newLevel ->
                                viewModel.onEvent(
                                    CharacterSheetEvent.BackgroundMeritLevelChanged(
                                        characterBackground,
                                        merit.id,
                                        newLevel
                                    )
                                )
                            },
                            onRemove = {
                                viewModel.onEvent(
                                    CharacterSheetEvent.BackgroundMeritRemoved(
                                        characterBackground,
                                        merit
                                    )
                                )
                            },
                            onAddNote = { note ->
                                viewModel.onEvent(
                                    CharacterSheetEvent.AddNoteToMerit(
                                        characterBackground,
                                        merit,
                                        note
                                    )
                                )
                            },
                            onRemoveNote = {
                                viewModel.onEvent(
                                    CharacterSheetEvent.RemoveNoteToMerit(
                                        characterBackground,
                                        merit
                                    )
                                )
                            }
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }
                if (allGameBackgrounds.find { it.id == characterBackground.id }?.flaws?.isNotEmpty() == true) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        Text(
                            modifier = Modifier.wrapContentWidth(),
                            text = stringResource(R.string.flaw),
                            style = MaterialTheme.typography.titleMedium
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        IconButton(onClick = { onAddFlawClick(characterBackground) }) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = stringResource(R.string.add_flaw)
                            )
                        }
                    }
                    characterBackground.flaws?.forEach { flaw ->
                        AdvantageDisplayItem(
                            advantage = flaw,
                            onLevelChange = { newLevel ->
                                viewModel.onEvent(
                                    CharacterSheetEvent.BackgroundFlawLevelChanged(
                                        characterBackground,
                                        flaw,
                                        newLevel
                                    )
                                )
                            },
                            onRemove = {
                                viewModel.onEvent(
                                    CharacterSheetEvent.BackgroundFlawRemoved(
                                        characterBackground,
                                        flaw
                                    )
                                )
                            },
                            onAddNote = { note ->
                                viewModel.onEvent(
                                    CharacterSheetEvent.AddNoteToFlaw(
                                        characterBackground,
                                        flaw,
                                        note
                                    )
                                )
                            },
                            onRemoveNote = {
                                viewModel.onEvent(
                                    CharacterSheetEvent.RemoveNoteToFlaw(
                                        characterBackground,
                                        flaw
                                    )
                                )
                            }
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
fun AdvantageDisplayItem(
    advantage: Advantage,
    onLevelChange: (Int) -> Unit,
    onRemove: () -> Unit,
    onAddNote: (String) -> Unit,
    onRemoveNote: () -> Unit
) {
    var itemExpanded by remember { mutableStateOf(false) }
    var noteText by remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    val currentMinLevel = advantage.minLevel ?: 0
    val currentMaxLevel = advantage.maxLevel ?: 5
    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { itemExpanded = !itemExpanded }
        ) {
            Text(
                text = advantage.title,
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.weight(1f)
            )
            if ((advantage.level ?: 1) > 0 && (advantage.minLevel == null || (advantage.level
                    ?: 1) <= (advantage.maxLevel ?: 5))
            ) {
                DotsWithMinMax(level = (advantage.level ?: 1), maxLevel = (advantage.maxLevel ?: 5))
                Spacer(Modifier.width(8.dp))
            }
            Icon(
                imageVector = if (itemExpanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                contentDescription = if (itemExpanded) stringResource(
                    R.string.collapse,
                    advantage.title
                ) else stringResource(R.string.expand_stuff, advantage.title)
            )
            IconButton(onClick = onRemove) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = stringResource(R.string.remove_stuff, advantage.title),
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }

        if (itemExpanded) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Column {
                    advantage.note?.let {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = it)
                            IconButton(onClick = onRemoveNote) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = stringResource(R.string.delete_note)
                                )
                            }
                        }
                    }
                    if (advantage.note == null) {
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
                    ContentExpander(title = stringResource(R.string.discipline_description)) {
                        Text(
                            text = advantage.description,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(horizontal = 8.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    if (currentMinLevel < currentMaxLevel || (advantage.level ?: 1) > 0) {

                        if (currentMinLevel != currentMaxLevel) {
                            advantage.level?.toFloat()?.let {
                                Slider(
                                    value = it,
                                    onValueChange = { newValue ->
                                        val newIntValue = newValue.toInt()
                                        if (newIntValue != advantage.level) {
                                            if (newIntValue == 0 && currentMinLevel == 0) {
                                                onRemove()
                                            } else if (newIntValue >= (advantage.minLevel
                                                    ?: 1) && newIntValue <= (advantage.maxLevel
                                                    ?: 5)
                                            ) {
                                                onLevelChange(newIntValue)
                                            } else if (newIntValue >= 1 && newIntValue <= (advantage.maxLevel
                                                    ?: 5)
                                            ) {
                                                onLevelChange(newIntValue)
                                            }
                                        }
                                    },
                                    valueRange = (currentMinLevel.toFloat())..(currentMaxLevel.toFloat()),
                                    steps = (currentMaxLevel - currentMinLevel - 1).coerceAtLeast(
                                        0
                                    ),
                                    colors = SliderDefaults.colors(
                                        activeTrackColor = MaterialTheme.colorScheme.tertiary
                                    ),
                                    modifier = Modifier.padding(horizontal = 8.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}