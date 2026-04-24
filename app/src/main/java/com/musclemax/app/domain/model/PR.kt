package com.musclemax.app.domain.model

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class PR(
    val userId: String = "",
    val exerciseId: String = "",
    val maxWeightKg: Double = 0.0,
    val maxWeightDate: Instant? = null,
    val maxWeightWorkoutId: String? = null,
    val estimatedOneRepMaxKg: Double = 0.0,
    val estimatedOneRepMaxDate: Instant? = null,
    val estimatedOneRepMaxWorkoutId: String? = null,
    val maxVolumeSessionKg: Double = 0.0,
    val maxVolumeDate: Instant? = null,
    val maxVolumeWorkoutId: String? = null
)
