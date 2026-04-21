package com.silkfitness.app.ui.auth

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.silkfitness.app.R
import com.silkfitness.app.ui.theme.neoCircle
import com.silkfitness.app.ui.theme.neoPressed
import com.silkfitness.app.ui.theme.neoRaised

@Composable
fun AuthScreen(
    viewModel: AuthViewModel,
    onSignedIn: () -> Unit
) {
    val context = LocalContext.current
    val clientId = stringResource(R.string.default_web_client_id)
    val state by viewModel.authState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(WindowInsets.safeDrawing.asPaddingValues())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.widthIn(max = 360.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(96.dp)
                        .neoRaised(shape = CircleShape, offset = 6.dp, blur = 14.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.FitnessCenter,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(44.dp)
                    )
                }
                Spacer(Modifier.height(24.dp))
                Text(
                    text = "Silk Fitness",
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text = "Your journey starts here.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(Modifier.height(48.dp))

                AuthButton(
                    label = "Continue with Google",
                    loading = state.isLoading,
                    leadingIcon = null,
                    showGoogleMark = true,
                    onClick = { viewModel.signInWithGoogle(context, clientId, onSignedIn) }
                )
                Spacer(Modifier.height(16.dp))
                AuthButton(
                    label = "Continue with Apple",
                    enabled = false,
                    leadingIcon = null,
                    onClick = {}
                )
                Spacer(Modifier.height(24.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    DividerLine()
                    Text(
                        text = "or",
                        modifier = Modifier.padding(horizontal = 12.dp),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.labelMedium
                    )
                    DividerLine()
                }
                Spacer(Modifier.height(24.dp))
                AuthButton(
                    label = "Continue with Email",
                    pressed = true,
                    leadingIcon = Icons.Outlined.Email,
                    enabled = false,
                    onClick = {}
                )

                val error = state.errorMessage
                if (!error.isNullOrBlank()) {
                    Spacer(Modifier.height(16.dp))
                    Text(
                        text = error,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.labelMedium,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        Text(
            text = "By continuing, you agree to Silk Fitness's\nTerms of Service and Privacy Policy.",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp, lineHeight = 16.sp),
            modifier = Modifier.padding(bottom = 8.dp)
        )
    }
}

@Composable
private fun AuthButton(
    label: String,
    enabled: Boolean = true,
    loading: Boolean = false,
    pressed: Boolean = false,
    leadingIcon: ImageVector? = null,
    showGoogleMark: Boolean = false,
    onClick: () -> Unit
) {
    val shape = RoundedCornerShape(20.dp)
    val baseMod = Modifier
        .fillMaxWidth()
        .height(56.dp)
        .let { if (pressed) it.neoPressed(shape = shape) else it.neoRaised(shape = shape) }
        .clickable(enabled = enabled && !loading, onClick = onClick)

    Row(
        modifier = baseMod.padding(horizontal = 20.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        when {
            leadingIcon != null -> {
                Icon(
                    imageVector = leadingIcon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(Modifier.width(12.dp))
            }
            showGoogleMark -> {
                Box(
                    modifier = Modifier
                        .size(22.dp)
                        .neoCircle(offset = 2.dp, blur = 4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "G",
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                }
                Spacer(Modifier.width(12.dp))
            }
        }
        Text(
            text = if (loading) "Signing in…" else label,
            style = MaterialTheme.typography.titleMedium,
            color = if (pressed) MaterialTheme.colorScheme.onSurfaceVariant
            else MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun DividerLine() {
    Box(
        modifier = Modifier
            .height(2.dp)
            .width(60.dp)
            .neoPressed(shape = RoundedCornerShape(1.dp))
    )
}
