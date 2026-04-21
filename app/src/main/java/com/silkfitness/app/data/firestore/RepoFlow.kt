package com.silkfitness.app.data.firestore

import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

internal inline fun <reified T : Any> Query.snapshotFlow(): Flow<List<T>> = callbackFlow {
    val registration = addSnapshotListener { snapshot, error ->
        if (error != null) { close(error); return@addSnapshotListener }
        trySend(snapshot?.toObjects(T::class.java).orEmpty())
    }
    awaitClose { registration.remove() }
}

internal fun QuerySnapshot?.sizeOrZero(): Int = this?.size() ?: 0
