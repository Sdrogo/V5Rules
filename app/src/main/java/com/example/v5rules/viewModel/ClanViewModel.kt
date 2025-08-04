package com.example.v5rules.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.v5rules.data.Clan
import com.example.v5rules.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class ClanViewModel @Inject constructor(
    private val mainRepository: MainRepository
) : ViewModel() {

    private val _clanUiState = MutableStateFlow<ClanUiState>(ClanUiState.Loading)
    val clanUiState: StateFlow<ClanUiState> = _clanUiState

    var allClans: List<Clan> = listOf()

    val currentLocale: Locale = Locale.getDefault()

    init {
        fetchClans(currentLocale)
    }

    private fun fetchClans(currentLocale: Locale) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                allClans = mainRepository.loadClans(currentLocale).sortedBy { it.name }
                _clanUiState.value = ClanUiState.Success(allClans)
            } catch (e: Exception) {
                _clanUiState.value =
                    ClanUiState.Error(e.message ?: "Errore durante il caricamento dei Clan")
            }
        }
    }
}

sealed class ClanUiState {
    object Loading : ClanUiState()
    data class Success(val clans: List<Clan>) : ClanUiState()
    data class Error(val message: String) : ClanUiState()
}
