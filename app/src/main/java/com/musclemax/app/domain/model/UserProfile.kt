package com.musclemax.app.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class UserProfile(
    val id: String = "",
    val displayName: String = "",
    val email: String = "",
    val photoUrl: String? = null,
    val heightCm: Int = 175,
    val settings: UserSettings = UserSettings()
)

@Serializable
data class UserSettings(
    val units: String = "metric",           // "metric" | "imperial"
    val weeklyVolumeGoalKg: Int = 10_000,
    val kcalTarget: Int = 2800,
    val proteinTargetG: Int = 160
)
