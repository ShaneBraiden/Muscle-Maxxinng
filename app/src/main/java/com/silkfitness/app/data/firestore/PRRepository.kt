package com.silkfitness.app.data.firestore

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.silkfitness.app.data.auth.AuthRepository
import com.silkfitness.app.domain.model.PR
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PRRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: AuthRepository
) {
    private val uid: String? get() = auth.currentUser?.uid

    fun observeAll(): Flow<List<PR>> {
        val user = uid ?: return emptyFlow()
        return firestore.prs(user).snapshotFlow()
    }

    fun observeRecent(limit: Long = 3L): Flow<List<PR>> {
        val user = uid ?: return emptyFlow()
        return firestore.prs(user)
            .orderBy("maxWeightDate", Query.Direction.DESCENDING)
            .limit(limit)
            .snapshotFlow()
    }

    suspend fun get(exerciseId: String): PR? {
        val user = uid ?: return null
        return firestore.prs(user).document(exerciseId).get().await().toObject(PR::class.java)
    }

    suspend fun upsert(pr: PR) {
        val user = uid ?: error("Not signed in")
        firestore.prs(user).document(pr.exerciseId).set(pr).await()
    }
}
