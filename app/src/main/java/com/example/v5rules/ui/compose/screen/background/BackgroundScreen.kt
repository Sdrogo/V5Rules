package com.example.v5rules.ui.compose.screen.background

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
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.v5rules.navigation.BackgroundDetailsNav
import com.example.v5rules.R
import com.example.v5rules.data.Background
import com.example.v5rules.ui.compose.component.TintedImage
import com.example.v5rules.viewModel.BackgroundUiState
import com.example.v5rules.viewModel.BackgroundViewModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun BackgroundScreen(
    backgroundViewModel: BackgroundViewModel, navController: NavHostController,
    onTitleChanged: (String) -> Unit
) {
    val uiState by backgroundViewModel.backgroundUiState.collectAsState()
    var searchText by remember { mutableStateOf("") }
    val filteredBackgrounds by backgroundViewModel.filteredBackgrounds.collectAsState()

    LaunchedEffect(Unit) {
        backgroundViewModel.updateSearchQuery("")
    }
    val title = stringResource(R.string.background_title_screen)
    LaunchedEffect(Unit) {
        onTitleChanged(title)
    }
    when (uiState) {
        is BackgroundUiState.Loading -> Text("Loading...")
        is BackgroundUiState.Success -> {
            Column {
                TextField(
                    value = searchText,
                    onValueChange = {
                        searchText = it
                        backgroundViewModel.updateSearchQuery(it)
                    },
                    shape = RoundedCornerShape(8.dp),
                    trailingIcon = {
                        TintedImage(
                            R.drawable.malkavian_symbol,
                            MaterialTheme.colorScheme.secondary,
                            48.dp
                        )
                    },
                    label = { Text("Search") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.primary,
                        unfocusedContainerColor = MaterialTheme.colorScheme.background,
                        focusedTextColor = MaterialTheme.colorScheme.secondary,
                        unfocusedIndicatorColor = MaterialTheme.colorScheme.secondary,
                        focusedLabelColor = MaterialTheme.colorScheme.secondary,
                        disabledContainerColor = MaterialTheme.colorScheme.secondary,
                        focusedIndicatorColor = MaterialTheme.colorScheme.secondary
                    )
                )
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
                                .fillMaxSize(),
                            horizontalArrangement = Arrangement.SpaceAround,
                            maxItemsInEachRow = maxRowItem
                        ) {
                            filteredBackgrounds.sortedBy { it.title }.forEach {
                                BackgroundLineItem(
                                    background = it,
                                    navController = navController,
                                    maxWidth = widthByOrientation
                                )
                            }
                        }
                    }
                }
            }

        }

        is BackgroundUiState.Error -> Text("Error: ${(uiState as BackgroundUiState.Error).message}")
    }
}

@Composable
fun BackgroundLineItem(background: Background, navController: NavHostController, maxWidth: Float) {
    Column(
        modifier = Modifier
            .fillMaxWidth(maxWidth)
            .background(color = MaterialTheme.colorScheme.background)
            .clickable {
                navController.navigate(
                    BackgroundDetailsNav(
                        name = background.title,
                        id = background.id
                    )
                )
            }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Text(
                text = background.title,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .wrapContentWidth()
                    .padding(horizontal = 8.dp)
            )
            background.prerequisites?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier
                        .wrapContentWidth()
                        .padding(horizontal = 8.dp)
                )
            }
        }
        background.directFlaws.forEach { flaw ->
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            ) {
                Text(
                    text = flaw.title,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier
                        .wrapContentWidth()
                        .padding(horizontal = 8.dp)
                )
                flaw.prerequisites?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier
                            .wrapContentWidth()
                            .padding(horizontal = 8.dp)
                    )
                }
            }
        }
    }
}