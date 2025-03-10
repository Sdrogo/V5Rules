package com.example.v5rules.ui.compose.screen.sheet

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import com.example.v5rules.data.Character
import com.example.v5rules.viewModel.CharacterSheetViewModel

@Composable
fun BackgroundSection(
    character: Character,
    viewModel: CharacterSheetViewModel,
    navController: NavHostController
){


    val uiState by viewModel.uiState.collectAsState()
    val loreSheets by viewModel.disciplines.collectAsState()

}