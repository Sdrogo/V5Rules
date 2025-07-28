package com.example.v5rules.ui.compose.screen.discipline

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.v5rules.DisciplineDetailsNav
import com.example.v5rules.R
import com.example.v5rules.data.Discipline
import com.example.v5rules.ui.compose.component.DisciplineIcon
import com.example.v5rules.viewModel.DisciplineUiState
import com.example.v5rules.viewModel.DisciplineViewModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DisciplineScreen(
    viewModel: DisciplineViewModel,
    navController: NavHostController,
    onTitleChanged: (String) -> Unit
) {
    val uiState by viewModel.disciplineUiState.collectAsState()
    val title = stringResource(id = R.string.discipline_screen_title)
    LaunchedEffect(Unit) {
        onTitleChanged(title)
    }
    when (uiState) {
        is DisciplineUiState.Loading -> Text("Loading...")
        is DisciplineUiState.Success -> {
            val disciplines = (uiState as DisciplineUiState.Success).disciplines
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
                    .background(color = MaterialTheme.colorScheme.background)
            ) {
                item {
                    val orientation = LocalConfiguration.current.orientation
                    val widthByOrientation =
                        if (orientation == Configuration.ORIENTATION_LANDSCAPE) 0.4f else 1f
                    val maxRowItem =
                        if (orientation == Configuration.ORIENTATION_LANDSCAPE) 2 else 1

                    FlowRow(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceAround,
                        maxItemsInEachRow = maxRowItem
                    ) {
                        disciplines.forEach {
                            DisciplineItem(
                                discipline = it,
                                navController = navController,
                                maxWidth = widthByOrientation
                            )
                        }
                    }
                }
            }
        }

        is DisciplineUiState.Error -> Text("Error: ${(uiState as DisciplineUiState.Error).message}")
    }
}


@Composable
fun DisciplineItem(
    discipline: Discipline,
    navController: NavHostController,
    maxWidth: Float = 1f
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(maxWidth)
            .padding(vertical = 8.dp)
            .fillMaxWidth(maxWidth)
            .clickable { navController.navigate(DisciplineDetailsNav(discipline.id)) }) {
        DisciplineIcon(
            disciplineId = discipline.id,
            contentDescription = discipline.title,
            size = 40.dp
        )
        Text(
            text = discipline.title,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .wrapContentWidth()
                .padding(horizontal = 8.dp)
        )
    }
}