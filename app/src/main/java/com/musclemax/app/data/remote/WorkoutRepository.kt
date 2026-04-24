package com.musclemax.app.data.remote

import com.musclemax.app.data.auth.AuthRepository
import com.musclemax.app.domain.model.Workout
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Order
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.datetime.Instant
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WorkoutRepository @Inject constructor(
    private val supabase: SupabaseClient,
    private val auth: AuthRepository
) {
    private val refresh = RefreshBus()

    private val uid: String? get() = auth.currentUser?.id

    fun observeAll(limit: Long = 200L): Flow<List<Workout>> {
        val user = uid ?: return emptyFlow()
        return refresh.observe { fetchAll(user, limit) }
    }

    private suspend fun fetchAll(user: String, limit: Long): List<Workout> =
        supabase.from(SupabaseTables.WORKOUTS)
            .select {
                filter { eq("user_id", user) }
                order("date", Order.DESCENDING)
                limit(limit)
            }
            .decodeList()

    fun observeBetween(from: Instant, to: Instant): Flow<List<Workout>> {
        val user = uid ?: return emptyFlow()
        return refresh.observe { fetchBetween(user, from, to) }
    }

    private suspend fun fetchBetween(user: String, from: Instant, to: Instant): List<Workout> =
        supabase.from(SupabaseTables.WORKOUTS)
            .select {
                filter {
                    eq("user_id", user)
                    gte("date", from.toString())
                    lte("date", to.toString())
                }
                order("date", Order.DESCENDING)
            }
            .decodeList()

    suspend fun getById(id: String): Workout? {
        val user = uid ?: return null
        return supabase.from(SupabaseTables.WORKOUTS)
            .select {
                filter {
                    eq("user_id", user)
                    eq("id", id)
                }
                limit(1)
            }
            .decodeSingleOrNull()
    }

    suspend fun upsert(workout: Workout): String {
        val user = uid ?: error("Not signed in")
        val id = workout.id.ifBlank { UUID.randomUUID().toString() }
        val row = workout.copy(id = id, userId = user)
        supabase.from(SupabaseTables.WORKOUTS).upsert(row) {
            onConflict = "user_id,id"
        }
        refresh.trigger()
        return id
    }

    suspend fun delete(id: String) {
        val user = uid ?: return
        supabase.from(SupabaseTables.WORKOUTS).delete {
            filter {
                eq("user_id", user)
                eq("id", id)
            }
        }
        refresh.trigger()
    }

    suspend fun findMostRecentSetFor(exerciseId: String): Workout? {
        val user = uid ?: return null
        return supabase.from(SupabaseTables.WORKOUTS)
            .select {
                filter { eq("user_id", user) }
                order("date", Order.DESCENDING)
                limit(50)
            }
            .decodeList<Workout>()
            .firstOrNull { w ->
                w.exercises.any { it.exerciseId == exerciseId && it.sets.isNotEmpty() }
            }
    }
}
