package com.example.v5rules.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
    private val characterRepository: CharacterRepository
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

                    // Abilities (Physical)
                    is CharacterSheetEvent.AthleticsChanged -> _uiState.update {
                        it.copy(
                            character = it.character.copy(
                                abilities = it.character.abilities.copy(athletics = event.athletics)
                            )
                        )
                    }

                    is CharacterSheetEvent.BrawlChanged -> _uiState.update {
                        it.copy(
                            character = it.character.copy(
                                abilities = it.character.abilities.copy(brawl = event.brawl)
                            )
                        )
                    }

                    is CharacterSheetEvent.CraftChanged -> _uiState.update {
                        it.copy(
                            character = it.character.copy(
                                abilities = it.character.abilities.copy(craft = event.craft)
                            )
                        )
                    }

                    is CharacterSheetEvent.DriveChanged -> _uiState.update {
                        it.copy(
                            character = it.character.copy(
                                abilities = it.character.abilities.copy(drive = event.drive)
                            )
                        )
                    }

                    is CharacterSheetEvent.FirearmsChanged -> _uiState.update {
                        it.copy(
                            character = it.character.copy(
                                abilities = it.character.abilities.copy(firearms = event.firearms)
                            )
                        )
                    }

                    is CharacterSheetEvent.MeleeChanged -> _uiState.update {
                        it.copy(
                            character = it.character.copy(
                                abilities = it.character.abilities.copy(melee = event.melee)
                            )
                        )
                    }

                    is CharacterSheetEvent.LarcenyChanged -> _uiState.update {
                        it.copy(
                            character = it.character.copy(
                                abilities = it.character.abilities.copy(larceny = event.larceny)
                            )
                        )
                    }

                    is CharacterSheetEvent.StealthChanged -> _uiState.update {
                        it.copy(
                            character = it.character.copy(
                                abilities = it.character.abilities.copy(stealth = event.stealth)
                            )
                        )
                    }

                    is CharacterSheetEvent.SurvivalChanged -> _uiState.update {
                        it.copy(
                            character = it.character.copy(
                                abilities = it.character.abilities.copy(survival = event.survival)
                            )
                        )
                    }

                    // Abilities (Social)
                    is CharacterSheetEvent.AnimalKenChanged -> _uiState.update {
                        it.copy(
                            character = it.character.copy(
                                abilities = it.character.abilities.copy(animalken = event.animalKen)
                            )
                        )
                    }

                    is CharacterSheetEvent.EtiquetteChanged -> _uiState.update {
                        it.copy(
                            character = it.character.copy(
                                abilities = it.character.abilities.copy(etiquette = event.etiquette)
                            )
                        )
                    }

                    is CharacterSheetEvent.InsightChanged -> _uiState.update {
                        it.copy(
                            character = it.character.copy(
                                abilities = it.character.abilities.copy(insight = event.insight)
                            )
                        )
                    }

                    is CharacterSheetEvent.IntimidationChanged -> _uiState.update {
                        it.copy(
                            character = it.character.copy(
                                abilities = it.character.abilities.copy(intimidation = event.intimidation)
                            )
                        )
                    }

                    is CharacterSheetEvent.LeadershipChanged -> _uiState.update {
                        it.copy(
                            character = it.character.copy(
                                abilities = it.character.abilities.copy(leadership = event.leadership)
                            )
                        )
                    }

                    is CharacterSheetEvent.PerformanceChanged -> _uiState.update {
                        it.copy(
                            character = it.character.copy(
                                abilities = it.character.abilities.copy(performance = event.performance)
                            )
                        )
                    }

                    is CharacterSheetEvent.PersuasionChanged -> _uiState.update {
                        it.copy(
                            character = it.character.copy(
                                abilities = it.character.abilities.copy(persuasion = event.persuasion)
                            )
                        )
                    }

                    is CharacterSheetEvent.StreetwiseChanged -> _uiState.update {
                        it.copy(
                            character = it.character.copy(
                                abilities = it.character.abilities.copy(streetwise = event.streetwise)
                            )
                        )
                    }

                    is CharacterSheetEvent.SubterfugeChanged -> _uiState.update {
                        it.copy(
                            character = it.character.copy(
                                abilities = it.character.abilities.copy(subterfuge = event.subterfuge)
                            )
                        )
                    }

                    // Abilities (Mental)
                    is CharacterSheetEvent.AcademicsChanged -> _uiState.update {
                        it.copy(
                            character = it.character.copy(
                                abilities = it.character.abilities.copy(academics = event.academics)
                            )
                        )
                    }

                    is CharacterSheetEvent.AwarenessChanged -> _uiState.update {
                        it.copy(
                            character = it.character.copy(
                                abilities = it.character.abilities.copy(awareness = event.awareness)
                            )
                        )
                    }

                    is CharacterSheetEvent.FinanceChanged -> _uiState.update {
                        it.copy(
                            character = it.character.copy(
                                abilities = it.character.abilities.copy(finance = event.finance)
                            )
                        )
                    }

                    is CharacterSheetEvent.InvestigationChanged -> _uiState.update {
                        it.copy(
                            character = it.character.copy(
                                abilities = it.character.abilities.copy(investigation = event.investigation)
                            )
                        )
                    }

                    is CharacterSheetEvent.MedicineChanged -> _uiState.update {
                        it.copy(
                            character = it.character.copy(
                                abilities = it.character.abilities.copy(medicine = event.medicine)
                            )
                        )
                    }

                    is CharacterSheetEvent.OccultChanged -> _uiState.update {
                        it.copy(
                            character = it.character.copy(
                                abilities = it.character.abilities.copy(occult = event.occult)
                            )
                        )
                    }

                    is CharacterSheetEvent.PoliticsChanged -> _uiState.update {
                        it.copy(
                            character = it.character.copy(
                                abilities = it.character.abilities.copy(politics = event.politics)
                            )
                        )
                    }

                    is CharacterSheetEvent.ScienceChanged -> _uiState.update {
                        it.copy(
                            character = it.character.copy(
                                abilities = it.character.abilities.copy(science = event.science)
                            )
                        )
                    }

                    is CharacterSheetEvent.TechnologyChanged -> _uiState.update {
                        it.copy(
                            character = it.character.copy(
                                abilities = it.character.abilities.copy(technology = event.technology)
                            )
                        )
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
                                clan.name.contains(query, ignoreCase = true) // Filtra in base al nome
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

    fun selectTab(tab:Int){
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
}