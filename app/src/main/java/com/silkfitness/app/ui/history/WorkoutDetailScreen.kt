package com.silkfitness.app.ui.history

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.outlined.FitnessCenter
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.firebase.Timestamp
import com.silkfitness.app.domain.model.WorkoutSet
import com.silkfitness.app.ui.components.BackTopBar
import com.silkfitness.app.ui.theme.neoPressed
import com.silkfitness.app.ui.theme.neoRaised
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun WorkoutDetailScreen(
    workoutId: String,
    onBack: () -> Unit,
    onEdit: () -> Unit,
    viewModel: WorkoutDetailViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(state.isDeleted) {
        if (state.isDeleted) onBack()
    }

    Column(modifier = Modifier.fillMaxSize()) {
        BackTopBar(title = "Muscle Maxxinnng", onBack = onBack)

        val w = state.workout
        if (w == null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    text = if (state.isLoading) "Loading..." else "Workout not found",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            return@Column
        }

        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "${formatDate(w.date)} · ${formatDuration(w.durationMinutes)}",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.weight(1f)
                    )
                    if (w.heavyMultiplier > 1.0) HeavyDayChip()
                }
            }
            item {
                Text(
                    text = w.routineName.ifBlank { "Workout" },
                    style = MaterialTheme.typography.displaySmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            item {
                val prs = state.exercises.count { it.trend == ExerciseTrend.IMPROVED }
                StatsRow(
                    volume = formatVolume(w.totalVolumeKg),
                    calories = w.caloriesBurned.toString(),
                    prs = prs.toString()
                )
            }
            item {
                Text(
                    text = "Exercises",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(top = 6.dp)
                )
            }

            items(state.exercises, key = { it.exercise.id }) { detail ->
                ExerciseDetailCard(detail)
            }

            item { Spacer(Modifier.height(8.dp)) }
            item {
                ActionButton(
                    label = "Edit Workout",
                    icon = Icons.Filled.EditNote,
                    color = MaterialTheme.colorScheme.primary,
                    onClick = onEdit
                )
            }
            item {
                ActionButton(
                    label = "Delete Session",
                    icon = Icons.Filled.DeleteOutline,
                    color = Color(0xFFDC2626),
                    onClick = { viewModel.delete() }
                )
            }
            item { Spacer(Modifier.height(16.dp)) }
        }
    }
}

@Composable
private fun HeavyDayChip() {
    Row(
        modifier = Modifier
            .neoPressed(
                shape = RoundedCornerShape(12.dp),
                fill = Color(0xFFEDE9FE)
            )
            .padding(horizontal = 10.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Filled.LocalFireDepartment,
            contentDescription = null,
            tint = Color(0xFF7C3AED),
            modifier = Modifier.size(12.dp)
        )
        Spacer(Modifier.width(4.dp))
        Text(
            text = "HEAVY DAY",
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF7C3AED)
        )
    }
}

@Composable
private fun StatsRow(volume: String, calories: String, prs: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        StatTile(
            icon = Icons.Outlined.FitnessCenter,
            value = volume,
            label = "VOL KG",
            modifier = Modifier.weight(1f)
        )
        StatTile(
            icon = Icons.Filled.LocalFireDepartment,
            value = calories,
            label = "KCAL",
            modifier = Modifier.weight(1f)
        )
        StatTile(
            icon = Icons.Filled.EmojiEvents,
            value = prs,
            label = "PRS",
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun StatTile(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    value: String,
    label: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .neoRaised(shape = RoundedCornerShape(18.dp))
            .padding(vertical = 14.dp, horizontal = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(18.dp)
        )
        Spacer(Modifier.height(6.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun ExerciseDetailCard(detail: DetailExercise) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .neoRaised(shape = RoundedCornerShape(20.dp))
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = detail.exercise.name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.weight(1f)
            )
            TrendBadge(detail)
        }
        Spacer(Modifier.height(12.dp))
        DetailSetHeader()
        Spacer(Modifier.height(4.dp))
        detail.sets.forEachIndexed { index, set ->
            SetLine(
                number = index + 1,
                set = set,
                previous = detail.previousSets.firstOrNull(),
                isBest = detail.sets.maxByOrNull { it.weightKg * 100 + it.reps } == set
            )
        }
    }
}

@Composable
private fun TrendBadge(detail: DetailExercise) {
    val (label, color, bgColor) = when (detail.trend) {
        ExerciseTrend.IMPROVED -> Triple("+PR", Color(0xFF059669), Color(0xFFD1FAE5))
        ExerciseTrend.MAINTAINED -> Triple("Maintained", MaterialTheme.colorScheme.onSurfaceVariant, Color(0xFFE5E7EB))
        ExerciseTrend.REGRESSED -> Triple("Down", Color(0xFFDC2626), Color(0xFFFEE2E2))
        ExerciseTrend.NEW -> Triple("New", MaterialTheme.colorScheme.primary, Color(0xFFE0E2FF))
    }
    Row(
        modifier = Modifier
            .neoPressed(shape = RoundedCornerShape(12.dp), fill = bgColor)
            .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (detail.trend == ExerciseTrend.IMPROVED) {
            Icon(
                imageVector = Icons.Filled.ArrowUpward,
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(10.dp)
            )
            Spacer(Modifier.width(3.dp))
        }
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.SemiBold,
            color = color
        )
    }
}

@Composable
private fun DetailSetHeader() {
    Row(modifier = Modifier.fillMaxWidth()) {
        HeaderText("SET", 0.5f)
        HeaderText("KG", 1f)
        HeaderText("REPS", 1f)
        HeaderText("PREV", 1.2f)
    }
}

@Composable
private fun androidx.compose.foundation.layout.RowScope.HeaderText(text: String, weight: Float) {
    Text(
        text = text,
        modifier = Modifier.weight(weight),
        style = MaterialTheme.typography.labelSmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center
    )
}

@Composable
private fun SetLine(number: Int, set: WorkoutSet, previous: WorkoutSet?, isBest: Boolean) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        CellText(number.toString(), 0.5f, bold = true)
        CellText(
            text = formatKg(set.weightKg),
            weight = 1f,
            bold = true,
            color = if (isBest) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
        )
        CellText(set.reps.toString(), 1f)
        CellText(
            text = previous?.let { "${formatKg(it.weightKg)}×${it.reps}" } ?: "—",
            weight = 1.2f,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun androidx.compose.foundation.layout.RowScope.CellText(
    text: String,
    weight: Float,
    bold: Boolean = false,
    color: Color = MaterialTheme.colorScheme.onSurface
) {
    Text(
        text = text,
        modifier = Modifier.weight(weight),
        style = MaterialTheme.typography.bodyMedium,
        color = color,
        fontWeight = if (bold) FontWeight.SemiBold else FontWeight.Normal,
        textAlign = TextAlign.Center
    )
}

@Composable
private fun ActionButton(
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .neoRaised(shape = RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .padding(vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = color,
            modifier = Modifier.size(18.dp)
        )
        Spacer(Modifier.width(8.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = color
        )
    }
}

private fun formatDate(ts: Timestamp): String =
    SimpleDateFormat("MMM d, yyyy", Locale.getDefault()).format(ts.toDate())

private fun formatDuration(min: Int): String {
    val h = min / 60
    val m = min % 60
    return if (h > 0) "${h}h ${m}m" else "${m}m"
}

private fun formatKg(v: Double): String =
    if (v % 1.0 == 0.0) v.toInt().toString() else String.format(Locale.getDefault(), "%.1f", v)

private fun formatVolume(v: Double): String {
    if (v >= 1000) return String.format(Locale.getDefault(), "%.1fk", v / 1000)
    return v.toInt().toString()
}
