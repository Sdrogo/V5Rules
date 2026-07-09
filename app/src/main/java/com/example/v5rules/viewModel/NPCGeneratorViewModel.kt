package com.example.v5rules.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.v5rules.data.*
import com.example.v5rules.di.AppModule
import com.example.v5rules.repository.CharacterRepository
import com.example.v5rules.repository.FavoriteNpcRepository
import com.example.v5rules.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*
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
    private val favoriteNpcRepository: FavoriteNpcRepository,
    @AppModule.IoDispatcher private val ioDispatcher: CoroutineDispatcher
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
    private val ICELANDIC_FIXED_SURNAMES = listOf(
        "Nguyen", "Blöndal", "Thorarensen", "Hansen", "Waage", "Nielsen",
        "Möller", "Briem", "Kvaran", "Thoroddsen", "Olsen", "Bergmann",
        "Jensen", "Fjeldsted", "Hall", "Hjaltested", "Scheving", "Johnson",
        "Schram", "Andersen"
    )

    init {
        fetchNpcNames()
    }

    private fun fetchNpcNames() {
        viewModelScope.launch(ioDispatcher) {
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

            updateCurrentFamilyNameGender(gender)
        }
    }

    private fun updateCurrentFamilyNameGender(newGender: Gender) {
        val currentState = _generationState.value
        val currentNpc = currentState.npc ?: return
        val nationality = currentState.selectedNationality ?: return
        val currentSurname = currentNpc.cognome

        val updatedSurname = when (nationality.uppercase()) {
            NpcNationality.RUSSO.name -> transformRussian(currentSurname, newGender)
            NpcNationality.ISLANDESE.name -> transformIcelandic(currentSurname, newGender)
            NpcNationality.LETTONE.name -> transformLatvian(currentSurname, newGender)
            NpcNationality.LITUANO.name -> transformLithuanian(currentSurname, newGender)
            NpcNationality.POLACCO.name -> transformPolish(currentSurname, newGender)
            else -> currentSurname
        }
        _generationState.update { it.copy(npc = currentNpc.copy(cognome = updatedSurname)) }
    }
    private fun transformRussian(surname: String, gender: Gender): String {
        val maleSuffixes = listOf("ov", "ev", "in", "yev", "sky", "ski", "iy", "yy")
        val femaleSuffixes = listOf("ova", "eva", "ina", "yeva", "skaya", "ska", "aya", "yaya")

        return if (gender == Gender.FEMALE) {
            maleSuffixes.forEachIndexed { index, s ->
                if (surname.endsWith(s, true)) return surname.dropLast(s.length) + femaleSuffixes[index]
            }
            surname
        } else {
            femaleSuffixes.forEachIndexed { index, s ->
                if (surname.endsWith(s, true)) return surname.dropLast(s.length) + maleSuffixes[index]
            }
            surname
        }
    }

    private fun transformIcelandic(surname: String, gender: Gender): String {
        if (ICELANDIC_FIXED_SURNAMES.any { it.equals(surname.trim(), true) }) return surname

        val suffix = if (gender == Gender.MALE) "son" else "dóttir"
        // Rimuove eventuali suffissi esistenti per tornare alla radice e applicare quello nuovo
        val root = surname.trim().removeSuffix("son").removeSuffix("dóttir")
        return root + suffix
    }

    private fun transformLatvian(surname: String, gender: Gender): String {
        return if (gender == Gender.FEMALE) {
            when {
                surname.endsWith("s", true) || surname.endsWith("š", true) -> surname.dropLast(1) + "a"
                surname.endsWith("is", true) -> surname.dropLast(2) + "e"
                else -> surname
            }
        } else {
            when {
                surname.endsWith("a", true) -> surname.dropLast(1) + "s"
                surname.endsWith("e", true) -> surname.dropLast(1) + "is"
                else -> surname
            }
        }
    }

    private fun transformLithuanian(surname: String, gender: Gender): String {
        return if (gender == Gender.FEMALE) {
            when {
                surname.endsWith("as", true) -> surname.dropLast(2) + "ienė"
                surname.endsWith("is", true) || surname.endsWith("ys", true) -> surname.dropLast(2) + "ienė"
                surname.endsWith("us", true) -> surname.dropLast(2) + "uvienė"
                surname.endsWith("ius", true) -> surname.dropLast(3) + "iuvienė"
                surname.endsWith("ov", true) -> surname + "a" // Supporto per russi in Lituania
                else -> surname
            }
        } else {
            when {
                surname.endsWith("ienė", true) -> surname.dropLast(4) + "as"
                surname.endsWith("uvienė", true) -> surname.dropLast(6) + "us"
                surname.endsWith("iuvienė", true) -> surname.dropLast(7) + "ius"
                else -> surname
            }
        }
    }

    private fun transformPolish(surname: String, gender: Gender): String {
        return if (gender == Gender.FEMALE) {
            if (surname.endsWith("ski", true) || surname.endsWith("cki", true) || surname.endsWith("dzki", true)) {
                surname.dropLast(1) + "a"
            } else surname
        } else {
            if (surname.endsWith("ska", true) || surname.endsWith("cka", true) || surname.endsWith("dzka", true)) {
                surname.dropLast(1) + "i"
            } else surname
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
        val culture = getNpcNationality(nationality)

        val supportsSecondName = culture?.supportsSecondName ?: false

        _generationState.update {
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
        viewModelScope.launch(ioDispatcher) {
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
        viewModelScope.launch(ioDispatcher) {
            favoriteNpcRepository.removeFavorite(favorite)
        }
    }

    fun createCharacterFromNpc() {
        val currentState = _generationState.value
        val currentNpc = currentState.npc ?: return
        viewModelScope.launch(ioDispatcher) {

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

    private fun getFamilyNamesForGender(nationality: String?, gender: Gender, allSurnames: List<String>): List<String> {
        val normalizedNationality = nationality?.lowercase(Locale.ROOT) ?: return allSurnames
        val culture = getNpcNationality(normalizedNationality) ?: return allSurnames

        if (!culture.hasGenderFamilyNameRules) return allSurnames

        return allSurnames.map { surname ->
            when (culture) {
                NpcNationality.ISLANDESE -> transformIcelandic(surname, gender)
                NpcNationality.RUSSO -> transformRussian(surname, gender)
                NpcNationality.LITUANO -> transformLithuanian(surname, gender)
                NpcNationality.LETTONE -> transformLatvian(surname, gender)
                NpcNationality.POLACCO -> transformPolish(surname, gender)
                else -> surname
            }
        }.distinct()
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