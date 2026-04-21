package com.silkfitness.app.ui.log

import androidx.lifecycle.ViewModel
import com.silkfitness.app.data.local.ExerciseLibrary
import com.silkfitness.app.domain.model.Category
import com.silkfitness.app.domain.model.Equipment
import com.silkfitness.app.domain.model.Exercise
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

data class ExercisePickerState(
    val query: String = "",
    val category: Category? = null,
    val equipment: Equipment? = null,
    val results: List<Exercise> = emptyList()
)

@HiltViewModel
class ExercisePickerViewModel @Inject constructor(
    private val library: ExerciseLibrary
) : ViewModel() {
    private val _state = MutableStateFlow(ExercisePickerState(results = library.getAll()))
    val state: StateFlow<ExercisePickerState> = _state.asStateFlow()

    fun setQuery(q: String) {
        _state.update { it.copy(query = q, results = library.search(q, it.category, it.equipment)) }
    }
    fun setCategory(c: Category?) {
        _state.update { it.copy(category = c, results = library.search(it.query, c, it.equipment)) }
    }
    fun setEquipment(e: Equipment?) {
        _state.update { it.copy(equipment = e, results = library.search(it.query, it.category, e)) }
    }
}
