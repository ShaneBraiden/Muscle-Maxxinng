package com.musclemax.app.ui.log

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.musclemax.app.ui.components.AppTopBar
import com.musclemax.app.ui.components.NeoButton
import com.musclemax.app.ui.components.NeoButtonStyle
import com.musclemax.app.ui.theme.SurfaceBackground
import com.musclemax.app.ui.theme.neoCircle
import com.musclemax.app.ui.theme.neoPressed
import com.musclemax.app.ui.theme.neoRaised
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LogWorkoutScreen(
    routineId: String?,
    onFinished: () -> Unit,
    onCancel: () -> Unit,
    viewModel: LogWorkoutViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(state.savedResult) {
        if (state.savedResult != null) onFinished()
    }

    var showPicker by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            AppTopBar(
                title = "Muscle Maxxinnng",
                leadingIcon = Icons.AutoMirrored.Filled.ArrowBack,
                onLeadingClick = onCancel
            )

            LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item { RoutineNameRow(name = state.routineName, onChange = viewModel::setName) }
                item {
                    MetaRow(
                        dateLabel = formatDate(Date(state.date.toEpochMilliseconds())),
                        duration = state.durationMinutes,
                        onDurationChange = viewModel::setDuration
                    )
                }
                item { HeavyDayToggle(heavy = state.heavyDay, onToggle = viewModel::toggleHeavy) }

                itemsIndexed(state.exercises, key = { _, d -> d.exercise.id }) { index, draft ->
                    ExerciseLogCard(
                        draft = draft,
                        onRemove = { viewModel.removeExercise(index) },
                        onAddSet = { viewModel.addSet(index) },
                        onRemoveSet = { setIdx -> viewModel.removeSet(index, setIdx) },
                        onUpdate = { setIdx, reps, weight, rpe ->
                            viewModel.updateSet(index, setIdx, reps, weight, rpe)
                        }
                    )
                }

                item {
                    AddExerciseButton(onClick = { showPicker = true })
                }

                item { Spacer(Modifier.height(80.dp)) }
            }
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            NeoButton(
                text = if (state.isSaving) "Saving..." else "Finish Workout",
                onClick = { viewModel.save() },
                enabled = state.canSave,
                style = NeoButtonStyle.Primary,
                leadingIcon = Icons.Filled.Check,
                height = 56
            )
        }
    }

    if (showPicker) {
        ModalBottomSheet(
            onDismissRequest = { showPicker = false },
            sheetState = sheetState,
            containerColor = SurfaceBackground,
            dragHandle = null
        ) {
            ExercisePickerSheet(
                initiallySelected = state.exercises.map { it.exercise.id }.toSet(),
                onConfirm = { ids ->
                    viewModel.addExercises(ids)
                    showPicker = false
                },
                onDismiss = { showPicker = false }
            )
        }
    }
}

@Composable
private fun RoutineNameRow(name: String, onChange: (String) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .neoPressed(shape = RoundedCornerShape(18.dp))
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        BasicTextField(
            value = name,
            onValueChange = onChange,
            textStyle = MaterialTheme.typography.headlineSmall.copy(
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Bold
            ),
            singleLine = true,
            modifier = Modifier.weight(1f)
        )
        Icon(
            imageVector = Icons.Filled.Edit,
            contentDescription = "Edit name",
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(18.dp)
        )
    }
}

@Composable
private fun MetaRow(
    dateLabel: String,
    duration: String,
    onDurationChange: (String) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            modifier = Modifier
                .weight(1f)
                .neoPressed(shape = RoundedCornerShape(16.dp))
                .padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Outlined.CalendarToday,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(16.dp)
            )
            Spacer(Modifier.width(8.dp))
            Text(
                text = dateLabel,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Medium
            )
        }
        Row(
            modifier = Modifier
                .weight(1f)
                .neoPressed(shape = RoundedCornerShape(16.dp))
                .padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Filled.LocalFireDepartment,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(16.dp)
            )
            Spacer(Modifier.width(8.dp))
            BasicTextField(
                value = duration,
                onValueChange = onDurationChange,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                textStyle = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Medium
                ),
                singleLine = true,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = "min",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun HeavyDayToggle(heavy: Boolean, onToggle: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "Heavy Day",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.SemiBold
        )
        NeoSwitch(on = heavy, onToggle = onToggle)
    }
}

@Composable
private fun NeoSwitch(on: Boolean, onToggle: () -> Unit) {
    val trackColor = if (on) MaterialTheme.colorScheme.primary else SurfaceBackground
    Box(
        modifier = Modifier
            .width(48.dp)
            .height(26.dp)
            .neoPressed(shape = RoundedCornerShape(13.dp), fill = trackColor)
            .clickable { onToggle() },
        contentAlignment = if (on) Alignment.CenterEnd else Alignment.CenterStart
    ) {
        Box(
            modifier = Modifier
                .padding(2.dp)
                .size(22.dp)
                .neoRaised(
                    shape = CircleShape,
                    fill = if (on) Color.White else MaterialTheme.colorScheme.surfaceContainerLow,
                    offset = 2.dp,
                    blur = 4.dp
                )
        )
    }
}

@Composable
private fun ExerciseLogCard(
    draft: WorkoutExerciseDraft,
    onRemove: () -> Unit,
    onAddSet: () -> Unit,
    onRemoveSet: (Int) -> Unit,
    onUpdate: (Int, String?, String?, String?) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .neoRaised(shape = RoundedCornerShape(20.dp))
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = draft.exercise.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                val lastLabel = draft.lastReference?.let {
                    "Last: ${formatKg(it.set.weightKg)}kg × ${it.set.reps}"
                } ?: "No previous record"
                Text(
                    text = lastLabel,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .neoCircle(offset = 3.dp, blur = 6.dp)
                    .clickable { onRemove() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Remove,
                    contentDescription = "Remove exercise",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(16.dp)
                )
            }
        }

        Spacer(Modifier.height(10.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            draft.exercise.muscleGroups.take(3).forEachIndexed { idx, mg ->
                MuscleChip(label = mg, highlighted = idx == 0)
            }
        }

        Spacer(Modifier.height(14.dp))
        SetHeaderRow()
        Spacer(Modifier.height(6.dp))
        draft.sets.forEachIndexed { index, set ->
            val last = draft.lastReference?.set
            val beatsLast = beatsLast(set, last)
            SetRow(
                number = index + 1,
                set = set,
                beatsLast = beatsLast,
                onUpdate = { reps, weight, rpe -> onUpdate(index, reps, weight, rpe) },
                onDelete = { onRemoveSet(index) }
            )
            if (index != draft.sets.lastIndex) Spacer(Modifier.height(8.dp))
        }

        Spacer(Modifier.height(14.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
                .neoPressed(shape = RoundedCornerShape(14.dp))
                .clickable { onAddSet() },
            contentAlignment = Alignment.Center
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(Modifier.width(6.dp))
                Text(
                    "Add Set",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
private fun MuscleChip(label: String, highlighted: Boolean) {
    Box(
        modifier = Modifier
            .height(22.dp)
            .neoPressed(
                shape = RoundedCornerShape(11.dp),
                fill = if (highlighted) Color(0xFFE0E2FF) else SurfaceBackground
            )
            .padding(horizontal = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = if (highlighted) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun SetHeaderRow() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        HeaderCell("Set", weight = 0.6f)
        HeaderCell("kg", weight = 1f)
        HeaderCell("Reps", weight = 1f)
        HeaderCell("RPE", weight = 1f)
        Spacer(Modifier.width(32.dp))
    }
}

@Composable
private fun androidx.compose.foundation.layout.RowScope.HeaderCell(text: String, weight: Float) {
    Text(
        text = text,
        modifier = Modifier.weight(weight),
        style = MaterialTheme.typography.labelSmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        textAlign = TextAlign.Center,
        fontWeight = FontWeight.Medium
    )
}

@Composable
private fun SetRow(
    number: Int,
    set: SetDraft,
    beatsLast: Boolean,
    onUpdate: (String?, String?, String?) -> Unit,
    onDelete: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .weight(0.6f)
                .height(36.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = number.toString(),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.SemiBold
            )
        }
        NumericCell(
            value = set.weight,
            onChange = { onUpdate(null, it, null) },
            weight = 1f
        )
        NumericCell(
            value = set.reps,
            onChange = { onUpdate(it, null, null) },
            weight = 1f
        )
        NumericCell(
            value = set.rpe,
            onChange = { onUpdate(null, null, it) },
            weight = 1f
        )
        Spacer(Modifier.width(6.dp))
        Box(
            modifier = Modifier
                .size(26.dp)
                .neoCircle(
                    offset = 2.dp,
                    blur = 4.dp,
                    fill = if (beatsLast) Color(0xFFD1FAE5) else SurfaceBackground
                )
                .clickable { onDelete() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = if (beatsLast) Icons.Filled.Check else Icons.Filled.Close,
                contentDescription = if (beatsLast) "Beats last" else "Delete set",
                tint = if (beatsLast) Color(0xFF059669) else MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(14.dp)
            )
        }
    }
}

@Composable
private fun androidx.compose.foundation.layout.RowScope.NumericCell(
    value: String,
    onChange: (String) -> Unit,
    weight: Float
) {
    Box(
        modifier = Modifier
            .weight(weight)
            .padding(horizontal = 3.dp)
            .height(36.dp)
            .neoPressed(shape = RoundedCornerShape(10.dp))
            .padding(horizontal = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        BasicTextField(
            value = value,
            onValueChange = { s -> onChange(s.filter { it.isDigit() || it == '.' }.take(5)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            textStyle = MaterialTheme.typography.bodyMedium.copy(
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center
            ),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun AddExerciseButton(onClick: () -> Unit) {
    val primary = MaterialTheme.colorScheme.primary
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(54.dp)
            .border(
                BorderStroke(1.5.dp, primary.copy(alpha = 0.4f)),
                shape = RoundedCornerShape(16.dp)
            )
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = null,
                tint = primary,
                modifier = Modifier.size(18.dp)
            )
            Spacer(Modifier.width(6.dp))
            Text(
                "Add Exercise",
                color = primary,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

private fun beatsLast(current: SetDraft, previous: com.musclemax.app.domain.model.WorkoutSet?): Boolean {
    if (previous == null) return false
    val reps = current.reps.toIntOrNull() ?: return false
    val weight = current.weight.toDoubleOrNull() ?: return false
    if (reps <= 0) return false
    val currVol = reps * weight
    val prevVol = previous.reps * previous.weightKg
    return currVol > prevVol
}

private fun formatKg(v: Double): String =
    if (v % 1.0 == 0.0) v.toInt().toString() else String.format("%.1f", v)

private fun formatDate(d: Date): String {
    val fmt = SimpleDateFormat("'Today,' h:mm a", Locale.getDefault())
    return fmt.format(d)
}
