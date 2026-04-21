package com.silkfitness.app.ui.log

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.silkfitness.app.domain.model.Category
import com.silkfitness.app.domain.model.Exercise
import com.silkfitness.app.ui.theme.neoPressed
import com.silkfitness.app.ui.theme.neoRaised

@Composable
fun ExercisePickerSheet(
    initiallySelected: Set<String> = emptySet(),
    onConfirm: (List<String>) -> Unit,
    onDismiss: () -> Unit,
    viewModel: ExercisePickerViewModel = hiltViewModel()
) {
    val uiState by viewModel.state.collectAsState()
    var selection by remember { mutableStateOf(initiallySelected.toSet()) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 400.dp)
            .padding(WindowInsets.ime.asPaddingValues())
            .padding(WindowInsets.navigationBars.asPaddingValues())
            .padding(horizontal = 16.dp)
    ) {
        Spacer(Modifier.height(8.dp))
        Box(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .height(4.dp)
                .width(40.dp)
                .neoPressed(shape = RoundedCornerShape(2.dp))
        )
        Spacer(Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Add Exercises", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            Text(
                "+ Custom",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.clickable { /* TODO inline custom form */ }
            )
        }

        Spacer(Modifier.height(16.dp))

        SearchField(
            value = uiState.query,
            onValueChange = viewModel::setQuery
        )

        Spacer(Modifier.height(16.dp))
        CategoryChips(
            selected = uiState.category,
            onSelect = viewModel::setCategory
        )

        Spacer(Modifier.height(16.dp))

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            items(uiState.results, key = { it.id }) { exercise ->
                ExerciseRow(
                    exercise = exercise,
                    checked = exercise.id in selection,
                    onToggle = {
                        selection = if (exercise.id in selection) selection - exercise.id
                        else selection + exercise.id
                    }
                )
            }
        }

        Spacer(Modifier.height(12.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .neoRaised(shape = RoundedCornerShape(24.dp))
                .clickable(enabled = selection.isNotEmpty()) {
                    onConfirm(selection.toList())
                },
            contentAlignment = Alignment.Center
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Add Exercises",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                if (selection.isNotEmpty()) {
                    Spacer(Modifier.width(8.dp))
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .neoRaised(shape = CircleShape, fill = MaterialTheme.colorScheme.primary),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = selection.size.toString(),
                            color = MaterialTheme.colorScheme.onPrimary,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
        Spacer(Modifier.height(12.dp))
    }
}

@Composable
private fun SearchField(value: String, onValueChange: (String) -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .neoPressed(shape = RoundedCornerShape(24.dp))
            .padding(horizontal = 16.dp)
    ) {
        Icon(
            imageVector = Icons.Outlined.Search,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(18.dp)
        )
        Spacer(Modifier.width(8.dp))
        Box(modifier = Modifier.weight(1f)) {
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                singleLine = true,
                cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                textStyle = MaterialTheme.typography.bodyMedium
                    .copy(color = MaterialTheme.colorScheme.onSurface),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search)
            )
            if (value.isEmpty()) {
                Text(
                    text = "Search exercises...",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Composable
private fun CategoryChips(selected: Category?, onSelect: (Category?) -> Unit) {
    val entries = listOf<Pair<String, Category?>>(
        "All" to null,
        "Push" to Category.PUSH,
        "Pull" to Category.PULL,
        "Legs" to Category.LEGS,
        "Core" to Category.CORE,
        "Cardio" to Category.CARDIO,
        "Mobility" to Category.MOBILITY
    )
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(entries) { (label, cat) ->
            val active = selected == cat
            Box(
                modifier = Modifier
                    .height(36.dp)
                    .let {
                        if (active) it.neoPressed(shape = RoundedCornerShape(18.dp))
                        else it.neoRaised(shape = RoundedCornerShape(18.dp))
                    }
                    .clickable { onSelect(cat) }
                    .padding(horizontal = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = if (active) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun ExerciseRow(
    exercise: Exercise,
    checked: Boolean,
    onToggle: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(72.dp)
            .neoRaised(shape = RoundedCornerShape(16.dp))
            .clickable(onClick = onToggle)
            .padding(horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .neoPressed(shape = CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = exercise.category.name.first().toString(),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = exercise.name,
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = exercise.muscleGroups.firstOrNull()?.replaceFirstChar { it.titlecase() }
                    ?.plus(" • ${exercise.equipment.display}")
                    ?: exercise.equipment.display,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Box(
            modifier = Modifier
                .size(28.dp)
                .let {
                    if (checked) it.neoRaised(
                        shape = RoundedCornerShape(8.dp),
                        fill = MaterialTheme.colorScheme.primary
                    )
                    else it.neoPressed(shape = RoundedCornerShape(8.dp))
                },
            contentAlignment = Alignment.Center
        ) {
            if (checked) {
                Icon(
                    imageVector = Icons.Filled.Check,
                    contentDescription = "Selected",
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}
