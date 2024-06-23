package com.example.v5rules.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.v5rules.data.Discipline
import com.example.v5rules.data.MainRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Locale

class DisciplineViewModel(
    private val mainRepository: MainRepository
) : ViewModel() {

    private val _Discipline_uiState = MutableStateFlow<DisciplineUiState>(DisciplineUiState.Loading)
    val disciplineUiState: StateFlow<DisciplineUiState> = _Discipline_uiState

    var allDisciplines: List<Discipline> = listOf()

    val currentLocale = Locale.getDefault()

    init {
        fetchDisciplines(currentLocale)
    }

    private fun fetchDisciplines(currentLocale: Locale) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val disciplines = mainRepository.loadDisciplines(currentLocale)
                allDisciplines = disciplines
                _Discipline_uiState.value = DisciplineUiState.Success(disciplines)
            } catch (e: Exception) {
                _Discipline_uiState.value = DisciplineUiState.Error(
                    e.message ?: "Errore durante il caricamento delle discipline"
                )
            }
        }
    }

    fun search(keyword: String) {

        val filteredDisciplines = allDisciplines.filter {
            it.title.contains(keyword, ignoreCase = true) ||
                    it.description.contains(keyword, ignoreCase = true) ||
                    it.type.contains(keyword, ignoreCase = true) ||
                    it.masquerade.contains(keyword, ignoreCase = true) ||
                    it.resonance.contains(keyword, ignoreCase = true) ||
                    it.subDisciplines.toString().contains(keyword, ignoreCase = true) ||
                    it.rituals.toString().contains(keyword, ignoreCase = true)
        }
        _Discipline_uiState.value = DisciplineUiState.Success(filteredDisciplines)
    }
}

sealed class DisciplineUiState {
    object Loading : DisciplineUiState()
    data class Success(val disciplines: List<Discipline>) : DisciplineUiState()
    data class Error(val message: String) : DisciplineUiState()
}
