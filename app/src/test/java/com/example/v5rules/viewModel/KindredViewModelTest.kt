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
class KindredViewModelTest {

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
    fun `initialization should fetch kindred and update state to Success`() = runTest {
        val kindred = listOf(Chapter(title = "Kindred History"))
        every { mainRepository.loadKindred(any()) } returns kindred

        val viewModel = KindredViewModel(mainRepository, testDispatcher)
        advanceUntilIdle()

        viewModel.kindredUiState.test {
            val state = awaitItem()
            assertTrue("Expected Success but was $state", state is KindredUiState.Success)
            assertEquals(1, (state as KindredUiState.Success).chapters.size)
            assertEquals("Kindred History", state.chapters[0].title)
        }
    }

    @Test
    fun `initialization should update state to Error when repository fails`() = runTest {
        every { mainRepository.loadKindred(any()) } throws Exception("Load failed")

        val viewModel = KindredViewModel(mainRepository, testDispatcher)
        advanceUntilIdle()

        viewModel.kindredUiState.test {
            val state = awaitItem()
            assertTrue("Expected Error but was $state", state is KindredUiState.Error)
            assertEquals("Load failed", (state as KindredUiState.Error).message)
        }
    }
}
