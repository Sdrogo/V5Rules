package com.example.v5rules.viewModel

import app.cash.turbine.test
import com.example.v5rules.data.local.model.PredatorType
import com.example.v5rules.data.local.repository.MainRepository
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
class PredatorTypeViewModelTest {

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
    fun `initialization should fetch predator types and update state to Success`() = runTest {
        val types = listOf(PredatorType(name = "Alleycat"))
        every { mainRepository.loadPredatorType(any()) } returns types

        val viewModel = PredatorTypeViewModel(mainRepository, testDispatcher)
        advanceUntilIdle()

        viewModel.predatorTypeUiState.test {
            val state = awaitItem()
            assertTrue("Expected Success but was $state", state is PredatorTypeUiState.Success)
            assertEquals(1, (state as PredatorTypeUiState.Success).clans.size)
            assertEquals("Alleycat", state.clans[0].name)
        }
    }

    @Test
    fun `initialization should update state to Error when repository fails`() = runTest {
        every { mainRepository.loadPredatorType(any()) } throws Exception("Load failed")

        val viewModel = PredatorTypeViewModel(mainRepository, testDispatcher)
        advanceUntilIdle()

        viewModel.predatorTypeUiState.test {
            val state = awaitItem()
            assertTrue("Expected Error but was $state", state is PredatorTypeUiState.Error)
            assertEquals("Load failed", (state as PredatorTypeUiState.Error).message)
        }
    }
}
