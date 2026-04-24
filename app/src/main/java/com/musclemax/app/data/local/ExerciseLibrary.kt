package com.musclemax.app.data.local

import android.content.Context
import com.musclemax.app.R
import com.musclemax.app.domain.model.Category
import com.musclemax.app.domain.model.Equipment
import com.musclemax.app.domain.model.Exercise
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ExerciseLibrary @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
    }

    val all: List<Exercise> by lazy {
        val raw = context.resources.openRawResource(R.raw.exercises)
            .bufferedReader().use { it.readText() }
        json.decodeFromString<List<Exercise>>(raw)
    }

    private val byId: Map<String, Exercise> by lazy { all.associateBy { it.id } }

    fun get(id: String): Exercise? = byId[id]

    fun search(
        query: String = "",
        category: Category? = null,
        equipment: Equipment? = null
    ): List<Exercise> {
        return all.asSequence()
            .filter { category == null || it.category == category }
            .filter { equipment == null || it.equipment == equipment }
            .filter { it.matches(query) }
            .sortedBy { it.name }
            .toList()
    }
}
