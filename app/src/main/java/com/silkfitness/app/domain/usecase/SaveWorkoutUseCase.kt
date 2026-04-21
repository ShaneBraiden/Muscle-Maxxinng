package com.silkfitness.app.domain.usecase

import com.silkfitness.app.data.firestore.BodyweightRepository
import com.silkfitness.app.data.firestore.UserRepository
import com.silkfitness.app.data.firestore.WorkoutRepository
import com.silkfitness.app.domain.model.Workout
import javax.inject.Inject

class SaveWorkoutUseCase @Inject constructor(
    private val workoutRepository: WorkoutRepository,
    private val bodyweightRepository: BodyweightRepository,
    private val userRepository: UserRepository,
    private val calculateCalories: CalculateCaloriesUseCase,
    private val computeVolume: ComputeVolumeUseCase,
    private val detectPRs: DetectPRsUseCase
) {
    suspend operator fun invoke(draft: Workout): SaveResult {
        val latestBw = bodyweightRepository.latest()?.weightKg
            ?: userRepository.getProfile()?.let { it.heightCm * 0.0 /* placeholder */ }
            ?: 75.0

        val kcal = calculateCalories(draft.exercises, latestBw, draft.durationMinutes, draft.heavyMultiplier)
        val volume = computeVolume(draft.exercises)

        val finalised = draft.copy(
            caloriesBurned = kcal,
            totalVolumeKg = volume
        )
        val id = workoutRepository.upsert(finalised)
        val saved = finalised.copy(id = id)
        val prs = detectPRs(saved)
        return SaveResult(savedId = id, volumeKg = volume, calories = kcal, prs = prs)
    }

    data class SaveResult(
        val savedId: String,
        val volumeKg: Double,
        val calories: Int,
        val prs: List<DetectedPR>
    )
}
