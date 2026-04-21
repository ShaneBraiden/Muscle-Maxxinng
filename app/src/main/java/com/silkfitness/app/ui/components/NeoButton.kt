package com.silkfitness.app.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.silkfitness.app.ui.theme.neoPressed
import com.silkfitness.app.ui.theme.neoRaised

enum class NeoButtonStyle { Raised, Pressed, Primary }

@Composable
fun NeoButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    style: NeoButtonStyle = NeoButtonStyle.Raised,
    leadingIcon: ImageVector? = null,
    trailingIcon: ImageVector? = null,
    height: Int = 48
) {
    val shape = RoundedCornerShape(16.dp)
    val base = modifier
        .fillMaxWidth()
        .height(height.dp)
        .let {
            when (style) {
                NeoButtonStyle.Raised -> it.neoRaised(shape = shape)
                NeoButtonStyle.Pressed -> it.neoPressed(shape = shape)
                NeoButtonStyle.Primary -> it.neoRaised(shape = shape, fill = MaterialTheme.colorScheme.primary)
            }
        }
        .clickable(enabled = enabled, onClick = onClick)
        .padding(horizontal = 16.dp)

    Row(
        modifier = base,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        val contentColor = when (style) {
            NeoButtonStyle.Raised -> MaterialTheme.colorScheme.primary
            NeoButtonStyle.Pressed -> MaterialTheme.colorScheme.onSurfaceVariant
            NeoButtonStyle.Primary -> Color.White
        }
        if (leadingIcon != null) {
            Icon(imageVector = leadingIcon, contentDescription = null, tint = contentColor, modifier = Modifier.size(18.dp))
            Spacer(Modifier.width(8.dp))
        }
        Text(
            text = text,
            color = contentColor,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )
        if (trailingIcon != null) {
            Spacer(Modifier.width(8.dp))
            Icon(imageVector = trailingIcon, contentDescription = null, tint = contentColor, modifier = Modifier.size(18.dp))
        }
    }
}
