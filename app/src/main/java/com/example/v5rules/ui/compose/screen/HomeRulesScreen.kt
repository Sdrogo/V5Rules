package com.example.v5rules.ui.compose.screen

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.v5rules.R
import com.example.v5rules.navigation.BackgroundNav
import com.example.v5rules.navigation.ClansNav
import com.example.v5rules.navigation.DisciplinesNav
import com.example.v5rules.navigation.KindredNav
import com.example.v5rules.navigation.LoreNav
import com.example.v5rules.navigation.LoresheetNav
import com.example.v5rules.navigation.PgNav
import com.example.v5rules.navigation.PredatorTypesNav
import com.example.v5rules.navigation.RulesNav
import com.example.v5rules.ui.compose.component.TintedImage

@Composable
fun HomeRulesScreen(navController: NavHostController, onTitleChanged: (String) -> Unit) {
    val title = stringResource(id = R.string.app_name)

    val orientation = LocalConfiguration.current.orientation
    val objectByOrientation = if (orientation == Configuration.ORIENTATION_LANDSCAPE) 3 else 2
    val widthByOrientation =
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) 0.3f else 0.5f

    LaunchedEffect(Unit) {
        onTitleChanged(title)
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(objectByOrientation),
        verticalArrangement = Arrangement.spacedBy(1.dp),
        horizontalArrangement = Arrangement.spacedBy(1.dp),
        modifier = Modifier.padding(16.dp)
    ) {
        item(span = { GridItemSpan(this.maxLineSpan) }) {
            TintedImage(
                R.drawable.logo_v5,
                MaterialTheme.colorScheme.onTertiary,
                300.dp
            )
        }
        item {
            Button(
                onClick = { navController.navigate(LoreNav) },
                modifier = Modifier
                    .fillMaxWidth(widthByOrientation)
                    .padding(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.tertiary,
                    contentColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text(text = stringResource(id = R.string.lore_screen_title))
            }
        }
        item {
            Button(
                onClick = { navController.navigate(ClansNav) },
                modifier = Modifier
                    .fillMaxWidth(widthByOrientation)
                    .padding(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.tertiary,
                    contentColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text(text = stringResource(id = R.string.clan_screen_title))
            }
        }
        item {
            Button(
                onClick = { navController.navigate(PgNav) },
                modifier = Modifier
                    .fillMaxWidth(widthByOrientation)
                    .padding(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.tertiary,
                    contentColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text(text = stringResource(id = R.string.pg_screen_button_label))
            }
        }
        item {
            Button(
                onClick = { navController.navigate(KindredNav) },
                modifier = Modifier
                    .fillMaxWidth(widthByOrientation)
                    .padding(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.tertiary,
                    contentColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text(text = stringResource(id = R.string.kindred_screen_button_label))
            }
        }
        item {
            Button(
                onClick = { navController.navigate(RulesNav) },
                modifier = Modifier
                    .fillMaxWidth(widthByOrientation)
                    .padding(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.tertiary,
                    contentColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text(text = stringResource(id = R.string.rules_screen_button_label))
            }
        }
        item {
            Button(
                onClick = { navController.navigate(DisciplinesNav) },
                modifier = Modifier
                    .fillMaxWidth(widthByOrientation)
                    .padding(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.tertiary,
                    contentColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text(text = stringResource(id = R.string.discipline_screen_title))
            }
        }

        item {
            Button(
                onClick = { navController.navigate(LoresheetNav) },
                modifier = Modifier
                    .fillMaxWidth(widthByOrientation)
                    .padding(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.tertiary,
                    contentColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text(text = stringResource(id = R.string.loresheet_title_screen))
            }
        }
        item {
            Button(
                onClick = { navController.navigate(BackgroundNav) },
                modifier = Modifier
                    .fillMaxWidth(widthByOrientation)
                    .padding(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.tertiary,
                    contentColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text(text = stringResource(id = R.string.background_title_screen))
            }
        }
        item {
            Button(
                onClick = { navController.navigate(PredatorTypesNav) },
                modifier = Modifier
                    .fillMaxWidth(widthByOrientation)
                    .padding(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.tertiary,
                    contentColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text(text = stringResource(id = R.string.predator_type_screen_title))
            }
        }
    }
}