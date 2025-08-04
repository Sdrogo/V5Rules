package com.example.v5rules.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.v5rules.R

sealed class BottomNavItem(val route: Any, val icon: ImageVector, val title: Int) {
    object Rules : BottomNavItem(HomeRulesNav, Icons.Filled.Menu, R.string.rules)
    object Disciplines : BottomNavItem(DisciplinesNav, Icons.Default.Star, R.string.discipline)
    object NPCGenerator : BottomNavItem(NPCGeneratorNav, Icons.Filled.Face, R.string.npc_generator)
    object Character : BottomNavItem(CharacterSheetListNav, Icons.Filled.AccountBox, R.string.characters)
}
