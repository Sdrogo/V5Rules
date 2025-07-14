package com.example.v5rules.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.v5rules.data.Character
import com.example.v5rules.data.FavoriteNpc
import com.example.v5rules.data.Gender
import com.example.v5rules.data.NationalityNpc
import com.example.v5rules.data.Npc
import com.example.v5rules.repository.CharacterRepository
import com.example.v5rules.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject
import kotlin.text.append

sealed class NpcNavigationEvent {
    data class ToCharacterSheet(val characterId: Int) : NpcNavigationEvent()
}

@HiltViewModel
class NPCGeneratorViewModel @Inject constructor(
    private val mainRepository: MainRepository,
    private val characterRepository: CharacterRepository
) : ViewModel() {

    data class UiState(
        val selectedGender: Gender,
        val includeSecondName: Boolean,
        val firstGeneration: Boolean,
        val selectedNationality: String?,
        val npc: Npc?
    )

    private val _npc_nationality_uiState = MutableStateFlow<NpcNationalityUiState>(
        NpcNationalityUiState.Loading
    )
    val npc_nationality_uiState: StateFlow<NpcNationalityUiState> = _npc_nationality_uiState
    var nationalities: List<String> = listOf()
    var allNamesByNationality: List<NationalityNpc> = listOf()
    private val _favoriteNpcs = MutableStateFlow<List<FavoriteNpc>>(emptyList())
    val favoriteNpcs: StateFlow<List<FavoriteNpc>> = _favoriteNpcs.asStateFlow()

    private val _uiState = MutableStateFlow(
        UiState(
            selectedGender = Gender.MALE,
            includeSecondName = false,
            firstGeneration = false,
            selectedNationality = null,
            npc = null
        )
    )
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val _navigationEvent = MutableSharedFlow<NpcNavigationEvent>()
    val navigationEvent: SharedFlow<NpcNavigationEvent> = _navigationEvent.asSharedFlow()


    init {
        fetchNpcNames()
    }

    private fun fetchNpcNames() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val npcNames = mainRepository.readNpcNames(Locale.getDefault())
                allNamesByNationality = npcNames
                nationalities = npcNames.map { it.nationality }.sortedBy { it }
                _npc_nationality_uiState.update {
                    NpcNationalityUiState.Success(nationalities)
                }
                _uiState.update {
                    it.copy(selectedNationality = nationalities.firstOrNull())
                }
            } catch (e: Exception) {
                _npc_nationality_uiState.value = NpcNationalityUiState.Error(
                    e.message
                        ?: "Errore durante il caricamento delle lista di nomi per nazionalità"
                )
            }
        }
    }

    fun setSelectedGender(gender: Gender) {
        _uiState.update { it.copy(selectedGender = gender) }
        if (_uiState.value.firstGeneration) {
            regenerateName()
            if (_uiState.value.includeSecondName) regenerateSecondName()
        }
    }

    fun setIncludeSecondName(include: Boolean) {
        _uiState.update {
            if (!include) {
                it.copy(
                    includeSecondName = false,
                    npc = it.npc?.copy(secondName = null)
                )
            } else {
                it.copy(includeSecondName = true)
            }
        }
        if (include && _uiState.value.firstGeneration) {
            regenerateSecondName()
        }
    }

    fun setSelectedNationality(nationality: String?) {
        _uiState.update {
            it.copy(
                selectedNationality = nationality,
                npc = null,
                firstGeneration = false
            )
        }
    }

    fun generateAll() {
        resetFavoriteStatus()
        val currentState = _uiState.value
        val namesMap = allNamesByNationality.find { it.nationality == currentState.selectedNationality } ?: return
        val random = kotlin.random.Random.Default

        val maleNames = namesMap.nomi_maschili
        val femaleNames = namesMap.nomi_femminili
        val familyNames = namesMap.cognomi

        val newName = if (currentState.selectedGender == Gender.MALE) maleNames.randomOrNull(random) else femaleNames.randomOrNull(random)
        val newSecondName = if (currentState.includeSecondName) {
            if (currentState.selectedGender == Gender.MALE) maleNames.randomOrNull(random) else femaleNames.randomOrNull(random)
        } else null
        val newFamilyName = familyNames.randomOrNull(random)

        _uiState.update {
            it.copy(
                firstGeneration = true,
                npc = Npc(
                    nome = newName.orEmpty(),
                    secondName = newSecondName,
                    cognome = newFamilyName.orEmpty()
                )
            )
        }
    }

    fun regenerateName() {
        resetFavoriteStatus()
        val currentState = _uiState.value
        val namesMap = allNamesByNationality.find { it.nationality == currentState.selectedNationality } ?: return
        val random = kotlin.random.Random.Default
        val names = if (currentState.selectedGender == Gender.MALE) namesMap.nomi_maschili else namesMap.nomi_femminili
        _uiState.update {
            it.copy(npc = it.npc?.copy(nome = names.randomOrNull(random).orEmpty()))
        }
    }

    fun regenerateSecondName() {
        if (!_uiState.value.includeSecondName) return
        resetFavoriteStatus()
        val currentState = _uiState.value
        val namesMap = allNamesByNationality.find { it.nationality == currentState.selectedNationality } ?: return
        val random = kotlin.random.Random.Default
        val names = if (currentState.selectedGender == Gender.MALE) namesMap.nomi_maschili else namesMap.nomi_femminili
        _uiState.update {
            it.copy(npc = it.npc?.copy(secondName = names.randomOrNull(random).orEmpty()))
        }
    }

    fun regenerateFamilyName() {
        resetFavoriteStatus()
        val currentState = _uiState.value
        val namesMap = allNamesByNationality.find { it.nationality == currentState.selectedNationality } ?: return
        val random = kotlin.random.Random.Default
        _uiState.update {
            it.copy(npc = it.npc?.copy(cognome = namesMap.cognomi.randomOrNull(random).orEmpty()))
        }
    }

    fun createCharacterFromNpc() {
        val currentNpc = _uiState.value.npc ?: return
        viewModelScope.launch(Dispatchers.IO) {
            val newCharacter = Character(
                name = buildString {
                    append(currentNpc.nome)
                    currentNpc.secondName?.let { append(" $it") }
                    append(" ${currentNpc.cognome}")
                }.trim()
            )
            val newId = characterRepository.saveCharacter(newCharacter)
            _navigationEvent.emit(NpcNavigationEvent.ToCharacterSheet(newId))
        }
    }

    fun toggleFavorite(npc: Npc) {
        viewModelScope.launch(Dispatchers.IO) {
            val existingFavoriteIndex = _favoriteNpcs.value.indexOfFirst {
                it.name == npc.nome && it.secondName == npc.secondName && it.familyName == npc.cognome
            }
            if (existingFavoriteIndex != -1) {
                _favoriteNpcs.update { currentFavorites -> // Usa .update
                    currentFavorites.toMutableList().also { updatedList ->
                        val favoriteNpc = updatedList[existingFavoriteIndex]
                        val newFavoriteNpc =
                            favoriteNpc.copy(isFavorite = !favoriteNpc.isFavorite) // Crea una nuova copia
                        if (newFavoriteNpc.isFavorite) {
                            updatedList[existingFavoriteIndex] = newFavoriteNpc
                        } else {
                            updatedList.removeAt(existingFavoriteIndex)
                        }
                    }
                }
            } else {
                _favoriteNpcs.update { currentFavorites -> // Usa .update
                    val newFavorite = FavoriteNpc(
                        name = npc.nome,
                        secondName = npc.secondName,
                        familyName = npc.cognome,
                        nationality = _uiState.value.selectedNationality ?: "",
                        isFavorite = true
                    )
                    currentFavorites + newFavorite
                }
            }
            _uiState.update { currentState -> // Usa .update e crea una *nuova* copia di Npc
                currentState.copy(
                    npc = currentState.npc?.copy(
                        isFavorite = _favoriteNpcs.value.any {
                            it.name == npc.nome && it.secondName == npc.secondName && it.familyName == npc.cognome && it.isFavorite
                        }
                    )
                )
            }
        }
    }

    private fun resetFavoriteStatus() {
        if (_uiState.value.npc?.isFavorite == true) {
            _uiState.update { it.copy(npc = it.npc?.copy(isFavorite = false)) }
        }
    }
}


sealed class NpcNationalityUiState {
    object Loading : NpcNationalityUiState()
    data class Success(val npcNationalities: List<String>) : NpcNationalityUiState()
    data class Error(val message: String) : NpcNationalityUiState()
}