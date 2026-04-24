package com.musclemax.app.ui.auth

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.musclemax.app.data.auth.AuthRepository
import com.musclemax.app.data.auth.SignUpOutcome
import com.musclemax.app.data.remote.UserRepository
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
    val errorMessage: String? = null,
    val verificationEmail: String? = null
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
            _state.update { it.copy(isLoading = true, errorMessage = null, verificationEmail = null) }
            authRepository.signInWithGoogle(activityContext, serverClientId)
                .onSuccess { user ->
                    userRepository.ensureProfile(
                        displayName = user.displayName,
                        email = user.email,
                        photoUrl = user.photoUrl
                    )
                    _state.update { it.copy(isLoading = false, isSignedIn = true) }
                    onSuccess()
                }
                .onFailure { err ->
                    _state.update { it.copy(isLoading = false, errorMessage = err.message ?: "Sign-in failed") }
                }
        }
    }

    fun signInWithEmail(email: String, password: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = null, verificationEmail = null) }
            authRepository.signInWithEmail(email.trim(), password)
                .onSuccess { user ->
                    userRepository.ensureProfile(
                        displayName = user.displayName,
                        email = user.email,
                        photoUrl = user.photoUrl
                    )
                    _state.update { it.copy(isLoading = false, isSignedIn = true) }
                    onSuccess()
                }
                .onFailure { err ->
                    _state.update { it.copy(isLoading = false, errorMessage = err.message ?: "Sign-in failed") }
                }
        }
    }

    fun signUpWithEmail(email: String, password: String, displayName: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = null, verificationEmail = null) }
            authRepository.signUpWithEmail(email.trim(), password, displayName.trim())
                .onSuccess { outcome ->
                    when (outcome) {
                        is SignUpOutcome.SignedIn -> {
                            userRepository.ensureProfile(
                                displayName = outcome.user.displayName,
                                email = outcome.user.email,
                                photoUrl = outcome.user.photoUrl
                            )
                            _state.update { it.copy(isLoading = false, isSignedIn = true) }
                            onSuccess()
                        }
                        is SignUpOutcome.VerificationRequired -> {
                            _state.update {
                                it.copy(isLoading = false, verificationEmail = outcome.email)
                            }
                        }
                    }
                }
                .onFailure { err ->
                    _state.update { it.copy(isLoading = false, errorMessage = err.message ?: "Sign-up failed") }
                }
        }
    }

    fun signOut() {
        viewModelScope.launch {
            authRepository.signOut()
            _state.update { AuthUiState(isSignedIn = false) }
        }
    }

    fun clearError() {
        _state.update { it.copy(errorMessage = null) }
    }
}
