package com.example.v5rules.viewModel

import app.cash.turbine.test
import com.example.v5rules.data.Loresheet
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
class LoresheetViewModelTest {

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
    fun `initialization should fetch loresheets and update state to Success`() = runTest {
        val loresheets = listOf(Loresheet(title = "Lore 1"), Loresheet(title = "Lore 2"))
        every { mainRepository.loadLoresheet(any()) } returns loresheets

        val viewModel = LoresheetViewModel(mainRepository)
        advanceUntilIdle()

        viewModel.loresheetUiState.test {
            val state = awaitItem()
            assertTrue(state is LoresheetUiState.Success)
            assertEquals(2, (state as LoresheetUiState.Success).loresheets.size)
        }
    }

    @Test
    fun `updateSearchQuery should filter loresheets correctly`() = runTest {
        val loresheets = listOf(
            Loresheet(title = "Ventrue Lore"),
            Loresheet(title = "Brujah Lore"),
            Loresheet(title = "Something Else")
        )
        every { mainRepository.loadLoresheet(any()) } returns loresheets

        val viewModel = LoresheetViewModel(mainRepository)
        advanceUntilIdle()

        viewModel.filteredLoresheets.test {
            // Initial value from stateIn is emptyList()
            assertEquals(0, awaitItem().size)

            // Then it computes the first combined value
            assertEquals(3, awaitItem().size)

            viewModel.updateSearchQuery("Lore")
            assertEquals(2, awaitItem().size)

            viewModel.updateSearchQuery("Ventrue")
            val filtered = awaitItem()
            assertEquals(1, filtered.size)
            assertEquals("Ventrue Lore", filtered[0].title)

            viewModel.updateSearchQuery("NonExistent")
            assertTrue(awaitItem().isEmpty())
        }
    }
}
