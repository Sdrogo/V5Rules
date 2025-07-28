package com.example.v5rules.ui.compose.screen

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.credentials.CredentialManager
import androidx.credentials.exceptions.GetCredentialException
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.v5rules.R
import com.example.v5rules.ui.compose.component.TintedImage
import com.example.v5rules.viewModel.LoginViewModel
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel(),
    onLoginSuccess: () -> Unit,
    onTitleChanged: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val credentialManager = remember { CredentialManager.create(context) }
    val title = stringResource(R.string.app_name)
    val infiniteTransition = rememberInfiniteTransition(label = "infinite transition")
    val animatedRed by infiniteTransition.animateColor(
        initialValue = MaterialTheme.colorScheme.tertiary,
        targetValue = MaterialTheme.colorScheme.onPrimary,
        animationSpec = infiniteRepeatable(tween(5000), RepeatMode.Reverse),
        label = "color"
    )

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
                animatedRed,
                300.dp,
            )
        }

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