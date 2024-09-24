package com.example.v5rules.ui.compose.screen

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import com.example.v5rules.ui.viewModel.ClanUiState
import com.example.v5rules.ui.viewModel.ClanViewModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ClanListScreen(
    viewModel: ClanViewModel,
    navController: NavHostController
) {
    val uiState by viewModel.clanUiState.collectAsState()

    CommonScaffold(navController = navController, title = "Clans") {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .background(color = MaterialTheme.colorScheme.secondary)
        ) {
            when (uiState) {
                is ClanUiState.Loading -> Text("Loading...")
                is ClanUiState.Success -> {
                    val clans = (uiState as ClanUiState.Success).clans
                    LazyColumn {
                        item {
                            val orientation = LocalConfiguration.current.orientation
                            if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                                FlowRow(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(8.dp),
                                    horizontalArrangement = Arrangement.SpaceEvenly,
                                ) {
                                    clans.forEach {
                                        ClanItem(
                                            clan = it,
                                            navController = navController
                                        )
                                    }
                                }
                            } else {
                                clans.forEach {
                                    ClanItem(
                                        clan = it,
                                        navController = navController,
                                        maxWidth = true
                                    )
                                }
                                Spacer(modifier = Modifier.height(16.dp))
                            }
                        }
                    }
                }
                is ClanUiState.Error -> Text("Error: ${(uiState as ClanUiState.Error).message}")
            }
        }
    }
}

@Composable
fun ClanItem(clan: Clan, navController: NavHostController, maxWidth: Boolean = false) {

    if(maxWidth){
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .background(color = MaterialTheme.colorScheme.secondary)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable { navController.navigate("clan_screen/${clan.name}") }) {
                ClanImage(
                    clanName = clan.name,
                    tintColor = MaterialTheme.colorScheme.tertiary,
                    width = 48.dp,
                )
                Text(
                    text = clan.name,
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .fillMaxWidth()
                )
            }
        }
    }else{
        Column(
            modifier = Modifier
                .wrapContentWidth()
                .padding(vertical = 8.dp)
                .background(color = MaterialTheme.colorScheme.secondary)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable { navController.navigate("clan_screen/${clan.name}") }) {
                ClanImage(
                    clanName = clan.name,
                    tintColor = MaterialTheme.colorScheme.tertiary,
                    width = 48.dp,
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
    }

}