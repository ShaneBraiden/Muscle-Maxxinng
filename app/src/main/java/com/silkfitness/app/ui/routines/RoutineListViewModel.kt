package com.silkfitness.app.ui.routines

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.silkfitness.app.data.firestore.RoutineRepository
import com.silkfitness.app.domain.model.Routine
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RoutineListViewModel @Inject constructor(
    private val routineRepository: RoutineRepository
) : ViewModel() {

    val routines: StateFlow<List<Routine>> = routineRepository.observeAll()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000L), emptyList())

    fun delete(id: String) {
        viewModelScope.launch { routineRepository.delete(id) }
    }
}
