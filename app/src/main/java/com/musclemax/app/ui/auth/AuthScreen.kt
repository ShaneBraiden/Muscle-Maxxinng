package com.musclemax.app.ui.auth

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
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.musclemax.app.R
import com.musclemax.app.ui.theme.neoCircle
import com.musclemax.app.ui.theme.neoPressed
import com.musclemax.app.ui.theme.neoRaised

private enum class AuthMode { SignIn, SignUp }

@Composable
fun AuthScreen(
    viewModel: AuthViewModel,
    onSignedIn: () -> Unit
) {
    val context = LocalContext.current
    val clientId = stringResource(R.string.default_web_client_id)
    val state by viewModel.authState.collectAsStateWithLifecycle()

    var mode by remember { mutableStateOf(AuthMode.SignIn) }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var displayName by remember { mutableStateOf("") }

    val canSubmit = email.contains('@') && password.length >= 6 &&
        (mode == AuthMode.SignIn || displayName.isNotBlank())

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
                    text = "Muscle Maxxinnng",
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

                Spacer(Modifier.height(32.dp))

                ModeToggle(mode = mode, onChange = { mode = it })

                Spacer(Modifier.height(20.dp))

                if (mode == AuthMode.SignUp) {
                    AuthField(
                        value = displayName,
                        onValueChange = { displayName = it },
                        placeholder = "Name",
                        keyboardType = KeyboardType.Text,
                        isPassword = false
                    )
                    Spacer(Modifier.height(12.dp))
                }

                AuthField(
                    value = email,
                    onValueChange = { email = it },
                    placeholder = "Email",
                    keyboardType = KeyboardType.Email,
                    isPassword = false
                )
                Spacer(Modifier.height(12.dp))

                AuthField(
                    value = password,
                    onValueChange = { password = it },
                    placeholder = "Password",
                    keyboardType = KeyboardType.Password,
                    isPassword = true
                )

                Spacer(Modifier.height(20.dp))

                AuthButton(
                    label = if (mode == AuthMode.SignIn) "Sign in" else "Create account",
                    loading = state.isLoading,
                    enabled = canSubmit && !state.isLoading,
                    onClick = {
                        if (mode == AuthMode.SignIn) {
                            viewModel.signInWithEmail(email, password, onSignedIn)
                        } else {
                            viewModel.signUpWithEmail(email, password, displayName, onSignedIn)
                        }
                    }
                )

                Spacer(Modifier.height(20.dp))
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
                Spacer(Modifier.height(20.dp))

                AuthButton(
                    label = "Continue with Google",
                    loading = false,
                    enabled = !state.isLoading,
                    showGoogleMark = true,
                    onClick = { viewModel.signInWithGoogle(context, clientId, onSignedIn) }
                )

                val verification = state.verificationEmail
                if (verification != null) {
                    Spacer(Modifier.height(16.dp))
                    Text(
                        text = "We sent a verification link to $verification. " +
                            "Confirm your email, then sign in.",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.labelMedium,
                        textAlign = TextAlign.Center
                    )
                }

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
            text = "By continuing, you agree to Muscle Max's\nTerms of Service and Privacy Policy.",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp, lineHeight = 16.sp),
            modifier = Modifier.padding(bottom = 8.dp)
        )
    }
}

@Composable
private fun ModeToggle(mode: AuthMode, onChange: (AuthMode) -> Unit) {
    val shape = RoundedCornerShape(14.dp)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(44.dp)
            .neoPressed(shape = shape)
            .padding(4.dp)
    ) {
        ModeTab(
            label = "Sign in",
            selected = mode == AuthMode.SignIn,
            modifier = Modifier.weight(1f),
            onClick = { onChange(AuthMode.SignIn) }
        )
        ModeTab(
            label = "Sign up",
            selected = mode == AuthMode.SignUp,
            modifier = Modifier.weight(1f),
            onClick = { onChange(AuthMode.SignUp) }
        )
    }
}

@Composable
private fun ModeTab(label: String, selected: Boolean, modifier: Modifier, onClick: () -> Unit) {
    val shape = RoundedCornerShape(10.dp)
    Box(
        modifier = modifier
            .fillMaxWidth()
            .let { if (selected) it.neoRaised(shape = shape, offset = 2.dp, blur = 4.dp) else it }
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.SemiBold,
            color = if (selected) MaterialTheme.colorScheme.primary
            else MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun AuthField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    keyboardType: KeyboardType,
    isPassword: Boolean
) {
    val shape = RoundedCornerShape(16.dp)
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp)
            .neoPressed(shape = shape)
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            visualTransformation = if (isPassword) PasswordVisualTransformation() else androidx.compose.ui.text.input.VisualTransformation.None,
            textStyle = TextStyle(
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = MaterialTheme.typography.titleMedium.fontSize,
                fontWeight = FontWeight.SemiBold
            ),
            modifier = Modifier.fillMaxWidth()
        )
        if (value.isEmpty()) {
            Text(
                text = placeholder,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.titleMedium
            )
        }
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
            text = if (loading) "Please wait…" else label,
            style = MaterialTheme.typography.titleMedium,
            color = if (enabled) MaterialTheme.colorScheme.primary
            else MaterialTheme.colorScheme.onSurfaceVariant,
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
