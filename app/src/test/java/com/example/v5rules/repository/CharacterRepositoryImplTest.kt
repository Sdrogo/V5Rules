package com.example.v5rules.repository

import com.example.v5rules.data.Character
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import io.mockk.every
import io.mockk.mockk
import io.mockk.unmockkAll
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CharacterRepositoryImplTest {

    private lateinit var firestore: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var repository: CharacterRepositoryImpl
    private lateinit var firebaseUser: FirebaseUser

    @Before
    fun setUp() {
        firestore = mockk(relaxed = true)
        auth = mockk(relaxed = true)
        firebaseUser = mockk(relaxed = true)

        every { auth.currentUser } returns firebaseUser
        every { firebaseUser.uid } returns "test_user_id"

        repository = CharacterRepositoryImpl(firestore, auth)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `saveCharacter when id is blank should add to collection and update id`() = runTest {
        val character = Character(id = "", name = "Test Character")
        val collectionRef = mockk<CollectionReference>(relaxed = true)
        val documentRef = mockk<DocumentReference>(relaxed = true)
        val addTask = mockk<Task<DocumentReference>>(relaxed = true)
        val updateTask = mockk<Task<Void>>(relaxed = true)

        every { firestore.collection("users").document("test_user_id").collection("characters") } returns collectionRef
        every { collectionRef.add(character) } returns addTask
        every { addTask.isComplete } returns true
        every { addTask.isSuccessful } returns true
        every { addTask.isCanceled } returns false
        every { addTask.exception } returns null
        every { addTask.result } returns documentRef
        every { documentRef.id } returns "new_id"
        
        every { documentRef.update("id", "new_id") } returns updateTask
        every { updateTask.isComplete } returns true
        every { updateTask.isSuccessful } returns true
        every { updateTask.isCanceled } returns false
        every { updateTask.exception } returns null

        val resultId = repository.saveCharacter(character)

        assertEquals("new_id", resultId)
    }

    @Test
    fun `saveCharacter when id is NOT blank should set to document`() = runTest {
        val character = Character(id = "existing_id", name = "Test Character")
        val collectionRef = mockk<CollectionReference>(relaxed = true)
        val documentRef = mockk<DocumentReference>(relaxed = true)
        val setTask = mockk<Task<Void>>(relaxed = true)

        every { firestore.collection("users").document("test_user_id").collection("characters") } returns collectionRef
        every { collectionRef.document("existing_id") } returns documentRef
        every { documentRef.set(character) } returns setTask
        every { setTask.isComplete } returns true
        every { setTask.isSuccessful } returns true
        every { setTask.isCanceled } returns false
        every { setTask.exception } returns null

        val resultId = repository.saveCharacter(character)

        assertEquals("existing_id", resultId)
    }

    @Test
    fun `getCharacter should return character if document exists`() = runTest {
        val characterId = "char_123"
        val collectionRef = mockk<CollectionReference>(relaxed = true)
        val documentRef = mockk<DocumentReference>(relaxed = true)
        val getTask = mockk<Task<DocumentSnapshot>>(relaxed = true)
        val documentSnapshot = mockk<DocumentSnapshot>(relaxed = true)
        val character = Character(id = characterId, name = "Found Character")

        every { firestore.collection("users").document("test_user_id").collection("characters") } returns collectionRef
        every { collectionRef.document(characterId) } returns documentRef
        every { documentRef.get() } returns getTask
        every { getTask.isComplete } returns true
        every { getTask.isSuccessful } returns true
        every { getTask.isCanceled } returns false
        every { getTask.exception } returns null
        every { getTask.result } returns documentSnapshot
        every { documentSnapshot.id } returns characterId
        
        every { documentSnapshot.toObject(Character::class.java) } returns character

        val result = repository.getCharacter(characterId)

        assertEquals(character, result)
        assertEquals(characterId, result?.id)
    }

    @Test
    fun `getCharacter should return null if document does not exist`() = runTest {
        val characterId = "non_existent"
        val collectionRef = mockk<CollectionReference>(relaxed = true)
        val documentRef = mockk<DocumentReference>(relaxed = true)
        val getTask = mockk<Task<DocumentSnapshot>>(relaxed = true)
        val documentSnapshot = mockk<DocumentSnapshot>(relaxed = true)

        every { firestore.collection("users").document("test_user_id").collection("characters") } returns collectionRef
        every { collectionRef.document(characterId) } returns documentRef
        every { documentRef.get() } returns getTask
        every { getTask.isComplete } returns true
        every { getTask.isSuccessful } returns true
        every { getTask.isCanceled } returns false
        every { getTask.exception } returns null
        every { getTask.result } returns documentSnapshot
        
        every { documentSnapshot.toObject(Character::class.java) } returns null

        val result = repository.getCharacter(characterId)

        assertEquals(null, result)
    }

    @Test(expected = IllegalStateException::class)
    fun `getAllCharacters when user not logged in should throw exception`() = runTest {
        every { auth.currentUser } returns null
        val flow = repository.getAllCharacters()
        flow.first()
    }

    @Test
    fun `deleteCharacter should delete document if id is not blank`() = runTest {
        val character = Character(id = "char_123")
        val collectionRef = mockk<CollectionReference>(relaxed = true)
        val documentRef = mockk<DocumentReference>(relaxed = true)
        val deleteTask = mockk<Task<Void>>(relaxed = true)

        every { firestore.collection("users").document("test_user_id").collection("characters") } returns collectionRef
        every { collectionRef.document("char_123") } returns documentRef
        every { documentRef.delete() } returns deleteTask
        every { deleteTask.isComplete } returns true
        every { deleteTask.isSuccessful } returns true
        every { deleteTask.isCanceled } returns false
        every { deleteTask.exception } returns null

        repository.deleteCharacter(character)
        
        verify { documentRef.delete() }
    }

    @Test
    fun `deleteCharacter should do nothing if id is blank`() = runTest {
        val character = Character(id = "")
        repository.deleteCharacter(character)
        verify(exactly = 0) { firestore.collection(any()) }
    }
}
