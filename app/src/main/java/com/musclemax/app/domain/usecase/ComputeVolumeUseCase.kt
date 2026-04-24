package com.musclemax.app.domain.usecase

import com.musclemax.app.domain.model.WorkoutExercise
import javax.inject.Inject

class ComputeVolumeUseCase @Inject constructor() {
    operator fun invoke(exercises: List<WorkoutExercise>): Double =
        exercises.sumOf { ex -> ex.sets.sumOf { it.reps * it.weightKg } }
}
