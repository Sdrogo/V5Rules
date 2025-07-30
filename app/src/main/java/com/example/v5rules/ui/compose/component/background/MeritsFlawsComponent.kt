package com.example.v5rules.ui.compose.component.background

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.v5rules.R
import com.example.v5rules.data.Advantage
import com.example.v5rules.ui.compose.component.ContentExpander
import com.example.v5rules.utils.CharacterSheetEvent

@Composable
fun DirectFlawsList(
    flaws: List<Advantage>,
    onEvent: (CharacterSheetEvent) -> Unit
) {
    Column {
        flaws.forEach { characterDirectFlaw ->
            AdvantageItem(
                characterDirectFlaw = characterDirectFlaw,
                onEvent = onEvent
            )
        }
    }
}

@Composable
fun AdvantageItem(
    characterDirectFlaw: Advantage,
    onEvent: (CharacterSheetEvent) -> Unit
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
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = characterDirectFlaw.title,
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier
                    .weight(1f)
                    .clickable { expanded = !expanded }
            )
            Spacer(Modifier.width(8.dp))
            InteractiveBackgroundDots(
                currentValue = characterDirectFlaw.level ?: 1,
                minValue = characterDirectFlaw.minLevel ?: 1,
                maxValue = characterDirectFlaw.maxLevel ?: 5,
                onValueChange = { newLevel ->
                    onEvent(CharacterSheetEvent.CharacterDirectFlawLevelChanged(characterDirectFlaw, newLevel))
                }
            )

            Spacer(Modifier.width(8.dp))
            IconButton(onClick = { onEvent(CharacterSheetEvent.CharacterDirectFlawRemoved(characterDirectFlaw)) })
            {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = stringResource(R.string.remove_stuff, characterDirectFlaw.title),
                    tint = MaterialTheme.colorScheme.error
                )
            }
            Spacer(Modifier.width(8.dp))
            IconButton(onClick = { expanded = !expanded }) {
                Icon(
                    imageVector = if (expanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                    contentDescription = if (expanded) "Collapse" else "Expand"
                )
            }
        }

        if (expanded) {
            Column(modifier = Modifier.padding(start = 8.dp, end = 8.dp, top = 4.dp)) {
                characterDirectFlaw.note?.let {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = it, modifier = Modifier.weight(1f))
                        IconButton(onClick = { onEvent(CharacterSheetEvent.RemoveNoteToDirectFlaw(characterDirectFlaw)) }) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = stringResource(R.string.delete_note)
                            )
                        }
                    }
                }
                if (characterDirectFlaw.note == null) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TextField(
                            value = noteText,
                            onValueChange = { noteText = it },
                            label = { Text(stringResource(R.string.add_note)) },
                            modifier = Modifier.weight(1f),
                            singleLine = true,
                            trailingIcon = {
                                IconButton(onClick = {
                                    onEvent(CharacterSheetEvent.AddNoteToDirectFlaw(characterDirectFlaw, noteText))
                                    keyboardController?.hide()
                                    focusManager.clearFocus()
                                }) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.Send,
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


@Composable
fun DirectFlawsListVisualization(
    flaws: List<Advantage>
) {
    Column {
        flaws.forEach { characterDirectFlaw ->
            AdvantageItemVisualization(
                characterDirectFlaw = characterDirectFlaw
            )
        }
    }
}

@Composable
fun AdvantageItemVisualization(
    characterDirectFlaw: Advantage
) {
    var expanded by remember { mutableStateOf(false) }

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
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.weight(1f)
                )
                Icon(
                        imageVector = if (expanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                contentDescription = if (expanded) stringResource(R.string.collapse, characterDirectFlaw.title) else stringResource(R.string.expand_stuff, characterDirectFlaw.title)
                )
                Spacer(Modifier.width(8.dp))
                if ((characterDirectFlaw.level ?: 1) > 0 &&
                    characterDirectFlaw.minLevel == null ||
                    (characterDirectFlaw.level ?: 1) <= (characterDirectFlaw.maxLevel ?: 5)
                ) {
                    // Manteniamo i DotsWithMinMax qui perché è solo visualizzazione
                    DotsWithMinMax(
                        level = (characterDirectFlaw.level ?: 1),
                        maxLevel = (characterDirectFlaw.maxLevel ?: 5)
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
                    characterDirectFlaw.note?.let {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = it)
                        }
                    }
                    characterDirectFlaw.note?.let { Text(text = it) }
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

// Assumo che DotsWithMinMax sia definito da qualche parte, se non lo è, questa è una possibile implementazione
@Composable
fun DotsWithMinMax(level: Int, maxLevel: Int) {
    Row {
        repeat(maxLevel) { index ->
            Box(
                modifier = Modifier
                    .size(16.dp)
                    .padding(1.dp)
                    .background(
                        if (index < level) MaterialTheme.colorScheme.secondary else Color.Transparent,
                        shape = CircleShape
                    )
                    .border(1.dp, MaterialTheme.colorScheme.primary, shape = CircleShape)
            )
        }
    }
}