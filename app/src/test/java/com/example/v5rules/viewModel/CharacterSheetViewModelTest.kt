package com.example.v5rules.viewModel

import app.cash.turbine.test
import com.example.v5rules.data.Character
import com.example.v5rules.data.Clan
import com.example.v5rules.data.Discipline
import com.example.v5rules.data.DisciplinePower
import com.example.v5rules.repository.CharacterRepository
import com.example.v5rules.repository.MainRepository
import com.example.v5rules.utils.CharacterSheetEvent
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CharacterSheetViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val mainRepository = mockk<MainRepository>(relaxed = true)
    private val characterRepository = mockk<CharacterRepository>(relaxed = true)

    private lateinit var viewModel: CharacterSheetViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        
        // Mock static data loading
        coEvery { mainRepository.loadClans(any()) } returns listOf(Clan(name = "Ventrue"))
        
        viewModel = CharacterSheetViewModel(mainRepository, characterRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initialization loads static data`() = runTest {
        advanceUntilIdle()
        
        viewModel.clans.test {
            val clans = awaitItem()
            assertEquals(1, clans.size)
            assertEquals("Ventrue", clans[0].name)
        }
    }

    @Test
    fun `setCharacter should update UI state with found character`() = runTest {
        val character = Character(id = "char_1", name = "Test Name")
        coEvery { characterRepository.getCharacter("char_1") } returns character

        viewModel.setCharacter("char_1")
        advanceUntilIdle()

        viewModel.uiState.test {
            val state = awaitItem()
            assertEquals("Test Name", state.character.name)
            assertEquals("char_1", state.character.id)
        }
    }

//    @Test
//    fun `onEvent NameChanged should update character name in UI state`() = runTest {
//        advanceUntilIdle()
//
//        viewModel.onEvent(CharacterSheetEvent.NameChanged("New Name"))
//        advanceUntilIdle()
//
//        viewModel.uiState.test {
//            val state = awaitItem()
//            assertEquals("New Name", state.character.name)
//        }
//    }

    @Test
    fun `onEvent ClanChanged should update character clan in UI state`() = runTest {
        val clan = Clan(name = "Brujah")
        advanceUntilIdle()
        
        viewModel.onEvent(CharacterSheetEvent.ClanChanged(clan))
        advanceUntilIdle()

        viewModel.uiState.test {
            val state = awaitItem()
            assertEquals("Brujah", state.character.clan?.name)
        }
    }

    @Test
    fun `onEvent GenerationChanged should update character generation in UI state`() = runTest {

        advanceUntilIdle()

        viewModel.onEvent(CharacterSheetEvent.GenerationChanged(11))
        advanceUntilIdle()

        viewModel.uiState.test {
            val state = awaitItem()
            assertEquals(11, state.character.generation)
        }
    }

    @Test
    fun `onEvent SireChanged should update character sire in UI state`() = runTest {

        advanceUntilIdle()

        viewModel.onEvent(CharacterSheetEvent.SireChanged("Test Sire"))
        advanceUntilIdle()

        viewModel.uiState.test {
            val state = awaitItem()
            assertEquals("Test Sire", state.character.sire)
        }
    }

    @Test
    fun `onEvent ConceptChanged should update character concept in UI state`() = runTest {

        advanceUntilIdle()

        viewModel.onEvent(CharacterSheetEvent.ConceptChanged("Test Concept"))
        advanceUntilIdle()

        viewModel.uiState.test {
            val state = awaitItem()
            assertEquals("Test Concept", state.character.concept)
        }
    }

    @Test
    fun `onEvent AmbitionChanged should update character ambition in UI state`() = runTest {

        advanceUntilIdle()

        viewModel.onEvent(CharacterSheetEvent.AmbitionChanged("Test Ambition"))
        advanceUntilIdle()

        viewModel.uiState.test {
            val state = awaitItem()
            assertEquals("Test Ambition", state.character.ambition)
        }
    }

    @Test
    fun `onEvent DesireChanged should update character desire in UI state`() = runTest {

        advanceUntilIdle()

        viewModel.onEvent(CharacterSheetEvent.DesireChanged("Test Desire"))
        advanceUntilIdle()

        viewModel.uiState.test {
            val state = awaitItem()
            assertEquals("Test Desire", state.character.desire)
        }
    }




    @Test
    fun `onEvent ShowSaveConfirmation should update dialogState`() = runTest {
        advanceUntilIdle()
        
        viewModel.onEvent(CharacterSheetEvent.ShowSaveConfirmation)
        advanceUntilIdle()

        viewModel.uiState.test {
            val state = awaitItem()
            assertEquals(DialogState.ShowSaveConfirmation, state.dialogState)
        }
    }

    @Test
    fun `onEvent DismissDialog should reset dialogState`() = runTest {
        advanceUntilIdle()
        viewModel.onEvent(CharacterSheetEvent.ShowSaveConfirmation)
        advanceUntilIdle()
        
        viewModel.onEvent(CharacterSheetEvent.DismissDialog)
        advanceUntilIdle()

        viewModel.uiState.test {
            val state = awaitItem()
            assertEquals(DialogState.None, state.dialogState)
        }
    }

    @Test
    fun `onEvent SaveClicked should call repository saveCharacter`() = runTest {
        coEvery { characterRepository.saveCharacter(any()) } returns "saved_id"
        advanceUntilIdle()
        
        viewModel.onEvent(CharacterSheetEvent.SaveClicked)
        advanceUntilIdle()

        viewModel.uiState.test {
            val state = awaitItem()
            assertEquals("saved_id", state.character.id)
        }
    }

    @Test
    fun `onEvent CleanupClicked should reset character`() = runTest {
        viewModel.onEvent(CharacterSheetEvent.NameChanged("Dirty"))
        advanceUntilIdle()
        
        viewModel.onEvent(CharacterSheetEvent.CleanupClicked)
        advanceUntilIdle()

        viewModel.uiState.test {
            val state = awaitItem()
            assertEquals("", state.character.name)
        }
    }

    @Test
    fun `onEvent StrengthChanged should update strength`() = runTest {
        advanceUntilIdle()
        viewModel.onEvent(CharacterSheetEvent.StrengthChanged(4))
        advanceUntilIdle()

        viewModel.uiState.test {
            val state = awaitItem()
            assertEquals(4, state.character.attributes.strength)
        }
    }

    @Test
    fun `onEvent DexterityChanged should update dexterity`() = runTest {
        advanceUntilIdle()
        viewModel.onEvent(CharacterSheetEvent.DexterityChanged(4))
        advanceUntilIdle()

        viewModel.uiState.test {
            val state = awaitItem()
            assertEquals(4, state.character.attributes.dexterity)
        }
    }

    @Test
    fun `onEvent StaminaChanged should update stamina`() = runTest {
        advanceUntilIdle()
        viewModel.onEvent(CharacterSheetEvent.StaminaChanged(4))
        advanceUntilIdle()

        viewModel.uiState.test {
            val state = awaitItem()
            assertEquals(4, state.character.attributes.stamina)
        }
    }

    @Test
    fun `onEvent CharismaChanged should update charisma`() = runTest {
        advanceUntilIdle()
        viewModel.onEvent(CharacterSheetEvent.CharismaChanged(4))
        advanceUntilIdle()

        viewModel.uiState.test {
            val state = awaitItem()
            assertEquals(4, state.character.attributes.charisma)
        }
    }

    @Test
    fun `onEvent ManipulationChanged should update manipulation`() = runTest {
        advanceUntilIdle()
        viewModel.onEvent(CharacterSheetEvent.ManipulationChanged(4))
        advanceUntilIdle()

        viewModel.uiState.test {
            val state = awaitItem()
            assertEquals(4, state.character.attributes.manipulation)
        }
    }

    @Test
    fun `onEvent ComposureChanged should update composure`() = runTest {
        advanceUntilIdle()
        viewModel.onEvent(CharacterSheetEvent.ComposureChanged(4))
        advanceUntilIdle()

        viewModel.uiState.test {
            val state = awaitItem()
            assertEquals(4, state.character.attributes.composure)
        }
    }

    @Test
    fun `onEvent IntelligenceChanged should update intelligence`() = runTest {
        advanceUntilIdle()
        viewModel.onEvent(CharacterSheetEvent.IntelligenceChanged(4))
        advanceUntilIdle()

        viewModel.uiState.test {
            val state = awaitItem()
            assertEquals(4, state.character.attributes.intelligence)
        }
    }

    @Test
    fun `onEvent WitsChanged should update wits`() = runTest {
        advanceUntilIdle()
        viewModel.onEvent(CharacterSheetEvent.WitsChanged(4))
        advanceUntilIdle()

        viewModel.uiState.test {
            val state = awaitItem()
            assertEquals(4, state.character.attributes.wits)
        }
    }

    @Test
    fun `onEvent ResolveChanged should update resolve`() = runTest {
        advanceUntilIdle()
        viewModel.onEvent(CharacterSheetEvent.ResolveChanged(4))
        advanceUntilIdle()

        viewModel.uiState.test {
            val state = awaitItem()
            assertEquals(4, state.character.attributes.resolve)
        }
    }

    @Test
    fun `onEvent AbilityChanged should update ability level`() = runTest {
        advanceUntilIdle()
        viewModel.onEvent(CharacterSheetEvent.AbilityChanged("Athletics", 3))
        advanceUntilIdle()

        viewModel.uiState.test {
            val state = awaitItem()
            val ability = state.character.abilities.find { it.name == "Athletics" }
            assertEquals(3, ability?.level)
        }
    }

    @Test
    fun `onEvent AbilitySpecializationChanged should update ability level`() = runTest {
        advanceUntilIdle()
        viewModel.onEvent(CharacterSheetEvent.AbilityChanged("Athletics", 3))
        advanceUntilIdle()

        viewModel.onEvent(CharacterSheetEvent.AbilitySpecializationChanged("Athletics", "Test"))
        advanceUntilIdle()

        viewModel.uiState.test {
            val state = awaitItem()
            val ability = state.character.abilities.find { it.name == "Athletics" }
            assertEquals("Test", ability?.specialization)
        }
    }

    @Test
    fun `onEvent DisciplineChanged should add Discipline`() = runTest {
        val testDiscipline = Discipline(id = "disc_1", title = "Test Discipline", level = 1)
        advanceUntilIdle()
        viewModel.onEvent(CharacterSheetEvent.DisciplineChanged(testDiscipline))
        advanceUntilIdle()

        viewModel.uiState.test {
            val state = awaitItem()
            val discipline = state.character.disciplines.find { it.id == "disc_1" }
            assertEquals(testDiscipline, discipline)
        }
    }

    @Test
    fun `onEvent DisciplinePowerAdded should add Discipline power`() = runTest {
        val testDiscipline = Discipline(id = "disc_1", title = "Test Discipline", level = 1)
        advanceUntilIdle()

        viewModel.onEvent(CharacterSheetEvent.DisciplineChanged(testDiscipline))
        advanceUntilIdle()

        val testDisciplinePower = DisciplinePower(id = "power_1", title = "Test Power", level = 1)
        viewModel.onEvent(CharacterSheetEvent.DisciplinePowerAdded("Test Discipline", testDisciplinePower))
        advanceUntilIdle()

        viewModel.uiState.test {
            val state = awaitItem()
            val discipline = state.character.disciplines.find { it.id == "disc_1" }
            assertEquals(listOf(testDisciplinePower), discipline?.selectedDisciplinePowers)
        }

        viewModel.onEvent(CharacterSheetEvent.DisciplinePowerRemoved("Test Discipline", testDisciplinePower))
        advanceUntilIdle()

        viewModel.uiState.test {
            val state = awaitItem()
            val discipline = state.character.disciplines.find { it.id == "disc_1" }
            assertEquals(listOf<DisciplinePower>(), discipline?.selectedDisciplinePowers)
        }

    }


    @Test
    fun `onEvent HungerChanged should update hunger level`() = runTest {
        advanceUntilIdle()
        viewModel.onEvent(CharacterSheetEvent.HungerChanged(2))
        advanceUntilIdle()

        viewModel.uiState.test {
            val state = awaitItem()
            assertEquals(2, state.character.hunger)
        }
    }

    @Test
    fun `onEvent HealthBoxClicked should toggle health damage type`() = runTest {
        advanceUntilIdle()
        // Initial state is DamageType.EMPTY (assuming default)
        viewModel.onEvent(CharacterSheetEvent.HealthBoxClicked(0))
        advanceUntilIdle()

        viewModel.uiState.test {
            val state = awaitItem()
            // EMPTY -> SUPERFICIAL according to toggleHealthBox logic in Character.kt
            assertEquals(com.example.v5rules.data.DamageType.SUPERFICIAL, state.character.health.boxes[0])
        }
    }

    @Test
    fun `onEvent ConfirmDelete should call repository delete and reset character`() = runTest {
        val character = Character(id = "char_to_delete", name = "Gone")
        coEvery { characterRepository.getCharacter("char_to_delete") } returns character
        
        viewModel.setCharacter("char_to_delete")
        advanceUntilIdle()
        
        viewModel.onEvent(CharacterSheetEvent.ConfirmDelete)
        advanceUntilIdle()

        viewModel.uiState.test {
            val state = awaitItem()
            assertEquals("", state.character.id)
            assertEquals("", state.character.name)
            coVerify { characterRepository.deleteCharacter(match { it.id == "char_to_delete" }) }
        }
    }
}
