package com.musclemax.app.domain.model

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class Workout(
    val id: String = "",
    val userId: String = "",
    val date: Instant = Clock.System.now(),
    val routineId: String? = null,
    val routineName: String = "",
    val durationMinutes: Int = 0,
    val heavyMultiplier: Double = 1.0,
    val notes: String = "",
    val caloriesBurned: Int = 0,
    val totalVolumeKg: Double = 0.0,
    val exercises: List<WorkoutExercise> = emptyList()
)

@Serializable
data class WorkoutExercise(
    val exerciseId: String = "",
    val order: Int = 0,
    val sets: List<WorkoutSet> = emptyList()
)

@Serializable
data class WorkoutSet(
    val reps: Int = 0,
    val weightKg: Double = 0.0,
    val rpe: Int? = null,
    val notes: String? = null
) {
    val volumeKg: Double get() = reps * weightKg
    val estimatedOneRepMaxKg: Double get() =
        if (reps == 0 || weightKg == 0.0) 0.0
        else weightKg * (1.0 + reps / 30.0)
}
