package com.musclemax.app.domain.usecase

import com.musclemax.app.data.local.ExerciseLibrary
import com.musclemax.app.domain.model.WorkoutExercise
import javax.inject.Inject
import kotlin.math.roundToInt

/**
 * kcal = MET_avg × bodyweight_kg × (duration_min / 60) × heavy_multiplier
 *
 * MET_avg is the set-count-weighted mean of per-exercise MET values.
 */
class CalculateCaloriesUseCase @Inject constructor(
    private val library: ExerciseLibrary
) {
    operator fun invoke(
        exercises: List<WorkoutExercise>,
        bodyweightKg: Double,
        durationMinutes: Int,
        heavyMultiplier: Double
    ): Int {
        if (bodyweightKg <= 0.0 || durationMinutes <= 0) return 0
        val totalSets = exercises.sumOf { it.sets.size }.coerceAtLeast(1)
        val weightedMet = exercises.sumOf { ex ->
            val met = library.get(ex.exerciseId)?.metValue ?: 4.0
            met * ex.sets.size
        } / totalSets
        val kcal = weightedMet * bodyweightKg * (durationMinutes / 60.0) * heavyMultiplier
        return kcal.roundToInt()
    }
}
