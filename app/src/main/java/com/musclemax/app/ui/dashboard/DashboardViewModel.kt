package com.musclemax.app.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.musclemax.app.data.remote.BodyweightRepository
import com.musclemax.app.data.remote.PRRepository
import com.musclemax.app.data.remote.RoutineRepository
import com.musclemax.app.data.remote.UserRepository
import com.musclemax.app.data.remote.WorkoutRepository
import com.musclemax.app.data.local.ExerciseLibrary
import com.musclemax.app.domain.model.BodyweightEntry
import com.musclemax.app.domain.model.DayOfWeek
import com.musclemax.app.domain.model.Exercise
import com.musclemax.app.domain.model.PR
import com.musclemax.app.domain.model.Routine
import com.musclemax.app.domain.model.UserProfile
import com.musclemax.app.domain.model.Workout
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

data class DashboardState(
    val displayName: String = "",
    val todayRoutine: Routine? = null,
    val streakDays: Int = 0,
    val bodyweightKg: Double? = null,
    val bodyweightDeltaKg: Double? = null,
    val weekVolume: Double = 0.0,
    val weekWorkouts: Int = 0,
    val weekCalories: Int = 0,
    val recentPRs: List<PRWithExercise> = emptyList()
)

data class PRWithExercise(val pr: PR, val exercise: Exercise)

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val routineRepository: RoutineRepository,
    private val workoutRepository: WorkoutRepository,
    private val bodyweightRepository: BodyweightRepository,
    private val prRepository: PRRepository,
    private val library: ExerciseLibrary
) : ViewModel() {

    private val _state = MutableStateFlow(DashboardState())
    val state: StateFlow<DashboardState> = _state.asStateFlow()

    init {
        observeData()
    }

    private fun observeData() {
        userRepository.observeProfile().onEach { profile ->
            _state.value = _state.value.copy(displayName = profile?.displayName.orEmpty())
        }.launchIn(viewModelScope)

        viewModelScope.launch {
            combine(
                workoutRepository.observeAll(100L),
                bodyweightRepository.observeAll(),
                prRepository.observeRecent(3L)
            ) { workouts, weights, prs -> Triple(workouts, weights, prs) }
                .collect { (workouts, weights, prs) ->
                    val weekStart = startOfWeekMillis()
                    val weekWorkouts = workouts.filter { it.date.toEpochMilliseconds() >= weekStart }
                    val volume = weekWorkouts.sumOf { it.totalVolumeKg }
                    val kcal = weekWorkouts.sumOf { it.caloriesBurned }
                    val latest = weights.firstOrNull()
                    val weekAgo = weights.firstOrNull { entry ->
                        entry.date.toEpochMilliseconds() <= weekStart
                    }
                    val delta = if (latest != null && weekAgo != null)
                        latest.weightKg - weekAgo.weightKg else null

                    val streak = computeStreak(workouts)
                    val todayDayCode = currentDayCode()
                    val todayRoutine = routineRepository.findForDay(todayDayCode)
                    val prWithEx = prs.mapNotNull { pr ->
                        library.get(pr.exerciseId)?.let { PRWithExercise(pr, it) }
                    }

                    _state.value = _state.value.copy(
                        todayRoutine = todayRoutine,
                        streakDays = streak,
                        bodyweightKg = latest?.weightKg,
                        bodyweightDeltaKg = delta,
                        weekVolume = volume,
                        weekWorkouts = weekWorkouts.size,
                        weekCalories = kcal,
                        recentPRs = prWithEx
                    )
                }
        }
    }

    private fun startOfWeekMillis(): Long {
        val cal = Calendar.getInstance().apply {
            firstDayOfWeek = Calendar.MONDAY
            set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        return cal.timeInMillis
    }

    private fun computeStreak(workouts: List<Workout>): Int {
        if (workouts.isEmpty()) return 0
        val days = workouts.map { dayKey(it.date.toEpochMilliseconds()) }.toSortedSet()
        val today = dayKey(System.currentTimeMillis())
        var streak = 0
        var cursor = today
        while (cursor in days) {
            streak++
            cursor -= 1
        }
        return streak
    }

    private fun dayKey(millis: Long): Long {
        val c = Calendar.getInstance().apply {
            timeInMillis = millis
            set(Calendar.HOUR_OF_DAY, 0); set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0); set(Calendar.MILLISECOND, 0)
        }
        return c.timeInMillis / (1000 * 60 * 60 * 24)
    }

    private fun currentDayCode(): String {
        val c = Calendar.getInstance()
        return when (c.get(Calendar.DAY_OF_WEEK)) {
            Calendar.MONDAY -> DayOfWeek.MON.code
            Calendar.TUESDAY -> DayOfWeek.TUE.code
            Calendar.WEDNESDAY -> DayOfWeek.WED.code
            Calendar.THURSDAY -> DayOfWeek.THU.code
            Calendar.FRIDAY -> DayOfWeek.FRI.code
            Calendar.SATURDAY -> DayOfWeek.SAT.code
            Calendar.SUNDAY -> DayOfWeek.SUN.code
            else -> DayOfWeek.MON.code
        }
    }
}
