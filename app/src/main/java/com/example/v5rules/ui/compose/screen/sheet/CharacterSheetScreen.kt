package com.example.v5rules.ui.compose.screen.sheet

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults.SecondaryIndicator
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.NavHostController
import com.example.v5rules.R
import com.example.v5rules.ui.compose.component.CommonScaffold
import com.example.v5rules.utils.CharacterSheetEvent
import com.example.v5rules.viewModel.CharacterSheetViewModel

@Composable
fun CharacterSheetScreen(
    viewModel: CharacterSheetViewModel,
    navController: NavHostController,
    id: String? = null
) {
    val uiState by viewModel.uiState.collectAsState()
    val character = uiState.character
    //val isLandscape = LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE
    val selectedTabIndex by viewModel.selectedTabIndex.collectAsState()


    LaunchedEffect(key1 = id) {
        if (id != null) {
            viewModel.setCharacter(id)
        }
    }

    val tabs = listOf(
        "Info Generali", "Attributi", "Abilità", "Discipline", "Background" /*... altre tab... */
    )

    CommonScaffold(
        navController = navController, title = stringResource(R.string.character_screen_title)
    ) { _ ->

        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .padding()
        ) {
            val (tabRow, content, buttons) = createRefs()
            // TabRow scorrevole
            ScrollableTabRow(selectedTabIndex = selectedTabIndex,
                modifier = Modifier
                    .constrainAs(tabRow) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        width = Dimension.fillToConstraints
                    }
                    .zIndex(1f),
                edgePadding = 0.dp, // Rimuovi il padding predefinito di TabRow
                containerColor = MaterialTheme.colorScheme.surface, // Colore di sfondo di TabRow
                indicator = { tabPositions ->
                    SecondaryIndicator(
                        Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                        color = MaterialTheme.colorScheme.tertiary // Colore dell'indicatore
                    )
                },
                divider = { } // Nascondi il divider tra le tab
            ) {
                tabs.forEachIndexed { index, tab ->
                    Tab(selected = selectedTabIndex == index,
                        onClick = { viewModel.selectTab(index) },
                        modifier = Modifier.widthIn(min = 80.dp), // Larghezza minima per ogni tab
                        text = {
                            Text(
                                tab,
                                style = MaterialTheme.typography.bodySmall,
                                maxLines = 1,
                                color = MaterialTheme.colorScheme.primary,
                                overflow = TextOverflow.Ellipsis
                            )
                        })
                }
            }

            // Contenuto scrollabile
            Column(modifier = Modifier
                .constrainAs(content) {
                    top.linkTo(tabRow.bottom) // Collega al bottom di TabRow
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(buttons.top)
                    width = Dimension.fillToConstraints // Aggiungi vincolo per la larghezza
                    height = Dimension.fillToConstraints
                }
            ) {
                //... (Contenuto come prima, ma usa when per selezionare la sezione)...
                when (selectedTabIndex) {
                    0 -> GeneralInfoSection(character, viewModel)
                    1 -> AttributeSection(character, viewModel)
                    2 -> AbilitySection(character, viewModel)
                    3 -> DisciplineSection(viewModel, navController)
                    4 -> BackgroundSection(viewModel)
                    //... altre sezioni...
                }
            }

            Row(modifier = Modifier
                .constrainAs(buttons) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                }
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween) {
                Button(
                    onClick = { viewModel.onEvent(CharacterSheetEvent.SaveClicked) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.tertiary,
                        contentColor = MaterialTheme.colorScheme.secondary
                    )
                ) {
                    Text("Save")
                }
                Button(
                    onClick = { viewModel.onEvent(CharacterSheetEvent.CleanupClicked) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.tertiary,
                        contentColor = MaterialTheme.colorScheme.secondary
                    )
                ) {
                    Text("Cleanup")
                }
                Button(
                    onClick = { viewModel.onEvent(CharacterSheetEvent.DeleteClicked) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.tertiary,
                        contentColor = MaterialTheme.colorScheme.secondary
                    )
                ) {
                    Text("Delete")
                }
            }
        }
    }
}
