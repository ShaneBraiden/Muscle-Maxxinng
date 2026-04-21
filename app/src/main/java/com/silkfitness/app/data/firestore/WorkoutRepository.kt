package com.silkfitness.app.data.firestore

import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.silkfitness.app.data.auth.AuthRepository
import com.silkfitness.app.domain.model.Workout
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WorkoutRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: AuthRepository
) {
    private val uid: String? get() = auth.currentUser?.uid

    fun observeAll(limit: Long = 200L): Flow<List<Workout>> {
        val user = uid ?: return emptyFlow()
        return firestore.workouts(user)
            .orderBy("date", Query.Direction.DESCENDING)
            .limit(limit)
            .snapshotFlow()
    }

    fun observeBetween(from: Timestamp, to: Timestamp): Flow<List<Workout>> {
        val user = uid ?: return emptyFlow()
        return firestore.workouts(user)
            .whereGreaterThanOrEqualTo("date", from)
            .whereLessThanOrEqualTo("date", to)
            .orderBy("date", Query.Direction.DESCENDING)
            .snapshotFlow()
    }

    suspend fun getById(id: String): Workout? {
        val user = uid ?: return null
        return firestore.workouts(user).document(id).get().await().toObject(Workout::class.java)
    }

    suspend fun upsert(workout: Workout): String {
        val user = uid ?: error("Not signed in")
        val docRef = if (workout.id.isBlank())
            firestore.workouts(user).document()
        else
            firestore.workouts(user).document(workout.id)
        docRef.set(workout.copy(id = docRef.id)).await()
        return docRef.id
    }

    suspend fun delete(id: String) {
        val user = uid ?: return
        firestore.workouts(user).document(id).delete().await()
    }

    suspend fun findMostRecentSetFor(exerciseId: String): Workout? {
        val user = uid ?: return null
        return firestore.workouts(user)
            .orderBy("date", Query.Direction.DESCENDING)
            .limit(50)
            .get().await()
            .toObjects(Workout::class.java)
            .firstOrNull { w -> w.exercises.any { it.exerciseId == exerciseId && it.sets.isNotEmpty() } }
    }
}
