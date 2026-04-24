package com.musclemax.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Insights
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.AddBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.musclemax.app.ui.navigation.Destinations
import com.musclemax.app.ui.navigation.navigateTopLevel
import com.musclemax.app.ui.theme.SurfaceBackground
import com.musclemax.app.ui.theme.neoPressed

data class BottomTab(val route: String, val icon: ImageVector, val label: String)

@Composable
fun BottomNavBar(navController: NavHostController, currentRoute: String?) {
    val tabs = remember {
        listOf(
            BottomTab(Destinations.DASHBOARD, Icons.Filled.Dashboard, "Home"),
            BottomTab(Destinations.ROUTINES, Icons.Outlined.AddBox, "Workout"),
            BottomTab(Destinations.HISTORY, Icons.Filled.History, "History"),
            BottomTab(Destinations.PROGRESSION, Icons.Filled.Insights, "Stats"),
            BottomTab(Destinations.PROFILE, Icons.Filled.Person, "Profile")
        )
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(SurfaceBackground, RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        tabs.forEach { tab ->
            val selected = currentRoute == tab.route
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .then(
                        if (selected)
                            Modifier.neoPressed(shape = RoundedCornerShape(12.dp))
                        else Modifier
                    )
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) { if (!selected) navController.navigateTopLevel(tab.route) }
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Icon(
                    imageVector = tab.icon,
                    contentDescription = tab.label,
                    tint = if (selected) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(22.dp)
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    text = tab.label,
                    style = MaterialTheme.typography.labelSmall,
                    color = if (selected) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
