package com.silkfitness.app.domain.model

import com.google.firebase.firestore.DocumentId

data class UserProfile(
    @DocumentId val uid: String = "",
    val displayName: String = "",
    val email: String = "",
    val photoUrl: String? = null,
    val heightCm: Int = 175,
    val settings: UserSettings = UserSettings()
)

data class UserSettings(
    val units: String = "metric",           // "metric" | "imperial"
    val weeklyVolumeGoalKg: Int = 10_000,
    val kcalTarget: Int = 2800,
    val proteinTargetG: Int = 160
)
