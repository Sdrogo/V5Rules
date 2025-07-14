package com.example.v5rules.ui.compose.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.credentials.CredentialManager
import androidx.credentials.exceptions.GetCredentialException
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.v5rules.R
import com.example.v5rules.viewModel.LoginViewModel
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel(),
    onLoginSuccess: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val credentialManager = remember { CredentialManager.create(context) }

    // This effect navigates away on successful login
    LaunchedEffect(key1 = uiState.isSuccess) {
        if (uiState.isSuccess) {
            onLoginSuccess()
        }
    }

    // This effect shows a snackbar on error
    LaunchedEffect(key1 = uiState.error) {
        uiState.error?.let {
            snackbarHostState.showSnackbar(message = it)
        }
    }

    Scaffold(snackbarHost = { SnackbarHost(snackbarHostState) }) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator()
            } else {
                Button(onClick = {
                    scope.launch {
                        viewModel.onSignInStarted()
                        try {
                            val serverClientId = context.getString(R.string.default_web_client_id)
                            val request = viewModel.buildGoogleSignInRequest(serverClientId)
                            val result = credentialManager.getCredential(
                                context = context,
                                request = request
                            )
                            viewModel.handleSignInResult(result)
                        } catch (e: GetCredentialException) {
                            viewModel.onSignInFailed(e)
                        }
                    }
                }) {
                    // You can add the Google logo here as an Icon
                    Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))
                    Text("Sign in with Google")
                }
            }
        }
    }
}