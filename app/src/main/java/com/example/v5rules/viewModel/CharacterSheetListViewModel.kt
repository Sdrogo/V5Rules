package com.example.v5rules.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.v5rules.data.Character
import com.example.v5rules.repository.CharacterRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

data class CharacterSheetListUiState(
    val characterList: List<Character> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class CharacterSheetListViewModel @Inject constructor(
    private val characterRepository: CharacterRepository,
    auth: FirebaseAuth
) : ViewModel() {

    private fun FirebaseAuth.authStateChanges(): Flow<Boolean> {
        return callbackFlow {
            val listener = FirebaseAuth.AuthStateListener { firebaseAuth ->
                trySend(firebaseAuth.currentUser != null)
            }
            addAuthStateListener(listener)
            awaitClose { removeAuthStateListener(listener) }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState: StateFlow<CharacterSheetListUiState> = auth.authStateChanges()
        .flatMapLatest { isLoggedIn ->
            if (isLoggedIn) {
                characterRepository.getAllCharacters()
                    .map { characters ->
                        // NOME DELLA CLASSE CORRETTO QUI
                        CharacterSheetListUiState(characterList = characters, isLoading = false)
                    }
                    .catch { e ->
                        Log.e("ViewModelError", "Firestore failed to get characters", e)
                        emit(CharacterSheetListUiState(error = "Errore Firestore: ${e.message}", isLoading = false))
                    }
            } else {
                flowOf(CharacterSheetListUiState(isLoading = false, characterList = emptyList()))
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = CharacterSheetListUiState(isLoading = true)
        )
}