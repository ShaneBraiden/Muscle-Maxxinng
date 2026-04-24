package com.musclemax.app.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Exercise(
    val id: String,
    val name: String,
    val category: Category,
    @SerialName("muscle_groups")
    val muscleGroups: List<String>,
    val equipment: Equipment,
    @SerialName("met_value")
    val metValue: Double,
    @SerialName("is_compound")
    val isCompound: Boolean = false,
    val aliases: List<String> = emptyList()
) {
    fun matches(query: String): Boolean {
        val q = query.trim().lowercase()
        if (q.isEmpty()) return true
        if (name.lowercase().contains(q)) return true
        return aliases.any { it.lowercase().contains(q) }
    }
}

@Serializable
enum class Category {
    @SerialName("push") PUSH,
    @SerialName("pull") PULL,
    @SerialName("legs") LEGS,
    @SerialName("core") CORE,
    @SerialName("cardio") CARDIO,
    @SerialName("mobility") MOBILITY;

    val display: String get() = name.lowercase().replaceFirstChar { it.titlecase() }
}

@Serializable
enum class Equipment {
    @SerialName("barbell") BARBELL,
    @SerialName("dumbbell") DUMBBELL,
    @SerialName("cable") CABLE,
    @SerialName("machine") MACHINE,
    @SerialName("bodyweight") BODYWEIGHT,
    @SerialName("kettlebell") KETTLEBELL,
    @SerialName("band") BAND,
    @SerialName("other") OTHER;

    val display: String get() = name.lowercase().replaceFirstChar { it.titlecase() }
}
