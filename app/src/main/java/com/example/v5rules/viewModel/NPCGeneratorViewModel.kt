package com.example.v5rules.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.v5rules.data.Gender
import com.example.v5rules.data.NationalityNpc
import com.example.v5rules.data.Npc
import com.example.v5rules.data.RegenerationType
import com.example.v5rules.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class NPCGeneratorViewModel @Inject constructor( private val mainRepository: MainRepository
) : ViewModel() {

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

    private val _uiState = MutableStateFlow(
        UiState(
            selectedGender = Gender.MALE,
            includeSecondName = false,
            firstGeneration = false,
            selectedNationality = "albanese",
            npc = Npc("", null, ""),
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
                _uiState.update {
                    it.copy(selectedNationality = nationalities.firstOrNull())
                }
            } catch (e: Exception) {
                _npc_nationality_uiState.value = NpcNationalityUiState.Error(
                    e.message ?: "Errore durante il caricamento delle lista di nomi per nazionalità"
                )
            }
        }
    }

    fun setSelectedGender(gender: Gender) {
        _uiState.value = _uiState.value.copy(selectedGender = gender)
        if (_uiState.value.firstGeneration) {
            regenerateName()
            if (_uiState.value.includeSecondName) regenerateSecondName()
        }
    }

    fun setIncludeSecondName(include: Boolean) {
        val currentUiState = _uiState.value
        if (!include) {
            removeSelectedRegenerationType(RegenerationType.SECOND_NAME)
            _uiState.value = currentUiState.copy(
                npc = currentUiState.npc?.copy(secondName = null)
            )
        } else {
            if (currentUiState.firstGeneration) regenerateSecondName()
        }
        _uiState.value = _uiState.value.copy(includeSecondName = include)
    }

    fun setSelectedNationality(nationality: String?) {

        _uiState.value = _uiState.value.copy(selectedNationality = nationality)
        if (_uiState.value.firstGeneration) {
            removeSelectedRegenerationType(RegenerationType.SECOND_NAME)
            generateNPC()
        }
    }

    fun addSelectedRegenerationType(type: RegenerationType) {
        val updatedTypes = _uiState.value.selectedRegenerationTypes.toMutableSet()
        updatedTypes.add(type)
        _uiState.value = _uiState.value.copy(selectedRegenerationTypes = updatedTypes)
    }

    fun removeSelectedRegenerationType(type: RegenerationType) {
        val updatedTypes = _uiState.value.selectedRegenerationTypes.toMutableSet()
        updatedTypes.remove(type)
        _uiState.value = _uiState.value.copy(selectedRegenerationTypes = updatedTypes)
    }

    fun generateNPC() {
        val currentUiState = _uiState.value
        val nomiCognomiMap =
            allNamesByNationality.find { it.nationality == currentUiState.selectedNationality }
        nomiCognomiMap?.let {
            val nomiMaschili = nomiCognomiMap.nomi_maschili
            val nomiFemminili = nomiCognomiMap.nomi_femminili
            val cognomi = nomiCognomiMap.cognomi
            val random = kotlin.random.Random.Default
            val nome = if (currentUiState.selectedGender == Gender.MALE) {
                nomiMaschili.randomOrNull(random)
            } else {
                nomiFemminili.randomOrNull(random)
            }
            val secondoNome =
                if (currentUiState.selectedGender == Gender.MALE && currentUiState.includeSecondName) {
                    nomiMaschili.randomOrNull(random)
                } else {
                    nomiFemminili.randomOrNull(random)
                }
            val cognome = cognomi.randomOrNull(random)

            if (!currentUiState.firstGeneration) {
                _uiState.value = currentUiState.copy(
                    firstGeneration = true,
                    npc = Npc(
                        nome = nome.orEmpty(),
                        secondName = if (currentUiState.includeSecondName) secondoNome.orEmpty() else null,
                        cognome = cognome.orEmpty()
                    )
                )
            } else {
                currentUiState.selectedRegenerationTypes.forEach { regenerationType ->
                    when (regenerationType) {
                        RegenerationType.NAME -> regenerateName()
                        RegenerationType.SECOND_NAME -> if (currentUiState.includeSecondName) regenerateSecondName()
                        RegenerationType.FAMILY_NAME -> regenerateCognome()
                        RegenerationType.ALL -> {
                            regenerateName()
                            regenerateCognome()
                            if (currentUiState.includeSecondName) regenerateSecondName()
                        }


                    }
                }
            }
        }
    }

    private fun regenerateName() {
        val currentUiState = _uiState.value
        val nomiCognomiMap =
            allNamesByNationality.find { it.nationality == currentUiState.selectedNationality }
        nomiCognomiMap?.let {
            val random = kotlin.random.Random.Default
            val nomiMaschili = nomiCognomiMap.nomi_maschili
            val nomiFemminili = nomiCognomiMap.nomi_femminili
            val nome = if (currentUiState.selectedGender == Gender.MALE) {
                nomiMaschili.randomOrNull(random)
            } else {
                nomiFemminili.randomOrNull(random)
            }
            _uiState.value = currentUiState.copy(
                npc = currentUiState.npc?.copy(nome = nome.orEmpty())
            )
        }

    }

    private fun regenerateSecondName() {
        val currentUiState = _uiState.value
        val nomiCognomiMap =
            allNamesByNationality.find { it.nationality == currentUiState.selectedNationality }
        nomiCognomiMap?.let {
            val random = kotlin.random.Random.Default
            val nomiMaschili = nomiCognomiMap.nomi_maschili
            val nomiFemminili = nomiCognomiMap.nomi_femminili

            val secondoNome = if (currentUiState.selectedGender == Gender.MALE) {
                nomiMaschili.randomOrNull(random)
            } else {
                nomiFemminili.randomOrNull(random)
            }
            _uiState.value = currentUiState.copy(
                npc = currentUiState.npc?.copy(secondName = secondoNome.orEmpty())
            )

        }
    }

    private fun regenerateCognome() {
        val currentUiState = _uiState.value
        val nomiCognomiMap =
            allNamesByNationality.find { it.nationality == currentUiState.selectedNationality }
        nomiCognomiMap?.let {
            val random = kotlin.random.Random.Default
            val cognomi = nomiCognomiMap.cognomi

            val cognome = cognomi.randomOrNull(random)

            _uiState.value = currentUiState.copy(
                npc = currentUiState.npc?.copy(cognome = cognome.orEmpty())
            )
        }
    }
}

sealed class NpcNationalityUiState {
    object Loading : NpcNationalityUiState()
    data class Success(val npcNationalities: List<String>) : NpcNationalityUiState()
    data class Error(val message: String) : NpcNationalityUiState()
}
