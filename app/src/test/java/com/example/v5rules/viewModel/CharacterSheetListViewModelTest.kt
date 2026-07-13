package com.example.v5rules.viewModel

import app.cash.turbine.test
import com.example.v5rules.data.local.model.Character
import com.example.v5rules.data.local.repository.CharacterRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CharacterSheetListViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val characterRepository = mockk<CharacterRepository>(relaxed = true)
    private val auth = mockk<FirebaseAuth>(relaxed = true)
    private val firebaseUser = mockk<FirebaseUser>(relaxed = true)

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `uiState should emit characters when user is logged in`() = runTest {
        val authListenerSlot = slot<FirebaseAuth.AuthStateListener>()
        every { auth.addAuthStateListener(capture(authListenerSlot)) } answers {
            authListenerSlot.captured.onAuthStateChanged(auth)
        }
        every { auth.currentUser } returns firebaseUser

        val characters = listOf(Character(id = "1", name = "Char 1"))
        every { characterRepository.getAllCharacters() } returns flowOf(characters)

        val viewModel = CharacterSheetListViewModel(characterRepository, auth)
        advanceUntilIdle()

        viewModel.uiState.test {
            // Initial state from stateIn might be emitted first depending on how fast the flow starts
            val state = awaitItem()
            if (state.isLoading) {
                val nextState = awaitItem()
                assertEquals(characters, nextState.characterList)
                assertFalse(nextState.isLoading)
            } else {
                assertEquals(characters, state.characterList)
                assertFalse(state.isLoading)
            }
        }
    }

    @Test
    fun `uiState should emit empty list when user is not logged in`() = runTest {
        val authListenerSlot = slot<FirebaseAuth.AuthStateListener>()
        every { auth.addAuthStateListener(capture(authListenerSlot)) } answers {
            authListenerSlot.captured.onAuthStateChanged(auth)
        }
        every { auth.currentUser } returns null

        val viewModel = CharacterSheetListViewModel(characterRepository, auth)
        advanceUntilIdle()

        viewModel.uiState.test {
            val state = awaitItem()
            if (state.isLoading) {
                val nextState = awaitItem()
                assertTrue(nextState.characterList.isEmpty())
                assertFalse(nextState.isLoading)
            } else {
                assertTrue(state.characterList.isEmpty())
                assertFalse(state.isLoading)
            }
        }
    }
}
