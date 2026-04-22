package com.silkfitness.app.ui.log

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Timestamp
import com.silkfitness.app.data.firestore.RoutineRepository
import com.silkfitness.app.data.local.ExerciseLibrary
import com.silkfitness.app.domain.model.Exercise
import com.silkfitness.app.domain.model.Workout
import com.silkfitness.app.domain.model.WorkoutExercise
import com.silkfitness.app.domain.model.WorkoutSet
import com.silkfitness.app.domain.usecase.GetLastSetForExerciseUseCase
import com.silkfitness.app.domain.usecase.LastSetReference
import com.silkfitness.app.domain.usecase.SaveWorkoutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class LogWorkoutState(
    val routineId: String? = null,
    val routineName: String = "New Workout",
    val date: Timestamp = Timestamp.now(),
    val durationMinutes: String = "60",
    val heavyDay: Boolean = false,
    val notes: String = "",
    val exercises: List<WorkoutExerciseDraft> = emptyList(),
    val isSaving: Boolean = false,
    val savedResult: SaveWorkoutUseCase.SaveResult? = null,
    val error: String? = null,
    val isLoading: Boolean = false
) {
    val canSave: Boolean
        get() = !isSaving && exercises.isNotEmpty() && exercises.any { ex ->
            ex.sets.any { it.reps.toIntOrNull() != null && it.reps.toIntOrNull()!! > 0 }
        }
}

data class WorkoutExerciseDraft(
    val exercise: Exercise,
    val sets: List<SetDraft> = listOf(SetDraft(), SetDraft(), SetDraft()),
    val lastReference: LastSetReference? = null
)

data class SetDraft(
    val reps: String = "",
    val weight: String = "",
    val rpe: String = ""
) {
    val beatsLast: Boolean get() = false
}

@HiltViewModel
class LogWorkoutViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val routineRepository: RoutineRepository,
    private val library: ExerciseLibrary,
    private val getLastSet: GetLastSetForExerciseUseCase,
    private val saveWorkout: SaveWorkoutUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(LogWorkoutState())
    val state: StateFlow<LogWorkoutState> = _state.asStateFlow()

    init {
        val id = savedStateHandle.get<String>("routineId")?.takeIf { it.isNotBlank() }
        if (id != null) loadRoutine(id)
    }

    private fun loadRoutine(id: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            val routine = routineRepository.getById(id)
            if (routine == null) {
                _state.update { it.copy(isLoading = false) }
                return@launch
            }
            val drafts = routine.exercises.mapNotNull { re ->
                val ex = library.get(re.exerciseId) ?: return@mapNotNull null
                WorkoutExerciseDraft(
                    exercise = ex,
                    sets = List(re.targetSets.coerceAtLeast(1)) {
                        SetDraft(
                            reps = re.targetReps.toString(),
                            weight = re.targetWeightKg?.let { formatKg(it) } ?: ""
                        )
                    }
                )
            }
            _state.update {
                it.copy(
                    routineId = routine.id,
                    routineName = routine.name,
                    exercises = drafts,
                    isLoading = false
                )
            }
            drafts.forEach { draft -> fetchLastSet(draft.exercise.id) }
        }
    }

    private fun fetchLastSet(exerciseId: String) {
        viewModelScope.launch {
            val ref = runCatching { getLastSet(exerciseId) }.getOrNull() ?: return@launch
            _state.update { st ->
                st.copy(exercises = st.exercises.map { d ->
                    if (d.exercise.id == exerciseId) d.copy(lastReference = ref) else d
                })
            }
        }
    }

    fun setName(v: String) = _state.update { it.copy(routineName = v) }
    fun setDuration(v: String) = _state.update { it.copy(durationMinutes = v.filter { c -> c.isDigit() }.take(3)) }
    fun toggleHeavy() = _state.update { it.copy(heavyDay = !it.heavyDay) }
    fun setNotes(v: String) = _state.update { it.copy(notes = v) }

    fun addExercises(ids: List<String>) {
        val currentIds = _state.value.exercises.map { it.exercise.id }.toSet()
        val toAdd = ids.filterNot { it in currentIds }
            .mapNotNull { library.get(it) }
            .map { WorkoutExerciseDraft(exercise = it) }
        _state.update { it.copy(exercises = it.exercises + toAdd) }
        toAdd.forEach { fetchLastSet(it.exercise.id) }
    }

    fun removeExercise(index: Int) = _state.update {
        it.copy(exercises = it.exercises.toMutableList().apply { removeAt(index) })
    }

    fun addSet(exerciseIndex: Int) = _state.update { st ->
        val list = st.exercises.toMutableList()
        val d = list[exerciseIndex]
        val template = d.sets.lastOrNull() ?: SetDraft()
        list[exerciseIndex] = d.copy(sets = d.sets + SetDraft(reps = template.reps, weight = template.weight))
        st.copy(exercises = list)
    }

    fun removeSet(exerciseIndex: Int, setIndex: Int) = _state.update { st ->
        val list = st.exercises.toMutableList()
        val d = list[exerciseIndex]
        if (d.sets.size <= 1) return@update st
        list[exerciseIndex] = d.copy(sets = d.sets.toMutableList().apply { removeAt(setIndex) })
        st.copy(exercises = list)
    }

    fun updateSet(
        exerciseIndex: Int,
        setIndex: Int,
        reps: String? = null,
        weight: String? = null,
        rpe: String? = null
    ) = _state.update { st ->
        val list = st.exercises.toMutableList()
        val d = list[exerciseIndex]
        val sets = d.sets.toMutableList()
        val existing = sets[setIndex]
        sets[setIndex] = existing.copy(
            reps = reps ?: existing.reps,
            weight = weight ?: existing.weight,
            rpe = rpe ?: existing.rpe
        )
        list[exerciseIndex] = d.copy(sets = sets)
        st.copy(exercises = list)
    }

    fun save() {
        val st = _state.value
        if (!st.canSave) return
        viewModelScope.launch {
            _state.update { it.copy(isSaving = true, error = null) }
            val draft = Workout(
                date = st.date,
                routineId = st.routineId,
                routineName = st.routineName.ifBlank { "Workout" },
                durationMinutes = st.durationMinutes.toIntOrNull() ?: 0,
                heavyMultiplier = if (st.heavyDay) 1.15 else 1.0,
                notes = st.notes,
                exercises = st.exercises.mapIndexed { idx, d ->
                    WorkoutExercise(
                        exerciseId = d.exercise.id,
                        order = idx,
                        sets = d.sets.mapNotNull { s ->
                            val reps = s.reps.toIntOrNull() ?: return@mapNotNull null
                            if (reps <= 0) return@mapNotNull null
                            WorkoutSet(
                                reps = reps,
                                weightKg = s.weight.toDoubleOrNull() ?: 0.0,
                                rpe = s.rpe.toIntOrNull()
                            )
                        }
                    )
                }.filter { it.sets.isNotEmpty() }
            )
            runCatching { saveWorkout(draft) }
                .onSuccess { result ->
                    _state.update { it.copy(isSaving = false, savedResult = result) }
                }
                .onFailure { e ->
                    _state.update { it.copy(isSaving = false, error = e.message ?: "Save failed") }
                }
        }
    }

    private fun formatKg(v: Double): String =
        if (v % 1.0 == 0.0) v.toInt().toString() else String.format("%.1f", v)
}
