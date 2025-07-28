package com.example.v5rules.ui.compose.screen.user

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.v5rules.R
import com.example.v5rules.viewModel.LoginViewModel
import com.example.v5rules.viewModel.UserProfileViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun UserProfileScreen(
    onLogout: () -> Unit, onTitleChanged: (String) -> Unit
) {
    // ViewModels
    val loginViewModel: LoginViewModel = hiltViewModel()
    val profileViewModel: UserProfileViewModel = hiltViewModel()

    // States
    val loginState by loginViewModel.uiState.collectAsState()
    val profileState by profileViewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    val currentUser = remember { FirebaseAuth.getInstance().currentUser }
    val title = stringResource(R.string.user_profile_title)
    // Effetto per mostrare i messaggi di feedback (es. snackbar)
    LaunchedEffect(profileState.feedbackMessage) {
        profileState.feedbackMessage?.let {
            snackbarHostState.showSnackbar(it)
        }
    }
    LaunchedEffect(Unit) {
        onTitleChanged(title)
    }

    // Se la modale per aggiungere amici deve essere mostrata
    if (profileState.showAddFriendDialog) {
        AddFriendDialog(
            email = profileState.friendEmail,
            onEmailChange = { profileViewModel.onEmailChanged(it) },
            onDismissRequest = { profileViewModel.dismissAddFriendDialog() },
            onConfirm = { profileViewModel.sendFriendRequest() },
            isLoading = profileState.isLoading,
            errorMessage = if (profileState.isLoading) null else profileState.feedbackMessage
        )
    }


    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        if (loginState.isLoading) {
            CircularProgressIndicator()
        } else {
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
                // ... (altri dettagli utente)

                Spacer(modifier = Modifier.height(32.dp))

                // Pulsante per aprire la modale "Add Friend"
                Button(onClick = { profileViewModel.showAddFriendDialog() }) {
                    Text("Add Friend")
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Pulsante di Logout
                Button(onClick = onLogout) {
                    Text(text = stringResource(id = R.string.logout_button_label)) // Aggiungi questa stringa in strings.xml
                }
            }
        }
    }
}

@Composable
private fun AddFriendDialog(
    email: String,
    onEmailChange: (String) -> Unit,
    onDismissRequest: () -> Unit,
    onConfirm: () -> Unit,
    isLoading: Boolean,
    errorMessage: String?
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text("Add a Friend") },
        text = {
            Column {
                OutlinedTextField(
                    value = email,
                    onValueChange = onEmailChange,
                    label = { Text("Friend's Gmail") },
                    singleLine = true,
                    isError = errorMessage != null
                )
                if (errorMessage != null) {
                    Text(
                        text = errorMessage,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = onConfirm,
                enabled = !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp))
                } else {
                    Text("Send Request")
                }
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text("Cancel")
            }
        }
    )
}
