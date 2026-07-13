package com.example.v5rules.viewModel

import app.cash.turbine.test
import com.example.v5rules.data.remote.repository.FriendRepository
import com.example.v5rules.data.remote.repository.FriendRequestResult
import io.mockk.coEvery
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
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class UserProfileViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val friendRepository = mockk<FriendRepository>(relaxed = true)

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `onEmailChanged should update email in uiState`() = runTest {
        val viewModel = UserProfileViewModel(friendRepository)
        viewModel.onEmailChanged("test@email.com")

        viewModel.uiState.test {
            val state = awaitItem()
            assertEquals("test@email.com", state.friendEmail)
            assertNull(state.feedbackMessage)
        }
    }

    @Test
    fun `showAddFriendDialog should reset dialog state`() = runTest {
        val viewModel = UserProfileViewModel(friendRepository)
        viewModel.showAddFriendDialog()

        viewModel.uiState.test {
            val state = awaitItem()
            assertTrue(state.showAddFriendDialog)
            assertEquals("", state.friendEmail)
            assertNull(state.feedbackMessage)
            assertFalse(state.isLoading)
        }
    }

    @Test
    fun `dismissAddFriendDialog should hide dialog`() = runTest {
        val viewModel = UserProfileViewModel(friendRepository)
        viewModel.showAddFriendDialog()
        viewModel.dismissAddFriendDialog()

        viewModel.uiState.test {
            assertFalse(awaitItem().showAddFriendDialog)
        }
    }

    @Test
    fun `sendFriendRequest should show error if email is blank`() = runTest {
        val viewModel = UserProfileViewModel(friendRepository)
        viewModel.onEmailChanged("")
        viewModel.sendFriendRequest()

        viewModel.uiState.test {
            assertEquals("Email cannot be empty.", awaitItem().feedbackMessage)
        }
    }

    @Test
    fun `sendFriendRequest should update feedback message on Success`() = runTest {
        coEvery { friendRepository.sendFriendRequest(any()) } returns FriendRequestResult.Success
        val viewModel = UserProfileViewModel(friendRepository)
        viewModel.onEmailChanged("test@email.com")
        viewModel.sendFriendRequest()
        
        advanceUntilIdle()

        viewModel.uiState.test {
            val state = awaitItem()
            assertEquals("Friend request sent!", state.feedbackMessage)
            assertFalse(state.showAddFriendDialog)
            assertFalse(state.isLoading)
        }
    }

    @Test
    fun `sendFriendRequest should update feedback message on Error`() = runTest {
        coEvery { friendRepository.sendFriendRequest(any()) } returns FriendRequestResult.Error("User not found")
        val viewModel = UserProfileViewModel(friendRepository)
        viewModel.onEmailChanged("test@email.com")
        viewModel.sendFriendRequest()
        
        advanceUntilIdle()

        viewModel.uiState.test {
            val state = awaitItem()
            assertEquals("User not found", state.feedbackMessage)
            assertFalse(state.isLoading)
        }
    }
}
