package com.silkfitness.app.ui.routines

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
import androidx.compose.foundation.layout.navigationBarsPadding
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DragHandle
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.silkfitness.app.domain.model.DayOfWeek
import com.silkfitness.app.ui.components.BackTopBar
import com.silkfitness.app.ui.components.NeoButton
import com.silkfitness.app.ui.components.NeoButtonStyle
import com.silkfitness.app.ui.log.ExercisePickerSheet
import com.silkfitness.app.ui.theme.SurfaceBackground
import com.silkfitness.app.ui.theme.neoPressed
import com.silkfitness.app.ui.theme.neoRaised
import kotlinx.coroutines.launch

@Composable
fun RoutineEditorScreen(
    routineId: String?,
    onDone: () -> Unit,
    viewModel: RoutineEditorViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    var showPicker by remember { mutableStateOf(false) }

    LaunchedEffect(state.savedSuccessfully) {
        if (state.savedSuccessfully) onDone()
    }

    Column(modifier = Modifier.fillMaxSize()) {
        BackTopBar(
            title = if (routineId.isNullOrBlank()) "New Routine" else "Edit Routine",
            onBack = onDone
        )

        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            item {
                Column {
                    SectionLabel("Routine Name")
                    Spacer(Modifier.height(8.dp))
                    TextFieldBox(
                        value = state.name,
                        placeholder = "e.g. Upper Body Power",
                        onValueChange = viewModel::setName
                    )
                }
            }
            item {
                Column {
                    SectionLabel("Active Days")
                    Spacer(Modifier.height(12.dp))
                    DayPicker(selected = state.day, onSelect = viewModel::setDay)
                }
            }
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    SectionLabel("Exercises")
                    Text(
                        text = "${state.entries.size} items",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
            itemsIndexed(state.entries, key = { _, e -> e.exercise.id }) { idx, entry ->
                ExerciseEntryCard(
                    entry = entry,
                    onSets = { viewModel.update(idx, sets = it) },
                    onReps = { viewModel.update(idx, reps = it) },
                    onWeight = { viewModel.update(idx, weight = it) },
                    onRemove = { viewModel.remove(idx) }
                )
            }
            item {
                NeoButton(
                    text = "Add Exercise",
                    leadingIcon = Icons.Filled.Add,
                    onClick = { showPicker = true },
                    style = NeoButtonStyle.Raised,
                    height = 52
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(horizontal = 20.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            NeoButton(
                text = "Cancel",
                onClick = onDone,
                style = NeoButtonStyle.Pressed,
                modifier = Modifier.weight(1f)
            )
            NeoButton(
                text = "Save Routine",
                leadingIcon = Icons.Filled.Save,
                onClick = viewModel::save,
                enabled = state.canSave,
                style = NeoButtonStyle.Raised,
                modifier = Modifier.weight(1.4f)
            )
        }
    }

    if (showPicker) {
        val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
        val scope = rememberCoroutineScope()
        ModalBottomSheet(
            onDismissRequest = { showPicker = false },
            sheetState = sheetState,
            containerColor = SurfaceBackground
        ) {
            ExercisePickerSheet(
                initiallySelected = state.entries.map { it.exercise.id }.toSet(),
                onConfirm = { ids ->
                    viewModel.addExercises(ids)
                    scope.launch { sheetState.hide(); showPicker = false }
                },
                onDismiss = { showPicker = false }
            )
        }
    }
}

@Composable
private fun SectionLabel(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelLarge,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        fontWeight = FontWeight.SemiBold
    )
}

@Composable
private fun TextFieldBox(
    value: String,
    placeholder: String,
    onValueChange: (String) -> Unit,
    keyboard: KeyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
    textAlign: TextAlign = TextAlign.Start,
    modifier: Modifier = Modifier,
    height: Int = 52
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(height.dp)
            .neoPressed(shape = RoundedCornerShape(16.dp))
            .padding(horizontal = 16.dp),
        contentAlignment = if (textAlign == TextAlign.Center) Alignment.Center else Alignment.CenterStart
    ) {
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            singleLine = true,
            cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
            textStyle = TextStyle(
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.SemiBold,
                textAlign = textAlign
            ).merge(MaterialTheme.typography.bodyLarge),
            keyboardOptions = keyboard,
            modifier = Modifier.fillMaxWidth()
        )
        if (value.isEmpty()) {
            Text(
                text = placeholder,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
private fun DayPicker(selected: DayOfWeek?, onSelect: (DayOfWeek?) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        DayOfWeek.values().forEach { day ->
            val isSelected = selected == day
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .let {
                        if (isSelected) it.neoRaised(
                            shape = CircleShape,
                            fill = MaterialTheme.colorScheme.primary
                        )
                        else it.neoPressed(shape = CircleShape)
                    }
                    .clickable { onSelect(if (isSelected) null else day) },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = day.short,
                    color = if (isSelected) Color.White
                    else MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun ExerciseEntryCard(
    entry: RoutineEntryUi,
    onSets: (String) -> Unit,
    onReps: (String) -> Unit,
    onWeight: (String) -> Unit,
    onRemove: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .neoRaised(shape = RoundedCornerShape(18.dp))
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Filled.DragHandle,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(18.dp)
        )
        Spacer(Modifier.width(10.dp))
        Column(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = entry.exercise.name,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(1f)
                )
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .neoPressed(shape = CircleShape)
                        .clickable { onRemove() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = "Remove",
                        tint = MaterialTheme.colorScheme.error,
                        modifier = Modifier.size(12.dp)
                    )
                }
            }
            Spacer(Modifier.height(10.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                MetricInput(
                    label = "SETS",
                    value = entry.targetSets,
                    onChange = onSets,
                    modifier = Modifier.weight(1f)
                )
                MetricInput(
                    label = "REPS",
                    value = entry.targetReps,
                    onChange = onReps,
                    modifier = Modifier.weight(1f)
                )
                MetricInput(
                    label = "KG",
                    value = entry.targetWeight,
                    onChange = onWeight,
                    modifier = Modifier.weight(1f),
                    allowDecimal = true
                )
            }
        }
    }
}

@Composable
private fun MetricInput(
    label: String,
    value: String,
    onChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    allowDecimal: Boolean = false
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = modifier) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(Modifier.height(4.dp))
        TextFieldBox(
            value = value,
            placeholder = if (label == "KG") "—" else "0",
            onValueChange = { raw ->
                val filtered = raw.filter { if (allowDecimal) it.isDigit() || it == '.' else it.isDigit() }
                onChange(filtered)
            },
            keyboard = KeyboardOptions(
                keyboardType = if (allowDecimal) KeyboardType.Decimal else KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            textAlign = TextAlign.Center,
            height = 44
        )
    }
}
