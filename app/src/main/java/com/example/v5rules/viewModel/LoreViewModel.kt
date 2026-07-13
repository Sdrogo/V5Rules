package com.example.v5rules.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.v5rules.data.local.model.Chapter
import com.example.v5rules.di.AppModule
import com.example.v5rules.data.local.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class LoreViewModel @Inject constructor(
    private val mainRepository: MainRepository,
    @AppModule.IoDispatcher private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {

    private val _loreUiState = MutableStateFlow<LoreUiState>(LoreUiState.Loading)
    val loreUiState: StateFlow<LoreUiState> = _loreUiState
    var allLore: List<Chapter> = emptyList()

    val currentLocale: Locale = Locale.getDefault()

    init {
        fetchLore(currentLocale)
    }

    private fun fetchLore(currentLocale: Locale) {
        viewModelScope.launch(ioDispatcher) {
            try {
                val lore = mainRepository.loadLore(currentLocale)
                allLore = lore
                _loreUiState.value = LoreUiState.Success(lore)
            } catch (e: Exception) {
                _loreUiState.value =
                    LoreUiState.Error(e.message ?: "Errore durante il caricamento della lore")
            }
        }
    }
}

sealed class LoreUiState {
    object Loading : LoreUiState()
    data class Success(val chapters: List<Chapter>) : LoreUiState()
    data class Error(val message: String) : LoreUiState()
}
