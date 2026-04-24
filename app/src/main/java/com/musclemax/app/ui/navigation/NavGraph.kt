package com.musclemax.app.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.musclemax.app.ui.auth.AuthScreen
import com.musclemax.app.ui.auth.AuthViewModel
import com.musclemax.app.ui.bodyweight.BodyweightScreen
import com.musclemax.app.ui.components.BottomNavBar
import com.musclemax.app.ui.dashboard.DashboardScreen
import com.musclemax.app.ui.history.HistoryScreen
import com.musclemax.app.ui.history.WorkoutDetailScreen
import com.musclemax.app.ui.log.LogWorkoutScreen
import com.musclemax.app.ui.profile.ProfileScreen
import com.musclemax.app.ui.progression.ProgressionScreen
import com.musclemax.app.ui.routines.RoutineEditorScreen
import com.musclemax.app.ui.routines.RoutineListScreen

@Composable
fun NavGraph(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val authViewModel: AuthViewModel = hiltViewModel()
    val authState by authViewModel.authState.collectAsStateWithLifecycle()

    val startDestination = if (authState.isSignedIn) Destinations.DASHBOARD else Destinations.AUTH

    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route
    val showBottomNav = currentRoute in topLevelRoutes

    Scaffold(
        modifier = modifier,
        containerColor = Color.Transparent,
        bottomBar = {
            if (showBottomNav) BottomNavBar(navController = navController, currentRoute = currentRoute)
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            NavHost(
                navController = navController,
                startDestination = startDestination
            ) {
                composable(Destinations.AUTH) {
                    AuthScreen(
                        viewModel = authViewModel,
                        onSignedIn = {
                            navController.navigate(Destinations.DASHBOARD) {
                                popUpTo(Destinations.AUTH) { inclusive = true }
                            }
                        }
                    )
                }
                composable(Destinations.DASHBOARD) {
                    DashboardScreen(
                        onStartWorkout = { routineId ->
                            navController.navigate(Destinations.logWorkout(routineId))
                        },
                        onOpenBodyweight = { navController.navigate(Destinations.BODYWEIGHT) },
                        onViewAllPRs = { navController.navigate(Destinations.PROGRESSION) }
                    )
                }
                composable(Destinations.ROUTINES) {
                    RoutineListScreen(
                        onCreateRoutine = { navController.navigate(Destinations.routineEditor(null)) },
                        onEditRoutine = { id -> navController.navigate(Destinations.routineEditor(id)) },
                        onStartRoutine = { id -> navController.navigate(Destinations.logWorkout(id)) }
                    )
                }
                composable(Destinations.HISTORY) {
                    HistoryScreen(
                        onOpenWorkout = { id -> navController.navigate(Destinations.workoutDetail(id)) }
                    )
                }
                composable(Destinations.PROGRESSION) { ProgressionScreen() }
                composable(Destinations.PROFILE) {
                    ProfileScreen(onSignOut = {
                        authViewModel.signOut()
                        navController.navigate(Destinations.AUTH) {
                            popUpTo(0) { inclusive = true }
                        }
                    })
                }

                composable(
                    route = Destinations.LOG_WORKOUT,
                    arguments = listOf(navArgument("routineId") {
                        type = NavType.StringType; nullable = true; defaultValue = null
                    })
                ) { entry ->
                    val routineId = entry.arguments?.getString("routineId")?.takeIf { it.isNotBlank() }
                    LogWorkoutScreen(
                        routineId = routineId,
                        onFinished = { navController.popBackStack() },
                        onCancel = { navController.popBackStack() }
                    )
                }
                composable(
                    route = Destinations.ROUTINE_EDITOR,
                    arguments = listOf(navArgument("routineId") {
                        type = NavType.StringType; nullable = true; defaultValue = null
                    })
                ) { entry ->
                    val routineId = entry.arguments?.getString("routineId")?.takeIf { it.isNotBlank() }
                    RoutineEditorScreen(
                        routineId = routineId,
                        onDone = { navController.popBackStack() }
                    )
                }
                composable(
                    route = Destinations.WORKOUT_DETAIL,
                    arguments = listOf(navArgument("workoutId") { type = NavType.StringType })
                ) { entry ->
                    val id = entry.arguments?.getString("workoutId").orEmpty()
                    WorkoutDetailScreen(
                        workoutId = id,
                        onBack = { navController.popBackStack() },
                        onEdit = { navController.navigate(Destinations.logWorkout(null)) }
                    )
                }
                composable(Destinations.BODYWEIGHT) {
                    BodyweightScreen(onBack = { navController.popBackStack() })
                }
            }
        }
    }
}

private val topLevelRoutes = setOf(
    Destinations.DASHBOARD,
    Destinations.ROUTINES,
    Destinations.HISTORY,
    Destinations.PROGRESSION,
    Destinations.PROFILE
)

internal fun NavHostController.navigateTopLevel(route: String) {
    navigate(route) {
        popUpTo(graph.startDestinationId) { saveState = true }
        launchSingleTop = true
        restoreState = true
    }
}
