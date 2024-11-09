package com.example.v5rules.ui.compose.screen

import android.content.res.Configuration
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.v5rules.R
import com.example.v5rules.ui.compose.component.CommonScaffold
import com.example.v5rules.ui.compose.component.TintedImage
import com.example.v5rules.ClansNav
import com.example.v5rules.DisciplinesNav
import com.example.v5rules.KindredNav
import com.example.v5rules.LoreNav
import com.example.v5rules.LoresheetNav
import com.example.v5rules.NPCGeneratorNav
import com.example.v5rules.PgNav
import com.example.v5rules.PredatorTypesNav
import com.example.v5rules.RulesNav

@Composable
fun HomeScreen(navController: NavHostController) {
    CommonScaffold(
        navController = navController, title = stringResource(id = R.string.app_name)
    ) {
        val orientation = LocalConfiguration.current.orientation
        val objectByOrientation = if (orientation == Configuration.ORIENTATION_LANDSCAPE) 3 else 2
        val widthByOrientation =
            if (orientation == Configuration.ORIENTATION_LANDSCAPE) 0.3f else 0.5f
        val infiniteTransition = rememberInfiniteTransition(label = "infinite transition")
        val animatedRed by infiniteTransition.animateColor(
            initialValue = MaterialTheme.colorScheme.tertiary,
            targetValue = MaterialTheme.colorScheme.onPrimary,
            animationSpec = infiniteRepeatable(tween(5000), RepeatMode.Reverse),
            label = "color"
        )
        val animatedWhite by infiniteTransition.animateColor(
            initialValue = MaterialTheme.colorScheme.primary,
            targetValue = MaterialTheme.colorScheme.onTertiary,
            animationSpec = infiniteRepeatable(tween(5000), RepeatMode.Reverse),
            label = "color"
        )
        LazyVerticalGrid(
            columns = GridCells.Fixed(objectByOrientation),
            verticalArrangement = Arrangement.spacedBy(1.dp),
            horizontalArrangement = Arrangement.spacedBy(1.dp),
            modifier = Modifier.padding(16.dp)
        ) {
            item(span = { GridItemSpan(this.maxLineSpan) }) {
                TintedImage(
                    R.drawable.logo_v5,
                    animatedWhite,
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
                        containerColor = animatedRed, // Your desired button color
                        contentColor = MaterialTheme.colorScheme.secondary // Text color for contrast
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
                        containerColor = animatedRed, // Your desired button color
                        contentColor = MaterialTheme.colorScheme.secondary // Text color for contrast
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
                        containerColor = animatedRed, // Your desired button color
                        contentColor = MaterialTheme.colorScheme.secondary // Text color for contrast
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
                        containerColor = animatedRed, // Your desired button color
                        contentColor = MaterialTheme.colorScheme.secondary // Text color for contrast
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
                        containerColor = animatedRed, // Your desired button color
                        contentColor = MaterialTheme.colorScheme.secondary // Text color for contrast
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
                        containerColor = animatedRed, // Your desired button color
                        contentColor = MaterialTheme.colorScheme.secondary // Text color for contrast
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
                        containerColor = animatedRed, // Your desired button color
                        contentColor = MaterialTheme.colorScheme.secondary // Text color for contrast
                    )
                ) {
                    Text(text = stringResource(id = R.string.loresheet_title_screen))
                }
            }

            item {
                Button(
                    onClick = { navController.navigate(PredatorTypesNav) },
                    modifier = Modifier
                        .fillMaxWidth(widthByOrientation)
                        .padding(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = animatedRed, // Your desired button color
                        contentColor = MaterialTheme.colorScheme.secondary // Text color for contrast
                    )
                ) {
                    Text(text = stringResource(id = R.string.predator_type_screen_title))
                }
            }

            item {
                Button(
                    onClick = { navController.navigate(NPCGeneratorNav) },
                    modifier = Modifier
                        .fillMaxWidth(widthByOrientation)
                        .padding(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = animatedRed, // Your desired button color
                        contentColor = MaterialTheme.colorScheme.secondary // Text color for contrast
                    )
                ) {
                    Text(text = stringResource(id = R.string.npc_generator_title))
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ButtonsBlock(navController: NavHostController, modifier: Modifier = Modifier) {
    val orientation = LocalConfiguration.current.orientation
    val widthByOrientation = if (orientation == Configuration.ORIENTATION_LANDSCAPE) 0.3f else 1f
    val infiniteTransition = rememberInfiniteTransition(label = "infinite transition")
    val animatedRed by infiniteTransition.animateColor(
        initialValue = MaterialTheme.colorScheme.tertiary,
        targetValue = MaterialTheme.colorScheme.onPrimary,
        animationSpec = infiniteRepeatable(tween(5000), RepeatMode.Reverse),
        label = "color"
    )
    FlowRow(
        modifier = modifier
            .fillMaxSize(),
        horizontalArrangement = Arrangement.SpaceAround,
    ) {
        Button(
            onClick = { navController.navigate(LoreNav) },
            modifier = Modifier
                .fillMaxWidth(widthByOrientation)
                .padding(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = animatedRed, // Your desired button color
                contentColor = MaterialTheme.colorScheme.secondary // Text color for contrast
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
                containerColor = animatedRed, // Your desired button color
                contentColor = MaterialTheme.colorScheme.secondary // Text color for contrast
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
                containerColor = animatedRed, // Your desired button color
                contentColor = MaterialTheme.colorScheme.secondary // Text color for contrast
            )
        ) {
            Text(text = stringResource(id = R.string.rules_screen_button_label))
        }
        Button(
            onClick = { navController.navigate(DisciplinesNav) },
            modifier = Modifier
                .fillMaxWidth(widthByOrientation)
                .padding(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = animatedRed, // Your desired button color
                contentColor = MaterialTheme.colorScheme.secondary // Text color for contrast
            )
        ) {
            Text(text = stringResource(id = R.string.discipline_screen_title))
        }
        Button(
            onClick = { navController.navigate(LoresheetNav) },
            modifier = Modifier
                .fillMaxWidth(widthByOrientation)
                .padding(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = animatedRed, // Your desired button color
                contentColor = MaterialTheme.colorScheme.secondary // Text color for contrast
            )
        ) {
            Text(text = stringResource(id = R.string.loresheet_title_screen))
        }
        Button(
            onClick = { navController.navigate(PredatorTypesNav) },
            modifier = Modifier
                .fillMaxWidth(widthByOrientation)
                .padding(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = animatedRed, // Your desired button color
                contentColor = MaterialTheme.colorScheme.secondary // Text color for contrast
            )
        ) {
            Text(text = stringResource(id = R.string.predator_type_screen_title))
        }
        Button(
            onClick = { navController.navigate(NPCGeneratorNav) },
            modifier = Modifier
                .fillMaxWidth(widthByOrientation)
                .padding(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = animatedRed, // Your desired button color
                contentColor = MaterialTheme.colorScheme.secondary // Text color for contrast
            )
        ) {
            Text(text = stringResource(id = R.string.npc_generator_title))
        }
    }
}
