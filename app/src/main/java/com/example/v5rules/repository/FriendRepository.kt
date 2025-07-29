package com.example.v5rules.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

// Definisce i possibili risultati dell'operazione
sealed class FriendRequestResult {
    data object Success : FriendRequestResult()
    data class Error(val message: String) : FriendRequestResult()
}

@Singleton
class FriendRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) {

    /**
     * Invia una richiesta di amicizia a un altro utente tramite la sua email.
     */
    suspend fun sendFriendRequest(recipientEmail: String): FriendRequestResult {
        val currentUser = auth.currentUser
            ?: return FriendRequestResult.Error("User not logged in.")

        // L'utente non può aggiungere se stesso
        if (currentUser.email.equals(recipientEmail, ignoreCase = true)) {
            return FriendRequestResult.Error("You cannot send a friend request to yourself.")
        }

        return try {
            val lowerCaseRecipientEmail = recipientEmail.trim().lowercase()
            // 1. Trova l'utente destinatario tramite email
            val recipientQuery = firestore.collection("users")
                .whereEqualTo("email", lowerCaseRecipientEmail)
                .limit(1)
                .get()
                .await()

            if (recipientQuery.isEmpty) {
                return FriendRequestResult.Error("User with that email does not exist.")
            }

            val recipientId = recipientQuery.documents.first().id

            // 2. Controlla se una richiesta (in un senso o nell'altro) esiste già
            val requestExistsQuery1 = firestore.collection("friend_requests")
                .whereEqualTo("senderId", currentUser.uid)
                .whereEqualTo("recipientId", recipientId)
                .get().await()

            val requestExistsQuery2 = firestore.collection("friend_requests")
                .whereEqualTo("senderId", recipientId)
                .whereEqualTo("recipientId", currentUser.uid)
                .get().await()

            if (!requestExistsQuery1.isEmpty || !requestExistsQuery2.isEmpty) {
                return FriendRequestResult.Error("A friend request already exists between you and this user.")
            }

            // 3. Crea il documento per la nuova richiesta di amicizia
            val request = hashMapOf(
                "senderId" to currentUser.uid,
                "senderEmail" to currentUser.email?.lowercase(), // Salva anche l'email del mittente in minuscolo per coerenza
                "recipientId" to recipientId,
                "recipientEmail" to lowerCaseRecipientEmail,
                "status" to "pending", // Gli stati possono essere "pending", "accepted", "declined"
                "timestamp" to System.currentTimeMillis()
            )

            firestore.collection("friend_requests").add(request).await()
            FriendRequestResult.Success

        } catch (e: Exception) {
            FriendRequestResult.Error(e.message ?: "An unknown error occurred.")
        }
    }
}
