package com.example.v5rules.ui.compose.screen.clan

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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.v5rules.data.Clan
import com.example.v5rules.ui.compose.component.ClanImage
import com.example.v5rules.ui.compose.component.CommonScaffold
import com.example.v5rules.viewModel.ClanUiState
import com.example.v5rules.viewModel.ClanViewModel
import com.example.v5rules.ClanDetailsNav

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ClanListScreen(
    viewModel: ClanViewModel,
    navController: NavHostController
) {
    val uiState by viewModel.clanUiState.collectAsState()

    CommonScaffold(navController = navController, title = "Clans") {
        when (uiState) {
            is ClanUiState.Loading -> Text("Loading...")
            is ClanUiState.Success -> {
                val clans = (uiState as ClanUiState.Success).clans
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 8.dp)
                        .background(color = MaterialTheme.colorScheme.background)
                ) {
                    item {
                        val orientation = LocalConfiguration.current.orientation
                        val widthByOrientation =
                            if (orientation == Configuration.ORIENTATION_LANDSCAPE) 0.3f else 0.5f
                        val numberOfRow =
                            if (orientation == Configuration.ORIENTATION_LANDSCAPE) 3 else 2
                        FlowRow(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            maxItemsInEachRow = numberOfRow
                        ) {
                            clans.forEach {
                                ClanItem(
                                    clan = it,
                                    navController = navController,
                                    widthByOrientation
                                )
                            }
                        }
                    }
                }
            }

            is ClanUiState.Error -> Text("Error: ${(uiState as ClanUiState.Error).message}")
        }
    }
}

@Composable
fun ClanItem(clan: Clan, navController: NavHostController, maxWidth: Float = 1f) {

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth(maxWidth)
            .padding(vertical = 8.dp)
            .clickable { navController.navigate(ClanDetailsNav(clan.name)) }
    ) {
        ClanImage(
            clanName = clan.name,
            tintColor = MaterialTheme.colorScheme.tertiary,
            width = 40.dp,
        )
        Text(
            text = clan.name,
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .wrapContentWidth()
        )
    }

}