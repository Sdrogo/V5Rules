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
class RulesViewModel @Inject constructor(
    private val mainRepository: MainRepository
) : ViewModel() {

    private val _rulesUiState = MutableStateFlow<RulesUiState>(RulesUiState.Loading)
    val rulesUiState: StateFlow<RulesUiState> = _rulesUiState
    var allRules: List<Chapter> = emptyList()

    val currentLocale: Locale = Locale.getDefault()

    init {
        fetchChapters(currentLocale)
    }

    private fun fetchChapters(currentLocale: Locale) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val rules = mainRepository.loadRules(currentLocale)
                allRules = rules
                _rulesUiState.value = RulesUiState.Success(rules)
            } catch (e: Exception) {
                _rulesUiState.value =
                    RulesUiState.Error(e.message ?: "Errore durante il caricamento delle regole")
            }
        }
    }
}

sealed class RulesUiState {
    object Loading : RulesUiState()
    data class Success(val chapters: List<Chapter>) : RulesUiState()
    data class Error(val message: String) : RulesUiState()
}
