package com.musclemax.app.data.remote

import com.musclemax.app.data.auth.AuthRepository
import com.musclemax.app.domain.model.Routine
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Order
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RoutineRepository @Inject constructor(
    private val supabase: SupabaseClient,
    private val auth: AuthRepository
) {
    private val refresh = RefreshBus()

    private val uid: String? get() = auth.currentUser?.id

    fun observeAll(): Flow<List<Routine>> {
        val user = uid ?: return emptyFlow()
        return refresh.observe { fetchAll(user) }
    }

    private suspend fun fetchAll(user: String): List<Routine> =
        supabase.from(SupabaseTables.ROUTINES)
            .select {
                filter { eq("user_id", user) }
                order("name", Order.ASCENDING)
            }
            .decodeList()

    suspend fun getById(id: String): Routine? {
        val user = uid ?: return null
        return supabase.from(SupabaseTables.ROUTINES)
            .select {
                filter {
                    eq("user_id", user)
                    eq("id", id)
                }
                limit(1)
            }
            .decodeSingleOrNull()
    }

    suspend fun upsert(routine: Routine): String {
        val user = uid ?: error("Not signed in")
        val id = routine.id.ifBlank { UUID.randomUUID().toString() }
        val row = routine.copy(id = id, userId = user)
        supabase.from(SupabaseTables.ROUTINES).upsert(row) {
            onConflict = "user_id,id"
        }
        refresh.trigger()
        return id
    }

    suspend fun delete(id: String) {
        val user = uid ?: return
        supabase.from(SupabaseTables.ROUTINES).delete {
            filter {
                eq("user_id", user)
                eq("id", id)
            }
        }
        refresh.trigger()
    }

    suspend fun findForDay(dayCode: String): Routine? {
        val user = uid ?: return null
        return supabase.from(SupabaseTables.ROUTINES)
            .select {
                filter {
                    eq("user_id", user)
                    eq("day_of_week", dayCode)
                }
                limit(1)
            }
            .decodeSingleOrNull()
    }
}
