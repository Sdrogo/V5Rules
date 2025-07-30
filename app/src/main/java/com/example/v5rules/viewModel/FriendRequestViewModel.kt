package com.example.v5rules.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.v5rules.data.FriendRequest
import com.example.v5rules.repository.FriendRepository
import com.example.v5rules.repository.FriendshipActionResult
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class FriendRequestUiState(
    val isLoading: Boolean = true,
    val friendRequests: List<FriendRequest> = emptyList(),
    val errorMessage: String? = null,
    val feedbackMessage: String? = null
)

@HiltViewModel
class FriendRequestViewModel @Inject constructor(
    private val friendRepository: FriendRepository,
    private val auth: FirebaseAuth
) : ViewModel() {

    private val _uiState = MutableStateFlow(FriendRequestUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadFriendRequests()
    }

    private fun loadFriendRequests() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            friendRepository.getFriendRequests()
                .onSuccess { requests ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            friendRequests = requests.sortedByDescending { req -> req.timestamp }
                        )
                    }
                }
                .onFailure { exception ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = exception.message ?: "Failed to load requests."
                        )
                    }
                }
        }
    }
    
    fun getCurrentUserId(): String? {
        return auth.currentUser?.uid
    }

    fun acceptFriendRequest(request: FriendRequest) {
        viewModelScope.launch {
            handleFriendshipAction(
                action = { friendRepository.acceptFriendRequest(request) },
                successMessage = "Friend request accepted!"
            )
        }
    }

    fun declineFriendRequest(requestId: String) {
        viewModelScope.launch {
            handleFriendshipAction(
                action = { friendRepository.declineFriendRequest(requestId) },
                successMessage = "Friend request declined."
            )
        }
    }
    
    fun cancelFriendRequest(requestId: String) {
        viewModelScope.launch {
            handleFriendshipAction(
                action = { friendRepository.cancelFriendRequest(requestId) },
                successMessage = "Friend request cancelled."
            )
        }
    }

    private suspend fun handleFriendshipAction(
        action: suspend () -> FriendshipActionResult,
        successMessage: String
    ) {
        _uiState.update { it.copy(isLoading = true, feedbackMessage = null) }
        when (val result = action()) {
            is FriendshipActionResult.Success -> {
                _uiState.update { it.copy(feedbackMessage = successMessage) }
                // Ricarica la lista per riflettere il cambiamento di stato
                loadFriendRequests() 
            }
            is FriendshipActionResult.Error -> {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        feedbackMessage = result.message
                    )
                }
            }
        }
    }
    
    fun dismissFeedbackMessage() {
        _uiState.update { it.copy(feedbackMessage = null) }
    }
}
