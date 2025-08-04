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
class KindredViewModel @Inject constructor(
    private val mainRepository: MainRepository
) : ViewModel() {

    private val _kindredUiState = MutableStateFlow<KindredUiState>(KindredUiState.Loading)
    val kindredUiState: StateFlow<KindredUiState> = _kindredUiState
    var allKindred: List<Chapter> = emptyList()

    val currentLocale: Locale = Locale.getDefault()

    init {
        fetchLore(currentLocale)
    }

    private fun fetchLore(currentLocale: Locale) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val kindred = mainRepository.loadKindred(currentLocale)
                allKindred = kindred
                _kindredUiState.value = KindredUiState.Success(kindred)
            } catch (e: Exception) {
                _kindredUiState.value =
                    KindredUiState.Error(e.message ?: "Errore durante il caricamento della lore")
            }
        }
    }
}

sealed class KindredUiState {
    object Loading : KindredUiState()
    data class Success(val chapters: List<Chapter>) : KindredUiState()
    data class Error(val message: String) : KindredUiState()
}
