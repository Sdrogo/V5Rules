package com.example.v5rules.repository

sealed class FriendshipActionResult {
    data object Success : FriendshipActionResult()
    data class Error(val message: String) : FriendshipActionResult()
}
