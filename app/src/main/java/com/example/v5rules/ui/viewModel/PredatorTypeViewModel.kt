package com.example.v5rules.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.v5rules.data.Clan
import com.example.v5rules.data.MainRepository
import com.example.v5rules.data.PredatorType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Locale

class PredatorTypeViewModel(
    private val mainRepository: MainRepository
) : ViewModel() {

    private val _predatorTypeUiState = MutableStateFlow<PredatorTypeUiState>(PredatorTypeUiState.Loading)
    val predatorTypeUiState: StateFlow<PredatorTypeUiState> = _predatorTypeUiState

    var allTypes: List<PredatorType> = listOf()

    val currentLocale = Locale.getDefault()
    init {
        fetchClans(currentLocale)
    }

    private fun fetchClans(currentLocale:Locale) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                allTypes = mainRepository.loadPredatorType(currentLocale)
                _predatorTypeUiState.value = PredatorTypeUiState.Success(allTypes)
            } catch (e: Exception) {
                _predatorTypeUiState.value = PredatorTypeUiState.Error(e.message ?: "Errore durante il caricamento delle discipline")
            }
        }
    }
}

sealed class PredatorTypeUiState {
    object Loading : PredatorTypeUiState()
    data class Success(val clans: List<PredatorType>) : PredatorTypeUiState()
    data class Error(val message: String) : PredatorTypeUiState()
}
