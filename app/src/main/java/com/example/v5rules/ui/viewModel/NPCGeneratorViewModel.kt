package com.example.v5rules.ui.viewModel

import JSONReader
import android.content.res.Resources
import androidx.lifecycle.ViewModel
import com.example.v5rules.data.Gender
import com.example.v5rules.data.Npc
import com.example.v5rules.data.RegenerationType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class NPCGeneratorViewModel @Inject constructor(private val resources: Resources) : ViewModel() {

    data class UiState(
        val selectedGender: Gender,
        val includeSecondName: Boolean,
        val firstGeneration: Boolean,
        val selectedNationality: String?,
        val npc: Npc?,
        val selectedRegenerationTypes: Set<RegenerationType>
    )

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
        val nomiCognomiMap = JSONReader(resources, currentUiState.selectedNationality ?: "")

        val nomiMaschili = nomiCognomiMap["nomi_maschili"] ?: emptyList()
        val nomiFemminili = nomiCognomiMap["nomi_femminili"] ?: emptyList()
        val cognomi = nomiCognomiMap["cognomi"] ?: emptyList()
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

    private fun regenerateName() {
        val currentUiState = _uiState.value
        val nomiCognomiMap = JSONReader(resources, currentUiState.selectedNationality ?: "")
        val random = kotlin.random.Random.Default
        val nomiMaschili = nomiCognomiMap["nomi_maschili"] ?: emptyList()
        val nomiFemminili = nomiCognomiMap["nomi_femminili"] ?: emptyList()

        val nome = if (currentUiState.selectedGender == Gender.MALE) {
            nomiMaschili.randomOrNull(random)
        } else {
            nomiFemminili.randomOrNull(random)
        }

        _uiState.value = currentUiState.copy(
            npc = currentUiState.npc?.copy(nome = nome.orEmpty())
        )
    }

    private fun regenerateSecondName() {
        val currentUiState = _uiState.value
        val nomiCognomiMap = JSONReader(resources, currentUiState.selectedNationality ?: "")
        val random = kotlin.random.Random.Default
        val nomiMaschili = nomiCognomiMap["nomi_maschili"] ?: emptyList()
        val nomiFemminili = nomiCognomiMap["nomi_femminili"] ?: emptyList()

        val secondoNome = if (currentUiState.selectedGender == Gender.MALE) {
            nomiMaschili.randomOrNull(random)
        } else {
            nomiFemminili.randomOrNull(random)
        }

        _uiState.value = currentUiState.copy(
            npc = currentUiState.npc?.copy(secondName = secondoNome.orEmpty())
        )
    }

    fun regenerateCognome() {
        val currentUiState = _uiState.value
        val nomiCognomiMap = JSONReader(resources, currentUiState.selectedNationality ?: "")
        val random = kotlin.random.Random.Default
        val cognomi = nomiCognomiMap["cognomi"] ?: emptyList()

        val cognome = cognomi.randomOrNull(random)

        _uiState.value = currentUiState.copy(
            npc = currentUiState.npc?.copy(cognome = cognome.orEmpty())
        )
    }
}
