package com.silkfitness.app.data.firestore

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.silkfitness.app.data.auth.AuthRepository
import com.silkfitness.app.domain.model.UserProfile
import com.silkfitness.app.domain.model.UserSettings
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: AuthRepository
) {
    private val uid: String? get() = auth.currentUser?.uid

    fun observeProfile(): Flow<UserProfile?> {
        val user = uid ?: return emptyFlow()
        return callbackFlow {
            val reg = firestore.user(user).addSnapshotListener { snap, err ->
                if (err != null) { close(err); return@addSnapshotListener }
                trySend(snap?.toObject(UserProfile::class.java))
            }
            awaitClose { reg.remove() }
        }
    }

    suspend fun getProfile(): UserProfile? {
        val user = uid ?: return null
        return firestore.user(user).get().await().toObject(UserProfile::class.java)
    }

    suspend fun ensureProfile(displayName: String, email: String, photoUrl: String?) {
        val user = uid ?: return
        val existing = firestore.user(user).get().await()
        if (!existing.exists()) {
            firestore.user(user).set(
                UserProfile(
                    uid = user,
                    displayName = displayName,
                    email = email,
                    photoUrl = photoUrl
                )
            ).await()
        }
    }

    suspend fun updateSettings(settings: UserSettings) {
        val user = uid ?: return
        firestore.user(user).set(mapOf("settings" to settings), SetOptions.merge()).await()
    }

    suspend fun updateHeight(cm: Int) {
        val user = uid ?: return
        firestore.user(user).set(mapOf("heightCm" to cm), SetOptions.merge()).await()
    }
}
