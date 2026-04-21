package com.silkfitness.app.ui.navigation

object Destinations {
    const val AUTH = "auth"
    const val DASHBOARD = "dashboard"
    const val ROUTINES = "routines"
    const val HISTORY = "history"
    const val PROGRESSION = "progression"
    const val PROFILE = "profile"

    // With args
    const val LOG_WORKOUT = "log_workout?routineId={routineId}"
    const val ROUTINE_EDITOR = "routine_editor?routineId={routineId}"
    const val WORKOUT_DETAIL = "workout_detail/{workoutId}"
    const val BODYWEIGHT = "bodyweight"

    fun logWorkout(routineId: String? = null) =
        if (routineId.isNullOrBlank()) "log_workout?routineId="
        else "log_workout?routineId=$routineId"

    fun routineEditor(routineId: String? = null) =
        if (routineId.isNullOrBlank()) "routine_editor?routineId="
        else "routine_editor?routineId=$routineId"

    fun workoutDetail(workoutId: String) = "workout_detail/$workoutId"
}
