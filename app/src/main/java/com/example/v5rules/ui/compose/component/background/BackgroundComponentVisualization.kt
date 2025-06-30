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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import com.example.v5rules.ui.compose.component.ContentExpander
import com.example.v5rules.ui.compose.component.DotsWithMinMax

@Composable
fun BackgroundListVisualization(
    backgrounds: List<Background>
) {
    Column {
        backgrounds.forEach { characterBackground ->
            BackgroundItemVisualization(
                characterBackground = characterBackground
            )
        }
    }
}

@Composable
fun BackgroundItemVisualization(
    characterBackground: Background
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
                    text = characterBackground.title,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.weight(1f)
                )
                if(characterBackground.maxLevel != null && characterBackground.maxLevel != 0){
                    DotsWithMinMax(
                        level = (characterBackground.level),
                        maxLevel = (characterBackground.maxLevel)
                    )
                }
                Spacer(Modifier.width(8.dp))
                Icon(
                    imageVector = if (expanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                    contentDescription = if (expanded) stringResource(
                        R.string.collapse,
                        characterBackground.title
                    ) else stringResource(R.string.expand_stuff, characterBackground.title)
                )
            }
        }
    }
    if (expanded) {
        Column {
            characterBackground.note?.let {
                Text(text = it)
            }
            ContentExpander(title = stringResource(R.string.discipline_description)) {
                Text(
                    text = characterBackground.description,
                    style = MaterialTheme.typography.bodySmall
                )
            }
            characterBackground.merits?.forEach { merit ->
                AdvantageDisplayItemVisualization(advantage = merit)
            }
            characterBackground.flaws?.forEach { flaw ->
                AdvantageDisplayItemVisualization(advantage = flaw)
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun AdvantageDisplayItemVisualization(
    advantage: Advantage
) {
    var itemExpanded by remember { mutableStateOf(false) }

    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { itemExpanded = !itemExpanded }
        ) {
            Text(
                text = advantage.title,
                style = MaterialTheme.typography.bodyLarge,
                color = if(advantage.isFlaw == true) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.primary,
                modifier = Modifier.weight(1f)
            )
            DotsWithMinMax(level = (advantage.level ?: 1), maxLevel = (advantage.maxLevel ?: 5))
            Spacer(Modifier.width(8.dp))
            Icon(
                imageVector = if (itemExpanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                contentDescription = if (itemExpanded) stringResource(
                    R.string.collapse,
                    advantage.title
                ) else stringResource(R.string.expand_stuff, advantage.title)
            )
        }

    }

    if (itemExpanded) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
        ) {
            Column {
                advantage.note?.let {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = it)
                    }
                }
                ContentExpander(title = stringResource(R.string.discipline_description)) {
                    Text(
                        text = advantage.description,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}