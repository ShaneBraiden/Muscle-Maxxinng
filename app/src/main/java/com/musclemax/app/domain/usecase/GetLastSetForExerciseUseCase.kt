package com.musclemax.app.domain.usecase

import com.musclemax.app.data.remote.WorkoutRepository
import com.musclemax.app.domain.model.WorkoutSet
import kotlinx.datetime.Instant
import javax.inject.Inject

data class LastSetReference(
    val set: WorkoutSet,
    val workoutDate: Instant
)

class GetLastSetForExerciseUseCase @Inject constructor(
    private val workoutRepository: WorkoutRepository
) {
    suspend operator fun invoke(exerciseId: String): LastSetReference? {
        val workout = workoutRepository.findMostRecentSetFor(exerciseId) ?: return null
        val ex = workout.exercises.firstOrNull { it.exerciseId == exerciseId } ?: return null
        val best = ex.sets.maxByOrNull { it.weightKg * 100 + it.reps } ?: return null
        return LastSetReference(best, workout.date)
    }
}
