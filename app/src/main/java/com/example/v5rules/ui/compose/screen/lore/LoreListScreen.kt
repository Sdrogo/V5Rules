package com.example.v5rules.ui.compose.screen.lore

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import com.example.v5rules.LoreDetailsNav
import com.example.v5rules.R
import com.example.v5rules.ui.compose.component.CommonScaffold
import com.example.v5rules.viewModel.LoreUiState
import com.example.v5rules.viewModel.LoreViewModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun LoreListScreen(loreViewModel : LoreViewModel, navController: NavHostController) {

    val uiState by loreViewModel.loreUiState.collectAsState()

    CommonScaffold(
        navController = navController,
        title = stringResource(id = R.string.lore_screen_title)
    )
    {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.background)
                .padding(start = 16.dp)
        ) {
            when (uiState) {
                is LoreUiState.Loading -> Text(
                    "Loading...",
                    color = MaterialTheme.colorScheme.primary,
                )

                is LoreUiState.Success -> {
                    val chapters = (uiState as LoreUiState.Success).chapters
                    LazyColumn(modifier = Modifier.fillMaxWidth()) {
                        item {
                            val orientation = LocalConfiguration.current.orientation
                            val widthByOrientation = if(orientation == Configuration.ORIENTATION_LANDSCAPE) 0.5f else 1f
                                FlowRow(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(8.dp),
                                    horizontalArrangement = Arrangement.Center,
                                ) {
                                    chapters.forEach {
                                        Text(
                                            text = it.title,
                                            style = MaterialTheme.typography.headlineSmall,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.primary,
                                            modifier = Modifier
                                                .fillMaxSize(widthByOrientation)
                                                .clickable { navController.navigate(LoreDetailsNav(it.title)) }
                                                .padding(8.dp)
                                        )
                                    }
                                }
                        }
                    }
                }

                is LoreUiState.Error -> Text(
                    "Error: ${(uiState as LoreUiState.Error).message}",
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}


