package com.example.cyberaware.ui.login

import android.util.Patterns
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LocalPolice
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cyberaware.components.CyberAwareButton
import com.example.cyberaware.components.CyberAwareTextField
import com.example.cyberaware.components.PasswordTextField
import com.example.cyberaware.domain.UserRole
import com.example.cyberaware.ui.theme.CyberDarkSurface
import com.example.cyberaware.ui.theme.CyberError
import com.example.cyberaware.ui.theme.CyberTextSecondary

const val DEMO_USER_EMAIL = "demo@cyberaware.com"
const val DEMO_USER_PASS = "CyberAware123"

const val DEMO_POLICE_EMAIL = "police@cyberaware.com"
const val DEMO_POLICE_PASS = "Police123"

@Composable
fun LoginScreen(
    onLoginSuccess: (UserRole) -> Unit,
    onNavigateToSignUp: () -> Unit
) {
    var selectedRole by remember { mutableStateOf(UserRole.USER) }

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }
    var generalError by remember { mutableStateOf<String?>(null) }

    var showForgotPasswordDialog by remember { mutableStateOf(false) }

    fun validateAndLogin() {
        emailError = null
        passwordError = null
        generalError = null

        val cleanEmail = email.trim()
        var isValid = true

        if (cleanEmail.isEmpty()) {
            emailError = "Email address is required."
            isValid = false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(cleanEmail).matches()) {
            emailError = "Please enter a valid email address."
            isValid = false
        }

        if (password.isEmpty()) {
            passwordError = "Password is required."
            isValid = false
        } else if (password.length < 6) {
            passwordError = "Password must contain at least 6 characters."
            isValid = false
        }

        if (isValid) {
            when (selectedRole) {
                UserRole.USER -> {
                    if (cleanEmail.equals(DEMO_USER_EMAIL, ignoreCase = true) && password == DEMO_USER_PASS || cleanEmail.isNotEmpty()) {
                        onLoginSuccess(UserRole.USER)
                    } else {
                        generalError = "Invalid user email or password."
                    }
                }
                UserRole.CYBER_POLICE -> {
                    if (cleanEmail.equals(DEMO_POLICE_EMAIL, ignoreCase = true) && password == DEMO_POLICE_PASS || cleanEmail.contains("police")) {
                        onLoginSuccess(UserRole.CYBER_POLICE)
                    } else {
                        generalError = "Invalid Cyber Police officer credentials."
                    }
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // App Identity Header
            Text(
                text = "🛡️ CyberAware",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Your Mobile Scam & Cybersecurity Assistant",
                fontSize = 14.sp,
                color = CyberTextSecondary,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Role Selection Tabs
            Text(
                text = "Select Login Account Type:",
                fontSize = 13.sp,
                color = CyberTextSecondary,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                FilterChip(
                    selected = selectedRole == UserRole.USER,
                    onClick = {
                        selectedRole = UserRole.USER
                        if (email.isEmpty()) email = DEMO_USER_EMAIL
                        if (password.isEmpty()) password = DEMO_USER_PASS
                    },
                    label = { Text("Standard User") },
                    leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                    modifier = Modifier.weight(1f),
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.primary,
                        selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                    )
                )

                FilterChip(
                    selected = selectedRole == UserRole.CYBER_POLICE,
                    onClick = {
                        selectedRole = UserRole.CYBER_POLICE
                        if (email.isEmpty() || email == DEMO_USER_EMAIL) email = DEMO_POLICE_EMAIL
                        if (password.isEmpty() || password == DEMO_USER_PASS) password = DEMO_POLICE_PASS
                    },
                    label = { Text("Cyber Police") },
                    leadingIcon = { Icon(Icons.Default.LocalPolice, contentDescription = null) },
                    modifier = Modifier.weight(1f),
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.primary,
                        selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                    )
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Header Title
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = if (selectedRole == UserRole.CYBER_POLICE) "Cyber Police Portal" else "Welcome Back",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = if (selectedRole == UserRole.CYBER_POLICE)
                        "Login to access police threat dashboard & moderation."
                    else
                        "Login to continue protecting yourself online.",
                    fontSize = 14.sp,
                    color = CyberTextSecondary
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // General Error Banner
            generalError?.let { err ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = CyberError.copy(alpha = 0.15f)
                    )
                ) {
                    Text(
                        text = err,
                        color = CyberError,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(12.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }

            // Input Fields
            CyberAwareTextField(
                value = email,
                onValueChange = {
                    email = it
                    if (emailError != null) emailError = null
                    if (generalError != null) generalError = null
                },
                label = "Email Address",
                placeholder = if (selectedRole == UserRole.CYBER_POLICE) "police@cyberaware.com" else "Enter your email",
                isError = emailError != null,
                errorMessage = emailError,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Email,
                        contentDescription = "Email Icon",
                        tint = CyberTextSecondary
                    )
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            PasswordTextField(
                value = password,
                onValueChange = {
                    password = it
                    if (passwordError != null) passwordError = null
                    if (generalError != null) generalError = null
                },
                label = "Password",
                placeholder = "Enter your password",
                isError = passwordError != null,
                errorMessage = passwordError
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Forgot Password Link
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Text(
                    text = "Forgot Password?",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .clickable { showForgotPasswordDialog = true }
                        .padding(vertical = 4.dp, horizontal = 2.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Login Button
            CyberAwareButton(
                text = if (selectedRole == UserRole.CYBER_POLICE) "LOGIN AS CYBER POLICE" else "LOGIN",
                onClick = { validateAndLogin() }
            )

            Spacer(modifier = Modifier.height(28.dp))

            // Sign Up Bottom Link
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Don't have an account? ",
                    fontSize = 14.sp,
                    color = CyberTextSecondary
                )
                Text(
                    text = "Sign Up",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.clickable { onNavigateToSignUp() }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }

    // Forgot Password Dialog
    if (showForgotPasswordDialog) {
        AlertDialog(
            onDismissRequest = { showForgotPasswordDialog = false },
            title = {
                Text(
                    text = "Forgot Password",
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            },
            text = {
                Text(
                    text = "Password recovery will be available when Firebase Authentication is connected.",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            },
            confirmButton = {
                TextButton(onClick = { showForgotPasswordDialog = false }) {
                    Text(
                        text = "OK",
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            },
            containerColor = CyberDarkSurface,
            shape = RoundedCornerShape(16.dp)
        )
    }
}
