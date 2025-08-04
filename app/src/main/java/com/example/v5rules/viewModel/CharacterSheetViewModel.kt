package com.example.v5rules.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.v5rules.data.Ability
import com.example.v5rules.data.Advantage
import com.example.v5rules.data.Background
import com.example.v5rules.data.Character
import com.example.v5rules.data.Clan
import com.example.v5rules.data.DamageType
import com.example.v5rules.data.Discipline
import com.example.v5rules.data.Loresheet
import com.example.v5rules.data.PredatorType
import com.example.v5rules.data.Ritual
import com.example.v5rules.repository.CharacterRepository
import com.example.v5rules.repository.MainRepository
import com.example.v5rules.utils.CharacterSheetEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.Locale
import java.util.UUID
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

    // Stato
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
    private val _backgrounds = MutableStateFlow<List<Background>>(emptyList())
    val backgrounds: StateFlow<List<Background>> = _backgrounds.asStateFlow()
    private val _directFlaws = MutableStateFlow<List<Advantage>>(emptyList())
    val directFlaws: StateFlow<List<Advantage>> = _directFlaws.asStateFlow()
    private val _selectedTabIndex = MutableStateFlow(0)
    val selectedTabIndex: StateFlow<Int> = _selectedTabIndex.asStateFlow()

    private val eventChannel = Channel<CharacterSheetEvent>()

    init {
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
                    is CharacterSheetEvent.NameChanged -> _uiState.update {
                        it.copy(
                            character = it.character.copy(
                                name = event.name
                            )
                        )
                    }

                    is CharacterSheetEvent.ClanChanged -> {
                        _uiState.update { it.copy(character = it.character.copy(clan = event.clan)) }
                    }

                    is CharacterSheetEvent.PredatorChanged -> {
                        _uiState.update { it.copy(character = it.character.copy(predator = event.predator)) }
                    }

                    is CharacterSheetEvent.GenerationChanged -> _uiState.update {
                        it.copy(
                            character = it.character.copy(
                                generation = event.generation
                            )
                        )
                    }

                    is CharacterSheetEvent.SireChanged -> _uiState.update {
                        it.copy(
                            character = it.character.copy(
                                sire = event.sire
                            )
                        )
                    }

                    is CharacterSheetEvent.ConceptChanged -> _uiState.update {
                        it.copy(
                            character = it.character.copy(
                                concept = event.concept
                            )
                        )
                    }

                    is CharacterSheetEvent.AmbitionChanged -> _uiState.update {
                        it.copy(
                            character = it.character.copy(
                                ambition = event.ambition
                            )
                        )
                    }

                    is CharacterSheetEvent.DesireChanged -> _uiState.update {
                        it.copy(
                            character = it.character.copy(
                                desire = event.desire
                            )
                        )
                    }
                    // Attributes
                    is CharacterSheetEvent.StrengthChanged -> _uiState.update {
                        it.copy(
                            character = it.character.copy(
                                attributes = it.character.attributes.copy(strength = event.strength)
                            )
                        )
                    }

                    is CharacterSheetEvent.DexterityChanged -> _uiState.update {
                        it.copy(
                            character = it.character.copy(
                                attributes = it.character.attributes.copy(dexterity = event.dexterity)
                            )
                        )
                    }

                    is CharacterSheetEvent.StaminaChanged -> {
                        _uiState.update { currentState ->
                            val newStamina = event.stamina
                            val newCharacter = currentState.character.copy(
                                attributes = currentState.character.attributes.copy(stamina = newStamina)
                            )

                            val newMaxHealthBoxes =
                                calculateMaxHealthBoxes(newStamina)

                            val updatedHealthBoxes = synchronizeDamageTrack(
                                currentBoxes = newCharacter.health.boxes,
                                newSize = newMaxHealthBoxes
                            )

                            currentState.copy(
                                character = newCharacter.copy(
                                    health = newCharacter.health.copy(boxes = updatedHealthBoxes)
                                )
                            )
                        }
                    }

                    is CharacterSheetEvent.CharismaChanged -> _uiState.update {
                        it.copy(
                            character = it.character.copy(
                                attributes = it.character.attributes.copy(charisma = event.charisma)
                            )
                        )
                    }

                    is CharacterSheetEvent.ManipulationChanged -> _uiState.update {
                        it.copy(
                            character = it.character.copy(
                                attributes = it.character.attributes.copy(manipulation = event.manipulation)
                            )
                        )
                    }

                    is CharacterSheetEvent.ComposureChanged -> {
                        _uiState.update { currentState ->
                            val newComposure = event.composure
                            var updatedCharacter = currentState.character.copy(
                                attributes = currentState.character.attributes.copy(composure = newComposure)
                            )

                            val newMaxWillpower = calculateMaxWillpowerBoxes(
                                resolve = updatedCharacter.attributes.resolve,
                                composure = newComposure
                            )

                            val updatedWillpowerBoxes = synchronizeDamageTrack(
                                currentBoxes = updatedCharacter.willpower.boxes,
                                newSize = newMaxWillpower
                            )

                            updatedCharacter = updatedCharacter.copy(
                                willpower = updatedCharacter.willpower.copy(boxes = updatedWillpowerBoxes)
                            )
                            currentState.copy(character = updatedCharacter)
                        }
                    }

                    is CharacterSheetEvent.IntelligenceChanged -> _uiState.update {
                        it.copy(
                            character = it.character.copy(
                                attributes = it.character.attributes.copy(intelligence = event.intelligence)
                            )
                        )
                    }

                    is CharacterSheetEvent.WitsChanged -> _uiState.update {
                        it.copy(
                            character = it.character.copy(
                                attributes = it.character.attributes.copy(wits = event.wits)
                            )
                        )
                    }

                    is CharacterSheetEvent.ResolveChanged -> {
                        _uiState.update { currentState ->
                            val newResolve = event.resolve
                            var updatedCharacter = currentState.character.copy(
                                attributes = currentState.character.attributes.copy(resolve = newResolve)
                            )

                            val newMaxWillpower = calculateMaxWillpowerBoxes(
                                resolve = newResolve,
                                composure = updatedCharacter.attributes.composure // Usa il valore corrente di composure
                            )

                            val updatedWillpowerBoxes = synchronizeDamageTrack(
                                currentBoxes = updatedCharacter.willpower.boxes,
                                newSize = newMaxWillpower
                            )

                            updatedCharacter = updatedCharacter.copy(
                                willpower = updatedCharacter.willpower.copy(boxes = updatedWillpowerBoxes)
                            )
                            currentState.copy(character = updatedCharacter)
                        }
                    }
                    //Ability
                    is CharacterSheetEvent.AbilityChanged -> {
                        _uiState.update { currentState ->
                            val character = currentState.character
                            val abilityExists = character.abilities.any { it.name == event.abilityName }

                            val updatedAbilities = if (abilityExists) {
                                // MODO SICURO: Usa map per creare una nuova lista aggiornata.
                                character.abilities.map {
                                    if (it.name == event.abilityName) it.copy(level = event.level) else it
                                }
                            } else {
                                character.abilities + Ability(name = event.abilityName, level = event.level)
                            }
                            currentState.copy(character = character.copy(abilities = updatedAbilities))
                        }
                    }

                    is CharacterSheetEvent.HealthBoxClicked -> {
                        _uiState.update { currentState ->
                            val currentHealthBoxes =
                                currentState.character.health.boxes.toMutableList()
                            val clickedIndex = event.index

                            if (clickedIndex < currentHealthBoxes.size) {
                                val currentDamageType = currentHealthBoxes[clickedIndex]
                                val nextDamageType = when (currentDamageType) {
                                    DamageType.EMPTY -> DamageType.SUPERFICIAL
                                    DamageType.SUPERFICIAL -> DamageType.AGGRAVATED
                                    DamageType.AGGRAVATED -> DamageType.EMPTY
                                }
                                currentHealthBoxes[clickedIndex] = nextDamageType

                                currentState.copy(
                                    character = currentState.character.copy(
                                        health = currentState.character.health.copy(boxes = currentHealthBoxes)
                                    )
                                )
                            } else {
                                currentState
                            }
                        }
                    }

                    is CharacterSheetEvent.WillpowerBoxClicked -> {
                        _uiState.update { currentState ->
                            val currentWillpowerBoxes =
                                currentState.character.willpower.boxes.toMutableList()
                            val clickedIndex = event.index

                            if (clickedIndex < currentWillpowerBoxes.size) {
                                val currentDamageType = currentWillpowerBoxes[clickedIndex]
                                val nextDamageType = when (currentDamageType) {
                                    DamageType.EMPTY -> DamageType.SUPERFICIAL
                                    DamageType.SUPERFICIAL -> DamageType.AGGRAVATED
                                    DamageType.AGGRAVATED -> DamageType.EMPTY
                                }
                                currentWillpowerBoxes[clickedIndex] = nextDamageType
                                currentState.copy(
                                    character = currentState.character.copy(
                                        willpower = currentState.character.willpower.copy(boxes = currentWillpowerBoxes)
                                    )
                                )
                            } else {
                                currentState
                            }
                        }
                    }

                    is CharacterSheetEvent.TotalExperienceChanged -> _uiState.update {
                        it.copy(
                            character = it.character.copy(
                                experience = it.character.experience.copy(total = event.total)
                            )
                        )
                    }

                    is CharacterSheetEvent.SpentExperienceChanged -> _uiState.update {
                        it.copy(
                            character = it.character.copy(
                                experience = it.character.experience.copy(spent = event.spent)
                            )
                        )
                    }

                    is CharacterSheetEvent.AbilitySpecializationChanged -> {
                        _uiState.update { currentState ->
                            val character = currentState.character
                            val abilityExists = character.abilities.any { it.name == event.abilityName }

                            val updatedAbilities = if (abilityExists) {
                                character.abilities.map {
                                    if (it.name == event.abilityName) it.copy(specialization = event.specialization) else it
                                }
                            } else {
                                character.abilities + Ability(name = event.abilityName, specialization = event.specialization)
                            }
                            currentState.copy(character = character.copy(abilities = updatedAbilities))
                        }
                    }

                    is CharacterSheetEvent.DisciplineChanged -> {
                        _uiState.update { currentState ->
                            val currentDisciplines =
                                currentState.character.disciplines.toMutableList()
                            // Controlla se la disciplina è già presente
                            val disciplineIndex =
                                currentDisciplines.indexOfFirst { it.title == event.discipline.title }

                            if (disciplineIndex == -1) {
                                //Se non è presente, aggiungila
                                val newDiscipline = event.discipline.copy(level = 1)
                                if (newDiscipline.id == "d9" || newDiscipline.id == "d10") {
                                    newDiscipline.ritual?.let {
                                        addRitual(
                                            disciplineName = newDiscipline.title,
                                            ritual = it
                                        )
                                    }
                                }
                                currentState.copy(
                                    character = currentState.character.copy(
                                        disciplines = currentDisciplines + newDiscipline
                                    )
                                )
                            } else {
                                //Se è presente, non fare nulla
                                currentState
                            }
                        }
                    }

                    is CharacterSheetEvent.DisciplinePowerAdded -> {
                        _uiState.update { currentState ->
                            val updatedDisciplines = currentState.character.disciplines.map { discipline ->
                                if (discipline.title == event.disciplineName) {
                                    if (discipline.selectedDisciplinePowers.none { it.id == event.power.id }) {
                                        discipline.copy(selectedDisciplinePowers = discipline.selectedDisciplinePowers + event.power)
                                    } else {
                                        discipline
                                    }
                                } else {
                                    discipline
                                }
                            }
                            currentState.copy(character = currentState.character.copy(disciplines = updatedDisciplines))
                        }
                    }

                    is CharacterSheetEvent.DisciplinePowerRemoved -> {
                        _uiState.update { currentState ->
                            val updatedDisciplines = currentState.character.disciplines.map { discipline ->
                                if (discipline.title == event.disciplineName) {
                                    discipline.copy(selectedDisciplinePowers = discipline.selectedDisciplinePowers.filterNot { it.id == event.power.id })
                                } else {
                                    discipline
                                }
                            }
                            currentState.copy(character = currentState.character.copy(disciplines = updatedDisciplines))
                        }
                    }

                    is CharacterSheetEvent.RitualPowerAdded -> {
                        _uiState.update { currentState ->
                            val updatedRituals = currentState.character.learnedRituals.map { ritual ->
                                if (ritual.title == event.ritual.title) {
                                    if (ritual.ritualsPowers.none { it.id == event.power.id }) {
                                        ritual.copy(ritualsPowers = ritual.ritualsPowers + event.power)
                                    } else {
                                        ritual
                                    }
                                } else {
                                    ritual
                                }
                            }
                            currentState.copy(character = currentState.character.copy(learnedRituals = updatedRituals))
                        }
                    }

                    is CharacterSheetEvent.RitualPowerRemoved -> {
                        _uiState.update { currentState ->
                            val updatedRituals = currentState.character.learnedRituals.map { ritual ->
                                if (ritual.title == event.ritual.title) {
                                    ritual.copy(ritualsPowers = ritual.ritualsPowers.filterNot { it.id == event.power.id })
                                } else {
                                    ritual
                                }
                            }
                            currentState.copy(character = currentState.character.copy(learnedRituals = updatedRituals))
                        }
                    }

                    is CharacterSheetEvent.DisciplineLevelChanged -> {
                        _uiState.update { currentState ->
                            val character = currentState.character
                            val disciplineTitle = event.discipline.title

                            if (event.newLevel == 0) {
                                // MODO SICURO: Creiamo nuove liste filtrando gli elementi da rimuovere.
                                // Non usiamo più indici, quindi non può esserci un crash.
                                val updatedDisciplines = character.disciplines.filterNot { it.title == disciplineTitle }
                                val updatedRituals = character.learnedRituals.filterNot { it.title == disciplineTitle }

                                currentState.copy(
                                    character = character.copy(
                                        disciplines = updatedDisciplines,
                                        learnedRituals = updatedRituals
                                    )
                                )
                            } else {
                                // MODO SICURO: Usiamo `map` per aggiornare l'elemento corretto senza rischiare errori di indice.
                                val updatedDisciplines = character.disciplines.map { discipline ->
                                    if (discipline.title == disciplineTitle) {
                                        val validPowers = discipline.selectedDisciplinePowers.filter { power ->
                                            power.level <= event.newLevel
                                        }
                                        discipline.copy(
                                            level = event.newLevel,
                                            selectedDisciplinePowers = validPowers
                                        )
                                    } else {
                                        discipline
                                    }
                                }
                                currentState.copy(
                                    character = character.copy(disciplines = updatedDisciplines)
                                )
                            }
                        }
                    }

                    is CharacterSheetEvent.LoresheetAdded -> {
                        val updatedCharacter = _uiState.value.character.copy(
                            loresheets = _uiState.value.character.loresheets + event.loresheet.copy(
                                level = event.level
                            )
                        )
                        _uiState.update {
                            it.copy(character = updatedCharacter)
                        }
                    }

                    is CharacterSheetEvent.LoresheetRemoved -> {
                        val updatedCharacter = _uiState.value.character.copy(
                            loresheets = _uiState.value.character.loresheets - event.loresheet
                        )
                        _uiState.update {
                            it.copy(character = updatedCharacter)
                        }
                    }

                    is CharacterSheetEvent.LoresheetLevelChanged -> {
                        _uiState.update { currentState ->
                            val updatedLoresheets = currentState.character.loresheets.map { loresheet ->
                                if (loresheet.title == event.loresheetName) {
                                    loresheet.copy(level = event.level)
                                } else {
                                    loresheet
                                }
                            }
                            currentState.copy(character = currentState.character.copy(loresheets = updatedLoresheets))
                        }
                    }
                    is CharacterSheetEvent.BackgroundAdded -> {
                        val updatedCharacter = _uiState.value.character.copy(
                            backgrounds = _uiState.value.character.backgrounds + event.background.copy(
                                level = 1,
                                identifier = UUID.randomUUID().toString(),
                                directFlaws = emptyList(),
                                merits = emptyList(),
                                flaws = emptyList()
                            )
                        )
                        _uiState.update {
                            it.copy(character = updatedCharacter)
                        }
                    }

                    is CharacterSheetEvent.BackgroundRemoved -> {
                        val updatedCharacter = _uiState.value.character.copy(
                            backgrounds = _uiState.value.character.backgrounds - event.background
                        )
                        _uiState.update {
                            it.copy(character = updatedCharacter)
                        }
                    }

                    is CharacterSheetEvent.BackgroundLevelChanged -> {
                        _uiState.update { currentState ->
                            val updatedBackgrounds = currentState.character.backgrounds.map {
                                if (it.identifier == event.background.identifier) {
                                    it.copy(level = event.newLevel)
                                } else {
                                    it
                                }
                            }
                            currentState.copy(
                                character = currentState.character.copy(backgrounds = updatedBackgrounds)
                            )
                        }
                    }

                    is CharacterSheetEvent.AdvantageAdded -> {
                        _uiState.update { currentState ->
                            val backgrounds =
                                currentState.character.backgrounds.toMutableList()
                            val backgroundIndex =
                                backgrounds.indexOfFirst { it.title == event.background.title }
                            if (backgroundIndex != -1) {
                                val currentBackground = backgrounds[backgroundIndex]
                                val currentAdvanges =
                                    if (event.advantage.isFlaw == true)
                                        currentBackground.flaws
                                    else
                                        currentBackground.merits
                                val updatedAdvantages =
                                    currentAdvanges + event.advantage.copy(level = event.level)
                                val updatedBackground =
                                    if (event.advantage.isFlaw == true)
                                        currentBackground.copy(flaws = updatedAdvantages)
                                    else
                                        currentBackground.copy(merits = updatedAdvantages)
                                backgrounds[backgroundIndex] = updatedBackground
                                currentState.copy(
                                    character = currentState.character.copy(
                                        backgrounds = backgrounds
                                    )
                                )
                            } else currentState
                        }

                        val updatedCharacter = _uiState.value.character.copy(
                            backgrounds = _uiState.value.character.backgrounds
                        )
                        _uiState.update {
                            it.copy(character = updatedCharacter)
                        }
                    }

                    is CharacterSheetEvent.AdvantageRemoved -> {
                        _uiState.update { currentState ->
                            val backgrounds =
                                currentState.character.backgrounds.toMutableList()
                            val backgroundIndex =
                                backgrounds.indexOfFirst { it.title == event.background.title }
                            if (backgroundIndex != -1) {
                                val currentBackground = backgrounds[backgroundIndex]
                                val currentAdvanges =
                                    if (event.advantage.isFlaw == true)
                                        currentBackground.flaws
                                    else
                                        currentBackground.merits
                                val updatedAdvantages = currentAdvanges - event.advantage
                                val updatedBackground =
                                    if (event.advantage.isFlaw == true)
                                        currentBackground.copy(flaws = updatedAdvantages)
                                    else
                                        currentBackground.copy(merits = updatedAdvantages)
                                backgrounds[backgroundIndex] = updatedBackground
                                currentState.copy(
                                    character = currentState.character.copy(
                                        backgrounds = backgrounds
                                    )
                                )
                            } else currentState
                        }

                        val updatedCharacter = _uiState.value.character.copy(
                            backgrounds = _uiState.value.character.backgrounds
                        )
                        _uiState.update {
                            it.copy(character = updatedCharacter)
                        }
                    }

                    is CharacterSheetEvent.AdvantageLevelChanged -> {
                        _uiState.update { currentState ->
                            val backgrounds =
                                currentState.character.backgrounds.toMutableList()
                            val backgroundIndex =
                                backgrounds.indexOfFirst { it.title == event.background.title }
                            if (backgroundIndex != -1) {
                                val advantages =
                                    if (event.advantage.isFlaw == true)
                                        backgrounds[backgroundIndex].flaws.toMutableList()
                                    else
                                        backgrounds[backgroundIndex].merits
                                            .toMutableList()
                                val updatedVantage =
                                    advantages.find {
                                        it.id == event.advantage.id
                                    }?.copy(level = event.level)
                                if (updatedVantage != null) {
                                    advantages[advantages.indexOf(updatedVantage)] = updatedVantage
                                }
                                backgrounds[backgroundIndex] =
                                    if (event.advantage.isFlaw == true)
                                        backgrounds[backgroundIndex].copy(flaws = advantages)
                                    else
                                        backgrounds[backgroundIndex].copy(merits = advantages)
                                currentState.copy(
                                    character = currentState.character.copy(
                                        backgrounds = backgrounds
                                    )
                                )
                            } else currentState
                        }

                        val updatedCharacter = _uiState.value.character.copy(
                            backgrounds = _uiState.value.character.backgrounds
                        )
                        _uiState.update {
                            it.copy(character = updatedCharacter)
                        }
                    }

                    is CharacterSheetEvent.AdvantageFlawAdded -> {
                        _uiState.update { currentState ->
                            val backgrounds =
                                currentState.character.backgrounds.toMutableList()
                            val backgroundIndex =
                                backgrounds.indexOfFirst { it.title == event.background.title }
                            if (backgroundIndex != -1) {
                                val currentBackground = backgrounds[backgroundIndex]
                                val currentAdvanges = currentBackground.flaws
                                val updatedAdvantages =
                                    currentAdvanges + event.advantage.copy(level = event.level)
                                val updatedBackground =
                                    currentBackground.copy(flaws = updatedAdvantages)
                                backgrounds[backgroundIndex] = updatedBackground
                                currentState.copy(
                                    character = currentState.character.copy(
                                        backgrounds = backgrounds
                                    )
                                )
                            } else currentState
                        }

                        val updatedCharacter = _uiState.value.character.copy(
                            backgrounds = _uiState.value.character.backgrounds
                        )
                        _uiState.update {
                            it.copy(character = updatedCharacter)
                        }
                    }

                    is CharacterSheetEvent.BackgroundMeritAdded -> {
                        _uiState.update { currentState ->
                            val updatedBackgrounds = currentState.character.backgrounds.map { bg ->
                                if (bg.identifier == event.background.identifier) {
                                    val newMerit = event.merit.copy(
                                        identifier = UUID.randomUUID().toString(),
                                        level = event.level
                                    )
                                    bg.copy(merits = bg.merits + newMerit)
                                } else {
                                    bg
                                }
                            }
                            currentState.copy(character = currentState.character.copy(backgrounds = updatedBackgrounds))
                        }
                    }

                    is CharacterSheetEvent.BackgroundMeritRemoved -> {
                        _uiState.update { currentState ->
                            val updatedBackgrounds = currentState.character.backgrounds.map { bg ->
                                if (bg.identifier == event.background.identifier) {
                                    bg.copy(merits = bg.merits.filterNot { it.identifier == event.merit.identifier })
                                } else {
                                    bg
                                }
                            }
                            currentState.copy(character = currentState.character.copy(backgrounds = updatedBackgrounds))
                        }
                    }

                    is CharacterSheetEvent.BackgroundMeritLevelChanged -> {
                        _uiState.update { currentState ->
                            val updatedBackgrounds = currentState.character.backgrounds.map { bg ->
                                if (bg.identifier == event.background.identifier) {
                                    val updatedMerits = bg.merits.map { merit ->
                                        if (merit.identifier == event.merit.identifier) {
                                            merit.copy(level = event.newLevel)
                                        } else {
                                            merit
                                        }
                                    }
                                    bg.copy(merits = updatedMerits)
                                } else {
                                    bg
                                }
                            }
                            currentState.copy(character = currentState.character.copy(backgrounds = updatedBackgrounds))
                        }
                    }

                    is CharacterSheetEvent.BackgroundFlawAdded -> {
                        _uiState.update { currentState ->
                            val updatedBackgrounds = currentState.character.backgrounds.map { bg ->
                                if (bg.identifier == event.background.identifier) {
                                    val newFlaw = event.flaw.copy(
                                        identifier = UUID.randomUUID().toString(),
                                        level = event.level
                                    )
                                    bg.copy(flaws = bg.flaws + newFlaw)
                                } else {
                                    bg
                                }
                            }
                            currentState.copy(character = currentState.character.copy(backgrounds = updatedBackgrounds))
                        }
                    }

                    is CharacterSheetEvent.BackgroundFlawRemoved -> {
                        _uiState.update { currentState ->
                            val updatedBackgrounds = currentState.character.backgrounds.map { bg ->
                                if (bg.identifier == event.background.identifier) {
                                    bg.copy(flaws = bg.flaws.filterNot { it.identifier == event.flaw.identifier })
                                } else {
                                    bg
                                }
                            }
                            currentState.copy(character = currentState.character.copy(backgrounds = updatedBackgrounds))
                        }
                    }

                    is CharacterSheetEvent.BackgroundFlawLevelChanged -> {
                        _uiState.update { currentState ->
                            val updatedBackgrounds = currentState.character.backgrounds.map { bg ->
                                if (bg.identifier == event.background.identifier) {
                                    val updatedFlaws = bg.flaws.map { flaw ->
                                        if (flaw.identifier == event.flaw.identifier) {
                                            flaw.copy(level = event.newLevel)
                                        } else {
                                            flaw
                                        }
                                    }
                                    bg.copy(flaws = updatedFlaws)
                                } else {
                                    bg
                                }
                            }
                            currentState.copy(character = currentState.character.copy(backgrounds = updatedBackgrounds))
                        }
                    }

                    is CharacterSheetEvent.CharacterDirectFlawAdded -> {
                        _uiState.update { currentState ->
                            val newDirectFlaw = event.directFlaw.copy(
                                identifier = UUID.randomUUID().toString(),
                                level = event.level
                            )
                            val updatedDirectFlaws = currentState.character.directFlaws.plus(
                                newDirectFlaw
                            )
                            currentState.copy(character = currentState.character.copy(directFlaws = updatedDirectFlaws))
                        }
                    }

                    is CharacterSheetEvent.CharacterDirectFlawRemoved -> {
                        _uiState.update { currentState ->
                            val updatedDirectFlaws =
                                currentState.character.directFlaws.filterNot { it.identifier == event.directFlaw.identifier }
                            currentState.copy(character = currentState.character.copy(directFlaws = updatedDirectFlaws))
                        }
                    }

                    is CharacterSheetEvent.CharacterDirectFlawLevelChanged -> {
                        _uiState.update { currentState ->
                            val updatedDirectFlaws =
                                currentState.character.directFlaws.map { directFlaw ->
                                    if (directFlaw.identifier == event.directFlaw.identifier) {
                                        directFlaw.copy(level = event.newLevel)
                                    } else {
                                        directFlaw
                                    }
                                }
                            currentState.copy(character = currentState.character.copy(directFlaws = updatedDirectFlaws))
                        }
                    }

                    is CharacterSheetEvent.AdvantageFlawRemoved -> {
                        _uiState.update { currentState ->
                            val backgrounds =
                                currentState.character.backgrounds.toMutableList()
                            val backgroundIndex =
                                backgrounds.indexOfFirst { it.title == event.background.title }
                            if (backgroundIndex != -1) {
                                val currentBackground = backgrounds[backgroundIndex]
                                val currentAdvanges = currentBackground.merits
                                val updatedAdvantages = currentAdvanges - event.advantage
                                val updatedBackground =
                                    currentBackground.copy(merits = updatedAdvantages)
                                backgrounds[backgroundIndex] = updatedBackground
                                currentState.copy(
                                    character = currentState.character.copy(
                                        backgrounds = backgrounds
                                    )
                                )
                            } else currentState
                        }

                        val updatedCharacter = _uiState.value.character.copy(
                            backgrounds = _uiState.value.character.backgrounds
                        )
                        _uiState.update {
                            it.copy(character = updatedCharacter)
                        }
                    }

                    is CharacterSheetEvent.AdvantageFlawLevelChanged -> {
                        _uiState.update { currentState ->
                            val backgrounds =
                                currentState.character.backgrounds.toMutableList()
                            val backgroundIndex =
                                backgrounds.indexOfFirst { it.title == event.background.title }
                            if (backgroundIndex != -1) {
                                val advantages =
                                    backgrounds[backgroundIndex].merits.toMutableList()
                                val updatedVantage =
                                    backgrounds[backgroundIndex].merits.find {
                                        it.id == event.advantage.id
                                    }?.copy(level = event.level)
                                if (updatedVantage != null) {
                                    advantages[advantages.indexOf(updatedVantage)] = updatedVantage
                                }
                                backgrounds[backgroundIndex] =
                                    backgrounds[backgroundIndex].copy(merits = advantages)
                                currentState.copy(
                                    character = currentState.character.copy(
                                        backgrounds = backgrounds
                                    )
                                )
                            } else currentState
                        }

                        val updatedCharacter = _uiState.value.character.copy(
                            backgrounds = _uiState.value.character.backgrounds
                        )
                        _uiState.update {
                            it.copy(character = updatedCharacter)
                        }
                    }

                    is CharacterSheetEvent.AddNoteToBackground -> {
                        _uiState.update { currentState ->
                            val updatedBackgrounds =
                                currentState.character.backgrounds.map { background ->
                                    if (background.identifier == event.background.identifier) {
                                        background.copy(note = event.note)
                                    } else {
                                        background
                                    }
                                }
                            currentState.copy(character = currentState.character.copy(backgrounds = updatedBackgrounds))
                        }
                    }

                    is CharacterSheetEvent.AddNoteToDirectFlaw -> {
                        _uiState.update { currentState ->
                            val updatedFlaws =
                                currentState.character.directFlaws.map { background ->
                                    if (background.identifier == event.advantage.identifier) {
                                        background.copy(note = event.note)
                                    } else {
                                        background
                                    }
                                }
                            currentState.copy(character = currentState.character.copy(directFlaws = updatedFlaws))
                        }
                    }

                    is CharacterSheetEvent.AddNoteToBackgroundFlaw -> {
                        _uiState.update { currentState ->
                            val updatedBackgrounds = currentState.character.backgrounds.map { bg ->
                                if (bg.identifier == event.background.identifier) {
                                    val updatedFlaws = bg.flaws.map { flaw ->
                                        if (flaw.identifier == event.flaw.identifier) {
                                            flaw.copy(note = event.note)
                                        } else {
                                            flaw
                                        }
                                    }
                                    bg.copy(flaws = updatedFlaws)
                                } else {
                                    bg
                                }
                            }
                            currentState.copy(character = currentState.character.copy(backgrounds = updatedBackgrounds))
                        }
                    }

                    is CharacterSheetEvent.AddNoteToMerit -> {
                        _uiState.update { currentState ->
                            val updatedBackgrounds = currentState.character.backgrounds.map { bg ->
                                if (bg.identifier == event.background.identifier) {
                                    val updatedMerits = bg.merits.map { merit ->
                                        if (merit.identifier == event.merit.identifier) {
                                            merit.copy(note = event.note)
                                        } else {
                                            merit
                                        }
                                    }
                                    bg.copy(merits = updatedMerits)
                                } else {
                                    bg
                                }
                            }
                            currentState.copy(character = currentState.character.copy(backgrounds = updatedBackgrounds))
                        }
                    }

                    is CharacterSheetEvent.AddNoteToFlaw -> {
                        _uiState.update { currentState ->
                            val updatedBackgrounds = currentState.character.backgrounds.map { bg ->
                                if (bg.identifier == event.background.identifier) {
                                    val updatedFlaws = bg.flaws.map { flaw ->
                                        if (flaw.identifier == event.flaw.identifier) {
                                            flaw.copy(note = event.note)
                                        } else {
                                            flaw
                                        }
                                    }
                                    bg.copy(flaws = updatedFlaws)
                                } else {
                                    bg
                                }
                            }
                            currentState.copy(character = currentState.character.copy(backgrounds = updatedBackgrounds))
                        }
                    }

                    is CharacterSheetEvent.RemoveNoteToDirectFlaw -> {
                        _uiState.update { currentState ->
                            val updatedFlaws =
                                currentState.character.directFlaws.map { background ->
                                    if (background.identifier == event.advantage.identifier) {
                                        background.copy(note = null)
                                    } else {
                                        background
                                    }
                                }
                            currentState.copy(character = currentState.character.copy(directFlaws = updatedFlaws))
                        }
                    }

                    is CharacterSheetEvent.RemoveNoteToBackground -> {
                        _uiState.update { currentState ->
                            val updatedBackgrounds = currentState.character.backgrounds.map { bg ->
                                if (bg.identifier == event.background.identifier) {
                                    bg.copy(note = null)
                                } else {
                                    bg
                                }
                            }
                            currentState.copy(character = currentState.character.copy(backgrounds = updatedBackgrounds))
                        }
                    }

                    is CharacterSheetEvent.RemoveNoteToBackgroundFlaw -> {
                        _uiState.update { currentState ->
                            val updatedBackgrounds = currentState.character.backgrounds.map { bg ->
                                if (bg.identifier == event.background.identifier) {
                                    val updatedFlaws = bg.flaws.map { flaw ->
                                        if (flaw.identifier == event.flaw.identifier) {
                                            flaw.copy(note = null)
                                        } else {
                                            flaw
                                        }
                                    }
                                    bg.copy(flaws = updatedFlaws)
                                } else {
                                    bg
                                }
                            }
                            currentState.copy(character = currentState.character.copy(backgrounds = updatedBackgrounds))
                        }
                    }

                    is CharacterSheetEvent.RemoveNoteToMerit -> {
                        _uiState.update { currentState ->
                            val updatedBackgrounds = currentState.character.backgrounds.map { bg ->
                                if (bg.identifier == event.background.identifier) {
                                    val updatedMerits = bg.merits.map { merit ->
                                        if (merit.identifier == event.merit.identifier) {
                                            merit.copy(note = null)
                                        } else {
                                            merit
                                        }
                                    }
                                    bg.copy(merits = updatedMerits)
                                } else {
                                    bg
                                }
                            }
                            currentState.copy(character = currentState.character.copy(backgrounds = updatedBackgrounds))
                        }
                    }

                    is CharacterSheetEvent.HungerChanged -> {
                        _uiState.update { currentState ->
                            val validHunger = event.newHunger.coerceIn(0, 5)
                            currentState.copy(
                                character = currentState.character.copy(hunger = validHunger)
                            )
                        }
                    }

                    is CharacterSheetEvent.HumanityChanged -> {
                        _uiState.update { currentState ->
                            val newHumanity = event.current.coerceIn(0, 10)
                            currentState.copy(
                                character = currentState.character.copy(
                                    humanity = currentState.character.humanity.copy(current = newHumanity)
                                )
                            )
                        }
                    }

                    is CharacterSheetEvent.StainsChanged -> {
                        _uiState.update { currentState ->
                            val newStains = event.stains.coerceAtLeast(0)
                            currentState.copy(
                                character = currentState.character.copy(
                                    humanity = currentState.character.humanity.copy(stains = newStains)
                                )
                            )
                        }
                    }

                    is CharacterSheetEvent.RemoveNoteToFlaw -> {
                        _uiState.update { currentState ->
                            val updatedBackgrounds = currentState.character.backgrounds.map { bg ->
                                if (bg.identifier == event.background.identifier) {
                                    val updatedFlaws = bg.flaws.map { flaw ->
                                        if (flaw.identifier == event.flaw.identifier) {
                                            flaw.copy(note = null)
                                        } else {
                                            flaw
                                        }
                                    }
                                    bg.copy(flaws = updatedFlaws)
                                } else {
                                    bg
                                }
                            }
                            currentState.copy(character = currentState.character.copy(backgrounds = updatedBackgrounds))
                        }
                    }

                    is CharacterSheetEvent.AddRitual -> addRitual(
                        event.disciplineName,
                        event.ritual
                    )

                    is CharacterSheetEvent.RemoveRitual -> removeRitual(
                        event.ritual
                    )

                    is CharacterSheetEvent.UpdateRitualLevel -> {
                        _uiState.update { currentState ->
                            val updatedRituals = currentState.character.learnedRituals
                                .map { ritual ->
                                    if (ritual.title == event.ritual.title) {
                                        ritual.copy(level = event.level)
                                    } else {
                                        ritual
                                    }
                                }
                            currentState.copy(character = currentState.character.copy(learnedRituals = updatedRituals))
                        }
                    }
                }
            }
        }

        viewModelScope.launch(Dispatchers.IO) {
            try {
                _uiState.update { it.copy(isLoading = true) }
                _clans.value = mainRepository.loadClans(Locale.getDefault())
                _predator.value = mainRepository.loadPredatorType(Locale.getDefault())
                _disciplines.value = mainRepository.loadDisciplines(Locale.getDefault())
                _loreSheets.value =
                    mainRepository.loadLoresheet(Locale.getDefault())
                        .sortedBy { it.title }
                _backgrounds.value =
                    mainRepository.loadBackground(Locale.getDefault())
                        .sortedBy { it.title }
                _directFlaws.value =
                    _backgrounds.value.flatMap { it.directFlaws }
                        .sortedBy { it.title }

                _uiState.update { it.copy(isLoading = false) }
            } catch (e: Exception) {
                _uiState.update { it.copy(error = "Errore durante il caricamento dei clan di default: ${e.message}") }
            }
        }
    }

    fun setCharacter(id: String) {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            try {
                _uiState.update {
                    it.copy(
                        character = characterRepository.getCharacter(id) ?: Character(),
                        isLoading = false,
                        isSaving = true,
                        error = null
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        isSaving = false,
                        error = e.message
                    )
                }
            }
        }
    }

    private fun addRitual(disciplineName: String, ritual: Ritual) {
        viewModelScope.launch {
            _uiState.update { currentState ->
                val character = currentState.character

                val currentDisciplineLevel =
                    (character.disciplines.first { it.title == disciplineName }.level)

                if (currentDisciplineLevel < 1) {
                    return@launch
                }

                val ritualsForDiscipline =
                    character.learnedRituals.filter { it.title == disciplineName }
                if (ritualsForDiscipline.contains(ritual)) {
                    return@launch
                }

                currentState.copy(
                    character = character.copy(
                        learnedRituals = character.learnedRituals + ritual.copy(
                            level = 1,
                            ritualsPowers = emptyList()
                        )
                    )
                )
            }
        }
    }

    private fun removeRitual(ritual: Ritual) {
        viewModelScope.launch {
            _uiState.update { currentState ->
                val character = currentState.character
                val originalRituals = character.learnedRituals
                val updatedRituals = originalRituals.filterNot { it.id == ritual.id }
                if (updatedRituals.size < originalRituals.size) {
                    currentState.copy(
                        character = character.copy(learnedRituals = updatedRituals)
                    )
                } else {
                    currentState
                }
            }
        }
    }

    private fun cleanupSheet() {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            try {
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

    private fun deleteSheet() {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            try {
                characterRepository.deleteCharacter(_uiState.value.character)
                _uiState.update {
                    it.copy(character = Character(), isLoading = false, error = null)
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }

    fun onEvent(event: CharacterSheetEvent) {
        viewModelScope.launch {
            eventChannel.send(event)
        }
    }

    fun selectTab(tab: Int) {
        _selectedTabIndex.value = tab
    }

    private fun saveSheet() {
        _uiState.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            try {
                characterRepository.saveCharacter(_uiState.value.character)
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        isSaving = true,
                        error = null
                    )
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }

    private fun calculateMaxHealthBoxes(stamina: Int): Int {
        return stamina + 3
    }

    private fun calculateMaxWillpowerBoxes(resolve: Int, composure: Int): Int {
        return resolve + composure
    }

    private fun synchronizeDamageTrack(
        currentBoxes: List<DamageType>,
        newSize: Int
    ): List<DamageType> {
        val newBoxes = MutableList(newSize) { DamageType.EMPTY }
        for (i in 0 until minOf(currentBoxes.size, newSize)) {
            newBoxes[i] = currentBoxes[i]
        }
        return newBoxes.toList()
    }

}
