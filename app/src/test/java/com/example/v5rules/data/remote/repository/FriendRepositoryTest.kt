package com.example.v5rules.data.remote.repository

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class FriendRepositoryTest {

    private val firestore = mockk<FirebaseFirestore>()
    private val auth = mockk<FirebaseAuth>()
    private val currentUser = mockk<FirebaseUser>()
    
    private lateinit var repository: FriendRepository

    @Before
    fun setUp() {
        repository = FriendRepository(firestore, auth)
        every { auth.currentUser } returns currentUser
        every { currentUser.uid } returns "my_uid"
        every { currentUser.email } returns "me@example.com"
        
        mockkStatic("kotlinx.coroutines.tasks.TasksKt")
    }

    @Test
    fun `sendFriendRequest should fail when user not logged in`() = runTest {
        every { auth.currentUser } returns null
        val result = repository.sendFriendRequest("other@example.com")
        assertTrue(result is FriendRequestResult.Error)
        assertEquals("User not logged in.", (result as FriendRequestResult.Error).message)
    }

    @Test
    fun `sendFriendRequest should fail when sending to self`() = runTest {
        val result = repository.sendFriendRequest("me@example.com")
        assertTrue(result is FriendRequestResult.Error)
        assertEquals("You cannot send a friend request to yourself.", (result as FriendRequestResult.Error).message)
    }

    @Test
    fun `declineFriendRequest should return Success on success`() = runTest {
        val requestId = "req123"
        val docRef = mockk<DocumentReference>()
        val mockTask = mockk<Task<Void>>()

        every { firestore.collection("friend_requests") } returns mockk {
            every { document(requestId) } returns docRef
        }
        every { docRef.update("status", "declined") } returns mockTask
        coEvery { mockTask.await() } returns mockk()

        val result = repository.declineFriendRequest(requestId)
        assertTrue(result is FriendshipActionResult.Success)
    }

    @Test
    fun `cancelFriendRequest should return Success on success`() = runTest {
        val requestId = "req123"
        val docRef = mockk<DocumentReference>()
        val mockTask = mockk<Task<Void>>()

        every { firestore.collection("friend_requests") } returns mockk {
            every { document(requestId) } returns docRef
        }
        every { docRef.delete() } returns mockTask
        coEvery { mockTask.await() } returns mockk()

        val result = repository.cancelFriendRequest(requestId)
        assertTrue(result is FriendshipActionResult.Success)
    }

    @Test
    fun `acceptFriendRequest should return Success on success`() = runTest {
        val requestId = "req123"
        val request = com.example.v5rules.data.remote.model.FriendRequest(id = requestId, senderId = "s", recipientId = "r")
        val docRef = mockk<DocumentReference>()
        val updateTask = mockk<Task<Void>>()
        val addTask = mockk<Task<DocumentReference>>()

        every { firestore.collection("friend_requests") } returns mockk {
            every { document(requestId) } returns docRef
        }
        every { docRef.update("status", "accepted") } returns updateTask
        coEvery { updateTask.await() } returns mockk()

        every { firestore.collection("friends") } returns mockk {
            every { add(any()) } returns addTask
        }
        coEvery { addTask.await() } returns mockk()

        val result = repository.acceptFriendRequest(request)
        assertTrue(result is FriendshipActionResult.Success)
    }

    @Test
    fun `getFriends should return list of friend IDs`() = runTest {
        val mockTask = mockk<Task<QuerySnapshot>>()
        val mockSnapshot = mockk<QuerySnapshot>()
        val mockDoc = mockk<com.google.firebase.firestore.QueryDocumentSnapshot>()

        every { firestore.collection("friends") } returns mockk {
            every { whereArrayContains("userIds", "my_uid") } returns mockk {
                every { get() } returns mockTask
            }
        }
        coEvery { mockTask.await() } returns mockSnapshot
        every { mockSnapshot.documents } returns listOf(mockDoc)
        every { mockDoc["userIds"] } returns listOf("my_uid", "friend_uid")

        val result = repository.getFriends()
        assertTrue(result.isSuccess)
        assertEquals(listOf("friend_uid"), result.getOrNull())
    }
}
