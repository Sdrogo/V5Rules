package com.example.v5rules.repository

import com.example.v5rules.data.FavoriteNpc
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObjects
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FavoriteNpcRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) : FavoriteNpcRepository {

    private fun userFavoritesCollection() = auth.currentUser?.uid?.let { userId ->
        firestore.collection("users").document(userId).collection("favorite_npcs")
    }

    override fun getAllFavorites(): Flow<List<FavoriteNpc>> {
        val collection = userFavoritesCollection() ?: return emptyFlow()

        return callbackFlow {
            val listenerRegistration = collection.addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error) // Close the flow on error
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    val favorites = snapshot.toObjects<FavoriteNpc>()
                    trySend(favorites) // Send the latest list to the flow
                }
            }
            // This is called when the flow is cancelled
            awaitClose { listenerRegistration.remove() }
        }
    }

    override suspend fun addFavorite(npc: FavoriteNpc) {
        userFavoritesCollection()?.add(npc)?.await()
    }

    override suspend fun removeFavorite(npc: FavoriteNpc) {
        val collection = userFavoritesCollection() ?: return

        // Find the specific document to delete it. This is more robust.
        val querySnapshot = collection
            .whereEqualTo("name", npc.name)
            .whereEqualTo("familyName", npc.familyName)
            // Firestore treats null and non-existent fields differently.
            // This query works if secondName was saved as null.
            .whereEqualTo("secondName", npc.secondName)
            .limit(1)
            .get()
            .await()

        if (!querySnapshot.isEmpty) {
            val documentToDelete = querySnapshot.documents.first()
            collection.document(documentToDelete.id).delete().await()
        }
    }

    override suspend fun findFavorite(name: String, familyName: String, secondName: String?): FavoriteNpc? {
        val collection = userFavoritesCollection() ?: return null

        val querySnapshot = collection
            .whereEqualTo("name", name)
            .whereEqualTo("familyName", familyName)
            .whereEqualTo("secondName", secondName)
            .limit(1)
            .get()
            .await()

        return if (!querySnapshot.isEmpty) {
            querySnapshot.documents.first().toObject(FavoriteNpc::class.java)
        } else {
            null
        }
    }
}