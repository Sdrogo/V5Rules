package com.example.v5rules.ui.compose.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.credentials.CredentialManager
import androidx.credentials.exceptions.GetCredentialException
import androidx.credentials.exceptions.NoCredentialException
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.v5rules.R
import com.example.v5rules.ui.compose.component.TintedImage
import com.example.v5rules.viewModel.LoginViewModel
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onTitleChanged: (String) -> Unit
) {
    val viewModel: LoginViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val credentialManager = remember { CredentialManager.create(context) }
    val title = ""
    val serverClientId = stringResource(R.string.default_web_client_id)

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
    LaunchedEffect(Unit) {
        onTitleChanged(title)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier.align(Alignment.TopCenter),
        ) {
            TintedImage(
                R.drawable.logo_v5,
                MaterialTheme.colorScheme.onTertiary,
                300.dp,
            )
        }

        if (uiState.isLoading) {
            CircularProgressIndicator(color = MaterialTheme.colorScheme.tertiary)
        } else {
            Button(onClick = {
                scope.launch {
                    viewModel.onSignInStarted()
                    try {
                        val request = viewModel.buildGoogleSignInRequest(serverClientId)
                        val result = credentialManager.getCredential(
                            context = context,
                            request = request
                        )
                        viewModel.handleSignInResult(result)
                    } catch (e: NoCredentialException) {
                        viewModel.onSignInFailed(e)
                    } catch (e: GetCredentialException) {
                        viewModel.onSignInFailed(e)
                    }
                }
            },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.tertiary, // Your desired button color
                    contentColor = MaterialTheme.colorScheme.primary // Text color for contrast
                )) {
                // You can add the Google logo here as an Icon
                Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))
                Text("Sign in with Google")
            }
        }
    }
}
