package com.example.v5rules.ui.compose.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.v5rules.R
import com.example.v5rules.data.Discipline
import com.example.v5rules.data.Ritual
import com.example.v5rules.data.DisciplinePower
import com.example.v5rules.ui.compose.component.CommonScaffold
import com.example.v5rules.ui.compose.component.ContentExpander
import com.example.v5rules.ui.compose.component.DisciplineIcon
import com.example.v5rules.ui.compose.component.TextBlock
import com.example.v5rules.ui.viewModel.DisciplineViewModel

@Composable
fun DisciplineDetailScreen(
    disciplineId: String,
    viewModel: DisciplineViewModel,
    navController: NavHostController
) {
    val discipline = viewModel.allDisciplines.find { it.id == disciplineId }
    CommonScaffold(navController = navController, title = discipline?.title ?: "") {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            if (discipline != null) {
                LazyColumn(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth()
                ) {
                    item {
                        Spacer(modifier = Modifier.padding(8.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(bottom = 16.dp)
                        ) {
                            Spacer(Modifier.weight(1f))
                            DisciplineIcon(
                                disciplineId = discipline.id,
                                contentDescription = discipline.title,
                                size = 128.dp
                            )
                            Spacer(Modifier.weight(1f))
                        }
                        ContentExpander(
                            stringResource(
                                id = R.string.discipline_characteristics
                            ),
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            initialState = false
                        ) {
                            DisciplineInfo(discipline)
                        }
                        if (!discipline.rituals.isNullOrEmpty())
                            RitualInfo(disciplineId = disciplineId)
                    }
                    items(
                        discipline.disciplinePowers.groupBy { it.level }.entries.toList()
                            .distinctBy { it.value.first().id }) { (level, subDisciplines) ->
                        DisciplineList(
                            level = level,
                            subDisciplines = subDisciplines,
                            disciplineId = disciplineId,
                            navController = navController
                        )
                    }
                    item {
                        if (disciplineId == "d9" || disciplineId == "d11") {
                            Text(
                                text = stringResource(id = if (disciplineId == "d9") R.string.discipline_rituals else R.string.discipline_ceremonies_oblivion),
                                style = MaterialTheme.typography.headlineMedium,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    discipline.rituals?.let { ritualsList ->
                        items(ritualsList.groupBy { it.level }.entries.toList()) { (level, rituals) ->
                            RitualsList(
                                level = level,
                                rituals = rituals,
                                disciplineId = disciplineId,
                                navController = navController
                            )
                        }
                    }
                }
            } else {
                Text(text = "Discipline not found", style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}

@Composable
fun SubDisciplineItem(
    disciplinePower: DisciplinePower,
    disciplineId: String,
    navController: NavHostController,
    exclusiveClan: String? = null,
    amalgama: String? = null

) {
    Column(modifier = Modifier
        .background(color = MaterialTheme.colorScheme.secondary)
        .padding(8.dp)
        .fillMaxWidth()
        .clickable { navController.navigate("discipline_detail_screen/${disciplineId}/${disciplinePower.id}") }) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = disciplinePower.title,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(8.dp),
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                if (exclusiveClan != null) {
                    Text(
                        text = exclusiveClan,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.tertiary,
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                }
                if (amalgama != null) {
                    Text(
                        text = amalgama,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.tertiary,
                    )
                }
            }
        }
    }
}

@Composable
fun RitualItem(ritual: Ritual, disciplineId: String, navController: NavHostController) {
    Column(
        modifier = Modifier
            .background(color = MaterialTheme.colorScheme.secondary)
            .padding(8.dp)
            .clickable { navController.navigate("discipline_ritual_screen/${disciplineId}/${ritual.id}") }) {
        Text(
            text = ritual.title,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun DisciplineInfo(discipline: Discipline) {
    Column {
        if (discipline.id == "d11") {
            TextBlock(
                title = stringResource(id = R.string.discipline_alchemy_pseudo),
                component = discipline.type,
                isHidden = discipline.type.isEmpty()
            )
            TextBlock(
                title = stringResource(id = R.string.discipline_description),
                component = discipline.description,
                isHidden = discipline.description.isEmpty()
            )
            TextBlock(
                title = stringResource(id = R.string.discipline_ingredients),
                component = discipline.masquerade,
                isHidden = discipline.masquerade.isEmpty()
            )
            TextBlock(
                title = stringResource(id = R.string.discipline_alchemy_learning),
                component = discipline.resonance,
                isHidden = discipline.resonance.isEmpty()
            )
        } else {
            TextBlock(
                title = stringResource(id = R.string.discipline_description),
                component = discipline.description,
                isHidden = discipline.description.isEmpty()
            )
            TextBlock(
                title = stringResource(id = R.string.discipline_type),
                component = discipline.type,
                isHidden = discipline.type.isEmpty()
            )
            TextBlock(
                title = stringResource(id = R.string.discipline_masquerade),
                component = discipline.masquerade,
                isHidden = discipline.masquerade.isEmpty()
            )
            var clanAffinity = ""
            discipline.clanAffinity.forEach {
                clanAffinity = clanAffinity.plus(it).plus(", ")
            }
            clanAffinity.dropLast(2)
            TextBlock(
                title = stringResource(id = R.string.discipline_clan_affinity),
                component = clanAffinity,
                isHidden = clanAffinity.isEmpty()
            )
            TextBlock(
                title = stringResource(id = R.string.discipline_resonance),
                component = discipline.resonance,
                isHidden = discipline.resonance.isEmpty()
            )
        }
    }
}


@Composable
fun DisciplineList(
    level: Int,
    subDisciplines: List<DisciplinePower>,
    disciplineId: String,
    navController: NavHostController
) {
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = !expanded }
        ) {
            Text(
                text = "Level $level",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .padding(8.dp)
            )
            Spacer(
                modifier = Modifier
                    .width(16.dp)
                    .weight(0.5f)
            )
            for (i in 1..level) Text(
                "●",
                color = MaterialTheme.colorScheme.tertiary,
            )
            for (i in level..5) Text(
                "○",
                color = MaterialTheme.colorScheme.tertiary,
            )
        }

        AnimatedVisibility(visible = expanded) {
            Column(modifier = Modifier.padding(8.dp)) {
                if (disciplineId == "d11") {
                    var text: String? = null
                    when (level) {
                        1 -> text = null
                        2 -> text = stringResource(R.string.alchemy_lvl2)
                        3 -> text = stringResource(R.string.alchemy_lvl3)
                        4 -> text = stringResource(R.string.alchemy_lvl4)
                        5 -> text = stringResource(R.string.alchemy_lvl5)
                    }
                    if (text != null) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = AnnotatedString(
                                stringResource(R.string.alchemy_lvl2),
                                paragraphStyle = ParagraphStyle(textAlign = TextAlign.Justify)
                            ),
                            style = MaterialTheme.typography.bodyMedium,
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
                subDisciplines.forEach { disciplinePower ->
                    SubDisciplineItem(
                        disciplinePower,
                        disciplineId,
                        navController,
                        disciplinePower.exclusiveClan,
                        disciplinePower.amalgama
                    )
                }
            }
        }
    }
}

@Composable
fun RitualInfo(disciplineId: String) {
    if (disciplineId == "d9") {
        ContentExpander(
            title = stringResource(id = R.string.discipline_rituals_rules),
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            false
        ) {
            Column {
                TextBlock(
                    title = stringResource(id = R.string.discipline_rituals_description),
                    component = stringResource(id = R.string.discipline_rituals_description_tremere),
                    isHidden = false
                )
                TextBlock(
                    title = stringResource(id = R.string.discipline_rituals_protection),
                    component = stringResource(id = R.string.discipline_rituals_protection_description),
                    isHidden = false
                )
                TextBlock(
                    title = stringResource(id = R.string.discipline_rituals_circle_protection),
                    component = stringResource(id = R.string.discipline_rituals_circle_protection_description),
                    isHidden = false
                )
            }

        }
    }
    if (disciplineId == "d10") {
        Spacer(modifier = Modifier.height(16.dp))
        ContentExpander(
            title = stringResource(id = R.string.discipline_ceremonies_oblivion_rules),
            style = MaterialTheme.typography.headlineSmall
        ) {
            Column {
                TextBlock(
                    title = stringResource(id = R.string.discipline_rituals_description),
                    component = stringResource(id = R.string.discipline_rituals_description_oblivion),
                    isHidden = false
                )
            }
        }
    }
}

@Composable
fun RitualsList(
    level: Int,
    rituals: List<Ritual>,
    disciplineId: String,
    navController: NavHostController
) {
    var expanded by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = !expanded }
        ) {
            Text(
                text = "Level $level",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .padding(8.dp)
            )
            Spacer(
                modifier = Modifier
                    .width(16.dp)
                    .weight(0.5f)
            )
            for (i in 1..level) Text(
                "●",
                color = MaterialTheme.colorScheme.tertiary
            )
            for (i in level..5) Text(
                "○",
                color = MaterialTheme.colorScheme.tertiary
            )
        }
        AnimatedVisibility(visible = expanded) {
            Column(modifier = Modifier.padding(8.dp)) {
                rituals.forEach { rituals ->
                    RitualItem(rituals, disciplineId, navController)
                }
            }
        }
    }
}
