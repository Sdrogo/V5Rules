package com.example.v5rules.viewModel

import app.cash.turbine.test
import com.example.v5rules.data.Clan
import com.example.v5rules.repository.MainRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ClanViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val mainRepository = mockk<MainRepository>(relaxed = true)

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        mockkStatic(Dispatchers::class)
        every { Dispatchers.IO } returns testDispatcher
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        unmockkStatic(Dispatchers::class)
    }

    @Test
    fun `initialization should fetch clans and update state to Success`() = runTest {
        val clans = listOf(Clan(name = "Brujah"), Clan(name = "Ventrue"))
        every { mainRepository.loadClans(any()) } returns clans

        val viewModel = ClanViewModel(mainRepository)
        advanceUntilIdle()

        viewModel.clanUiState.test {
            val state = awaitItem()
            assertTrue("Expected Success but was $state", state is ClanUiState.Success)
            assertEquals(2, (state as ClanUiState.Success).clans.size)
            assertEquals("Brujah", state.clans[0].name)
        }
    }

    @Test
    fun `initialization should update state to Error when repository fails`() = runTest {
        every { mainRepository.loadClans(any()) } throws Exception("Failed to load")

        val viewModel = ClanViewModel(mainRepository)
        advanceUntilIdle()

        viewModel.clanUiState.test {
            val state = awaitItem()
            assertTrue("Expected Error but was $state", state is ClanUiState.Error)
            assertEquals("Failed to load", (state as ClanUiState.Error).message)
        }
    }
}
