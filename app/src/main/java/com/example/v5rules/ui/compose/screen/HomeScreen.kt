package com.example.v5rules.ui.compose.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
                .background(color = MaterialTheme.colorScheme.secondary),
            verticalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = { navController.navigate("clan_screen") },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.tertiary, // Your desired button color
                    contentColor = MaterialTheme.colorScheme.primary // Text color for contrast
                )
            ) {
                Text(text = stringResource(id = R.string.clan_screen_title))
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { navController.navigate("rules_screen") },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.tertiary, // Your desired button color
                    contentColor = MaterialTheme.colorScheme.primary // Text color for contrast
                )
            ) {
                Text(text = stringResource(id = R.string.rules_screen_button_label))
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { navController.navigate("predator_type_screen") },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.tertiary, // Your desired button color
                    contentColor = MaterialTheme.colorScheme.primary // Text color for contrast
                )
            ) {
                Text(text = stringResource(id = R.string.predator_type_screen_title))
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { navController.navigate("discipline_screen") },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.tertiary, // Your desired button color
                    contentColor = MaterialTheme.colorScheme.primary // Text color for contrast
                )
            ) {
                Text(text = stringResource(id = R.string.discipline_screen_title))
            }
        }
    }
}
