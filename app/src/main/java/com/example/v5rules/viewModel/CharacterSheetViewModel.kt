package com.example.v5rules.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.v5rules.data.*
import com.example.v5rules.repository.CharacterRepository
import com.example.v5rules.repository.MainRepository
import com.example.v5rules.utils.CharacterSheetEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

sealed class DialogState {
    data object None : DialogState()
    data object ShowSaveConfirmation : DialogState()
    data object ShowCleanupConfirmation : DialogState()
    data object ShowDeleteConfirmation : DialogState()
}

@HiltViewModel
class CharacterSheetViewModel @Inject constructor(
    private val mainRepository: MainRepository,
    private val characterRepository: CharacterRepository
) : ViewModel() {

    data class CharacterSheetState(
        val character: Character = Character(),
        val isLoading: Boolean = false,
        val isSaving: Boolean = false,
        val error: String? = null,
        val message: String = "",
        val dialogState: DialogState = DialogState.None
    )

    private val _uiState = MutableStateFlow(CharacterSheetState())
    val uiState: StateFlow<CharacterSheetState> = _uiState.asStateFlow()

    private val _clans = MutableStateFlow<List<Clan>>(emptyList())
    val clans: StateFlow<List<Clan>> = _clans.asStateFlow()

    private val _predator = MutableStateFlow<List<PredatorType>>(emptyList())
    val predator: StateFlow<List<PredatorType>> = _predator.asStateFlow()

    private val _disciplines = MutableStateFlow<List<Discipline>>(emptyList())
    val disciplines: StateFlow<List<Discipline>> = _disciplines.asStateFlow()

    private val _loreSheets = MutableStateFlow<List<Loresheet>>(emptyList())
    val loreSheets: StateFlow<List<Loresheet>> = _loreSheets.asStateFlow()

    private val _allBackgrounds = MutableStateFlow<List<Background>>(emptyList())
    val allBackgrounds: StateFlow<List<Background>> = _allBackgrounds.asStateFlow()

    private val _directFlaws = MutableStateFlow<List<Advantage>>(emptyList())
    val directFlaws: StateFlow<List<Advantage>> = _directFlaws.asStateFlow()

    private val _selectedTabIndex = MutableStateFlow(0)
    val selectedTabIndex: StateFlow<Int> = _selectedTabIndex.asStateFlow()

    private val eventChannel = Channel<CharacterSheetEvent>()

    init {
        loadStaticData()
        handleEvents()
    }

    private fun handleEvents() {
        viewModelScope.launch {
            eventChannel.consumeAsFlow().collect { event ->
                when (event) {
                    is CharacterSheetEvent.ConfirmSave -> {
                        saveSheet()
                        _uiState.update { it.copy(dialogState = DialogState.None) }
                    }

                    is CharacterSheetEvent.ConfirmCleanup -> {
                        cleanupSheet()
                        _uiState.update { it.copy(dialogState = DialogState.None) }
                    }

                    is CharacterSheetEvent.ConfirmDelete -> {
                        deleteSheet()
                        _uiState.update { it.copy(dialogState = DialogState.None) }
                    }

                    is CharacterSheetEvent.ShowSaveConfirmation -> _uiState.update {
                        it.copy(
                            dialogState = DialogState.ShowSaveConfirmation
                        )
                    }

                    is CharacterSheetEvent.ShowCleanupConfirmation -> _uiState.update {
                        it.copy(
                            dialogState = DialogState.ShowCleanupConfirmation
                        )
                    }

                    is CharacterSheetEvent.ShowDeleteConfirmation -> _uiState.update {
                        it.copy(
                            dialogState = DialogState.ShowDeleteConfirmation
                        )
                    }

                    is CharacterSheetEvent.DismissDialog -> _uiState.update { it.copy(dialogState = DialogState.None) }
                    is CharacterSheetEvent.SaveClicked -> saveSheet()
                    is CharacterSheetEvent.DeleteClicked -> deleteSheet()
                    is CharacterSheetEvent.CleanupClicked -> cleanupSheet()

                    // Character Info
                    is CharacterSheetEvent.NameChanged -> _uiState.update {
                        it.copy(
                            character = it.character.updateName(
                                event.name
                            )
                        )
                    }

                    is CharacterSheetEvent.ClanChanged -> _uiState.update {
                        it.copy(
                            character = it.character.updateClan(
                                event.clan
                            )
                        )
                    }

                    is CharacterSheetEvent.PredatorChanged -> _uiState.update {
                        it.copy(
                            character = it.character.updatePredator(
                                event.predator
                            )
                        )
                    }

                    is CharacterSheetEvent.GenerationChanged -> _uiState.update {
                        it.copy(
                            character = it.character.updateGeneration(
                                event.generation
                            )
                        )
                    }

                    is CharacterSheetEvent.SireChanged -> _uiState.update {
                        it.copy(
                            character = it.character.updateSire(
                                event.sire
                            )
                        )
                    }

                    is CharacterSheetEvent.ConceptChanged -> _uiState.update {
                        it.copy(
                            character = it.character.updateConcept(
                                event.concept
                            )
                        )
                    }

                    is CharacterSheetEvent.AmbitionChanged -> _uiState.update {
                        it.copy(
                            character = it.character.updateAmbition(
                                event.ambition
                            )
                        )
                    }

                    is CharacterSheetEvent.DesireChanged -> _uiState.update {
                        it.copy(
                            character = it.character.updateDesire(
                                event.desire
                            )
                        )
                    }

                    // Attributes
                    is CharacterSheetEvent.StrengthChanged -> _uiState.update {
                        it.copy(
                            character = it.character.updateStrength(
                                event.strength
                            )
                        )
                    }

                    is CharacterSheetEvent.DexterityChanged -> _uiState.update {
                        it.copy(
                            character = it.character.updateDexterity(
                                event.dexterity
                            )
                        )
                    }

                    is CharacterSheetEvent.StaminaChanged -> _uiState.update {
                        it.copy(
                            character = it.character.updateStamina(
                                event.stamina
                            )
                        )
                    }

                    is CharacterSheetEvent.CharismaChanged -> _uiState.update {
                        it.copy(
                            character = it.character.updateCharisma(
                                event.charisma
                            )
                        )
                    }

                    is CharacterSheetEvent.ManipulationChanged -> _uiState.update {
                        it.copy(
                            character = it.character.updateManipulation(event.manipulation)
                        )
                    }

                    is CharacterSheetEvent.ComposureChanged -> _uiState.update {
                        it.copy(
                            character = it.character.updateComposure(
                                event.composure
                            )
                        )
                    }

                    is CharacterSheetEvent.IntelligenceChanged -> _uiState.update {
                        it.copy(
                            character = it.character.updateIntelligence(event.intelligence)
                        )
                    }

                    is CharacterSheetEvent.WitsChanged -> _uiState.update {
                        it.copy(
                            character = it.character.updateWits(
                                event.wits
                            )
                        )
                    }

                    is CharacterSheetEvent.ResolveChanged -> _uiState.update {
                        it.copy(
                            character = it.character.updateResolve(
                                event.resolve
                            )
                        )
                    }

                    // Abilities
                    is CharacterSheetEvent.AbilityChanged -> _uiState.update {
                        it.copy(
                            character = it.character.updateAbilityLevel(
                                event.abilityName,
                                event.level
                            )
                        )
                    }

                    is CharacterSheetEvent.AbilitySpecializationChanged -> _uiState.update {
                        it.copy(
                            character = it.character.updateAbilitySpecialization(
                                event.abilityName,
                                event.specialization
                            )
                        )
                    }

                    // Trackers
                    is CharacterSheetEvent.HealthBoxClicked -> _uiState.update {
                        it.copy(
                            character = it.character.toggleHealthBox(
                                event.index
                            )
                        )
                    }

                    is CharacterSheetEvent.WillpowerBoxClicked -> _uiState.update {
                        it.copy(
                            character = it.character.toggleWillpowerBox(event.index)
                        )
                    }

                    is CharacterSheetEvent.HungerChanged -> _uiState.update {
                        it.copy(
                            character = it.character.updateHunger(
                                event.newHunger
                            )
                        )
                    }

                    is CharacterSheetEvent.HumanityChanged -> _uiState.update {
                        it.copy(
                            character = it.character.updateHumanity(
                                event.current
                            )
                        )
                    }

                    is CharacterSheetEvent.StainsChanged -> _uiState.update {
                        it.copy(
                            character = it.character.updateStains(
                                event.stains
                            )
                        )
                    }

                    is CharacterSheetEvent.TotalExperienceChanged -> _uiState.update {
                        it.copy(
                            character = it.character.updateTotalExperience(event.total)
                        )
                    }

                    is CharacterSheetEvent.SpentExperienceChanged -> _uiState.update {
                        it.copy(
                            character = it.character.updateSpentExperience(event.spent)
                        )
                    }

                    // Disciplines & Powers
                    is CharacterSheetEvent.DisciplineChanged -> _uiState.update {
                        it.copy(
                            character = it.character.addDiscipline(
                                event.discipline
                            )
                        )
                    }

                    is CharacterSheetEvent.DisciplineLevelChanged -> _uiState.update {
                        it.copy(
                            character = it.character.updateDisciplineLevel(
                                event.discipline.title,
                                event.newLevel
                            )
                        )
                    }

                    is CharacterSheetEvent.DisciplinePowerAdded -> _uiState.update {
                        it.copy(
                            character = it.character.addDisciplinePower(
                                event.disciplineName,
                                event.power
                            )
                        )
                    }

                    is CharacterSheetEvent.DisciplinePowerRemoved -> _uiState.update {
                        it.copy(
                            character = it.character.removeDisciplinePower(
                                event.disciplineName,
                                event.power
                            )
                        )
                    }

                    // Rituals
                    is CharacterSheetEvent.AddRitual -> _uiState.update {
                        it.copy(
                            character = it.character.addRitual(
                                event.disciplineName,
                                event.ritual
                            )
                        )
                    }

                    is CharacterSheetEvent.RemoveRitual -> _uiState.update {
                        it.copy(
                            character = it.character.removeRitual(
                                event.ritualId
                            )
                        )
                    }

                    is CharacterSheetEvent.UpdateRitualLevel -> _uiState.update {
                        it.copy(
                            character = it.character.updateRitualLevel(
                                event.ritual.title,
                                event.level
                            )
                        )
                    }

                    is CharacterSheetEvent.RitualPowerAdded -> _uiState.update {
                        it.copy(
                            character = it.character.addRitualPower(
                                event.ritual.title,
                                event.power
                            )
                        )
                    }

                    is CharacterSheetEvent.RitualPowerRemoved -> _uiState.update {
                        it.copy(
                            character = it.character.removeRitualPower(
                                event.ritual.title,
                                event.power.id
                            )
                        )
                    }

                    // Loresheets
                    is CharacterSheetEvent.LoresheetAdded -> _uiState.update {
                        it.copy(
                            character = it.character.addLoresheet(
                                event.loresheet,
                                event.level
                            )
                        )
                    }

                    is CharacterSheetEvent.LoresheetRemoved -> _uiState.update {
                        it.copy(
                            character = it.character.removeLoresheet(
                                event.loresheet.title
                            )
                        )
                    }

                    is CharacterSheetEvent.LoresheetLevelChanged -> _uiState.update {
                        it.copy(
                            character = it.character.updateLoresheetLevel(
                                event.loresheetName,
                                event.level
                            )
                        )
                    }

                    // Backgrounds & Advantages
                    is CharacterSheetEvent.BackgroundAdded -> _uiState.update {
                        it.copy(
                            character = it.character.addBackground(
                                event.background,
                                event.level
                            )
                        )
                    }

                    is CharacterSheetEvent.BackgroundRemoved -> _uiState.update {
                        it.copy(
                            character = it.character.removeBackground(
                                event.background.identifier.orEmpty()
                            )
                        )
                    }

                    is CharacterSheetEvent.BackgroundLevelChanged -> _uiState.update {
                        it.copy(
                            character = it.character.updateBackgroundLevel(
                                event.background.identifier ?: "", event.newLevel
                            )
                        )
                    }

                    is CharacterSheetEvent.BackgroundMeritAdded -> _uiState.update {
                        it.copy(
                            character = it.character.addBackgroundMerit(
                                event.background.identifier ?: "", event.merit, event.level
                            )
                        )
                    }

                    is CharacterSheetEvent.BackgroundMeritRemoved -> _uiState.update {
                        it.copy(
                            character = it.character.removeBackgroundMerit(
                                event.background.identifier ?: "", event.merit.identifier ?: ""
                            )
                        )
                    }

                    is CharacterSheetEvent.BackgroundMeritLevelChanged -> _uiState.update {
                        it.copy(
                            character = it.character.updateBackgroundMeritLevel(
                                event.background.identifier ?: "",
                                event.merit.identifier ?: "",
                                event.newLevel
                            )
                        )
                    }

                    is CharacterSheetEvent.BackgroundFlawAdded -> _uiState.update {
                        it.copy(
                            character = it.character.addBackgroundFlaw(
                                event.background.identifier ?: "", event.flaw, event.level
                            )
                        )
                    }

                    is CharacterSheetEvent.BackgroundFlawRemoved -> _uiState.update {
                        it.copy(
                            character = it.character.removeBackgroundFlaw(
                                event.background.identifier ?: "", event.flaw.identifier ?: ""
                            )
                        )
                    }

                    is CharacterSheetEvent.BackgroundFlawLevelChanged -> _uiState.update {
                        it.copy(
                            character = it.character.updateBackgroundFlawLevel(
                                event.background.identifier ?: "",
                                event.flaw.identifier ?: "",
                                event.newLevel
                            )
                        )
                    }

                    is CharacterSheetEvent.CharacterDirectFlawAdded -> _uiState.update {
                        it.copy(
                            character = it.character.addDirectFlaw(event.directFlaw, event.level)
                        )
                    }

                    is CharacterSheetEvent.CharacterDirectFlawRemoved -> _uiState.update {
                        it.copy(
                            character = it.character.removeDirectFlaw(
                                event.directFlaw.identifier ?: ""
                            )
                        )
                    }

                    is CharacterSheetEvent.CharacterDirectFlawLevelChanged -> _uiState.update {
                        it.copy(
                            character = it.character.updateDirectFlawLevel(
                                event.directFlaw.identifier ?: "", event.newLevel
                            )
                        )
                    }

                    // Notes
                    is CharacterSheetEvent.AddNoteToBackground -> _uiState.update {
                        it.copy(
                            character = it.character.updateBackgroundNote(
                                event.background.identifier ?: "", event.note
                            )
                        )
                    }

                    is CharacterSheetEvent.RemoveNoteToBackground -> _uiState.update {
                        it.copy(
                            character = it.character.updateBackgroundNote(
                                event.background.identifier ?: "", null
                            )
                        )
                    }

                    is CharacterSheetEvent.AddNoteToMerit -> _uiState.update {
                        it.copy(
                            character = it.character.updateMeritNote(
                                event.background.identifier ?: "",
                                event.merit.identifier ?: "",
                                event.note
                            )
                        )
                    }

                    is CharacterSheetEvent.RemoveNoteToMerit -> _uiState.update {
                        it.copy(
                            character = it.character.updateMeritNote(
                                event.background.identifier ?: "",
                                event.merit.identifier ?: "",
                                null
                            )
                        )
                    }

                    is CharacterSheetEvent.AddNoteToFlaw -> _uiState.update {
                        it.copy(
                            character = it.character.updateFlawNote(
                                event.background.identifier ?: "",
                                event.flaw.identifier ?: "",
                                event.note
                            )
                        )
                    }

                    is CharacterSheetEvent.RemoveNoteToFlaw -> _uiState.update {
                        it.copy(
                            character = it.character.updateFlawNote(
                                event.background.identifier ?: "",
                                event.flaw.identifier ?: "",
                                null
                            )
                        )
                    }

                    is CharacterSheetEvent.AddNoteToDirectFlaw -> _uiState.update {
                        it.copy(
                            character = it.character.updateDirectFlawNote(
                                event.advantage.identifier ?: "", event.note
                            )
                        )
                    }

                    is CharacterSheetEvent.RemoveNoteToDirectFlaw -> _uiState.update {
                        it.copy(
                            character = it.character.updateDirectFlawNote(
                                event.advantage.identifier ?: "", null
                            )
                        )
                    }

                    is CharacterSheetEvent.AddNoteToBackgroundFlaw -> _uiState.update {
                        it.copy(
                            character = it.character.updateFlawNote(
                                event.background.identifier ?: "",
                                event.flaw.identifier ?: "",
                                event.note
                            )
                        )
                    }

                    is CharacterSheetEvent.RemoveNoteToBackgroundFlaw -> _uiState.update {
                        it.copy(
                            character = it.character.updateFlawNote(
                                event.background.identifier ?: "",
                                event.flaw.identifier ?: "",
                                null
                            )
                        )
                    }

                    // Legacy/Others
                    is CharacterSheetEvent.AdvantageAdded -> {
                        _uiState.update {
                            it.copy(
                                character = it.character.addAdvantage(
                                    event.advantage,
                                    event.background,
                                    event.level
                                )
                            )
                        }
                    }

                    is CharacterSheetEvent.AdvantageRemoved -> {
                        _uiState.update {
                            it.copy(
                                character = it.character.removeAdvantage(
                                    event.advantage,
                                    event.background
                                )
                            )
                        }
                    }

                    is CharacterSheetEvent.AdvantageLevelChanged -> {
                        _uiState.update {
                            it.copy(
                                character = it.character.updateAdvantage(
                                    event.advantage,
                                    event.background,
                                    event.level
                                )
                            )
                        }
                    }

                    is CharacterSheetEvent.AdvantageFlawAdded -> {
                        _uiState.update {
                            it.copy(
                                character = it.character.addAdvantageFlaw(
                                    event.advantage,
                                    event.background,
                                    event.level
                                )
                            )
                        }
                    }

                    is CharacterSheetEvent.AdvantageFlawRemoved -> {
                        _uiState.update {
                            it.copy(
                                character = it.character.removeAdvantageFlaw(
                                    event.advantage,
                                    event.background
                                )
                            )
                        }
                    }

                    is CharacterSheetEvent.AdvantageFlawLevelChanged -> {
                        _uiState.update {
                            it.copy(
                                character = it.character.updateAdvantageFlawLevel(
                                    event.advantage,
                                    event.background,
                                    event.level
                                )
                            )
                        }
                    }
                }
            }
        }
    }

    private fun loadStaticData() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _uiState.update { it.copy(isLoading = true) }
                val locale = Locale.getDefault()
                _clans.value = mainRepository.loadClans(locale)
                _predator.value = mainRepository.loadPredatorType(locale)
                _disciplines.value = mainRepository.loadDisciplines(locale)
                _loreSheets.value = mainRepository.loadLoresheet(locale).sortedBy { it.title }
                _allBackgrounds.value = mainRepository.loadBackground(locale).sortedBy { it.title }
                _directFlaws.value =
                    _allBackgrounds.value.flatMap { it.directFlaws }.sortedBy { it.title }
                _uiState.update { it.copy(isLoading = false) }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = "Errore caricamento dati: ${e.message}"
                    )
                }
            }
        }
    }

    fun setCharacter(id: String) {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            try {
                val character = characterRepository.getCharacter(id) ?: Character()
                _uiState.update {
                    it.copy(
                        character = character,
                        isLoading = false,
                        isSaving = true,
                        error = null
                    )
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, isSaving = false, error = e.message) }
            }
        }
    }

    private fun cleanupSheet() {
        _uiState.update { it.copy(character = Character(), error = null) }
    }

    private fun deleteSheet() {
        val character = _uiState.value.character
        if (character.id.isBlank()) return

        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            try {
                characterRepository.deleteCharacter(character)
                _uiState.update {
                    it.copy(
                        character = Character(),
                        isLoading = false,
                        error = null
                    )
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }

    private fun saveSheet() {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            try {
                val id = characterRepository.saveCharacter(_uiState.value.character)
                _uiState.update {
                    it.copy(
                        character = it.character.copy(id = id),
                        isLoading = false,
                        error = null
                    )
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }

    fun onEvent(event: CharacterSheetEvent) {
        viewModelScope.launch { eventChannel.send(event) }
    }

    fun selectTab(tab: Int) {
        _selectedTabIndex.value = tab
    }
}
