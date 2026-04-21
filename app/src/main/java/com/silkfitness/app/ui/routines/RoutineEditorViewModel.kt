package com.silkfitness.app.ui.routines

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.silkfitness.app.data.firestore.RoutineRepository
import com.silkfitness.app.data.local.ExerciseLibrary
import com.silkfitness.app.domain.model.DayOfWeek
import com.silkfitness.app.domain.model.Exercise
import com.silkfitness.app.domain.model.Routine
import com.silkfitness.app.domain.model.RoutineExercise
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class RoutineEditorState(
    val existingId: String? = null,
    val name: String = "",
    val day: DayOfWeek? = null,
    val entries: List<RoutineEntryUi> = emptyList(),
    val isSaving: Boolean = false,
    val savedSuccessfully: Boolean = false,
    val error: String? = null
) {
    val canSave: Boolean get() = name.isNotBlank() && entries.isNotEmpty() && !isSaving
}

data class RoutineEntryUi(
    val exercise: Exercise,
    val targetSets: String = "3",
    val targetReps: String = "8",
    val targetWeight: String = ""
)

@HiltViewModel
class RoutineEditorViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val routineRepository: RoutineRepository,
    private val library: ExerciseLibrary
) : ViewModel() {

    private val _state = MutableStateFlow(RoutineEditorState())
    val state: StateFlow<RoutineEditorState> = _state.asStateFlow()

    init {
        val id = savedStateHandle.get<String>("routineId")?.takeIf { it.isNotBlank() }
        if (id != null) loadExisting(id)
    }

    private fun loadExisting(id: String) {
        viewModelScope.launch {
            val routine = routineRepository.getById(id) ?: return@launch
            _state.update {
                it.copy(
                    existingId = routine.id,
                    name = routine.name,
                    day = DayOfWeek.from(routine.dayOfWeek),
                    entries = routine.exercises.mapNotNull { re ->
                        val ex = library.get(re.exerciseId) ?: return@mapNotNull null
                        RoutineEntryUi(
                            exercise = ex,
                            targetSets = re.targetSets.toString(),
                            targetReps = re.targetReps.toString(),
                            targetWeight = re.targetWeightKg?.let { w -> formatKg(w) } ?: ""
                        )
                    }
                )
            }
        }
    }

    fun setName(v: String) = _state.update { it.copy(name = v) }
    fun setDay(d: DayOfWeek?) = _state.update { it.copy(day = d) }

    fun addExercises(ids: List<String>) {
        val current = _state.value.entries
        val existing = current.map { it.exercise.id }.toSet()
        val newEntries = ids.filterNot { it in existing }
            .mapNotNull { library.get(it) }
            .map { RoutineEntryUi(exercise = it) }
        _state.update { it.copy(entries = current + newEntries) }
    }

    fun remove(index: Int) {
        _state.update { it.copy(entries = it.entries.toMutableList().apply { removeAt(index) }) }
    }

    fun update(index: Int, sets: String? = null, reps: String? = null, weight: String? = null) {
        _state.update { st ->
            val list = st.entries.toMutableList()
            val e = list[index]
            list[index] = e.copy(
                targetSets = sets ?: e.targetSets,
                targetReps = reps ?: e.targetReps,
                targetWeight = weight ?: e.targetWeight
            )
            st.copy(entries = list)
        }
    }

    fun save() {
        val st = _state.value
        if (!st.canSave) return
        viewModelScope.launch {
            _state.update { it.copy(isSaving = true, error = null) }
            runCatching {
                routineRepository.upsert(
                    Routine(
                        id = st.existingId.orEmpty(),
                        name = st.name.trim(),
                        dayOfWeek = st.day?.code,
                        exercises = st.entries.map { e ->
                            RoutineExercise(
                                exerciseId = e.exercise.id,
                                targetSets = e.targetSets.toIntOrNull() ?: 3,
                                targetReps = e.targetReps.toIntOrNull() ?: 8,
                                targetWeightKg = e.targetWeight.toDoubleOrNull()
                            )
                        }
                    )
                )
            }.onSuccess {
                _state.update { it.copy(isSaving = false, savedSuccessfully = true) }
            }.onFailure { e ->
                _state.update { it.copy(isSaving = false, error = e.message ?: "Save failed") }
            }
        }
    }

    fun deleteExisting() {
        val id = _state.value.existingId ?: return
        viewModelScope.launch {
            routineRepository.delete(id)
            _state.update { it.copy(savedSuccessfully = true) }
        }
    }

    private fun formatKg(v: Double): String =
        if (v % 1.0 == 0.0) v.toInt().toString() else String.format("%.1f", v)
}
