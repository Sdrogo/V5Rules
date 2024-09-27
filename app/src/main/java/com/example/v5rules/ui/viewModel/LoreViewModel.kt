package com.example.v5rules.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.v5rules.data.Chapter
import com.example.v5rules.data.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class LoreViewModel @Inject constructor(
    private val mainRepository: MainRepository
) : ViewModel() {

    private val _loreUiState = MutableStateFlow<LoreUiState>(LoreUiState.Loading)
    val loreUiState: StateFlow<LoreUiState> = _loreUiState
    var allLore: List<Chapter> = emptyList()


    val currentLocale = Locale.getDefault()
    init {
        fetchLore(currentLocale)
    }
    private fun fetchLore(currentLocale:Locale) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val Lore = mainRepository.loadLore(currentLocale)
                allLore = Lore
                _loreUiState.value = LoreUiState.Success(Lore)
            } catch (e: Exception) {
                _loreUiState.value =
                    LoreUiState.Error(e.message ?: "Errore durante il caricamento delle discipline")
            }
        }
    }
}

sealed class LoreUiState {
    object Loading : LoreUiState()
    data class Success(val chapters: List<Chapter>) : LoreUiState()
    data class Error(val message: String) : LoreUiState()
}
