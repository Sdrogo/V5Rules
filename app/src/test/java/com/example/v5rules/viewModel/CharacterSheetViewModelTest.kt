package com.example.v5rules.viewModel

import app.cash.turbine.test
import com.example.v5rules.data.Character
import com.example.v5rules.data.Clan
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

    @Test
    fun `onEvent NameChanged should update character name in UI state`() = runTest {
        advanceUntilIdle()
        
        viewModel.onEvent(CharacterSheetEvent.NameChanged("New Name"))
        advanceUntilIdle()

        viewModel.uiState.test {
            val state = awaitItem()
            assertEquals("New Name", state.character.name)
        }
    }

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
