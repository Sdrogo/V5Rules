package com.example.v5rules.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.v5rules.data.Discipline
import com.example.v5rules.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class DisciplineViewModel @Inject constructor(
    private val mainRepository: MainRepository
) : ViewModel() {

    private val _discipline_uiState = MutableStateFlow<DisciplineUiState>(DisciplineUiState.Loading)
    val disciplineUiState: StateFlow<DisciplineUiState> = _discipline_uiState

    var allDisciplines: List<Discipline> = listOf()

    val currentLocale = Locale.getDefault()

    init {
        fetchDisciplines(currentLocale)
    }

    private fun fetchDisciplines(currentLocale: Locale) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val disciplines =
                    mainRepository.loadDisciplines(currentLocale).sortedBy { it.title }
                allDisciplines = disciplines
                _discipline_uiState.value = DisciplineUiState.Success(disciplines)
            } catch (e: Exception) {
                _discipline_uiState.value = DisciplineUiState.Error(
                    e.message ?: "Errore durante il caricamento delle discipline"
                )
            }
        }
    }
}

sealed class DisciplineUiState {
    object Loading : DisciplineUiState()
    data class Success(val disciplines: List<Discipline>) : DisciplineUiState()
    data class Error(val message: String) : DisciplineUiState()
}
