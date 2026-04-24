package com.musclemax.app.domain.usecase

import com.musclemax.app.data.remote.PRRepository
import com.musclemax.app.domain.model.PR
import com.musclemax.app.domain.model.Workout
import javax.inject.Inject

data class DetectedPR(
    val exerciseId: String,
    val isNewMaxWeight: Boolean,
    val isNewOneRepMax: Boolean,
    val isNewVolume: Boolean
) {
    val any: Boolean get() = isNewMaxWeight || isNewOneRepMax || isNewVolume
}

class DetectPRsUseCase @Inject constructor(
    private val prRepository: PRRepository
) {
    suspend operator fun invoke(workout: Workout): List<DetectedPR> {
        val results = mutableListOf<DetectedPR>()
        for (ex in workout.exercises) {
            if (ex.sets.isEmpty()) continue
            val bestWeight = ex.sets.maxOf { it.weightKg }
            val bestOneRm = ex.sets.maxOf { it.estimatedOneRepMaxKg }
            val sessionVolume = ex.sets.sumOf { it.reps * it.weightKg }

            val existing = prRepository.get(ex.exerciseId) ?: PR(exerciseId = ex.exerciseId)
            val newWeight = bestWeight > existing.maxWeightKg
            val newOneRm = bestOneRm > existing.estimatedOneRepMaxKg
            val newVolume = sessionVolume > existing.maxVolumeSessionKg

            if (newWeight || newOneRm || newVolume) {
                val updated = existing.copy(
                    exerciseId = ex.exerciseId,
                    maxWeightKg = if (newWeight) bestWeight else existing.maxWeightKg,
                    maxWeightDate = if (newWeight) workout.date else existing.maxWeightDate,
                    maxWeightWorkoutId = if (newWeight) workout.id else existing.maxWeightWorkoutId,
                    estimatedOneRepMaxKg = if (newOneRm) bestOneRm else existing.estimatedOneRepMaxKg,
                    estimatedOneRepMaxDate = if (newOneRm) workout.date else existing.estimatedOneRepMaxDate,
                    estimatedOneRepMaxWorkoutId = if (newOneRm) workout.id else existing.estimatedOneRepMaxWorkoutId,
                    maxVolumeSessionKg = if (newVolume) sessionVolume else existing.maxVolumeSessionKg,
                    maxVolumeDate = if (newVolume) workout.date else existing.maxVolumeDate,
                    maxVolumeWorkoutId = if (newVolume) workout.id else existing.maxVolumeWorkoutId
                )
                prRepository.upsert(updated)
                results.add(DetectedPR(ex.exerciseId, newWeight, newOneRm, newVolume))
            }
        }
        return results
    }
}
