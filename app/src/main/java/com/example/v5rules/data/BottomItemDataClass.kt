package com.example.v5rules.data

import androidx.compose.ui.graphics.vector.ImageVector

data class BottomItemDataClass(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val route: String
)