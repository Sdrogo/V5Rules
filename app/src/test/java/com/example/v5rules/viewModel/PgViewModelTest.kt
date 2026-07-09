package com.example.v5rules.viewModel

import app.cash.turbine.test
import com.example.v5rules.data.Chapter
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
class PgViewModelTest {

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
    fun `initialization should fetch pg data and update state to Success`() = runTest {
        val chapters = listOf(Chapter(title = "Character Creation"))
        every { mainRepository.loadPg(any()) } returns chapters

        val viewModel = PgViewModel(mainRepository, testDispatcher)
        advanceUntilIdle()

        viewModel.pgUiState.test {
            val state = awaitItem()
            assertTrue("Expected Success but was $state", state is PgUiState.Success)
            assertEquals(1, (state as PgUiState.Success).chapters.size)
            assertEquals("Character Creation", state.chapters[0].title)
        }
    }

    @Test
    fun `initialization should update state to Error when repository fails`() = runTest {
        every { mainRepository.loadPg(any()) } throws Exception("Load failed")

        val viewModel = PgViewModel(mainRepository, testDispatcher)
        advanceUntilIdle()

        viewModel.pgUiState.test {
            val state = awaitItem()
            assertTrue("Expected Error but was $state", state is PgUiState.Error)
            assertEquals("Load failed", (state as PgUiState.Error).message)
        }
    }
}
