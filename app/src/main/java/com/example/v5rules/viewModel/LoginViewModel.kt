package com.example.v5rules.viewModel

import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.exceptions.GetCredentialException
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

data class LoginUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSuccess: Boolean = false
)

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore // Inject Firestore
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState = _uiState.asStateFlow()

    fun buildGoogleSignInRequest(serverClientId: String): GetCredentialRequest {
        val googleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId(serverClientId)
            .build()

        return GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()
    }

    fun onSignInStarted() {
        _uiState.update { it.copy(isLoading = true, error = null) }
    }

    fun handleSignInResult(result: GetCredentialResponse) {
        try {
            val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(result.credential.data)
            val idToken = googleIdTokenCredential.idToken
            firebaseAuthWithGoogle(idToken)
        } catch (e: Exception) {
            _uiState.update {
                it.copy(
                    isLoading = false,
                    error = "Sign-in credential was not a valid Google ID Token. ${e.message.toString()}"
                )
            }
        }
    }

    fun onSignInFailed(e: GetCredentialException) {
        _uiState.update { it.copy(isLoading = false, error = "Sign-in failed: ${e.message}") }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        viewModelScope.launch {
            try {
                val authResult = firebaseAuth.signInWithCredential(GoogleAuthProvider.getCredential(idToken, null)).await()
                val firebaseUser = authResult.user

                // *** LOGICA PER SALVARE L'UTENTE IN FIRESTORE CON EMAIL IN MINUSCOLO ***
                if (firebaseUser != null) {
                    val userDocument = mapOf(
                        "uid" to firebaseUser.uid,
                        "displayName" to firebaseUser.displayName,
                        "email" to firebaseUser.email?.lowercase() // Salva l'email in minuscolo
                    )
                    // Usa SetOptions.merge() per non sovrascrivere dati esistenti
                    firestore.collection("users").document(firebaseUser.uid)
                        .set(userDocument, SetOptions.merge()).await()
                }

                _uiState.update { it.copy(isLoading = false, isSuccess = true) }

            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = "Firebase Auth or data saving failed: ${e.message}"
                    )
                }
            }
        }
    }
}
