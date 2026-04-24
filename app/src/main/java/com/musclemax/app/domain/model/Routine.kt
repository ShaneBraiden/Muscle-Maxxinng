package com.musclemax.app.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Routine(
    val id: String = "",
    val userId: String = "",
    val name: String = "",
    val dayOfWeek: String? = null,           // "MON"…"SUN"
    val exercises: List<RoutineExercise> = emptyList()
)

@Serializable
data class RoutineExercise(
    val exerciseId: String = "",
    val targetSets: Int = 3,
    val targetReps: Int = 8,
    val targetWeightKg: Double? = null
)

enum class DayOfWeek(val code: String, val short: String) {
    MON("MON", "M"), TUE("TUE", "T"), WED("WED", "W"),
    THU("THU", "T"), FRI("FRI", "F"), SAT("SAT", "S"), SUN("SUN", "S");

    companion object {
        fun from(code: String?): DayOfWeek? = values().firstOrNull { it.code == code }
    }
}
