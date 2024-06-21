package com.example.v5rules.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.v5rules.data.Discipline
import com.example.v5rules.data.DisciplineRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.internal.filterList
import java.util.Locale

class DisciplineViewModel(
    private val disciplineRepository: DisciplineRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState


    var allDisciplines: List<Discipline> = listOf()

    val currentLocale = Locale.getDefault()
    init {
        fetchDisciplines(currentLocale)
    }

    private fun fetchDisciplines(currentLocale:Locale) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val disciplines = disciplineRepository.loadDiscipline(currentLocale)
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
                    it.description.contains(keyword, ignoreCase = true) ||
                    it.type.contains(keyword, ignoreCase = true) ||
                    it.masquerade.contains(keyword, ignoreCase = true) ||
                    it.resonance.contains(keyword, ignoreCase = true)
        }
        _uiState.value = UiState.Success(filteredDisciplines)
    }
}

sealed class UiState {
    object Loading : UiState()
    data class Success(val disciplines: List<Discipline>) : UiState()
    data class Error(val message: String) : UiState()
}
