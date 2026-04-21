package com.silkfitness.app.domain.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId

data class PR(
    @DocumentId val exerciseId: String = "",
    val maxWeightKg: Double = 0.0,
    val maxWeightDate: Timestamp? = null,
    val maxWeightWorkoutId: String? = null,
    val estimatedOneRepMaxKg: Double = 0.0,
    val estimatedOneRepMaxDate: Timestamp? = null,
    val estimatedOneRepMaxWorkoutId: String? = null,
    val maxVolumeSessionKg: Double = 0.0,
    val maxVolumeDate: Timestamp? = null,
    val maxVolumeWorkoutId: String? = null
)
