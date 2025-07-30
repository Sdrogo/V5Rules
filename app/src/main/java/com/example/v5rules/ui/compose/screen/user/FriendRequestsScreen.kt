package com.example.v5rules.ui.compose.screen.user

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.v5rules.data.FriendRequest
import com.example.v5rules.viewModel.FriendRequestViewModel

@Composable
fun FriendRequestsScreen(
    onTitleChanged: (String) -> Unit,
    viewModel: FriendRequestViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val currentUserId = viewModel.getCurrentUserId()

    LaunchedEffect(Unit) {
        onTitleChanged("Friend Requests")
    }

    LaunchedEffect(uiState.feedbackMessage) {
        uiState.feedbackMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.dismissFeedbackMessage()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        if (uiState.isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        } else if (uiState.errorMessage != null) {
            Text(
                text = uiState.errorMessage!!,
                color = MaterialTheme.colorScheme.error,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(16.dp)
            )
        } else if (uiState.friendRequests.isEmpty()) {
            Text(
                text = "You have no friend requests.",
                modifier = Modifier.align(Alignment.Center)
            )
        } else {
            LazyColumn(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Separa le richieste ricevute da quelle inviate
                val receivedRequests = uiState.friendRequests.filter { it.recipientId == currentUserId && it.status == "pending" }
                val sentRequests = uiState.friendRequests.filter { it.senderId == currentUserId && it.status == "pending" }
                val oldRequests = uiState.friendRequests.filter { it.status != "pending" }

                // Mostra prima le richieste ricevute
                if (receivedRequests.isNotEmpty()) {
                    item {
                        SectionHeader("Received Requests")
                    }
                    items(receivedRequests) { request ->
                        FriendRequestItem(
                            request = request,
                            isReceived = true,
                            onAccept = { viewModel.acceptFriendRequest(request) },
                            onDecline = { viewModel.declineFriendRequest(request.id) }
                        )
                    }
                }

                // Poi le richieste inviate
                if (sentRequests.isNotEmpty()) {
                    item {
                        SectionHeader("Sent Requests")
                    }
                    items(sentRequests) { request ->
                        FriendRequestItem(
                            request = request,
                            isReceived = false,
                            onCancel = { viewModel.cancelFriendRequest(request.id) }
                        )
                    }
                }
                 if (oldRequests.isNotEmpty()) {
                    item {
                        SectionHeader("Old Requests")
                    }
                    items(oldRequests) { request ->
                        FriendRequestItem(
                            request = request,
                            isReceived = request.recipientId == currentUserId
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SectionHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleLarge,
        modifier = Modifier.padding(bottom = 8.dp)
    )
}

@Composable
private fun FriendRequestItem(
    request: FriendRequest,
    isReceived: Boolean,
    onAccept: () -> Unit = {},
    onDecline: () -> Unit = {},
    onCancel: () -> Unit = {}
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "From: ${request.senderEmail}",
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = "To: ${request.recipientEmail}",
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = "Status: ${request.status}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            if (request.status == "pending") {
                Row {
                    if (isReceived) {
                        IconButton(onClick = onAccept) {
                            Icon(Icons.Filled.Check, contentDescription = "Accept", tint = MaterialTheme.colorScheme.primary)
                        }
                        Spacer(Modifier.width(8.dp))
                        IconButton(onClick = onDecline) {
                            Icon(Icons.Filled.Close, contentDescription = "Decline", tint = MaterialTheme.colorScheme.error)
                        }
                    } else {
                        IconButton(onClick = onCancel) {
                            Icon(Icons.Filled.Close, contentDescription = "Cancel Request", tint = MaterialTheme.colorScheme.error)
                        }
                    }
                }
            }
        }
    }
}
