package com.silkfitness.app.data.firestore

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

internal object FirestorePaths {
    const val USERS = "users"
    const val ROUTINES = "routines"
    const val WORKOUTS = "workouts"
    const val BODYWEIGHT_LOG = "bodyweight_log"
    const val PRS = "prs"
    const val MEALS = "meals"
}

internal fun FirebaseFirestore.user(uid: String): DocumentReference =
    collection(FirestorePaths.USERS).document(uid)

internal fun FirebaseFirestore.routines(uid: String): CollectionReference =
    user(uid).collection(FirestorePaths.ROUTINES)

internal fun FirebaseFirestore.workouts(uid: String): CollectionReference =
    user(uid).collection(FirestorePaths.WORKOUTS)

internal fun FirebaseFirestore.bodyweightLog(uid: String): CollectionReference =
    user(uid).collection(FirestorePaths.BODYWEIGHT_LOG)

internal fun FirebaseFirestore.prs(uid: String): CollectionReference =
    user(uid).collection(FirestorePaths.PRS)
