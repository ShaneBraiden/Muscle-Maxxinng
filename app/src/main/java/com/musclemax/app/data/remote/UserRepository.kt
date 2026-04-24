package com.musclemax.app.data.remote

import com.musclemax.app.data.auth.AuthRepository
import com.musclemax.app.domain.model.UserProfile
import com.musclemax.app.domain.model.UserSettings
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val supabase: SupabaseClient,
    private val auth: AuthRepository
) {
    private val refresh = RefreshBus()

    fun observeProfile(): Flow<UserProfile?> = refresh.observe { getProfile() }

    suspend fun getProfile(): UserProfile? {
        val uid = auth.currentUser?.id ?: return null
        return supabase.from(SupabaseTables.USERS)
            .select {
                filter { eq("id", uid) }
                limit(1)
            }
            .decodeSingleOrNull()
    }

    suspend fun ensureProfile(displayName: String, email: String, photoUrl: String?) {
        val uid = auth.currentUser?.id ?: return
        val existing = getProfile()
        if (existing == null) {
            supabase.from(SupabaseTables.USERS).insert(
                UserProfile(
                    id = uid,
                    displayName = displayName,
                    email = email,
                    photoUrl = photoUrl
                )
            )
            refresh.trigger()
        }
    }

    suspend fun updateSettings(settings: UserSettings) {
        val current = getProfile() ?: return
        supabase.from(SupabaseTables.USERS).update(current.copy(settings = settings)) {
            filter { eq("id", current.id) }
        }
        refresh.trigger()
    }

    suspend fun updateHeight(cm: Int) {
        val current = getProfile() ?: return
        supabase.from(SupabaseTables.USERS).update(current.copy(heightCm = cm)) {
            filter { eq("id", current.id) }
        }
        refresh.trigger()
    }
}
