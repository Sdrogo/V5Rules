package com.example.v5rules.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.v5rules.data.Background
import com.example.v5rules.di.AppModule
import com.example.v5rules.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class BackgroundViewModel @Inject constructor(
    private val mainRepository: MainRepository,
    @AppModule.IoDispatcher private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {

    private val _backgroundUiState = MutableStateFlow<BackgroundUiState>(BackgroundUiState.Loading)
    val backgroundUiState: StateFlow<BackgroundUiState> = _backgroundUiState
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query

    }
    val filteredBackgrounds: StateFlow<List<Background>> = combine(backgroundUiState, searchQuery) { uiState, query ->
        when (uiState) {
            is BackgroundUiState.Success -> {
                if (query.isEmpty()) {
                    uiState.backgrounds
                } else {
                    uiState.backgrounds.filter { background ->
                        background.title.contains(query, ignoreCase = true) ||
                                background.merits.any{ it.title.contains(query, ignoreCase = true) } ||
                                background.flaws.any{ it.title.contains(query, ignoreCase = true) } ||
                                background.directFlaws.any { it.title.contains(query, ignoreCase = true) } ||
                                background.prerequisites?.contains(query, ignoreCase = true) == true
                    }
                }
            }
            else -> emptyList()
        }
    }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())


    val currentLocale: Locale = Locale.getDefault()

    init {
        fetchLore(currentLocale)
    }

    private fun fetchLore(currentLocale: Locale) {
        viewModelScope.launch(ioDispatcher) {
            try {
                val backgrounds = mainRepository.loadBackground(currentLocale)
                _backgroundUiState.value = BackgroundUiState.Success(backgrounds)
            } catch (e: Exception) {
                _backgroundUiState.value =
                    BackgroundUiState.Error(e.message ?: "Errore durante il caricamento della lore")
            }
        }
    }
}

sealed class BackgroundUiState {
    object Loading : BackgroundUiState()
    data class Success(val backgrounds: List<Background>) : BackgroundUiState()
    data class Error(val message: String) : BackgroundUiState()
}
