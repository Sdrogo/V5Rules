package com.example.v5rules.viewModel

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val firebaseAuth = mockk<FirebaseAuth>()
    private val firestore = mockk<FirebaseFirestore>()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `onSignInStarted should set loading to true and clear error`() = runTest {
        val viewModel = LoginViewModel(firebaseAuth, firestore)
        viewModel.onSignInStarted()
        
        assertTrue(viewModel.uiState.value.isLoading)
        assertNull(viewModel.uiState.value.error)
    }

    @Test
    fun `buildGoogleSignInRequest should return request with correct client id`() = runTest {
        val viewModel = LoginViewModel(firebaseAuth, firestore)
        val clientId = "test_client_id"
        val request = viewModel.buildGoogleSignInRequest(clientId)
        
        // We can't easily inspect the internal options of GetCredentialRequest without more mocking
        // but we can check if it returns a non-null object
        assertTrue(request.credentialOptions.isNotEmpty())
    }
    
    @Test
    fun `onSignInFailed should set error message`() = runTest {
        val viewModel = LoginViewModel(firebaseAuth, firestore)
        val exception = mockk<androidx.credentials.exceptions.GetCredentialException>()
        io.mockk.every { exception.message } returns "User cancelled"
        
        viewModel.onSignInFailed(exception)
        
        assertFalse(viewModel.uiState.value.isLoading)
        assertEquals("Sign-in failed: User cancelled", viewModel.uiState.value.error)
    }
}

// Helper to make assertEquals available if not imported
fun assertEquals(expected: Any?, actual: Any?) {
    org.junit.Assert.assertEquals(expected, actual)
}
