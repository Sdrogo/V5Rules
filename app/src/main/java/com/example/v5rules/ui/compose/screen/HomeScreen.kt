package com.example.v5rules.ui.compose.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.v5rules.R
import com.example.v5rules.ui.compose.component.CommonScaffold

@Composable
fun HomeScreen(navController: NavHostController) {
    CommonScaffold(navController = navController, title = stringResource(id = R.string.app_name)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .padding(it),
            verticalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = { navController.navigate("clan_screen") },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF76031a), // Your desired button color
                    contentColor = Color.White // Text color for contrast
                )
            ) {
                Text(text = stringResource(id = R.string.clan_screen_title))
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { navController.navigate("predator_type_screen") },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF76031a), // Your desired button color
                    contentColor = Color.White // Text color for contrast
                )
            ) {
                Text(text = stringResource(id = R.string.predator_type_screen_title))
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { navController.navigate("discipline_screen") },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF76031a), // Your desired button color
                    contentColor = Color.White // Text color for contrast
                )
            ) {
                Text(text = stringResource(id = R.string.discipline_screen_title))
            }
        }
    }
}
