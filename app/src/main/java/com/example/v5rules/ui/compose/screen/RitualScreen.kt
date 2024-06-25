package com.example.v5rules.ui.compose.screen

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.v5rules.R
import com.example.v5rules.data.Discipline
import com.example.v5rules.data.Ritual
import com.example.v5rules.ui.compose.component.CommonScaffold
import com.example.v5rules.ui.compose.component.RemoteIcon
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
        Column(
            modifier = Modifier
                .padding(it)
        )
        {
            if (ritual != null) {
                LazyColumn(modifier = Modifier.padding(16.dp)) { // Wrap content in LazyColumn
                    item { // Use 'item' to add individual composables to the LazyColumn
                        RitualInfo(ritual = ritual, discipline = discipline)
                    }
                }
            }
        }
    }
}


@Composable
fun RitualInfo(ritual: Ritual, discipline: Discipline) {
    Column {
       /* Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            RemoteIcon(
                imageUrl = discipline.imageUrl,
                contentDescription = discipline.title,
                size = 128.dp
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(text = ritual.title, style = MaterialTheme.typography.headlineMedium)
        }
        Spacer(modifier = Modifier.height(16.dp))*/
        TextBlock(
            title = stringResource(id = R.string.discipline_rituals_description),
            component = ritual.description,
            isHidden = ritual.description.isEmpty()
        )
        Spacer(modifier = Modifier.height(8.dp))
        Box(
            modifier = Modifier.border(
                1.dp,
                MaterialTheme.colorScheme.primary,
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

