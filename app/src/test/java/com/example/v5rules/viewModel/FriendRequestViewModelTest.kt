package com.example.v5rules.viewModel

import app.cash.turbine.test
import com.example.v5rules.data.remote.model.FriendRequest
import com.example.v5rules.data.remote.repository.FriendRepository
import com.example.v5rules.data.remote.repository.FriendshipActionResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import io.mockk.coEvery
import io.mockk.every
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
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class FriendRequestViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val friendRepository = mockk<FriendRepository>(relaxed = true)
    private val auth = mockk<FirebaseAuth>(relaxed = true)
    private val firebaseUser = mockk<FirebaseUser>(relaxed = true)

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        // Ensure loadFriendRequests doesn't crash on init
        coEvery { friendRepository.getFriendRequests() } returns Result.success(emptyList())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initialization should load friend requests`() = runTest {
        val requests = listOf(FriendRequest(id = "1", senderId = "sender"))
        coEvery { friendRepository.getFriendRequests() } returns Result.success(requests)

        val viewModel = FriendRequestViewModel(friendRepository, auth)
        advanceUntilIdle()

        viewModel.uiState.test {
            val state = awaitItem()
            assertEquals(requests, state.friendRequests)
            assertFalse(state.isLoading)
        }
    }

    @Test
    fun `initialization should set error message on failure`() = runTest {
        coEvery { friendRepository.getFriendRequests() } returns Result.failure(Exception("Failed"))

        val viewModel = FriendRequestViewModel(friendRepository, auth)
        advanceUntilIdle()

        viewModel.uiState.test {
            val state = awaitItem()
            assertEquals("Failed", state.errorMessage)
            assertFalse(state.isLoading)
        }
    }

    @Test
    fun `getCurrentUserId should return user uid`() = runTest {
        every { auth.currentUser } returns firebaseUser
        every { firebaseUser.uid } returns "test_uid"

        val viewModel = FriendRequestViewModel(friendRepository, auth)
        assertEquals("test_uid", viewModel.getCurrentUserId())
    }

    @Test
    fun `acceptFriendRequest should update feedback on Success`() = runTest {
        val request = FriendRequest(id = "1")
        coEvery { friendRepository.acceptFriendRequest(request) } returns FriendshipActionResult.Success
        coEvery { friendRepository.getFriendRequests() } returns Result.success(emptyList())

        val viewModel = FriendRequestViewModel(friendRepository, auth)
        advanceUntilIdle()

        viewModel.acceptFriendRequest(request)
        advanceUntilIdle()

        viewModel.uiState.test {
            val state = awaitItem()
            assertEquals("Friend request accepted!", state.feedbackMessage)
        }
    }

    @Test
    fun `declineFriendRequest should update feedback on Success`() = runTest {
        coEvery { friendRepository.declineFriendRequest(any()) } returns FriendshipActionResult.Success
        coEvery { friendRepository.getFriendRequests() } returns Result.success(emptyList())

        val viewModel = FriendRequestViewModel(friendRepository, auth)
        advanceUntilIdle()

        viewModel.declineFriendRequest("1")
        advanceUntilIdle()

        viewModel.uiState.test {
            val state = awaitItem()
            assertEquals("Friend request declined.", state.feedbackMessage)
        }
    }

    @Test
    fun `cancelFriendRequest should update feedback on Success`() = runTest {
        coEvery { friendRepository.cancelFriendRequest(any()) } returns FriendshipActionResult.Success
        coEvery { friendRepository.getFriendRequests() } returns Result.success(emptyList())

        val viewModel = FriendRequestViewModel(friendRepository, auth)
        advanceUntilIdle()

        viewModel.cancelFriendRequest("1")
        advanceUntilIdle()

        viewModel.uiState.test {
            val state = awaitItem()
            assertEquals("Friend request cancelled.", state.feedbackMessage)
        }
    }

    @Test
    fun `dismissFeedbackMessage should clear feedbackMessage`() = runTest {
        coEvery { friendRepository.cancelFriendRequest(any()) } returns FriendshipActionResult.Success
        val viewModel = FriendRequestViewModel(friendRepository, auth)
        advanceUntilIdle()

        viewModel.cancelFriendRequest("1")
        advanceUntilIdle()
        
        viewModel.dismissFeedbackMessage()

        viewModel.uiState.test {
            assertNull(awaitItem().feedbackMessage)
        }
    }
}
