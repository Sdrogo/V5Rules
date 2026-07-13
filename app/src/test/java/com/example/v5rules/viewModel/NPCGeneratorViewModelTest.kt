package com.example.v5rules.viewModel

import com.example.v5rules.data.local.model.FavoriteNpc
import com.example.v5rules.data.local.model.Gender
import com.example.v5rules.data.local.model.NationalityNpc
import com.example.v5rules.data.local.repository.CharacterRepository
import com.example.v5rules.data.local.repository.FavoriteNpcRepository
import com.example.v5rules.data.local.repository.MainRepository
import io.mockk.coEvery
import io.mockk.mockk
import app.cash.turbine.test
import io.mockk.coVerify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Assert.assertFalse

@OptIn(ExperimentalCoroutinesApi::class)
class NPCGeneratorViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private val mockMainRepository = mockk<MainRepository>()
    private val mockCharacterRepository = mockk<CharacterRepository>()
    private val mockFavoriteNpcRepository = mockk<FavoriteNpcRepository>()

    private lateinit var viewModel: NPCGeneratorViewModel

    private val allTestNationalities = listOf(
        NationalityNpc(
            nationality = "Russo",
            nomiMaschili = listOf("Ivan"), nomiFemminili = listOf("Svetlana"),
            cognomi = listOf("Ivanov", "Tchaikovsky")
        ),
        NationalityNpc(
            nationality = "Islandese",
            nomiMaschili = listOf("Erik"), nomiFemminili = listOf("Helga"),
            cognomi = listOf("Erikson")
        ),
        NationalityNpc(
            nationality = "Italiano",
            nomiMaschili = listOf("Mario"), nomiFemminili = listOf("Giulia"),
            cognomi = listOf("Rossi", "Bianchi")
        ),
        NationalityNpc(
            nationality = "Lettone",
            nomiMaschili = listOf("Jānis"), nomiFemminili = listOf("Ilze"),
            cognomi = listOf("Kalniņš", "Ozols")
        ),
        NationalityNpc(
            nationality = "Lituano",
            nomiMaschili = listOf("Jonas"), nomiFemminili = listOf("Asta"),
            cognomi = listOf("Kazlauskas", "Paulauskas")
        ),
        NationalityNpc(
            nationality = "Polacco",
            nomiMaschili = listOf("Jan"), nomiFemminili = listOf("Anna"),
            cognomi = listOf("Kowalski")
        )
    )

    private val favoritesFlow = MutableStateFlow<List<FavoriteNpc>>(emptyList())

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        // IMPORTANTE: Il mock deve restituire TUTTE le nazionalità usate nei test
        coEvery { mockMainRepository.readNpcNames(any()) } returns allTestNationalities
        coEvery { mockFavoriteNpcRepository.getAllFavorites() } returns favoritesFlow

        viewModel = NPCGeneratorViewModel(
            mainRepository = mockMainRepository,
            characterRepository = mockCharacterRepository,
            favoriteNpcRepository = mockFavoriteNpcRepository,
            ioDispatcher = testDispatcher
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    // --- TEST TRASFORMAZIONI LINGUISTICHE ---

    @Test
    fun `regola lettone trasforma correttamente i cognomi`() = runTest {
        advanceUntilIdle()
        viewModel.uiState.test {
            awaitItem()
            viewModel.setSelectedNationality("Lettone")
            viewModel.setSelectedGender(Gender.MALE)

            var state = awaitItem()
            while (state.npc == null || state.selectedNationality != "Lettone") state = awaitItem()

            viewModel.setSelectedGender(Gender.FEMALE)
            state = awaitItem()
            while (state.selectedGender != Gender.FEMALE) state = awaitItem()

            val surname = state.npc!!.cognome
            // Kalniņš -> Kalniņa
            assertTrue("Lettone Femminile fallito: $surname", surname.endsWith("a") || surname.endsWith("e"))
        }
    }

    @Test
    fun `regola lituana trasforma correttamente i cognomi`() = runTest {
        advanceUntilIdle()
        viewModel.uiState.test {
            awaitItem()
            viewModel.setSelectedNationality("Lituano")
            viewModel.setSelectedGender(Gender.MALE)

            var state = awaitItem()
            while (state.npc == null) state = awaitItem()

            viewModel.setSelectedGender(Gender.FEMALE)
            state = awaitItem()
            while (state.selectedGender != Gender.FEMALE || !state.npc!!.cognome.endsWith("ienė")) {
                state = awaitItem()
            }

            assertTrue(state.npc.cognome.endsWith("ienė") || state.npc.cognome.endsWith("uvienė"))
        }
    }

    @Test
    fun `regola polacca trasforma correttamente i cognomi`() = runTest {
        advanceUntilIdle()
        viewModel.uiState.test {
            awaitItem()
            viewModel.setSelectedNationality("Polacco")
            viewModel.setSelectedGender(Gender.MALE)

            var state = awaitItem()
            while (state.npc == null) state = awaitItem()

            viewModel.setSelectedGender(Gender.FEMALE)
            state = awaitItem()
            while (state.selectedGender != Gender.FEMALE || !state.npc!!.cognome.endsWith("ska")) {
                state = awaitItem()
            }

            assertEquals("Kowalska", state.npc.cognome)
        }
    }

    @Test
    fun `regola russa avanzata trasforma sky in skaya`() = runTest {
        advanceUntilIdle()
        viewModel.uiState.test {
            awaitItem()
            viewModel.setSelectedNationality("Russo")

            // Per forzare Tchaikovsky, dovremmo manipolare il random o avere solo quello nel mock.
            // In questo test, dato che Ivanov e Tchaikovsky sono nel mock, attendiamo uno dei due.
            viewModel.setSelectedGender(Gender.MALE)
            var state = awaitItem()
            while (state.npc == null) state = awaitItem()

            viewModel.setSelectedGender(Gender.FEMALE)
            state = awaitItem()
            while (state.selectedGender != Gender.FEMALE) state = awaitItem()

            val surname = state.npc!!.cognome
            assertTrue(surname.endsWith("ova") || surname.endsWith("skaya"))
        }
    }


    @Test
    fun `inizializzazione carica correttamente le nationality`() = runTest {
        advanceUntilIdle()

        viewModel.nationalityState.test {
            val state = awaitItem()
            assertTrue(state is NpcNationalityUiState.Success)
            assertEquals(6, (state as NpcNationalityUiState.Success).npcNationalities.size)
        }
    }

    @Test
    fun `generateAll crea un NPC valido per la nationality selezionata`() = runTest {
        advanceUntilIdle() // Attende il caricamento iniziale da init{}

        viewModel.uiState.test {
            awaitItem()

            viewModel.setSelectedNationality("Italiano")

            var state = awaitItem()
            while (state.npc == null) {
                state = awaitItem()
            }
            assertNotNull(state.npc)
            assertTrue(listOf("Rossi", "Bianchi").contains(state.npc.cognome))
            assertTrue(listOf("Mario", "Luigi", "Giulia", "Francesca").contains(state.npc.nome))
        }
    }

    @Test
    fun `regola russa trasforma correttamente il cognome al cambio genere`() = runTest {
        advanceUntilIdle()

        viewModel.uiState.test {
            awaitItem() // Skip initial

            viewModel.setSelectedNationality("Russo")
            viewModel.setSelectedGender(Gender.MALE)

            // Attendiamo che lo stato si stabilizzi su Russo Maschio
            var state = awaitItem()
            while (state.selectedNationality != "Russo" || state.npc == null) {
                state = awaitItem()
            }

            // Atto: Cambiamo in Femmina
            viewModel.setSelectedGender(Gender.FEMALE)

            // Verifica: Il cognome Ivanov deve diventare Ivanova o Petrov -> Petrova
            state = awaitItem()
            while (state.selectedGender != Gender.FEMALE || !state.npc!!.cognome.endsWith("a")) {
                state = awaitItem()
            }

            assertTrue(state.npc.cognome.endsWith("a"))
        }
    }

    @Test
    fun `regola islandese applica correttamente son o dottir`() = runTest {
        advanceUntilIdle()

        viewModel.uiState.test {
            awaitItem()

            viewModel.setSelectedNationality("Islandese")
            viewModel.setSelectedGender(Gender.MALE)

            var state = awaitItem()
            while (state.selectedNationality != "Islandese" || state.npc == null) {
                state = awaitItem()
            }

            viewModel.setSelectedGender(Gender.FEMALE)

            state = awaitItem()
            while (state.selectedGender != Gender.FEMALE || !state.npc!!.cognome.endsWith("dóttir")) {
                state = awaitItem()
            }

            assertTrue("Il cognome dovrebbe finire in dóttir", state.npc.cognome.endsWith("dóttir"))
        }
    }

    @Test
    fun `uiState riflette correttamente lo stato dei preferiti`() = runTest {
        advanceUntilIdle()

        viewModel.uiState.test {
            awaitItem()

            viewModel.setSelectedNationality("Italiano")

            var state = awaitItem()
            while (state.npc == null) {
                state = awaitItem()
            }

            val currentNpc = state.npc

            // Simuliamo che l'NPC generato venga aggiunto ai preferiti nel DB
            favoritesFlow.value = listOf(
                FavoriteNpc(
                    name = currentNpc.nome,
                    familyName = currentNpc.cognome,
                    secondName = currentNpc.secondName,
                    nationality = "Italiano"
                )
            )

            // Attendiamo che il combine riceva il nuovo valore dai preferiti
            state = awaitItem()
            while (!state.npc!!.isFavorite) {
                state = awaitItem()
            }

            assertTrue(state.npc.isFavorite)
        }
    }

    @Test
    fun `cambio nationality resetta o abilita il secondo nome in base alla cultura`() = runTest {
        advanceUntilIdle()

        viewModel.uiState.test {
            awaitItem()

            viewModel.setIncludeSecondName(true)
            // 1. Islandese: supportsSecondName = false nell'enum
            viewModel.setSelectedNationality("Islandese")
            var state = awaitItem()
            while (state.selectedNationality != "Islandese") {
                state = awaitItem()
            }
            assertFalse(state.includeSecondName)

            // 2. Italiano: supportsSecondName = true
            viewModel.setSelectedNationality("Italiano")
            viewModel.setIncludeSecondName(true)

            state = awaitItem()
            while (state.selectedNationality != "Italiano" || !state.includeSecondName) {
                state = awaitItem()
            }
            assertTrue(state.includeSecondName)

            // 3. Tornando a Islandese, includeSecondName deve tornare false automaticamente
            viewModel.setSelectedNationality("Islandese")
            state = awaitItem()
            while (state.selectedNationality != "Islandese") {
                state = awaitItem()
            }
            assertFalse(state.includeSecondName)
        }
    }
    @Test
    fun `toggleFavorite aggiunge un NPC ai preferiti se non presente`() = runTest {
        advanceUntilIdle()

        // Mock: l'NPC non è nei preferiti
        coEvery { mockFavoriteNpcRepository.findFavorite(any(), any(), any()) } returns null
        coEvery { mockFavoriteNpcRepository.addFavorite(any()) } returns Unit

        viewModel.setSelectedNationality("Italiano")
        advanceUntilIdle()

        viewModel.toggleFavorite()
        advanceUntilIdle()

        // Verifichiamo che addFavorite sia stato chiamato
        coVerify { mockFavoriteNpcRepository.addFavorite(any()) }
    }

    @Test
    fun `toggleFavorite rimuove un NPC dai preferiti se gia' presente`() = runTest {
        advanceUntilIdle()

        val currentNpc = FavoriteNpc(name = "Mario", familyName = "Rossi", nationality = "Italiano")

        // Mock: l'NPC è già nei preferiti
        coEvery { mockFavoriteNpcRepository.findFavorite(any(), any(), any()) } returns currentNpc
        coEvery { mockFavoriteNpcRepository.removeFavorite(any()) } returns Unit

        viewModel.setSelectedNationality("Italiano")
        advanceUntilIdle()

        viewModel.toggleFavorite()
        advanceUntilIdle()

        coVerify { mockFavoriteNpcRepository.removeFavorite(currentNpc) }
    }

    @Test
    fun `createCharacterFromNpc salva il personaggio e naviga`() = runTest {
        advanceUntilIdle()

        // Mock del salvataggio che ritorna un ID
        coEvery { mockCharacterRepository.saveCharacter(any()) } returns "new_char_id"

        viewModel.setSelectedNationality("Italiano")
        advanceUntilIdle()

        // Usiamo Turbine per monitorare gli eventi di navigazione
        viewModel.navigationEvent.test {
            viewModel.createCharacterFromNpc()

            val event = awaitItem()
            assertTrue(event is NpcNavigationEvent.ToCharacterSheet)
            assertEquals("new_char_id", (event as NpcNavigationEvent.ToCharacterSheet).characterId)
        }

        // Verifichiamo che il repository abbia ricevuto il nome corretto (Western order)
        coVerify {
            mockCharacterRepository.saveCharacter(match { it.name.contains("Rossi") || it.name.contains("Bianchi") })
        }
    }

    @Test
    fun `selectFavorite aggiorna lo stato con i dati del preferito`() = runTest {
        advanceUntilIdle()
        val favorite = FavoriteNpc(
            name = "Svetlana",
            secondName = "Volkova",
            familyName = "Ivanova",
            nationality = "Russo"
        )

        viewModel.uiState.test {
            // Saltiamo lo stato iniziale
            awaitItem()

            viewModel.selectFavorite(favorite)

            // Attendiamo l'emissione con i dati corretti
            var state = awaitItem()
            while (state.npc?.nome != "Svetlana") {
                state = awaitItem()
            }

            assertEquals("Svetlana", state.npc.nome)
            assertEquals("Ivanova", state.npc.cognome)
            assertEquals("Volkova", state.npc.secondName)
            assertEquals("Russo", state.selectedNationality)
            assertTrue(state.includeSecondName)
        }
    }
}
