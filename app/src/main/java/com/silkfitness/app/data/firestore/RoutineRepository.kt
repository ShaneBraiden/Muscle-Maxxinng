package com.silkfitness.app.data.firestore

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.silkfitness.app.data.auth.AuthRepository
import com.silkfitness.app.domain.model.Routine
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RoutineRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: AuthRepository
) {
    private val uid: String? get() = auth.currentUser?.uid

    fun observeAll(): Flow<List<Routine>> {
        val user = uid ?: return emptyFlow()
        return firestore.routines(user)
            .orderBy("name", Query.Direction.ASCENDING)
            .snapshotFlow()
    }

    suspend fun getById(id: String): Routine? {
        val user = uid ?: return null
        return firestore.routines(user).document(id).get().await().toObject(Routine::class.java)
    }

    suspend fun upsert(routine: Routine): String {
        val user = uid ?: error("Not signed in")
        val docRef = if (routine.id.isBlank())
            firestore.routines(user).document()
        else
            firestore.routines(user).document(routine.id)
        docRef.set(routine.copy(id = docRef.id)).await()
        return docRef.id
    }

    suspend fun delete(id: String) {
        val user = uid ?: return
        firestore.routines(user).document(id).delete().await()
    }

    suspend fun findForDay(dayCode: String): Routine? {
        val user = uid ?: return null
        return firestore.routines(user)
            .whereEqualTo("dayOfWeek", dayCode)
            .limit(1)
            .get().await()
            .toObjects(Routine::class.java).firstOrNull()
    }
}
