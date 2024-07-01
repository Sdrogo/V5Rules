package com.example.v5rules.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.v5rules.data.Chapter
import com.example.v5rules.data.MainRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Locale

class ChapterViewModel(
    private val mainRepository: MainRepository
) : ViewModel() {

    private val _ChapterUiState = MutableStateFlow<ChapterUiState>(ChapterUiState.Loading)
    val chapterUiState: StateFlow<ChapterUiState> = _ChapterUiState

    val currentLocale = Locale.getDefault()
    init {
        fetchChapters(currentLocale)
    }

    private fun fetchChapters(currentLocale:Locale) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val chapters = mainRepository.loadChapters(currentLocale)
                _ChapterUiState.value = ChapterUiState.Success(chapters)
            } catch (e: Exception) {
                _ChapterUiState.value = ChapterUiState.Error(e.message ?: "Errore durante il caricamento delle discipline")
            }
        }
    }
}

sealed class ChapterUiState {
    object Loading : ChapterUiState()
    data class Success(val chapters: List<Chapter>) : ChapterUiState()
    data class Error(val message: String) : ChapterUiState()
}
