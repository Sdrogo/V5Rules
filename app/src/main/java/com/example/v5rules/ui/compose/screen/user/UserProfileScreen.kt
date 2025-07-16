package com.example.v5rules.ui.compose.screen.user

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer // Importato
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height // Importato
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button // Importato
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.v5rules.HomeNav
import com.example.v5rules.LoginNav
import com.example.v5rules.ui.compose.component.CommonScaffold
import com.example.v5rules.viewModel.LoginViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun UserProfileScreen(navController: NavHostController) {
    val loginViewModel: LoginViewModel = hiltViewModel()
    val loginState by loginViewModel.uiState.collectAsState()

    val firebaseAuth = remember { FirebaseAuth.getInstance() }
    val currentUser = firebaseAuth.currentUser

    // Effetto per gestire la navigazione dopo il logout
    LaunchedEffect(loginState.isLoggedOut) {
        if (loginState.isLoggedOut) {
            navController.navigate(LoginNav) {
                popUpTo(HomeNav) { inclusive = true } // Rimuove lo stack fino alla Home
            }
            loginViewModel.onLogoutNavigated() // Resetta l'evento
        }
    }

    CommonScaffold(
        navController = navController,
        title = "User Profile"
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            if (!loginState.isLoading) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Welcome, ${currentUser?.displayName ?: currentUser?.email ?: "Guest"}!",
                        style = MaterialTheme.typography.headlineMedium,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    currentUser?.email?.let {
                        Text(
                            text = "Email: $it",
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                    }
                    currentUser?.uid?.let {
                        Text(
                            text = "User ID: $it",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                    Spacer(modifier = Modifier.height(32.dp))
                    Button(onClick = { loginViewModel.signOut() }) {
                        Text("Logout")
                    }
                }
            } else {
                CircularProgressIndicator()
            }
        }
    }
}