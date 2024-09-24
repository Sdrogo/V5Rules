package com.example.v5rules.ui.compose.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.v5rules.R
import com.example.v5rules.data.Discipline
import com.example.v5rules.data.DisciplinePower
import com.example.v5rules.ui.compose.component.CommonScaffold
import com.example.v5rules.ui.compose.component.DisciplineIcon
import com.example.v5rules.ui.compose.component.TableContent
import com.example.v5rules.ui.compose.component.TextBlock
import com.example.v5rules.ui.viewModel.DisciplineViewModel

@Composable
fun DisciplinePowerScreen(
    viewModel: DisciplineViewModel,
    navController: NavHostController,
    disciplineId: String,
    disciplinePowerId: String
) {

    val discipline = viewModel.allDisciplines.find { it.id == disciplineId }
    val disciplinePower = discipline?.disciplinePowers?.find { it.id == disciplinePowerId }
    CommonScaffold(navController = navController, title = disciplinePower?.title ?: "") {
        Column( modifier = Modifier.background(color = MaterialTheme.colorScheme.secondary)){
            if (disciplinePower != null) {
                LazyColumn(modifier = Modifier.padding(horizontal = 16.dp)) { // Wrap content in LazyColumn
                    item { // Use 'item' to add individual composables to the LazyColumn
                        DisciplinePowerInfo(disciplinePower = disciplinePower, discipline = discipline)
                    }
                }
            }
        }
    }
}

@Composable
fun DisciplinePowerInfo(disciplinePower: DisciplinePower, discipline: Discipline) {
    Column(modifier = Modifier.background(MaterialTheme.colorScheme.secondary)) {
        Spacer(modifier = Modifier.padding(8.dp))
        TextBlock(
            title = stringResource(id = R.string.discipline_amalgama),
            component = disciplinePower.amalgama.orEmpty(),
            isHidden = disciplinePower.amalgama.isNullOrEmpty()
        )
        TextBlock(
            title = stringResource(id = R.string.discipline_description),
            component = disciplinePower.description,
            isHidden = disciplinePower.description.isEmpty()
        )
        Spacer(modifier = Modifier.height(8.dp))
        Box(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.secondary)
                .border(
                    1.dp,
                    MaterialTheme.colorScheme.tertiary,
                    RoundedCornerShape(8.dp)
                )
        ) {
            Column(
                modifier = Modifier
                    .padding(8.dp)
                    .background(color = MaterialTheme.colorScheme.secondary)
            ) {
                TextBlock(
                    title = stringResource(id = R.string.discipline_cost),
                    component = disciplinePower.cost.orEmpty(),
                    isHidden = disciplinePower.cost.isNullOrEmpty()
                )
                TextBlock(
                        title = stringResource(id = R.string.discipline_ingredients),
                component = disciplinePower.ingredients.orEmpty(),
                isHidden = disciplinePower.ingredients.isNullOrEmpty()
                )
                TextBlock(
                    title = stringResource(id = R.string.discipline_dice_pool),
                    component = disciplinePower.dicePool.orEmpty(),
                    isHidden = disciplinePower.dicePool.isNullOrEmpty()
                )
                TextBlock(
                    title = stringResource(id = R.string.discipline_system),
                    component = disciplinePower.system,
                    isHidden = disciplinePower.system.isEmpty()
                )
                TextBlock(
                    title = stringResource(id = R.string.discipline_duration),
                    component = disciplinePower.duration.orEmpty(),
                    isHidden = disciplinePower.duration.isNullOrEmpty()
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        if (disciplinePower.table != null) {
            TableContent(
                headerList = disciplinePower.table.headers,
                contentList = disciplinePower.table.columns
            )
        }
        Row{
            Spacer(modifier = Modifier.weight(1f))
            DisciplineIcon(
                disciplineId = discipline.id,
                contentDescription = discipline.title,
                size = 64.dp
            )
            Spacer(modifier = Modifier.weight(1f))
        }
        Spacer(modifier = Modifier.height(8.dp))

    }
}
