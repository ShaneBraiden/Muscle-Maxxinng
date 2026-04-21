package com.silkfitness.app.domain.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId

data class Workout(
    @DocumentId val id: String = "",
    val date: Timestamp = Timestamp.now(),
    val routineId: String? = null,
    val routineName: String = "",
    val durationMinutes: Int = 0,
    val heavyMultiplier: Double = 1.0,
    val notes: String = "",
    val caloriesBurned: Int = 0,
    val totalVolumeKg: Double = 0.0,
    val exercises: List<WorkoutExercise> = emptyList()
)

data class WorkoutExercise(
    val exerciseId: String = "",
    val order: Int = 0,
    val sets: List<WorkoutSet> = emptyList()
)

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
