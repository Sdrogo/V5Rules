package com.example.v5rules.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.v5rules.data.FavoriteNpc
import com.example.v5rules.data.Gender
import com.example.v5rules.data.NationalityNpc
import com.example.v5rules.data.Npc
import com.example.v5rules.data.RegenerationType
import com.example.v5rules.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class NPCGeneratorViewModel @Inject constructor(private val mainRepository: MainRepository) :
    ViewModel() {

    data class UiState(
        val selectedGender: Gender,
        val includeSecondName: Boolean,
        val firstGeneration: Boolean,
        val selectedNationality: String?,
        val npc: Npc?,
        val selectedRegenerationTypes: Set<RegenerationType>
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
            selectedNationality = "albanese",
            npc = null, // Inizializza npc a null
            selectedRegenerationTypes = emptySet()
        )
    )
    val uiState: StateFlow<UiState> = _uiState

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
        _uiState.update { it.copy(selectedGender = gender) } // Usa .update e .copy
        if (_uiState.value.firstGeneration) {
            regenerateName()
            if (_uiState.value.includeSecondName) regenerateSecondName()
        }
    }

    fun setIncludeSecondName(include: Boolean) {
        _uiState.update {  // Usa .update e .copy
            if (!include) {
                _uiState.value.selectedRegenerationTypes.toMutableSet().remove(RegenerationType.SECOND_NAME)
                it.copy(
                    includeSecondName = false,
                    npc = it.npc?.copy(secondName = null) // Crea una nuova copia dell'NPC
                )
            } else {
                if (it.firstGeneration) regenerateSecondName()
                it.copy(includeSecondName = true) // Crea una nuova copia di UiState
            }
        }
    }

    fun setSelectedNationality(nationality: String?) {
        _uiState.update { // Usa .update e .copy
            it.copy(
                selectedNationality = nationality,
                npc = null, // Resetta l'NPC quando cambi nazionalità
                firstGeneration = false // Resetta firstGeneration
            )
        }
        generateNPC()
    }

    fun generateNPC() {
        _uiState.update { currentState -> // Usa .update
            resetFavoriteStatus() // Resetta *prima* di generare
            val nomiCognomiMap =
                allNamesByNationality.find { it.nationality == currentState.selectedNationality }
            nomiCognomiMap?.let {
                val nomiMaschili = it.nomi_maschili
                val nomiFemminili = it.nomi_femminili
                val cognomi = it.cognomi
                val random = kotlin.random.Random.Default
                val nome = if (currentState.selectedGender == Gender.MALE) {
                    nomiMaschili.randomOrNull(random)
                } else {
                    nomiFemminili.randomOrNull(random)
                }
                val secondoNome =
                    if (currentState.selectedGender == Gender.MALE && currentState.includeSecondName) {
                        nomiMaschili.randomOrNull(random)
                    } else {
                        nomiFemminili.randomOrNull(random)
                    }
                val cognome = cognomi.randomOrNull(random)
                if (!currentState.firstGeneration) {
                    _uiState.value = currentState.copy(
                        firstGeneration = true,
                        npc = Npc(
                            nome = nome.orEmpty(),
                            secondName = if (currentState.includeSecondName) secondoNome.orEmpty() else null,
                            cognome = cognome.orEmpty()
                        )
                    )
                } else {
                    currentState.selectedRegenerationTypes.forEach { regenerationType ->
                        when (regenerationType) {
                            RegenerationType.NAME -> regenerateName()
                            RegenerationType.SECOND_NAME -> if (currentState.includeSecondName) regenerateSecondName()
                            RegenerationType.FAMILY_NAME -> regenerateCognome()
                            RegenerationType.ALL -> {
                                regenerateName()
                                regenerateCognome()
                                if (currentState.includeSecondName) regenerateSecondName()
                            }
                        }
                    }
                }
                currentState.copy( // Crea una *nuova* copia di UiState
                    firstGeneration = true,
                    npc = currentState.npc
                )
            } ?: currentState // Se nomiCognomiMap è null, restituisci lo stato corrente
        }
    }


    private fun regenerateName() {
        _uiState.update { currentState -> // Usa .update
            resetFavoriteStatus()
            val nomiCognomiMap =
                allNamesByNationality.find { it.nationality == currentState.selectedNationality }
            nomiCognomiMap?.let {
                val random = kotlin.random.Random.Default
                val nomiMaschili = it.nomi_maschili
                val nomiFemminili = it.nomi_femminili
                val nome = if (currentState.selectedGender == Gender.MALE) {
                    nomiMaschili.randomOrNull(random)
                } else {
                    nomiFemminili.randomOrNull(random)
                }

                currentState.copy( // Crea una *nuova* copia di UiState
                    npc = currentState.npc?.copy(nome = nome.orEmpty()) // Crea una *nuova* copia di Npc
                )
            } ?: currentState
        }
    }

    private fun regenerateSecondName() {
        _uiState.update { currentState ->  // Usa .update
            resetFavoriteStatus()
            val nomiCognomiMap =
                allNamesByNationality.find { it.nationality == currentState.selectedNationality }
            nomiCognomiMap?.let {
                val random = kotlin.random.Random.Default
                val nomiMaschili = it.nomi_maschili
                val nomiFemminili = it.nomi_femminili

                val secondoNome = if (currentState.selectedGender == Gender.MALE) {
                    nomiMaschili.randomOrNull(random)
                } else {
                    nomiFemminili.randomOrNull(random)
                }
                currentState.copy( // Crea una *nuova* copia di UiState
                    npc = currentState.npc?.copy(secondName = secondoNome.orEmpty()) // Crea una *nuova* copia di Npc
                )
            } ?: currentState
        }
    }

    private fun regenerateCognome() {
        _uiState.update { currentState ->  // Usa .update
            resetFavoriteStatus()
            val nomiCognomiMap =
                allNamesByNationality.find { it.nationality == currentState.selectedNationality }
            nomiCognomiMap?.let {
                val random = kotlin.random.Random.Default
                val cognomi = it.cognomi

                val cognome = cognomi.randomOrNull(random)

                currentState.copy( // Crea una *nuova* copia di UiState
                    npc = currentState.npc?.copy(cognome = cognome.orEmpty()) // Crea una *nuova* copia di Npc
                )
            } ?: currentState
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

    fun toggleRegenerationType(type: RegenerationType) {
        val currentSelection = _uiState.value.selectedRegenerationTypes.toMutableSet() // Create a mutable copy
        if(type == RegenerationType.ALL){
            currentSelection.remove(RegenerationType.NAME)
            currentSelection.remove(RegenerationType.SECOND_NAME)
            currentSelection.remove(RegenerationType.FAMILY_NAME)
        }
        if (currentSelection.contains(type)) {
            currentSelection.remove(type)
        } else {
            if (currentSelection.contains(RegenerationType.ALL) && type != RegenerationType.ALL)
                currentSelection.remove(RegenerationType.ALL)
            currentSelection.add(type)
        }
        _uiState.update { currentState ->
            currentState.copy(selectedRegenerationTypes = currentSelection)
        }
    }

    private fun resetFavoriteStatus() {
        _uiState.update { currentState -> // Usa .update
            currentState.copy(
                npc = currentState.npc?.copy(isFavorite = false) // Crea una *nuova* copia di Npc
            )
        }
    }
}

sealed class NpcNationalityUiState {
    object Loading : NpcNationalityUiState()
    data class Success(val npcNationalities: List<String>) : NpcNationalityUiState()
    data class Error(val message: String) : NpcNationalityUiState()
}