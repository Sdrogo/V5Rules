package com.example.v5rules.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.v5rules.data.Character
import com.example.v5rules.repository.CharacterRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
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
    characterRepository: CharacterRepository
) : ViewModel() {

    val uiState: StateFlow<CharacterSheetListUiState> = characterRepository.getAllCharacters()
        .map { characters ->
            CharacterSheetListUiState(characterList = characters)
        }.catch { e ->
            emit(CharacterSheetListUiState(error = e.message ?: "Errore Sconosciuto"))
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = CharacterSheetListUiState(isLoading = true)
        )
}