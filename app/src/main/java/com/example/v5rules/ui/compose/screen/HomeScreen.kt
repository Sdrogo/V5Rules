package com.example.v5rules.ui.compose.screen

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.v5rules.R
import com.example.v5rules.ui.compose.component.CommonScaffold
import com.example.v5rules.ui.compose.component.TintedImage
import com.example.v5rules.utils.ClansNav
import com.example.v5rules.utils.DisciplinesNav
import com.example.v5rules.utils.LoreNav
import com.example.v5rules.utils.NPCGeneratorNav
import com.example.v5rules.utils.PredatorTypesNav
import com.example.v5rules.utils.RulesNav

@Composable
fun HomeScreen(navController: NavHostController) {
    CommonScaffold(
        navController = navController, title = stringResource(id = R.string.app_name)
    ) {

        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TintedImage(
                R.drawable.logo_v5,
                MaterialTheme.colorScheme.primary,
                300.dp
            )
            Spacer(modifier = Modifier.weight(0.5f))
            LazyColumn(
                modifier = Modifier
                    .wrapContentSize()
                    .padding(16.dp)
                    .background(color = MaterialTheme.colorScheme.secondary),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item {
                    ButtonsBlock(navController = navController)
                }
            }
            Spacer(modifier = Modifier.weight(0.5f))
        }

    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ButtonsBlock(navController: NavHostController, modifier: Modifier = Modifier) {
    val orientation = LocalConfiguration.current.orientation
    val widthByOrientation = if (orientation == Configuration.ORIENTATION_LANDSCAPE) 0.3f else 1f
    FlowRow(
        modifier = modifier
            .fillMaxSize()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceAround,
    ) {
        Button(
            onClick = { navController.navigate(LoreNav) },
            modifier = Modifier
                .fillMaxWidth(widthByOrientation)
                .padding(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.tertiary, // Your desired button color
                contentColor = MaterialTheme.colorScheme.primary // Text color for contrast
            )
        ) {
            Text(text = stringResource(id = R.string.lore_screen_title))
        }
        Button(
            onClick = { navController.navigate(ClansNav) },
            modifier = Modifier
                .fillMaxWidth(widthByOrientation)
                .padding(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.tertiary, // Your desired button color
                contentColor = MaterialTheme.colorScheme.primary // Text color for contrast
            )
        ) {
            Text(text = stringResource(id = R.string.clan_screen_title))
        }
        Button(
            onClick = { navController.navigate(RulesNav) },
            modifier = Modifier
                .fillMaxWidth(widthByOrientation)
                .padding(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.tertiary, // Your desired button color
                contentColor = MaterialTheme.colorScheme.primary // Text color for contrast
            )
        ) {
            Text(text = stringResource(id = R.string.rules_screen_button_label))
        }
        Button(
            onClick = { navController.navigate(PredatorTypesNav) },
            modifier = Modifier
                .fillMaxWidth(widthByOrientation)
                .padding(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.tertiary, // Your desired button color
                contentColor = MaterialTheme.colorScheme.primary // Text color for contrast
            )
        ) {
            Text(text = stringResource(id = R.string.predator_type_screen_title))
        }
        Button(
            onClick = { navController.navigate(DisciplinesNav) },
            modifier = Modifier
                .fillMaxWidth(widthByOrientation)
                .padding(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.tertiary, // Your desired button color
                contentColor = MaterialTheme.colorScheme.primary // Text color for contrast
            )
        ) {
            Text(text = stringResource(id = R.string.discipline_screen_title))
        }
        Button(
            onClick = { navController.navigate(NPCGeneratorNav) },
            modifier = Modifier
                .fillMaxWidth(widthByOrientation)
                .padding(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.tertiary, // Your desired button color
                contentColor = MaterialTheme.colorScheme.primary // Text color for contrast
            )
        ) {
            Text(text = stringResource(id = R.string.npc_generator_title))
        }
    }
}
