package com.example.v5rules.ui.compose.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@Composable
fun HomeScreen(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Welcome to V5Rules", modifier = Modifier.padding(bottom = 16.dp))
        Button(
            onClick = { navController.navigate("discipline_screen") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "View Disciplines")
        }
    }
}
