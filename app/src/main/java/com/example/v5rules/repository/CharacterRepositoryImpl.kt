package com.example.v5rules.repository

import com.example.v5rules.data.Character
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.snapshots
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CharacterRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) : CharacterRepository {

    private fun getUserCharactersCollection() = auth.currentUser?.uid?.let { userId ->
        firestore.collection("users").document(userId).collection("characters")
    }

    override fun getAllCharacters(): Flow<List<Character>> {
        val collection = getUserCharactersCollection()
            ?: throw IllegalStateException("User not logged in")

        return collection.snapshots().map { snapshot ->
            snapshot.documents.mapNotNull { document ->
                document.toObject<Character>()?.apply {
                    id = document.id
                }
            }
        }
    }

    override suspend fun getCharacter(id: String): Character? {
        val document = getUserCharactersCollection()
            ?.document(id)
            ?.get()
            ?.await()

        return document?.toObject<Character>()?.apply {
            this.id = document.id
        }
    }

    override suspend fun saveCharacter(character: Character): String {
        val collection = getUserCharactersCollection()
            ?: throw IllegalStateException("User not logged in")

        return if (character.id.isBlank()) {
            val newDocumentRef = collection.add(character).await()
            newDocumentRef.update("id", newDocumentRef.id).await()
            newDocumentRef.id
        } else {
            collection.document(character.id).set(character).await()
            character.id
        }
    }

    override suspend fun deleteCharacter(character: Character) {
        if (character.id.isBlank()) return

        getUserCharactersCollection()
            ?.document(character.id)
            ?.delete()
            ?.await()
    }
}