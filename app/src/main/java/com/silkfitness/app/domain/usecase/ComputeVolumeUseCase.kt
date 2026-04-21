package com.silkfitness.app.domain.usecase

import com.silkfitness.app.domain.model.WorkoutExercise
import javax.inject.Inject

class ComputeVolumeUseCase @Inject constructor() {
    operator fun invoke(exercises: List<WorkoutExercise>): Double =
        exercises.sumOf { ex -> ex.sets.sumOf { it.reps * it.weightKg } }
}
