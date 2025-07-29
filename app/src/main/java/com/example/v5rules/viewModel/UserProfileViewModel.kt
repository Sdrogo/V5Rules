package com.example.v5rules.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.v5rules.repository.FriendRepository
import com.example.v5rules.repository.FriendRequestResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class UserProfileUiState(
    val showAddFriendDialog: Boolean = false,
    val friendEmail: String = "",
    val isLoading: Boolean = false,
    val feedbackMessage: String? = null
)

@HiltViewModel
class UserProfileViewModel @Inject constructor(
    private val friendRepository: FriendRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(UserProfileUiState())
    val uiState = _uiState.asStateFlow()

    fun onEmailChanged(email: String) {
        // Resetta il messaggio di feedback quando l'utente scrive
        _uiState.update { it.copy(friendEmail = email, feedbackMessage = null) }
    }

    fun showAddFriendDialog() {
        _uiState.update {
            it.copy(
                showAddFriendDialog = true,
                friendEmail = "",
                feedbackMessage = null,
                isLoading = false
            )
        }
    }

    fun dismissAddFriendDialog() {
        _uiState.update { it.copy(showAddFriendDialog = false) }
    }

    fun sendFriendRequest() {
        if (_uiState.value.friendEmail.isBlank()) {
            _uiState.update { it.copy(feedbackMessage = "Email cannot be empty.") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, feedbackMessage = null) }
            when (val result = friendRepository.sendFriendRequest(_uiState.value.friendEmail)) {
                is FriendRequestResult.Success -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            feedbackMessage = "Friend request sent!",
                            showAddFriendDialog = false // Chiude la modale in caso di successo
                        )
                    }
                }
                is FriendRequestResult.Error -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            feedbackMessage = result.message
                        )
                    }
                }
            }
        }
    }
}
