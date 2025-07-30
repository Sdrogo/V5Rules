package com.example.v5rules

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(val route: Any, val icon: ImageVector, val title: Int) {
    object Home : BottomNavItem(HomeNav, Icons.Filled.Home, R.string.home)
    object Rules : BottomNavItem(RulesNav, Icons.Filled.Menu, R.string.rules)
    object Character : BottomNavItem(CharacterSheetListNav, Icons.Filled.AccountBox, R.string.characters)
}
