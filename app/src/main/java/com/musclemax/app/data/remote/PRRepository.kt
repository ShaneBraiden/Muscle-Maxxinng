package com.musclemax.app.data.remote

import com.musclemax.app.data.auth.AuthRepository
import com.musclemax.app.domain.model.PR
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Order
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PRRepository @Inject constructor(
    private val supabase: SupabaseClient,
    private val auth: AuthRepository
) {
    private val refresh = RefreshBus()

    private val uid: String? get() = auth.currentUser?.id

    fun observeAll(): Flow<List<PR>> {
        val user = uid ?: return emptyFlow()
        return refresh.observe { fetchAll(user) }
    }

    private suspend fun fetchAll(user: String): List<PR> =
        supabase.from(SupabaseTables.PRS)
            .select { filter { eq("user_id", user) } }
            .decodeList()

    fun observeRecent(limit: Long = 3L): Flow<List<PR>> {
        val user = uid ?: return emptyFlow()
        return refresh.observe { fetchRecent(user, limit) }
    }

    private suspend fun fetchRecent(user: String, limit: Long): List<PR> =
        supabase.from(SupabaseTables.PRS)
            .select {
                filter { eq("user_id", user) }
                order("max_weight_date", Order.DESCENDING, nullsFirst = false)
                limit(limit)
            }
            .decodeList()

    suspend fun get(exerciseId: String): PR? {
        val user = uid ?: return null
        return supabase.from(SupabaseTables.PRS)
            .select {
                filter {
                    eq("user_id", user)
                    eq("exercise_id", exerciseId)
                }
                limit(1)
            }
            .decodeSingleOrNull()
    }

    suspend fun upsert(pr: PR) {
        val user = uid ?: error("Not signed in")
        supabase.from(SupabaseTables.PRS).upsert(pr.copy(userId = user)) {
            onConflict = "user_id,exercise_id"
        }
        refresh.trigger()
    }
}
