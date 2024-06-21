package com.example.v5rules.ui.compose.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.v5rules.R
import com.example.v5rules.data.SubDiscipline
import com.example.v5rules.ui.compose.component.ContentExpander
import com.example.v5rules.ui.compose.component.RemoteIcon
import com.example.v5rules.ui.viewModel.DisciplineViewModel

@Composable
fun DisciplineDetailScreen(
    disciplineId: String,
    viewModel: DisciplineViewModel,
    navController: NavHostController
) {
    val discipline = viewModel.allDisciplines.find { it.id == disciplineId }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Button(
            onClick = { navController.navigateUp() },
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            Text("Back")
        }

        if (discipline != null) {
            LazyColumn(modifier = Modifier.padding(16.dp)) {
                item {
                    Row (verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(bottom = 16.dp)){
                        Text(
                            text = discipline.title,
                            style = MaterialTheme.typography.headlineLarge,
                        )
                        Spacer(modifier = Modifier.width(48.dp))
                        RemoteIcon(
                            imageUrl = discipline.imageUrl,
                            contentDescription = discipline.title,
                            size = 128.dp
                        )
                    }
                    Text(
                        text = stringResource(id = R.string.discipline_description),
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    Text(
                        text = discipline.description,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    Text(
                        text = stringResource(id = R.string.discipline_type),
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    Text(
                        text = discipline.type,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    var clanAffinity = ""
                    discipline.clanAffinity.forEach { clanAffinity = clanAffinity.plus(it).plus(", ")
                    }
                    clanAffinity.dropLast(2)
                    Text(
                         text = "${stringResource(id = R.string.discipline_clan_affinity)} $clanAffinity ",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    Text(
                        text = stringResource(id = R.string.discipline_resonance),
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    Text(
                        text = discipline.resonance,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                }

                items(discipline.subDisciplines.groupBy { it.level }.entries.toList()) { (level, subDisciplines) ->
                    var expanded by remember { mutableStateOf(false) }

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    ) {
                        Row (verticalAlignment = Alignment.CenterVertically){
                            Text(
                                text = "Level $level",
                                style = MaterialTheme.typography.headlineSmall,
                                modifier = Modifier
                                    .clickable { expanded = !expanded }
                                    .padding(8.dp)
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            for(i in 1..level)Text("●")
                            for(i in level..5)Text("○")
                        }

                        AnimatedVisibility(visible = expanded) {
                            Column(modifier = Modifier.padding(8.dp)) {
                                subDisciplines.forEach { subDiscipline ->
                                    SubDisciplineItem(subDiscipline)
                                }
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

@Composable
fun SubDisciplineItem(subDiscipline: SubDiscipline) {
    Column(modifier = Modifier.padding(8.dp)) {
        ContentExpander(title = subDiscipline.title, false) {
            SubDisciplineInfo(subDiscipline)
        }
    }
}

@Composable
fun SubDisciplineInfo(discipline: SubDiscipline){
    Column {
        if (discipline.amalgama != null) {
            Text(
                text = "${stringResource(id = R.string.discipline_amalgama)} ${discipline.amalgama}",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = discipline.description,
            style = MaterialTheme.typography.bodyMedium,
            fontSize = 16.sp,
            modifier = Modifier.padding(start= 16.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Box(
            modifier = Modifier.border(
                2.dp,
                MaterialTheme.colorScheme.primary,
                RoundedCornerShape(8.dp)
            )
        ) {
            Column(
                modifier = Modifier.padding(8.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.discipline_cost),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Text(
                    text = discipline.cost,
                    style = MaterialTheme.typography.bodyMedium,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(start = 16.dp)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = stringResource(id = R.string.discipline_dice_pool),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Text(
                    text = "${discipline.dicePool}",
                    style = MaterialTheme.typography.bodyMedium,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(start = 16.dp)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = stringResource(id = R.string.discipline_system),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Text(
                    text = discipline.system,
                    style = MaterialTheme.typography.bodyMedium,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(start = 16.dp)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = stringResource(id = R.string.discipline_duration),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Text(
                    text = discipline.duration,
                    style = MaterialTheme.typography.bodyMedium,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
    }
}
