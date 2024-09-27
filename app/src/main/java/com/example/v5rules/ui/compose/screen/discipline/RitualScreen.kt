package com.example.v5rules.ui.compose.screen.discipline

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import com.example.v5rules.data.Ritual
import com.example.v5rules.ui.compose.component.CommonScaffold
import com.example.v5rules.ui.compose.component.TableContent
import com.example.v5rules.ui.compose.component.TextBlock
import com.example.v5rules.ui.viewModel.DisciplineViewModel

@Composable
fun RitualScreen(
    viewModel: DisciplineViewModel,
    navController: NavHostController,
    disciplineId: String,
    ritualId: String
) {

    val discipline = viewModel.allDisciplines.find { it.id == disciplineId }
    val ritual = discipline?.rituals?.find { it.id == ritualId }
    CommonScaffold(navController = navController, title = ritual?.title ?: "") {
        Column(modifier = Modifier.background(color = MaterialTheme.colorScheme.secondary)) {
            if (ritual != null) {
                LazyColumn(modifier = Modifier.padding(8.dp)) { // Wrap content in LazyColumn
                    item { // Use 'item' to add individual composables to the LazyColumn
                        DisciplineInfo(ritual = ritual)
                    }
                }
            }
        }
    }
}


@Composable
fun DisciplineInfo(ritual: Ritual) {
    Column {
        TextBlock(
            title = stringResource(id = R.string.discipline_rituals_description),
            component = ritual.description,
            isHidden = ritual.description.isEmpty()
        )
        Spacer(modifier = Modifier.height(8.dp))
        Box(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .border(
                1.dp,
                MaterialTheme.colorScheme.tertiary,
                RoundedCornerShape(8.dp)
            )
        ) {
            Column(
                modifier = Modifier.padding(8.dp)
            ) {
                TextBlock(
                    title = stringResource(id = R.string.discipline_rituals_ingredients),
                    component = ritual.ingredients,
                    isHidden = ritual.ingredients.isEmpty()
                )
                ritual.execution?.let {
                    TextBlock(
                        title = stringResource(id = R.string.discipline_rituals_execution),
                        component = it,
                        isHidden = it.isEmpty()
                    )
                }
                ritual.system?.let {
                    TextBlock(
                        title = stringResource(id = R.string.discipline_rituals_system),
                        component = it,
                        isHidden = it.isEmpty()
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        if (ritual.table != null) {
            TableContent(headerList = ritual.table.headers, contentList = ritual.table.columns)
        }
    }
}

