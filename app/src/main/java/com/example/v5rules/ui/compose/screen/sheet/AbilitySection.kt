package com.example.v5rules.ui.compose.screen.sheet

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.v5rules.R
import com.example.v5rules.data.Ability
import com.example.v5rules.data.Character
import com.example.v5rules.utils.CharacterSheetEvent
import com.example.v5rules.viewModel.CharacterSheetViewModel

@Composable
fun AbilitySection(character: Character, viewModel: CharacterSheetViewModel) {

    val pagerState = rememberPagerState(pageCount = { 3 })

    LazyColumn {
        item {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.padding(horizontal = 8.dp),
                contentPadding = PaddingValues(horizontal = 16.dp)
            ) { page ->
                when (page) {
                    0 -> {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                                .border(
                                    width = 1.dp,
                                    color = MaterialTheme.colorScheme.tertiary,
                                    shape = RoundedCornerShape(8.dp)
                                )
                        ) {
                            Column(modifier = Modifier.padding(8.dp)) {
                                Text(
                                    text = stringResource(R.string.character_screen_physical),
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .wrapContentWidth(Alignment.CenterHorizontally)
                                        .padding(bottom = 8.dp)
                                )

                                val physicalAbilities = listOf(
                                    stringResource(R.string.character_screen_abilities_athletics),
                                    stringResource(R.string.character_screen_abilities_brawl),
                                    stringResource(R.string.character_screen_abilities_craft),
                                    stringResource(R.string.character_screen_abilities_drive),
                                    stringResource(R.string.character_screen_abilities_firearms),
                                    stringResource(R.string.character_screen_abilities_larceny),
                                    stringResource(R.string.character_screen_abilities_melee),
                                    stringResource(R.string.character_screen_abilities_stealth),
                                    stringResource(R.string.character_screen_abilities_survival),
                                ).sorted()
                                physicalAbilities.forEach { abilityName ->
                                    val ability =
                                        character.abilities.firstOrNull { it.name == abilityName }
                                            ?: Ability(name = abilityName, level = 0)
                                    InteractiveAbilityRow(ability, viewModel)
                                }
                            }
                        }
                    }
                    1 -> {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                                .border(
                                    width = 1.dp,
                                    color = MaterialTheme.colorScheme.tertiary,
                                    shape = RoundedCornerShape(8.dp)
                                )
                        ) {
                            Column(modifier = Modifier.padding(8.dp)) {
                                Text(
                                    text = stringResource(R.string.character_screen_social),
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .wrapContentWidth(Alignment.CenterHorizontally)
                                        .padding(bottom = 8.dp)
                                )

                                val socialAbilities = listOf(
                                    stringResource(R.string.character_screen_abilities_animal_ken),
                                    stringResource(R.string.character_screen_abilities_etiquette),
                                    stringResource(R.string.character_screen_abilities_insight),
                                    stringResource(R.string.character_screen_abilities_intimidation),
                                    stringResource(R.string.character_screen_abilities_leadership),
                                    stringResource(R.string.character_screen_abilities_performance),
                                    stringResource(R.string.character_screen_abilities_persuasion),
                                    stringResource(R.string.character_screen_abilities_streetwise),
                                    stringResource(R.string.character_screen_abilities_subterfuge),
                                ).sorted()
                                socialAbilities.forEach { abilityName ->
                                    val ability =
                                        character.abilities.firstOrNull { it.name == abilityName }
                                            ?: Ability(name = abilityName, level = 0)
                                    InteractiveAbilityRow(ability, viewModel)
                                }
                            }
                        }
                    }
                    2 -> {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                                .border(
                                    width = 1.dp,
                                    color = MaterialTheme.colorScheme.tertiary,
                                    shape = RoundedCornerShape(8.dp)
                                )
                        ) {
                            Column(modifier = Modifier.padding(8.dp)) {
                                Text(
                                    text = stringResource(R.string.character_screen_mental),
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .wrapContentWidth(Alignment.CenterHorizontally)
                                        .padding(bottom = 8.dp)
                                )

                                val mentalAbilities = listOf(
                                    stringResource(R.string.character_screen_abilities_academics),
                                    stringResource(R.string.character_screen_abilities_awareness),
                                    stringResource(R.string.character_screen_abilities_finance),
                                    stringResource(R.string.character_screen_abilities_investigation),
                                    stringResource(R.string.character_screen_abilities_medicine),
                                    stringResource(R.string.character_screen_abilities_occult),
                                    stringResource(R.string.character_screen_abilities_politics),
                                    stringResource(R.string.character_screen_abilities_science),
                                    stringResource(R.string.character_screen_abilities_technology),
                                ).sorted()
                                mentalAbilities.forEach { abilityName ->
                                    val ability =
                                        character.abilities.firstOrNull { it.name == abilityName }
                                            ?: Ability(name = abilityName, level = 0)
                                    InteractiveAbilityRow(ability, viewModel)
                                }
                            }
                        }
                    }
                }
            }
        }
        item {
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                repeat(pagerState.pageCount) { iteration ->
                    val color = if (pagerState.currentPage == iteration) {
                        MaterialTheme.colorScheme.tertiary
                    } else {
                        Color.LightGray
                    }
                    Box(
                        modifier = Modifier
                            .padding(8.dp)
                            .clip(CircleShape)
                            .background(color)
                            .size(8.dp)
                    )
                }
            }
        }
        item {
            Text(text = stringResource(R.string.ability_distribution))
        }
    }
}

@Composable
fun InteractiveAbilityRow(
    ability: Ability,
    viewModel: CharacterSheetViewModel,
    maxDots: Int = 5,
    dotSize: Dp = 20.dp,
    textStyle: TextStyle = MaterialTheme.typography.bodyMedium,
) {
    var specializationText by remember(ability.specialization) { mutableStateOf(ability.specialization ?: "") }
    var isExpanded by remember { mutableStateOf(false) }
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    Column{
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = ability.name, style = textStyle, modifier = Modifier.weight(1f))
            Row(horizontalArrangement = Arrangement.End) {
                for (i in 1..maxDots) {
                    Box(
                        modifier = Modifier
                            .size(dotSize)
                            .padding(2.dp)
                            .clip(CircleShape)
                            .background(
                                if (i <= ability.level) MaterialTheme.colorScheme.tertiary else Color.Transparent
                            )
                            .border(
                                width = 1.dp,
                                color = if (i <= ability.level) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(
                                    alpha = 0.3f
                                ),
                                shape = CircleShape
                            )
                            .clickable {
                                val newLevel = if (i == ability.level) 0 else i
                                viewModel.onEvent(
                                    CharacterSheetEvent.AbilityChanged(ability.name, newLevel)
                                )
                            }
                    )
                }
            }
            IconButton(onClick = { isExpanded = !isExpanded }) {
                Icon(
                    imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = if (isExpanded) stringResource(R.string.collapse) else stringResource(R.string.expand)
                )
            }
        }

        ability.specialization?.let {
            if (it.isNotEmpty()) {
                Text(
                    text = stringResource(R.string.specialization, it),
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(start = 8.dp, top = 2.dp)
                )
            }
        }

        if (isExpanded) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextField(
                    value = specializationText,
                    onValueChange = { specializationText = it },
                    label = { Text(stringResource(R.string.add_specialization)) },
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                    textStyle = MaterialTheme.typography.bodySmall,
                    trailingIcon = {
                        IconButton(onClick = {
                            viewModel.onEvent(
                                CharacterSheetEvent.AbilitySpecializationChanged(
                                    ability.name,
                                    specializationText
                                )
                            )
                            isExpanded = false // Collapse after submitting
                            keyboardController?.hide()
                            focusManager.clearFocus()
                        }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.Send,
                                contentDescription = stringResource(R.string.add_specialization)
                            )
                        }
                    }
                )
            }
        }
    }
}