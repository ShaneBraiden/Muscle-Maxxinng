package com.musclemax.app.data.remote

import com.musclemax.app.data.auth.AuthRepository
import com.musclemax.app.domain.model.BodyweightEntry
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Order
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BodyweightRepository @Inject constructor(
    private val supabase: SupabaseClient,
    private val auth: AuthRepository
) {
    private val refresh = RefreshBus()

    private val uid: String? get() = auth.currentUser?.id

    fun observeAll(): Flow<List<BodyweightEntry>> {
        val user = uid ?: return emptyFlow()
        return refresh.observe { fetchAll(user) }
    }

    private suspend fun fetchAll(user: String): List<BodyweightEntry> =
        supabase.from(SupabaseTables.BODYWEIGHT_LOG)
            .select {
                filter { eq("user_id", user) }
                order("date", Order.DESCENDING)
            }
            .decodeList()

    suspend fun latest(): BodyweightEntry? {
        val user = uid ?: return null
        return supabase.from(SupabaseTables.BODYWEIGHT_LOG)
            .select {
                filter { eq("user_id", user) }
                order("date", Order.DESCENDING)
                limit(1)
            }
            .decodeSingleOrNull()
    }

    suspend fun logToday(weightKg: Double) {
        val user = uid ?: error("Not signed in")
        val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date.toString()
        supabase.from(SupabaseTables.BODYWEIGHT_LOG).upsert(
            BodyweightEntry(
                id = today,
                userId = user,
                date = Clock.System.now(),
                weightKg = weightKg
            )
        ) {
            onConflict = "user_id,id"
        }
        refresh.trigger()
    }

    suspend fun delete(id: String) {
        val user = uid ?: return
        supabase.from(SupabaseTables.BODYWEIGHT_LOG).delete {
            filter {
                eq("user_id", user)
                eq("id", id)
            }
        }
        refresh.trigger()
    }
}
