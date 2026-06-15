package com.example.v5rules.repository

import com.example.v5rules.data.FavoriteNpc
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.QuerySnapshot
import app.cash.turbine.test
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class FavoriteNpcRepositoryImplTest {

    private lateinit var firestore: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var repository: FavoriteNpcRepositoryImpl
    private lateinit var firebaseUser: FirebaseUser

    @Before
    fun setUp() {
        firestore = mockk(relaxed = true)
        auth = mockk(relaxed = true)
        firebaseUser = mockk(relaxed = true)

        every { auth.currentUser } returns firebaseUser
        every { firebaseUser.uid } returns "test_user_id"

        repository = FavoriteNpcRepositoryImpl(firestore, auth)
    }

    @After
    fun tearDown() {
        // No static mocks to clean up here
    }

    @Test
    fun `addFavorite should add to collection`() = runTest {
        val npc = FavoriteNpc(name = "Test", familyName = "Npc")
        val collectionRef = mockk<CollectionReference>(relaxed = true)
        val addTask = mockk<Task<DocumentReference>>(relaxed = true)

        every { firestore.collection("users").document("test_user_id").collection("favorite_npcs") } returns collectionRef
        every { collectionRef.add(npc) } returns addTask
        every { addTask.isComplete } returns true
        every { addTask.isSuccessful } returns true
        every { addTask.isCanceled } returns false
        every { addTask.exception } returns null

        repository.addFavorite(npc)

        verify { collectionRef.add(npc) }
    }

    @Test
    fun `getAllFavorites should emit list of favorites`() = runTest {
        val collectionRef = mockk<CollectionReference>(relaxed = true)
        val querySnapshot = mockk<QuerySnapshot>(relaxed = true)
        val npc = FavoriteNpc(name = "Fav", familyName = "Npc")
        val listenerRegistration = mockk<ListenerRegistration>(relaxed = true)
        val listenerSlot = slot<EventListener<QuerySnapshot>>()

        every { firestore.collection("users").document("test_user_id").collection("favorite_npcs") } returns collectionRef
        every { collectionRef.addSnapshotListener(capture(listenerSlot)) } returns listenerRegistration
        
        // Mocking toObjects via the QuerySnapshot mock
        every { querySnapshot.toObjects(FavoriteNpc::class.java) } returns listOf(npc)

        repository.getAllFavorites().test {
            // Trigger the listener
            listenerSlot.captured.onEvent(querySnapshot, null)
            
            val result = awaitItem()
            assertEquals(1, result.size)
            assertEquals("Fav", result[0].name)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `findFavorite should return npc if exists`() = runTest {
        val collectionRef = mockk<CollectionReference>(relaxed = true)
        val querySnapshot = mockk<QuerySnapshot>(relaxed = true)
        val documentSnapshot = mockk<DocumentSnapshot>(relaxed = true)
        val getTask = mockk<Task<QuerySnapshot>>(relaxed = true)
        val npc = FavoriteNpc(name = "Mario", familyName = "Rossi", secondName = "Luigi")

        every { firestore.collection("users").document("test_user_id").collection("favorite_npcs") } returns collectionRef
        // findFavorite uses whereEqualTo and limit
        every { collectionRef.whereEqualTo(any<String>(), any()) } returns collectionRef
        every { collectionRef.limit(any()) } returns collectionRef
        every { collectionRef.get() } returns getTask
        
        every { getTask.isComplete } returns true
        every { getTask.isSuccessful } returns true
        every { getTask.isCanceled } returns false
        every { getTask.exception } returns null
        every { getTask.result } returns querySnapshot
        
        every { querySnapshot.documents } returns listOf(documentSnapshot)
        every { documentSnapshot.toObject(FavoriteNpc::class.java) } returns npc

        val result = repository.findFavorite("Mario", "Rossi", "Luigi")

        assertEquals(npc, result)
    }
}
