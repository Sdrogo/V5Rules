package com.example.v5rules.viewModel

import app.cash.turbine.test
import com.example.v5rules.data.Discipline
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
class DisciplineViewModelTest {

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
    fun `initialization should fetch disciplines and update state to Success`() = runTest {
        val disciplines = listOf(Discipline(title = "Animalism"), Discipline(title = "Celerity"))
        every { mainRepository.loadDisciplines(any()) } returns disciplines

        val viewModel = DisciplineViewModel(mainRepository)
        advanceUntilIdle()

        viewModel.disciplineUiState.test {
            val state = awaitItem()
            assertTrue(state is DisciplineUiState.Success)
            assertEquals(2, (state as DisciplineUiState.Success).disciplines.size)
            assertEquals("Animalism", state.disciplines[0].title)
        }
    }

    @Test
    fun `initialization should update state to Error when repository fails`() = runTest {
        every { mainRepository.loadDisciplines(any()) } throws Exception("Failed to load")

        val viewModel = DisciplineViewModel(mainRepository)
        advanceUntilIdle()

        viewModel.disciplineUiState.test {
            val state = awaitItem()
            assertTrue(state is DisciplineUiState.Error)
            assertEquals("Failed to load", (state as DisciplineUiState.Error).message)
        }
    }
}
