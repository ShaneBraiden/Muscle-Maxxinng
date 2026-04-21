package com.silkfitness.app.data.firestore

import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.silkfitness.app.data.auth.AuthRepository
import com.silkfitness.app.domain.model.BodyweightEntry
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.tasks.await
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BodyweightRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: AuthRepository
) {
    private val uid: String? get() = auth.currentUser?.uid

    fun observeAll(): Flow<List<BodyweightEntry>> {
        val user = uid ?: return emptyFlow()
        return firestore.bodyweightLog(user)
            .orderBy("date", Query.Direction.DESCENDING)
            .snapshotFlow()
    }

    suspend fun latest(): BodyweightEntry? {
        val user = uid ?: return null
        return firestore.bodyweightLog(user)
            .orderBy("date", Query.Direction.DESCENDING)
            .limit(1)
            .get().await()
            .toObjects(BodyweightEntry::class.java)
            .firstOrNull()
    }

    suspend fun logToday(weightKg: Double) {
        val user = uid ?: error("Not signed in")
        val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date.toString()
        firestore.bodyweightLog(user).document(today)
            .set(BodyweightEntry(id = today, date = Timestamp.now(), weightKg = weightKg))
            .await()
    }

    suspend fun delete(id: String) {
        val user = uid ?: return
        firestore.bodyweightLog(user).document(id).delete().await()
    }
}
