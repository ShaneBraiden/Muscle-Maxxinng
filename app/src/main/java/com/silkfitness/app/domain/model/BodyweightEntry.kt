package com.silkfitness.app.domain.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId

data class BodyweightEntry(
    @DocumentId val id: String = "",     // dateId YYYY-MM-DD
    val date: Timestamp = Timestamp.now(),
    val weightKg: Double = 0.0
)
