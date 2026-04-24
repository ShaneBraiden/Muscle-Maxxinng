package com.musclemax.app.data.auth

data class AppUser(
    val id: String,
    val displayName: String,
    val email: String,
    val photoUrl: String?
)
