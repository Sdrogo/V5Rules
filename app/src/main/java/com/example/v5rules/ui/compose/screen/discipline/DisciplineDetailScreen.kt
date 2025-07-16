package com.example.v5rules.ui.compose.screen.discipline

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.v5rules.DisciplinePowerNav
import com.example.v5rules.R
import com.example.v5rules.RitualNav
import com.example.v5rules.data.Discipline
import com.example.v5rules.data.DisciplinePower
import com.example.v5rules.data.Ritual
import com.example.v5rules.ui.compose.component.CommonScaffold
import com.example.v5rules.ui.compose.component.ContentExpander
import com.example.v5rules.ui.compose.component.DisciplineIcon
import com.example.v5rules.ui.compose.component.DotsForLevel
import com.example.v5rules.ui.compose.component.TextBlock
import com.example.v5rules.ui.compose.component.TextBlockList
import com.example.v5rules.viewModel.DisciplineViewModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DisciplineDetailScreen(
    disciplineId: String, viewModel: DisciplineViewModel, navController: NavHostController
) {
    val discipline = viewModel.allDisciplines.find { it.id == disciplineId }
    val orientation = LocalConfiguration.current.orientation
    val isLandscape = orientation == Configuration.ORIENTATION_LANDSCAPE
    CommonScaffold(navController = navController, title = discipline?.title ?: "") {

        if (discipline != null) {
            LazyColumn(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .wrapContentWidth()
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
                        DisciplineInfo(
                            discipline, orientation == Configuration.ORIENTATION_LANDSCAPE
                        )
                    }
                }
                item {
                    FlowRow(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        maxItemsInEachRow = 3
                    ) {
                        discipline.disciplinePowers.groupBy { it.level }.entries.toList()
                            .distinctBy { it.value.first().id }.forEach { (level, subDisciplines) ->
                                DisciplineList(
                                    level = level,
                                    subDisciplines = subDisciplines,
                                    disciplineId = disciplineId,
                                    navController = navController,
                                    isLandscape = isLandscape
                                )
                            }
                    }
                }
                item {
                    if (discipline.rituals.isNotEmpty()) RitualInfo(disciplineId = disciplineId)
                }
                item {
                    discipline.rituals.let { ritualsList ->
                        FlowRow(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(8.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            maxItemsInEachRow = 3
                        ) {
                            ritualsList.groupBy { it.level }.entries.toList()
                                .forEach { (level, rituals) ->
                                    RitualsList(
                                        level = level,
                                        rituals = rituals,
                                        disciplineId = disciplineId,
                                        navController = navController,
                                        isLandscape = isLandscape
                                    )
                                }
                        }
                    }
                }
            }
        } else {
            Text(text = "Discipline not found", style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SubDisciplineItem(
    disciplinePower: DisciplinePower,
    disciplineId: String,
    navController: NavHostController,
    exclusiveClan: String? = null,
    amalgama: String? = null,
    isLandscape: Boolean = false
) {
    val widthValue = if (isLandscape && !isTablet()) 0.3f else 1f
    FlowRow(verticalArrangement = Arrangement.Center,
        horizontalArrangement = Arrangement.SpaceBetween,
        maxItemsInEachRow = if (isLandscape&& !isTablet()) 1 else 3,
        modifier = Modifier
            .background(color = MaterialTheme.colorScheme.background)
            .padding(8.dp)
            .fillMaxWidth(widthValue)
            .clickable {
                navController.navigate(
                    DisciplinePowerNav(
                        disciplineId, disciplinePower.id
                    )
                )
            }) {
        Text(
            text = disciplinePower.title,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.wrapContentWidth(),
        )
        exclusiveClan?.let {
            Text(
                text = it,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.tertiary,
                modifier = Modifier.wrapContentWidth()
            )
        }
        amalgama?.let {
            Text(
                text = it,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.tertiary,
                modifier = Modifier.wrapContentWidth()
            )
        }
    }
}

@Composable
fun RitualItem(
    ritual: Ritual,
    disciplineId: String,
    navController: NavHostController,
    isLandscape: Boolean = false
) {

    val widthValue = if (isLandscape) 0.3f else 1f
    Column(modifier = Modifier
        .background(color = MaterialTheme.colorScheme.background)
        .fillMaxWidth(widthValue)
        .padding(8.dp)
        .clickable { navController.navigate(RitualNav(disciplineId, ritual.id)) }) {
        Text(
            text = ritual.title,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun DisciplineInfo(discipline: Discipline, orientation: Boolean = false) {
    val widthByOrientation = if (orientation) 0.3f else 1f
    Column(modifier = Modifier.fillMaxWidth(widthByOrientation)) {
        TextBlock(
            title = stringResource(id = if (discipline.id == "d11") R.string.discipline_alchemy_pseudo else R.string.discipline_type),
            component = discipline.type,
            isHidden = discipline.type.isEmpty()
        )
        TextBlock(
            title = stringResource(id = R.string.discipline_description),
            component = discipline.description,
            isHidden = discipline.description.isEmpty()
        )
        TextBlock(
            title = stringResource(id = if (discipline.id == "d11") R.string.discipline_ingredients else R.string.discipline_masquerade_threat),
            component = discipline.masquerade,
            isHidden = discipline.masquerade.isEmpty()
        )
        TextBlock(
            title = stringResource(id = if (discipline.id == "d11") R.string.discipline_alchemy_learning else R.string.discipline_resonance),
            component = discipline.resonance,
            isHidden = discipline.resonance.isEmpty()
        )
        TextBlockList(
            title = stringResource(id = R.string.discipline_clan_affinity),
            component = discipline.clanAffinity,
            isHidden = discipline.description.isEmpty()
        )
    }
}


@Composable
fun DisciplineList(
    level: Int,
    subDisciplines: List<DisciplinePower>,
    disciplineId: String,
    navController: NavHostController,
    isLandscape: Boolean = false
) {
    var expanded by remember { mutableStateOf(false) }
    Column {
        DotsForLevel(level = level, (isLandscape && !isTablet())) { expanded = !expanded }
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
                        Box(modifier = Modifier.fillMaxWidth(if(isLandscape && !isTablet())0.4f else 1f)) {
                            Text(
                                text = AnnotatedString(
                                    text,
                                    paragraphStyle = ParagraphStyle(textAlign = TextAlign.Justify)
                                ),
                                style = MaterialTheme.typography.bodyMedium,
                                fontSize = 16.sp,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                }
                subDisciplines.forEach { disciplinePower ->
                    SubDisciplineItem(
                        disciplinePower,
                        disciplineId,
                        navController,
                        disciplinePower.exclusiveClan,
                        disciplinePower.amalgama,
                        isLandscape
                    )
                }
            }
        }
    }
}

@Composable
fun RitualInfo(disciplineId: String) {
    if (disciplineId == "d9") {
        Column {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(id = R.string.discipline_rituals),
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
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
    }
    if (disciplineId == "d10") {
        Column {
            Text(
                text = stringResource(id = R.string.discipline_ceremonies_oblivion),
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
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
}

@Composable
fun RitualsList(
    level: Int,
    rituals: List<Ritual>,
    disciplineId: String,
    navController: NavHostController,
    isLandscape: Boolean = false
) {

    var expanded by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        DotsForLevel(level = level, isLandscape) { expanded = !expanded }
        AnimatedVisibility(visible = expanded) {
            Column(modifier = Modifier.padding(8.dp)) {
                rituals.forEach { rituals ->
                    RitualItem(rituals, disciplineId, navController, isLandscape)
                }
            }
        }
    }
}

@Composable
fun isTablet(): Boolean {
    val configuration = LocalConfiguration.current
    val smallestScreenWidthDp = configuration.smallestScreenWidthDp
    return smallestScreenWidthDp >= 600 // You can adjust this threshold
}
