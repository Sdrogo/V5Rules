package com.example.v5rules.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.v5rules.data.Chapter
import com.example.v5rules.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class PgViewModel @Inject constructor(
    private val mainRepository: MainRepository
) : ViewModel() {

    private val _pgUiState = MutableStateFlow<PgUiState>(PgUiState.Loading)
    val pgUiState: StateFlow<PgUiState> = _pgUiState
    var allPg: List<Chapter> = emptyList()


    val currentLocale = Locale.getDefault()

    init {
        fetchLore(currentLocale)
    }

    private fun fetchLore(currentLocale: Locale) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val kindred = mainRepository.loadPg(currentLocale)
                allPg = kindred
                _pgUiState.value = PgUiState.Success(kindred)
            } catch (e: Exception) {
                _pgUiState.value =
                    PgUiState.Error(e.message ?: "Errore durante il caricamento della lore")
            }
        }
    }
}

sealed class PgUiState {
    object Loading : PgUiState()
    data class Success(val chapters: List<Chapter>) : PgUiState()
    data class Error(val message: String) : PgUiState()
}
