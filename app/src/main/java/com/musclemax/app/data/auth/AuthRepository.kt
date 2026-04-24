package com.musclemax.app.data.auth

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential.Companion.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.Google
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.auth.providers.builtin.IDToken
import io.github.jan.supabase.auth.status.SessionStatus
import io.github.jan.supabase.auth.user.UserInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import javax.inject.Inject
import javax.inject.Singleton

sealed interface SignUpOutcome {
    data class SignedIn(val user: AppUser) : SignUpOutcome
    data class VerificationRequired(val email: String) : SignUpOutcome
}

@Singleton
class AuthRepository @Inject constructor(
    private val supabase: SupabaseClient
) {
    val currentUser: AppUser?
        get() = supabase.auth.currentUserOrNull()?.toAppUser()

    fun authStateFlow(): Flow<AppUser?> =
        supabase.auth.sessionStatus.map { status ->
            (status as? SessionStatus.Authenticated)?.session?.user?.toAppUser()
        }

    suspend fun signInWithGoogle(
        activityContext: Context,
        serverClientId: String
    ): Result<AppUser> = runCatching {
        val credentialManager = CredentialManager.create(activityContext)
        val googleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId(serverClientId)
            .setAutoSelectEnabled(true)
            .build()
        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        val response = credentialManager.getCredential(activityContext, request)
        val credential = response.credential as? CustomCredential
            ?: error("Unexpected credential type")
        require(credential.type == TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
            "Unexpected credential type: ${credential.type}"
        }
        val googleIdToken = GoogleIdTokenCredential.createFrom(credential.data).idToken

        supabase.auth.signInWith(IDToken) {
            idToken = googleIdToken
            provider = Google
        }

        supabase.auth.currentUserOrNull()?.toAppUser()
            ?: error("Supabase sign-in returned no session")
    }

    suspend fun signInWithEmail(email: String, password: String): Result<AppUser> = runCatching {
        supabase.auth.signInWith(Email) {
            this.email = email
            this.password = password
        }
        supabase.auth.currentUserOrNull()?.toAppUser()
            ?: error("Supabase sign-in returned no session")
    }

    suspend fun signUpWithEmail(
        email: String,
        password: String,
        displayName: String
    ): Result<SignUpOutcome> = runCatching {
        supabase.auth.signUpWith(Email) {
            this.email = email
            this.password = password
            if (displayName.isNotBlank()) {
                data = buildJsonObject {
                    put("full_name", JsonPrimitive(displayName))
                }
            }
        }
        val user = supabase.auth.currentUserOrNull()?.toAppUser()
        if (user != null) SignUpOutcome.SignedIn(user)
        else SignUpOutcome.VerificationRequired(email)
    }

    suspend fun signOut() {
        supabase.auth.signOut()
    }
}

private fun UserInfo.toAppUser(): AppUser {
    val meta = userMetadata
    val name = meta?.get("full_name")?.stringValue()
        ?: meta?.get("name")?.stringValue()
        ?: email?.substringBefore('@')
        ?: ""
    val picture = meta?.get("avatar_url")?.stringValue()
        ?: meta?.get("picture")?.stringValue()
    return AppUser(
        id = id,
        displayName = name,
        email = email.orEmpty(),
        photoUrl = picture
    )
}

private fun JsonElement.stringValue(): String? =
    (this as? JsonPrimitive)?.takeIf { it.isString }?.content
