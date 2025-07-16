package com.example.v5rules.ui.compose.screen.loresheet

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.v5rules.data.LoresheetPower
import com.example.v5rules.ui.compose.component.CommonScaffold
import com.example.v5rules.ui.compose.component.CustomContentExpander
import com.example.v5rules.ui.compose.component.DotsOnlyForLevel
import com.example.v5rules.viewModel.LoresheetUiState
import com.example.v5rules.viewModel.LoresheetViewModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun LoresheetDetailsScreen(
    id: String,
    name: String,
    loresheetViewModel: LoresheetViewModel,
    navController: NavHostController,
) {
    CommonScaffold(
        navController = navController,
        title = name
    ) {
        val uiState by loresheetViewModel.loresheetUiState.collectAsState()
        val loresheet = (uiState as LoresheetUiState.Success).loresheets.first { it.id == id }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .background(color = MaterialTheme.colorScheme.background)
        ) {
            item {
                val orientation =
                    LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE
                val widthByOrientation =
                    if (orientation) 0.4f else 1f
                val maxRowItem = if (orientation) 2 else 1

                FlowRow(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 8.dp),
                    horizontalArrangement = Arrangement.SpaceAround,
                    maxItemsInEachRow = maxRowItem
                ) {
                    loresheet.limitation?.let { text ->
                        Text(
                            text = text,
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.tertiary,
                            modifier = Modifier.wrapContentSize()
                        )
                    }
                    Surface(
                        modifier = Modifier
                            .padding(8.dp)
                            .wrapContentSize()
                            .background(MaterialTheme.colorScheme.background)
                            .border(
                                1.dp,
                                MaterialTheme.colorScheme.tertiary,
                                RoundedCornerShape(8.dp)
                            ),
                        shape = RoundedCornerShape(8.dp)
                    ) {

                        Text(
                            text = loresheet.content,
                            Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                        )
                    }
                    loresheet.powers.forEach { power ->
                        LoresheetPower(
                            loresheetPower = power,
                            widthByOrientation = widthByOrientation,
                            isLandscape = orientation
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun LoresheetPower(
    loresheetPower: LoresheetPower,
    widthByOrientation: Float,
    isLandscape: Boolean = false
) {
    CustomContentExpander(
        maxWith = widthByOrientation,
        header = {
            LoresheetPowerLineItem(
                isLandscape = isLandscape,
                level = loresheetPower.level,
                name = loresheetPower.title
            )
        },
        content = {
            Text(
                text = loresheetPower.content,
                modifier = Modifier.fillMaxWidth()
            )
        }
    )
}

@Composable
fun LoresheetPowerLineItem(level: Int, name: String, isLandscape: Boolean = false) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isLandscape) Arrangement.Center else Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        DotsOnlyForLevel(level = level)
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = name,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.wrapContentWidth()
        )
    }
}