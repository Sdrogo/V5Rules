package com.example.v5rules.ui.compose.component.background

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.v5rules.R
import com.example.v5rules.data.Advantage
import com.example.v5rules.data.Background
import com.example.v5rules.ui.compose.component.ContentExpander
import com.example.v5rules.utils.CharacterSheetEvent

@Composable
fun BackgroundList(
    backgrounds: List<Background>,
    allGameBackgrounds: List<Background>,
    onAddMeritClick: (Background) -> Unit,
    onAddFlawClick: (Background) -> Unit,
    onEvent:(CharacterSheetEvent) -> Unit,
) {
    Column {
        backgrounds.forEach { characterBackground ->
            val gameBackground = allGameBackgrounds.find { it.id == characterBackground.id }
            if (gameBackground != null) {
                BackgroundItem(
                    characterBackground = characterBackground,
                    gameBackground = gameBackground,
                    onEvent = {onEvent},
                    onAddMeritClick = onAddMeritClick,
                    onAddFlawClick = onAddFlawClick,
                )
            }
        }
    }
}

@Composable
fun BackgroundItem(
    characterBackground: Background,
    gameBackground: Background,
    onEvent: (CharacterSheetEvent) -> Unit,
    onAddMeritClick: (Background) -> Unit,
    onAddFlawClick: (Background) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    var noteText by remember { mutableStateOf(characterBackground.note ?: "") }
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    val minLevel = gameBackground.minLevel ?: 1
    val maxLevel = gameBackground.maxLevel ?: 5

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = gameBackground.title, style = MaterialTheme.typography.titleMedium, modifier = Modifier.weight(1f))
            InteractiveBackgroundDots(
                currentValue = characterBackground.level,
                minValue = minLevel,
                maxValue = maxLevel,
                onValueChange = { newLevel -> onEvent(CharacterSheetEvent.BackgroundLevelChanged(characterBackground, newLevel)) }
            )
            Spacer(Modifier.width(8.dp))
            IconButton(
                onClick = { onEvent(CharacterSheetEvent.BackgroundRemoved(characterBackground)) }
            )
            {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Remove Background",
                    tint = MaterialTheme.colorScheme.error
                )
            }
            IconButton(onClick = { expanded = !expanded }) {
                Icon(
                    imageVector = if (expanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                    contentDescription = if (expanded) "Collapse" else "Expand"
                )
            }
        }

        if (expanded) {
            Column(modifier = Modifier.padding(start = 8.dp, end = 8.dp, top = 8.dp)) {
                ContentExpander(stringResource(id = R.string.discipline_description)) {
                    Text(text = gameBackground.description, style = MaterialTheme.typography.bodySmall)
                }
                characterBackground.note?.let {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = it, modifier = Modifier.weight(1f))
                        IconButton(onClick = { onEvent(CharacterSheetEvent.RemoveNoteToBackground(characterBackground)) }) {
                            Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete Note")
                        }
                    }
                }
                if (characterBackground.note == null) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TextField(
                            value = noteText,
                            onValueChange = { noteText = it },
                            label = { Text("Add Note") },
                            modifier = Modifier.weight(1f),
                            singleLine = true,
                            trailingIcon = {
                                IconButton(onClick = {
                                    onEvent(CharacterSheetEvent.AddNoteToBackground(characterBackground, noteText))
                                    keyboardController?.hide()
                                    focusManager.clearFocus()
                                }) {
                                    Icon(imageVector = Icons.AutoMirrored.Filled.Send, contentDescription = "Add Note")
                                }
                            }
                        )
                    }
                }
                if (!characterBackground.merits.isNullOrEmpty()) {
                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                    Text(stringResource(R.string.merit), style = MaterialTheme.typography.titleSmall)
                    characterBackground.merits.forEach { merit ->
                        BackgroundAdvantageItem(
                            advantage = merit,
                            isFlaw = false,
                            onRemove = { onEvent(CharacterSheetEvent.BackgroundMeritRemoved(characterBackground, merit)) },
                            onLevelChanged = { newLevel -> onEvent(CharacterSheetEvent.BackgroundMeritLevelChanged(characterBackground, merit.id, newLevel))},
                                onAddNote = { note -> onEvent(CharacterSheetEvent.AddNoteToMerit(characterBackground, merit, note)) },
                            onRemoveNote = { onEvent(CharacterSheetEvent.RemoveNoteToMerit(characterBackground, merit)) }
                        )
                    }
                }
                if (!characterBackground.flaws.isNullOrEmpty()) {
                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                    Text(stringResource(R.string.flaw), style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.error)
                    characterBackground.flaws.forEach { flaw ->
                        BackgroundAdvantageItem(
                            advantage = flaw,
                            isFlaw = true,
                            onRemove = { onEvent(CharacterSheetEvent.BackgroundFlawRemoved(characterBackground, flaw)) },
                            onLevelChanged = { newLevel -> onEvent(CharacterSheetEvent.BackgroundFlawLevelChanged(characterBackground, flaw, newLevel)) },
                            onAddNote = { note -> onEvent(CharacterSheetEvent.AddNoteToFlaw(characterBackground, flaw, note)) },
                            onRemoveNote = { onEvent(CharacterSheetEvent.RemoveNoteToFlaw(characterBackground, flaw)) }
                        )
                    }
                }
                Row {
                    Button(onClick = { onAddMeritClick(characterBackground) }, modifier = Modifier.padding(end = 8.dp)) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = null)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Add Merit")
                    }
                    Button(onClick = { onAddFlawClick(characterBackground) }) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = null)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Add Flaw")
                    }
                }

            }
        }
    }
}


@Composable
fun BackgroundAdvantageItem(
    advantage: Advantage,
    isFlaw: Boolean,
    onRemove: () -> Unit,
    onLevelChanged: (Int) -> Unit,
    onAddNote: (String) -> Unit,
    onRemoveNote: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var noteText by remember(advantage.note) { mutableStateOf(advantage.note ?: "") }
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    Column(modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = advantage.title, style = MaterialTheme.typography.bodyMedium, modifier = Modifier.weight(1f))
            InteractiveAdvantageDots(
                currentValue = advantage.level ?: 0,
                minValue = advantage.minLevel ?: 1,
                maxValue = advantage.maxLevel ?: 5,
                isFlaw = isFlaw,
                onValueChange = onLevelChanged
            )
            IconButton(onClick = onRemove, modifier = Modifier.size(24.dp)) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = "Remove", tint = MaterialTheme.colorScheme.error)
            }
            IconButton(onClick = { expanded = !expanded }, modifier = Modifier.size(24.dp)) {
                Icon(imageVector = if (expanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown, contentDescription = "Expand")
            }
        }
        if (expanded) {
            if (advantage.description.isNotEmpty()) {
                Text(text = advantage.description, style = MaterialTheme.typography.bodySmall)
            }
            advantage.note?.let {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = it, modifier = Modifier.weight(1f))
                    IconButton(onClick = onRemoveNote) {
                        Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete Note")
                    }
                }
            }
            if (advantage.note == null) {
                TextField(
                    value = noteText,
                    onValueChange = { noteText = it },
                    label = { Text("Add Note") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp),
                    singleLine = true,
                    trailingIcon = {
                        IconButton(onClick = {
                            onAddNote(noteText)
                            keyboardController?.hide()
                            focusManager.clearFocus()
                        }) {
                            Icon(imageVector = Icons.AutoMirrored.Filled.Send, contentDescription = "Add Note")
                        }
                    }
                )
            }
        }
    }
}


@Composable
private fun InteractiveAdvantageDots(
    currentValue: Int,
    minValue: Int,
    maxValue: Int,
    isFlaw: Boolean,
    onValueChange: (Int) -> Unit
) {
    val filledColor = if (isFlaw) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.tertiary
    val borderColor = if (isFlaw) MaterialTheme.colorScheme.errorContainer else MaterialTheme.colorScheme.primary
    Row {
        for (i in minValue..maxValue) {
            Box(
                modifier = Modifier
                    .size(20.dp)
                    .padding(1.dp)
                    .clip(CircleShape)
                    .background(if (i <= currentValue) filledColor else Color.Transparent)
                    .border(
                        1.dp,
                        if (i <= currentValue) borderColor else MaterialTheme.colorScheme.onSurface.copy(
                            alpha = 0.3f
                        ),
                        CircleShape
                    )
                    .clickable { onValueChange(i) }
            )
        }
    }
}


@Composable
fun InteractiveBackgroundDots(
    currentValue: Int,
    minValue: Int,
    maxValue: Int,
    onValueChange: (Int) -> Unit
) {
    if (minValue == maxValue) return

    Row {
        for (i in minValue..maxValue) {
            Box(
                modifier = Modifier
                    .size(22.dp)
                    .padding(2.dp)
                    .clip(CircleShape)
                    .background(if (i <= currentValue) MaterialTheme.colorScheme.tertiary else Color.Transparent)
                    .border(
                        1.dp,
                        if (i <= currentValue) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(
                            alpha = 0.3f
                        ),
                        CircleShape
                    )
                    .clickable { onValueChange(i) }
            )
        }
    }
}