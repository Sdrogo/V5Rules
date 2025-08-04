package com.example.v5rules.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.AccountBox
import androidx.compose.material.icons.sharp.Face
import androidx.compose.material.icons.sharp.Menu
import androidx.compose.material.icons.sharp.Star
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.v5rules.R

sealed class BottomNavItem(val route: Any, val icon: ImageVector, val title: Int) {
    object Rules : BottomNavItem(HomeRulesNav, Icons.Sharp.Menu, R.string.rules)
    object Disciplines : BottomNavItem(DisciplinesNav, Icons.Sharp.Star, R.string.discipline)
    object NPCGenerator : BottomNavItem(NPCGeneratorNav, Icons.Sharp.Face, R.string.npc_generator)

    object Character : BottomNavItem(CharacterSheetListNav, Icons.Sharp.AccountBox, R.string.characters)
}
