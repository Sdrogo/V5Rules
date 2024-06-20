package com.example.v5rules.ui.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.v5rules.data.Discipline
import com.example.v5rules.data.RulesRepository
import com.example.v5rules.utils.Language
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DisciplineViewModel(
    private val rulesRepository: RulesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState

    var allDisciplines: List<Discipline> = listOf()

    init {
        fetchDisciplines(Language.ENGLISH)
    }

    fun fetchDisciplines(language: Language) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val disciplines = rulesRepository.loadRules(language)
                allDisciplines = disciplines
                _uiState.value = UiState.Success(disciplines)
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "Errore durante il caricamento delle discipline")
            }
        }
    }

    fun search(keyword: String) {
        val filteredDisciplines = allDisciplines.filter {
            it.title.contains(keyword, ignoreCase = true) ||
                    it.description.contains(keyword, ignoreCase = true)
        }
        _uiState.value = UiState.Success(filteredDisciplines)
    }
}

sealed class UiState {
    object Loading : UiState()
    data class Success(val disciplines: List<Discipline>) : UiState()
    data class Error(val message: String) : UiState()
}
