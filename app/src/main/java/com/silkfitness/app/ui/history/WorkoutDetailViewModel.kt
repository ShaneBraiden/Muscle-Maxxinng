package com.silkfitness.app.ui.history

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.silkfitness.app.data.firestore.WorkoutRepository
import com.silkfitness.app.data.local.ExerciseLibrary
import com.silkfitness.app.domain.model.Exercise
import com.silkfitness.app.domain.model.Workout
import com.silkfitness.app.domain.model.WorkoutSet
import com.silkfitness.app.domain.usecase.GetLastSetForExerciseUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class WorkoutDetailState(
    val workout: Workout? = null,
    val exercises: List<DetailExercise> = emptyList(),
    val isLoading: Boolean = true,
    val isDeleted: Boolean = false
)

data class DetailExercise(
    val exercise: Exercise,
    val sets: List<WorkoutSet>,
    val previousSets: List<WorkoutSet>,
    val trend: ExerciseTrend
)

enum class ExerciseTrend { IMPROVED, MAINTAINED, REGRESSED, NEW }

@HiltViewModel
class WorkoutDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val workoutRepository: WorkoutRepository,
    private val library: ExerciseLibrary,
    private val getLastSet: GetLastSetForExerciseUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(WorkoutDetailState())
    val state: StateFlow<WorkoutDetailState> = _state.asStateFlow()

    init {
        val id = savedStateHandle.get<String>("workoutId").orEmpty()
        if (id.isNotBlank()) load(id)
    }

    private fun load(id: String) {
        viewModelScope.launch {
            val w = workoutRepository.getById(id)
            if (w == null) {
                _state.update { it.copy(isLoading = false) }
                return@launch
            }
            val items = w.exercises.mapNotNull { we ->
                val ex = library.get(we.exerciseId) ?: return@mapNotNull null
                val lastRef = runCatching { getLastSet(we.exerciseId) }.getOrNull()
                val previousSets = lastRef?.let { listOf(it.set) } ?: emptyList()
                val trend = classifyTrend(we.sets, lastRef?.set)
                DetailExercise(ex, we.sets, previousSets, trend)
            }
            _state.update { it.copy(workout = w, exercises = items, isLoading = false) }
        }
    }

    fun delete() {
        val id = _state.value.workout?.id ?: return
        viewModelScope.launch {
            workoutRepository.delete(id)
            _state.update { it.copy(isDeleted = true) }
        }
    }

    private fun classifyTrend(current: List<WorkoutSet>, previous: WorkoutSet?): ExerciseTrend {
        if (previous == null) return ExerciseTrend.NEW
        val best = current.maxByOrNull { it.weightKg } ?: return ExerciseTrend.NEW
        return when {
            best.weightKg > previous.weightKg -> ExerciseTrend.IMPROVED
            best.weightKg < previous.weightKg -> ExerciseTrend.REGRESSED
            else -> ExerciseTrend.MAINTAINED
        }
    }
}
