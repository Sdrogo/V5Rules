package com.example.v5rules.data.remote.model

data class FriendRequest(
    val id: String = "",
    val senderId: String = "",
    val senderEmail: String? = null,
    val recipientId: String = "",
    val recipientEmail: String? = null,
    val status: String = "",
    val timestamp: Long = 0
)