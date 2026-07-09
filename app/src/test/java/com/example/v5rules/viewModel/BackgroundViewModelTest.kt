package com.example.v5rules.viewModel

import app.cash.turbine.test
import com.example.v5rules.data.Background
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
class BackgroundViewModelTest {

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
    fun `initialization should fetch backgrounds and update state to Success`() = runTest {
        val backgrounds = listOf(
            Background(title = "Resources"),
            Background(title = "Contacts")
        )
        every { mainRepository.loadBackground(any()) } returns backgrounds

        val viewModel = BackgroundViewModel(mainRepository, testDispatcher)
        advanceUntilIdle()

        viewModel.backgroundUiState.test {
            val state = awaitItem()
            assertTrue("Expected Success but was $state", state is BackgroundUiState.Success)
            assertEquals(2, (state as BackgroundUiState.Success).backgrounds.size)
            assertEquals("Resources", state.backgrounds[0].title)
        }
    }

    @Test
    fun `initialization should update state to Error when repository fails`() = runTest {
        every { mainRepository.loadBackground(any()) } throws Exception("Failed to load backgrounds")

        val viewModel = BackgroundViewModel(mainRepository, testDispatcher)
        advanceUntilIdle()

        viewModel.backgroundUiState.test {
            val state = awaitItem()
            assertTrue("Expected Error but was $state", state is BackgroundUiState.Error)
            assertEquals("Failed to load backgrounds", (state as BackgroundUiState.Error).message)
        }
    }

    @Test
    fun `updateSearchQuery should filter backgrounds by title`() = runTest {
        val backgrounds = listOf(
            Background(id = "1", title = "Resources"),
            Background(id = "2", title = "Contacts")
        )
        every { mainRepository.loadBackground(any()) } returns backgrounds

        val viewModel = BackgroundViewModel(mainRepository, testDispatcher)
        viewModel.filteredBackgrounds.test {
            awaitItem()

            // Filter by "Res"
            viewModel.updateSearchQuery("Res")
            val filtered = awaitItem()
            assertEquals(1, filtered.size)
            assertEquals("Resources", filtered[0].title)
        }
    }

//    @Test
//    fun `filteredBackgrounds should filter by merits, flaws, directFlaws and prerequisites`() = runTest {
//        val backgrounds = listOf(
//            Background(
//                title = "Allies",
//                merits = listOf(Advantage(title = "Wealthy Ally")),
//                flaws = listOf(Advantage(title = "Unreliable Ally")),
//                directFlaws = listOf(Advantage(title = "Enemy")),
//                prerequisites = "Charisma 2"
//            ),
//            Background(title = "Contacts")
//        )
//        every { mainRepository.loadBackground(any()) } returns backgrounds
//
//        val viewModel = BackgroundViewModel(mainRepository, testDispatcher)
//        advanceUntilIdle()
//
//        viewModel.filteredBackgrounds.test {
//            awaitItem() // Initial state with empty query
//
//            // Search by merit
//            viewModel.updateSearchQuery("Wealthy")
//            var result = awaitItem()
//            assertEquals(1, result.size)
//            assertEquals("Allies", result[0].title)
//
//            // Search by flaw
//            viewModel.updateSearchQuery("Unreliable")
//            result = awaitItem()
//            assertEquals(1, result.size)
//            assertEquals("Allies", result[0].title)
//
//            // Search by direct flaw
//            viewModel.updateSearchQuery("Enemy")
//            result = awaitItem()
//            assertEquals(1, result.size)
//            assertEquals("Allies", result[0].title)
//
//            // Search by prerequisites
//            viewModel.updateSearchQuery("Charisma")
//            result = awaitItem()
//            assertEquals(1, result.size)
//            assertEquals("Allies", result[0].title)
//        }
//    }
}
