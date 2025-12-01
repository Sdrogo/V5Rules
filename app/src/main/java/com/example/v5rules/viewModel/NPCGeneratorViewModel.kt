package com.example.v5rules.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.v5rules.data.Character
import com.example.v5rules.data.FavoriteNpc
import com.example.v5rules.data.Gender
import com.example.v5rules.data.NameOrder
import com.example.v5rules.data.NationalityNpc
import com.example.v5rules.data.Npc
import com.example.v5rules.data.NpcNationality
import com.example.v5rules.repository.CharacterRepository
import com.example.v5rules.repository.FavoriteNpcRepository
import com.example.v5rules.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

sealed class NpcNavigationEvent {
    data class ToCharacterSheet(val characterId: String) : NpcNavigationEvent()
}

@HiltViewModel
class NPCGeneratorViewModel @Inject constructor(
    private val mainRepository: MainRepository,
    private val characterRepository: CharacterRepository,
    private val favoriteNpcRepository: FavoriteNpcRepository
) : ViewModel() {
    private val _generationState = MutableStateFlow(GenerationState())
    private val favoriteNpcsFromDb: StateFlow<List<FavoriteNpc>> = favoriteNpcRepository.getAllFavorites()
        .stateIn(viewModelScope, kotlinx.coroutines.flow.SharingStarted.WhileSubscribed(5000), emptyList())

    val uiState: StateFlow<UiState> = combine(
        _generationState,
        favoriteNpcsFromDb
    ) { state, favorites ->
        val updatedNpc = state.npc?.let { currentNpc ->
            val isFavorite = favorites.any { fav ->
                fav.name == currentNpc.nome && fav.familyName == currentNpc.cognome && fav.secondName == currentNpc.secondName
            }
            currentNpc.copy(isFavorite = isFavorite)
        }
        UiState(
            selectedGender = state.selectedGender,
            includeSecondName = state.includeSecondName,
            firstGeneration = state.firstGeneration,
            selectedNationality = state.selectedNationality,
            npc = updatedNpc,
            favoriteNpcs = favorites
        )
    }.stateIn(viewModelScope, kotlinx.coroutines.flow.SharingStarted.WhileSubscribed(5000), UiState())

    private val _navigationEvent = MutableSharedFlow<NpcNavigationEvent>()
    val navigationEvent: SharedFlow<NpcNavigationEvent> = _navigationEvent.asSharedFlow()

    private val _nationalityState = MutableStateFlow<NpcNationalityUiState>(NpcNationalityUiState.Loading)
    val nationalityState: StateFlow<NpcNationalityUiState> = _nationalityState.asStateFlow()

    var nationalities: List<String> = emptyList()
    private var allNamesByNationality: List<NationalityNpc> = emptyList()

    init {
        fetchNpcNames()
    }

    private fun fetchNpcNames() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val npcNames = mainRepository.readNpcNames(Locale.getDefault())
                allNamesByNationality = npcNames
                nationalities = npcNames.map { it.nationality }.sorted()
                _nationalityState.value = NpcNationalityUiState.Success(nationalities)
                _generationState.update { it.copy(selectedNationality = nationalities.firstOrNull()) }
            } catch (e: Exception) {
                _nationalityState.value = NpcNationalityUiState.Error("Failed to load names ${e.message.toString()}")
            }
        }
    }

    fun setSelectedGender(gender: Gender) {
        _generationState.update { it.copy(selectedGender = gender) }

        if (_generationState.value.firstGeneration) {
            regenerateName()
            if (_generationState.value.includeSecondName) regenerateSecondName()

            if (_generationState.value.selectedNationality?.equals("islandese", ignoreCase = true) == true) {
                regenerateFamilyName()
            }
        }
    }

    fun setIncludeSecondName(include: Boolean) {
        _generationState.update {
            if (!include) {
                it.copy(includeSecondName = false, npc = it.npc?.copy(secondName = null))
            } else {
                it.copy(includeSecondName = true)
            }
        }
        if (include && _generationState.value.firstGeneration) {
            regenerateSecondName()
        }
    }

    fun setSelectedNationality(nationality: String?) {
        val culture = getNpcNationality(nationality) // Ottiene il nuovo enum

        // Controlla se il secondo nome è supportato dalla nuova cultura
        val supportsSecondName = culture?.supportsSecondName ?: false

        _generationState.update {
            // Aggiorna la nazionalità e forza includeSecondName a false se non supportato
            it.copy(
                selectedNationality = nationality,
                includeSecondName = if (supportsSecondName) it.includeSecondName else false
            )
        }
        generateAll()
    }

    fun generateAll() {
        val currentState = _generationState.value
        val namesMap = allNamesByNationality.find { it.nationality == currentState.selectedNationality } ?: return
        val random = kotlin.random.Random

        val nameList = if (currentState.selectedGender == Gender.MALE) namesMap.nomiMaschili else namesMap.nomiFemminili
        val newName = nameList.randomOrNull(random).orEmpty()
        val newSecondName = if (currentState.includeSecondName) nameList.randomOrNull(random) else null

        val availableSurnames = getFamilyNamesForGender(currentState.selectedNationality, currentState.selectedGender, namesMap.cognomi)
        val newFamilyName = availableSurnames.randomOrNull(random).orEmpty()

        _generationState.update {
            it.copy(
                firstGeneration = true,
                npc = Npc(nome = newName, secondName = newSecondName, cognome = newFamilyName)
            )
        }
    }

    fun regenerateName() {
        val currentState = _generationState.value
        val namesMap = allNamesByNationality.find { it.nationality == currentState.selectedNationality } ?: return
        val names = if (currentState.selectedGender == Gender.MALE) namesMap.nomiMaschili else namesMap.nomiFemminili
        _generationState.update { state ->
            state.copy(npc = state.npc?.copy(nome = names.randomOrNull().orEmpty()))
        }
    }

    fun regenerateSecondName() {
        if (!_generationState.value.includeSecondName) return
        val currentState = _generationState.value
        val namesMap = allNamesByNationality.find { it.nationality == currentState.selectedNationality } ?: return
        val names = if (currentState.selectedGender == Gender.MALE) namesMap.nomiMaschili else namesMap.nomiFemminili
        _generationState.update { state ->
            state.copy(npc = state.npc?.copy(secondName = names.randomOrNull()))
        }
    }

    fun regenerateFamilyName() {
        val currentState = _generationState.value
        val namesMap = allNamesByNationality.find { it.nationality == currentState.selectedNationality } ?: return
        val random = kotlin.random.Random

        val availableSurnames = getFamilyNamesForGender(currentState.selectedNationality, currentState.selectedGender, namesMap.cognomi)

        _generationState.update { state ->
            state.copy(npc = state.npc?.copy(cognome = availableSurnames.randomOrNull(random).orEmpty()))
        }
    }

    fun toggleFavorite() {
        val currentNpc = _generationState.value.npc ?: return
        viewModelScope.launch(Dispatchers.IO) {
            val favorite = favoriteNpcRepository.findFavorite(
                name = currentNpc.nome,
                familyName = currentNpc.cognome,
                secondName = currentNpc.secondName
            )
            if (favorite != null) {
                favoriteNpcRepository.removeFavorite(favorite)
            } else {
                favoriteNpcRepository.addFavorite(
                    FavoriteNpc(
                        name = currentNpc.nome,
                        secondName = currentNpc.secondName,
                        familyName = currentNpc.cognome,
                        nationality = _generationState.value.selectedNationality.orEmpty()
                    )
                )
            }
        }
    }

    fun selectFavorite(favorite: FavoriteNpc) {
        _generationState.update {
            it.copy(
                includeSecondName = !favorite.secondName.isNullOrEmpty(),
                selectedNationality = favorite.nationality,
                npc = Npc(
                    nome = favorite.name,
                    secondName = favorite.secondName,
                    cognome = favorite.familyName,
                    isFavorite = true
                ),
                firstGeneration = true
            )
        }
    }


    fun deleteFavorite(favorite: FavoriteNpc) {
        viewModelScope.launch(Dispatchers.IO) {
            favoriteNpcRepository.removeFavorite(favorite)
        }
    }

    fun createCharacterFromNpc() {
        val currentState = _generationState.value
        val currentNpc = currentState.npc ?: return
        viewModelScope.launch(Dispatchers.IO) {

            // Determina l'ordine corretto del nome completo in base alla nazionalità usando l'enum
            val culture = getNpcNationality(currentState.selectedNationality) // Usa getNpcNationality qui
            val nameOrder = culture?.nameOrder ?: NameOrder.WESTERN // Usa NameOrder qui

            val fullName = buildString {
                if (nameOrder == NameOrder.EASTERN) {
                    // Ordine Orientale: COGNOME, NOME, (SECONDO NOME)
                    append(currentNpc.cognome)
                    append(" ")
                    append(currentNpc.nome)
                    currentNpc.secondName?.let { append(" $it") }
                } else {
                    // Ordine Occidentale: NOME, (SECONDO NOME), COGNOME
                    append(currentNpc.nome)
                    currentNpc.secondName?.let { append(" $it") }
                    append(" ")
                    append(currentNpc.cognome)
                }
            }.trim()

            val newCharacter = Character(
                name = fullName
            )
            val newId = characterRepository.saveCharacter(newCharacter)
            _navigationEvent.emit(NpcNavigationEvent.ToCharacterSheet(newId))
        }
    }

    /**
     * Cerca e restituisce l'oggetto NpcNationality corrispondente alla stringa di nazionalità.
     */
    private fun getNpcNationality(nationality: String?): NpcNationality? { // Rinomina NpcCulture in NpcNationality
        // Usa `entries` per iterare su tutti i valori dell'enum.
        return NpcNationality.entries.find { // Usa NpcNationality.entries
            it.displayName.equals(nationality, ignoreCase = true)
        }
    }
    // ...
    private fun getFamilyNamesForGender(nationality: String?, gender: Gender, allSurnames: List<String>): List<String> {
        val normalizedNationality = nationality?.lowercase(Locale.ROOT) ?: return allSurnames
        val culture = getNpcNationality(normalizedNationality) ?: return allSurnames // Usa getNpcNationality qui

        // Caso veloce: se la nazionalità non ha regole specifiche per i cognomi.
        if (!culture.hasGenderFamilyNameRules) return allSurnames
        return when (culture) {

            NpcNationality.ISLANDESE -> {
                val suffix = if (gender == Gender.MALE) "son" else "dóttir"
                val filtered = allSurnames.filter { it.endsWith(suffix, ignoreCase = true) }

                if (filtered.isNotEmpty()) filtered else allSurnames
            }
            NpcNationality.RUSSO -> {
                if (gender == Gender.FEMALE) {
                    val maleSuffixes = listOf("ov", "ev", "in", "yev", "sky", "ski", "iy", "yy")
                    val femaleSuffixes = listOf("ova", "eva", "ina", "yeva", "skaya", "ska", "aya", "yaya")

                    allSurnames.map { surname ->
                        var femaleSurname = surname
                        maleSuffixes.forEachIndexed { index, maleSuffix ->
                            if (surname.endsWith(maleSuffix, ignoreCase = true)) {
                                femaleSurname = surname.dropLast(maleSuffix.length) + femaleSuffixes[index]
                                return@map femaleSurname
                            }
                        }
                        femaleSurname
                    }
                } else {
                    allSurnames
                }
            }
            NpcNationality.LITUANO -> {
                if (gender == Gender.FEMALE) {
                    allSurnames.map { surname ->
                        when {
                            surname.endsWith("as", ignoreCase = true) -> surname.dropLast(2) + "aitė"
                            surname.endsWith("is", ignoreCase = true) -> surname.dropLast(2) + "ytė"
                            surname.endsWith("us", ignoreCase = true) -> surname.dropLast(2) + "utė"
                            else -> surname
                        }
                    }
                } else {
                    allSurnames
                }
            }
            NpcNationality.LETTONE -> {
                if (gender == Gender.FEMALE) {
                    allSurnames.map { surname ->
                        when {
                            surname.endsWith("s", ignoreCase = true) -> surname.dropLast(1) + "a"
                            surname.endsWith("š", ignoreCase = true) -> surname.dropLast(1) + "a"
                            surname.endsWith("is", ignoreCase = true) -> surname.dropLast(2) + "e"
                            else -> surname
                        }
                    }
                } else {
                    allSurnames
                }
            }
            else -> allSurnames
        }
    }

}

data class UiState(
    val selectedGender: Gender = Gender.MALE,
    val includeSecondName: Boolean = false,
    val firstGeneration: Boolean = false,
    val selectedNationality: String? = null,
    val npc: Npc? = null,
    val favoriteNpcs: List<FavoriteNpc> = emptyList()
)

private data class GenerationState(
    val selectedGender: Gender = Gender.MALE,
    val includeSecondName: Boolean = false,
    val firstGeneration: Boolean = false,
    val selectedNationality: String? = null,
    val npc: Npc? = null
)

sealed class NpcNationalityUiState {
    object Loading : NpcNationalityUiState()
    data class Success(val npcNationalities: List<String>) : NpcNationalityUiState()
    data class Error(val message: String) : NpcNationalityUiState()
}