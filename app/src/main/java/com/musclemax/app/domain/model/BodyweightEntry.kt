package com.musclemax.app.domain.model

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class BodyweightEntry(
    val id: String = "",           // YYYY-MM-DD, unique per user
    val userId: String = "",
    val date: Instant = Clock.System.now(),
    val weightKg: Double = 0.0
)
