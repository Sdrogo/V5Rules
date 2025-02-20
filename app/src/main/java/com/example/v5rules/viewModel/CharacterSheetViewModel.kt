package com.example.v5rules.viewModel

import android.content.res.Resources
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.v5rules.R
import com.example.v5rules.data.Ability
import com.example.v5rules.data.Character
import com.example.v5rules.data.Clan
import com.example.v5rules.data.Discipline
import com.example.v5rules.data.SelectedClan
import com.example.v5rules.repository.CharacterRepository
import com.example.v5rules.repository.MainRepository
import com.example.v5rules.utils.CharacterSheetEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
@HiltViewModel
class CharacterSheetViewModel @Inject constructor(
    private val mainRepository: MainRepository,
    private val characterRepository: CharacterRepository,
    resources: Resources
) : ViewModel() {

    // Stato
    data class CharacterSheetState(
        val character: Character = Character(),
        val isLoading: Boolean = false,
        val error: String? = null
    )

    private val _uiState = MutableStateFlow(CharacterSheetState())
    val uiState: StateFlow<CharacterSheetState> = _uiState.asStateFlow()
    private val _clans = MutableStateFlow<List<Clan>>(emptyList())
    val clans: StateFlow<List<Clan>> = _clans.asStateFlow()
    private val _disciplines = MutableStateFlow<List<Discipline>>(emptyList())
    val disciplines: StateFlow<List<Discipline>> = _disciplines.asStateFlow()
    private val _selectedTabIndex = MutableStateFlow(0)
    val selectedTabIndex: StateFlow<Int> = _selectedTabIndex.asStateFlow()
    private val _clanSearchQuery = MutableStateFlow("")  // Aggiungi questo
    val clanSearchQuery: StateFlow<String> = _clanSearchQuery.asStateFlow() //e questo
    private val _selectedClan = MutableStateFlow<SelectedClan?>(null) // Aggiungi questo
    val selectedClan: StateFlow<SelectedClan?> = _selectedClan.asStateFlow() // E questo
    val allAbilities: List<String> = resources.getStringArray(R.array.abilities).toList().sorted()

    private val eventChannel = Channel<CharacterSheetEvent>()


    init {
        viewModelScope.launch {
            eventChannel.consumeAsFlow().collect { event ->
                when (event) {
                    //... Gestione degli altri eventi...
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
                        _uiState.update {
                            it.copy(
                                character = it.character.copy(
                                    clan = event.clan
                                )
                            )
                        }
                        event.clan?.name?.let { setClanSearchQuery(it, event.clan) }
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

                    is CharacterSheetEvent.StaminaChanged -> _uiState.update {
                        it.copy(
                            character = it.character.copy(
                                attributes = it.character.attributes.copy(stamina = event.stamina)
                            )
                        )
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

                    is CharacterSheetEvent.ComposureChanged -> _uiState.update {
                        it.copy(
                            character = it.character.copy(
                                attributes = it.character.attributes.copy(composure = event.composure)
                            )
                        )
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

                    is CharacterSheetEvent.ResolveChanged -> _uiState.update {
                        it.copy(
                            character = it.character.copy(
                                attributes = it.character.attributes.copy(resolve = event.resolve)
                            )
                        )
                    }


                    is CharacterSheetEvent.AbilityChanged -> {
                        _uiState.update { currentState ->
                            val currentAbilities = currentState.character.abilities.toMutableList()
                            val abilityIndex =
                                currentAbilities.indexOfFirst { it.name == event.abilityName }

                            val updatedAbilities = if (abilityIndex != -1) {
                                currentAbilities.apply {
                                    set(
                                        abilityIndex,
                                        currentAbilities[abilityIndex].copy(level = event.level)
                                    )
                                }
                            } else {
                                currentAbilities + Ability(
                                    name = event.abilityName,
                                    level = event.level
                                )
                            }

                            currentState.copy(
                                character = currentState.character.copy(
                                    abilities = updatedAbilities
                                )
                            )
                        }
                    }

                    // Health
                    is CharacterSheetEvent.MaxHealthChanged -> _uiState.update {
                        it.copy(
                            character = it.character.copy(
                                health = it.character.health.copy(max = event.max)
                            )
                        )
                    }

                    is CharacterSheetEvent.CurrentHealthChanged -> _uiState.update {
                        it.copy(
                            character = it.character.copy(health = it.character.health.copy(current = event.current))
                        )
                    }

                    // Willpower
                    is CharacterSheetEvent.MaxWillpowerChanged -> _uiState.update {
                        it.copy(
                            character = it.character.copy(
                                willpower = it.character.willpower.copy(
                                    max = event.max
                                )
                            )
                        )
                    }

                    is CharacterSheetEvent.CurrentWillpowerChanged -> _uiState.update {
                        it.copy(
                            character = it.character.copy(
                                willpower = it.character.willpower.copy(current = event.current)
                            )
                        )
                    }

                    is CharacterSheetEvent.HumanityChanged -> _uiState.update {
                        it.copy(
                            character = it.character.copy(
                                humanity = it.character.humanity.copy(
                                    current = event.current
                                )
                            )
                        )
                    }

                    is CharacterSheetEvent.StainsChanged -> _uiState.update {
                        it.copy(
                            character = it.character.copy(
                                humanity = it.character.humanity.copy(
                                    stains = event.stains
                                )
                            )
                        )
                    }

                    // Experience
                    is CharacterSheetEvent.TotalExperienceChanged -> _uiState.update {
                        it.copy(
                            character = it.character.copy(
                                experience = it.character.experience.copy(
                                    total = event.total
                                )
                            )
                        )
                    }

                    is CharacterSheetEvent.SpentExperienceChanged -> _uiState.update {
                        it.copy(
                            character = it.character.copy(
                                experience = it.character.experience.copy(
                                    spent = event.spent
                                )
                            )
                        )
                    }
                    is CharacterSheetEvent.AbilitySpecializationChanged -> setAbilitySpecialization(event.abilityName, event.specialization)
                    is CharacterSheetEvent.DisciplineChanged -> {
                        _uiState.update { currentState ->
                            val currentDisciplines = currentState.character.disciplines.toMutableList()
                            // Controlla se la disciplina è già presente
                            val disciplineIndex = currentDisciplines.indexOfFirst { it.title == event.discipline.title}

                            if (disciplineIndex == -1) {
                                //Se non è presente, aggiungila
                                val newDiscipline = event.discipline.copy(level = 1) //Inizia a livello 1
                                currentState.copy(character = currentState.character.copy(disciplines = currentDisciplines + newDiscipline))
                            } else {
                                //Se è presente, non fare nulla
                                currentState
                            }
                        }
                    }
                    is CharacterSheetEvent.DisciplinePowerAdded -> {
                        _uiState.update { currentState ->
                            val currentDisciplines = currentState.character.disciplines.toMutableList()
                            val disciplineIndex = currentDisciplines.indexOfFirst { it.title == event.disciplineName }

                            if (disciplineIndex != -1) {
                                // 1. Ottieni la disciplina corrente
                                val currentDiscipline = currentDisciplines[disciplineIndex]
                                val actualDiscipline = currentDiscipline.selectedDisciplinePowers.orEmpty()
                                // 2. Controlla che il potere non sia già presente
                                if (actualDiscipline.none { it.id == event.power.id }) {
                                    // 3. Crea una nuova lista di poteri selezionati, aggiungendo il nuovo potere
                                    val newPowers = actualDiscipline + event.power

                                    // 4. Crea una *nuova* istanza di Discipline con la lista aggiornata
                                    val updatedDiscipline = currentDiscipline.copy(selectedDisciplinePowers = newPowers)

                                    // 5. Aggiorna la lista di discipline nel personaggio
                                    currentDisciplines[disciplineIndex] = updatedDiscipline // Sostituisci la disciplina
                                    currentState.copy(character = currentState.character.copy(disciplines = currentDisciplines))
                                } else {
                                    currentState // Se il potere è già presente, non fare nulla
                                }
                            } else {
                                currentState // Se la disciplina non esiste, non fare nulla (questo non dovrebbe succedere)
                            }
                        }
                    }

                    is CharacterSheetEvent.DisciplinePowerRemoved -> {
                        _uiState.update { currentState ->
                            val currentDisciplines = currentState.character.disciplines.toMutableList()
                            val disciplineIndex = currentDisciplines.indexOfFirst { it.title == event.disciplineName }

                            if (disciplineIndex != -1) {
                                val currentDiscipline = currentDisciplines[disciplineIndex]
                                val newPowers = currentDiscipline.selectedDisciplinePowers?.filterNot { it.id == event.power.id }
                                val updatedDiscipline = currentDiscipline.copy(selectedDisciplinePowers = newPowers)
                                currentDisciplines[disciplineIndex] = updatedDiscipline
                                currentState.copy(character = currentState.character.copy(disciplines = currentDisciplines))
                            } else {
                                currentState
                            }
                        }
                    }
                    is CharacterSheetEvent.DisciplineLevelChanged -> {
                        _uiState.update { currentState ->
                            val currentDisciplines = currentState.character.disciplines.toMutableList()
                            val disciplineIndex = currentDisciplines.indexOfFirst { it.title == event.disciplineName }

                            if (disciplineIndex != -1) {
                                val currentDiscipline = currentDisciplines[disciplineIndex]
                                val updatedDiscipline = currentDiscipline.copy(level = event.newLevel) //Aggiorna solo il livello
                                currentDisciplines[disciplineIndex] = updatedDiscipline
                                currentState.copy(character = currentState.character.copy(disciplines = currentDisciplines))
                            } else {
                                currentState // Disciplina non trovata (non dovrebbe succedere)
                            }
                        }
                    }

                }
            }
        }

        // Carica i dati iniziali
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _clans.value = mainRepository.loadClans(Locale.getDefault())
                _disciplines.value = mainRepository.loadDisciplines(Locale.getDefault())
                _clanSearchQuery
                    .debounce(300)  // Aggiungi un debounce (opzionale ma consigliato)
                    .mapLatest { query -> //usa mapLatest
                        if (query.isBlank()) {
                            mainRepository.loadClans(Locale.getDefault()) // Se la query è vuota, carica tutti i clan
                        } else {
                            mainRepository.loadClans(Locale.getDefault()).filter { clan ->
                                clan.name.contains(
                                    query,
                                    ignoreCase = true
                                ) // Filtra in base al nome
                            }
                        }
                    }
                    .collect { filteredClans ->
                        _clans.value = filteredClans // Aggiorna la lista dei clan nello stato
                    }
            } catch (e: Exception) {
                _uiState.update { it.copy(error = "Errore durante il caricamento dei clan") }
            }
        }
    }

    fun setCharacter(id: Int) {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            try {
                _uiState.update {
                    it.copy(
                        character = characterRepository.getCharacter(id) ?: Character(),
                        isLoading = false,
                        error = null
                    )
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = e.message) }
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
                _clanSearchQuery.value = "" //reset della query
                _selectedClan.value = null

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

    // Funzioni
    fun onEvent(event: CharacterSheetEvent) {
        viewModelScope.launch {
            eventChannel.send(event)
        }
    }

    fun setClanSearchQuery(query: String, clan: Clan? = null) {  // Aggiungi questa funzione
        _clanSearchQuery.value = query
        _selectedClan.value = clan?.let { SelectedClan(it.name) }
    }

    fun selectTab(tab: Int) {
        _selectedTabIndex.value = tab
    }

    private fun saveSheet() {
        _uiState.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            try {
                characterRepository.saveCharacter(_uiState.value.character)
                _uiState.update { it.copy(isLoading = false, error = null) }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }

    private fun setAbilitySpecialization(abilityName: String, specialization: String?) {
        _uiState.update { currentState ->
            val currentAbilities = currentState.character.abilities.toMutableList()
            val abilityIndex = currentAbilities.indexOfFirst { it.name == abilityName }

            val updatedAbilities = if (abilityIndex != -1) {
                currentAbilities.apply {
                    set(
                        abilityIndex,
                        currentAbilities[abilityIndex].copy(specialization = specialization)
                    )
                }
            } else {
                // Se l'abilità non esiste, potresti volerla aggiungere con livello 0,
                // oppure potresti gestire la situazione in modo diverso (es. errore).
                // Dipende dalla tua logica.
                currentAbilities + Ability(
                    name = abilityName,
                    level = 0,
                    specialization = specialization
                )
            }
            currentState.copy(
                character = currentState.character.copy(
                    abilities = updatedAbilities
                )
            )
        }
    }
}