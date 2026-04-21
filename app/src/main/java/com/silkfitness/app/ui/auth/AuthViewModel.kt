package com.silkfitness.app.ui.auth

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.silkfitness.app.data.auth.AuthRepository
import com.silkfitness.app.data.firestore.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AuthUiState(
    val isSignedIn: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _state = MutableStateFlow(AuthUiState(isSignedIn = authRepository.currentUser != null))
    val authState: StateFlow<AuthUiState> = _state.asStateFlow()

    init {
        authRepository.authStateFlow()
            .onEach { user -> _state.update { it.copy(isSignedIn = user != null) } }
            .launchIn(viewModelScope)
    }

    fun signInWithGoogle(activityContext: Context, serverClientId: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = null) }
            val result = authRepository.signInWithGoogle(activityContext, serverClientId)
            result.onSuccess { user ->
                userRepository.ensureProfile(
                    displayName = user.displayName.orEmpty(),
                    email = user.email.orEmpty(),
                    photoUrl = user.photoUrl?.toString()
                )
                _state.update { it.copy(isLoading = false, isSignedIn = true) }
                onSuccess()
            }.onFailure { err ->
                _state.update { it.copy(isLoading = false, errorMessage = err.message ?: "Sign-in failed") }
            }
        }
    }

    fun signOut() {
        authRepository.signOut()
        _state.update { AuthUiState(isSignedIn = false) }
    }

    fun clearError() {
        _state.update { it.copy(errorMessage = null) }
    }
}
