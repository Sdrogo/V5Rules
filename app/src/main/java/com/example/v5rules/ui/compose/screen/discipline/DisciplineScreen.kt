package com.example.v5rules.ui.compose.screen.discipline

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.v5rules.R
import com.example.v5rules.data.Discipline
import com.example.v5rules.ui.compose.component.CommonScaffold
import com.example.v5rules.ui.compose.component.DisciplineIcon
import com.example.v5rules.ui.viewModel.DisciplineViewModel
import com.example.v5rules.ui.viewModel.DisciplineUiState
import com.example.v5rules.DisciplineDetailsNav

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DisciplineScreen(
    viewModel: DisciplineViewModel,
    navController: NavHostController
) {
    val uiState by viewModel.disciplineUiState.collectAsState()

    CommonScaffold(
        navController = navController,
        title = stringResource(id = R.string.discipline_screen_title)
    ) {
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
                        val widthByOrientation = if (orientation == Configuration.ORIENTATION_LANDSCAPE) 0.4f else 1f
                        val maxRowItem = if (orientation == Configuration.ORIENTATION_LANDSCAPE) 2 else 1

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
}


@Composable
fun DisciplineItem(
    discipline: Discipline,
    navController: NavHostController,
    maxWidth: Float = 1f
) {
    Row(modifier = Modifier
        .fillMaxWidth(maxWidth)
        .padding(vertical = 8.dp)
        .fillMaxWidth(maxWidth)
        .clickable { navController.navigate(DisciplineDetailsNav(discipline.id))}) {
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