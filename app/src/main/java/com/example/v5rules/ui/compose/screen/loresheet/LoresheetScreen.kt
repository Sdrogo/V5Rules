package com.example.v5rules.ui.compose.screen.loresheet

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.v5rules.LoresheetDetailsNav
import com.example.v5rules.R
import com.example.v5rules.data.Loresheet
import com.example.v5rules.ui.compose.component.CommonScaffold
import com.example.v5rules.ui.viewModel.DisciplineUiState
import com.example.v5rules.ui.viewModel.LoresheetUiState
import com.example.v5rules.ui.viewModel.LoresheetViewModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun LoresheetScreen(loresheetViewModel: LoresheetViewModel, navController: NavHostController) {
    val uiState by loresheetViewModel.loresheetUiState.collectAsState()
    CommonScaffold(
        navController = navController,
        title = stringResource(id = R.string.loresheet_title_screen)
    ) {
        when (uiState) {
            is LoresheetUiState.Loading -> Text("Loading...")
            is LoresheetUiState.Success -> {
                val loresheets = (uiState as LoresheetUiState.Success).loresheets
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
                            loresheets.forEach {
                                LoresheetLineItem(
                                    loresheet = it,
                                    navController = navController,
                                    maxWidth = widthByOrientation
                                )
                            }
                        }
                    }
                }
            }

            is LoresheetUiState.Error -> Text("Error: ${(uiState as LoresheetUiState.Error).message}")
        }
    }
}

@Composable
fun LoresheetLineItem(loresheet: Loresheet, navController: NavHostController, maxWidth: Float) {
    Column(modifier = Modifier
        .fillMaxWidth(maxWidth)
        .padding(vertical = 8.dp)
        .fillMaxWidth(maxWidth)
        .clickable {
            navController.navigate(
                LoresheetDetailsNav(
                    name = loresheet.title,
                    id = loresheet.id
                )
            )
        }) {
        Text(
            text = loresheet.title,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .wrapContentWidth()
                .padding(horizontal = 8.dp)
        )
        loresheet.limitation?.let {
            Text(
                text = it,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.tertiary,
                modifier = Modifier
                    .wrapContentWidth()
                    .padding(horizontal = 8.dp)
            )
        }
    }
}
