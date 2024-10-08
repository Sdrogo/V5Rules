package com.example.v5rules.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.v5rules.data.Loresheet
import com.example.v5rules.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class LoresheetViewModel @Inject constructor(
    private val mainRepository: MainRepository
) : ViewModel() {

    private val _loresheetUiState = MutableStateFlow<LoresheetUiState>(LoresheetUiState.Loading)
    val loresheetUiState: StateFlow<LoresheetUiState> = _loresheetUiState
    var allLore: List<Loresheet> = emptyList()


    val currentLocale = Locale.getDefault()

    init {
        fetchLore(currentLocale)
    }

    private fun fetchLore(currentLocale: Locale) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val loresheets = mainRepository.loadLoresheet(currentLocale)
                allLore = loresheets
                _loresheetUiState.value = LoresheetUiState.Success(loresheets)
            } catch (e: Exception) {
                _loresheetUiState.value =
                    LoresheetUiState.Error(e.message ?: "Errore durante il caricamento della lore")
            }
        }
    }
}

sealed class LoresheetUiState {
    object Loading : LoresheetUiState()
    data class Success(val loresheets: List<Loresheet>) : LoresheetUiState()
    data class Error(val message: String) : LoresheetUiState()
}
