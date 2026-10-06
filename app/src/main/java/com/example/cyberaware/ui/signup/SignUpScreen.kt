package com.example.cyberaware.ui.signup

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
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
import com.example.cyberaware.ui.theme.CyberSuccess
import com.example.cyberaware.ui.theme.CyberTextSecondary
import com.example.cyberaware.ui.theme.CyberWarning

enum class PasswordStrength {
    EMPTY, WEAK, MEDIUM, STRONG
}

fun calculatePasswordStrength(pass: String): PasswordStrength {
    if (pass.isEmpty()) return PasswordStrength.EMPTY
    if (pass.length < 6) return PasswordStrength.WEAK

    val hasUpper = pass.any { it.isUpperCase() }
    val hasLower = pass.any { it.isLowerCase() }
    val hasDigit = pass.any { it.isDigit() }
    val hasSpecial = pass.any { !it.isLetterOrDigit() }

    return if (pass.length >= 8 && hasUpper && hasLower && hasDigit && hasSpecial) {
        PasswordStrength.STRONG
    } else {
        PasswordStrength.MEDIUM
    }
}

@Composable
fun SignUpScreen(
    onSignUpSuccess: () -> Unit,
    onNavigateToLogin: () -> Unit
) {
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var termsAccepted by remember { mutableStateOf(false) }

    var fullNameError by remember { mutableStateOf<String?>(null) }
    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }
    var confirmPasswordError by remember { mutableStateOf<String?>(null) }
    var termsError by remember { mutableStateOf<String?>(null) }

    var showTermsDialog by remember { mutableStateOf(false) }
    var showSuccessDialog by remember { mutableStateOf(false) }

    val passwordStrength = calculatePasswordStrength(password)

    fun validateAndSignUp() {
        fullNameError = null
        emailError = null
        passwordError = null
        confirmPasswordError = null
        termsError = null

        var isValid = true

        val cleanName = fullName.trim()
        if (cleanName.isEmpty()) {
            fullNameError = "Full name is required."
            isValid = false
        } else if (cleanName.length < 3) {
            fullNameError = "Full name must be at least 3 characters."
            isValid = false
        }

        val cleanEmail = email.trim()
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

        if (confirmPassword.isEmpty()) {
            confirmPasswordError = "Confirm password is required."
            isValid = false
        } else if (confirmPassword != password) {
            confirmPasswordError = "Passwords do not match."
            isValid = false
        }

        if (!termsAccepted) {
            termsError = "You must agree to the Terms and Privacy Policy."
            isValid = false
        }

        if (isValid) {
            // Success! Show success dialog which will navigate to Login
            showSuccessDialog = true
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
            verticalArrangement = Arrangement.Top
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // App Brand
            Text(
                text = "🛡️ CyberAware",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Header Title
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = "Create Account",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Create your CyberAware account and stay protected.",
                    fontSize = 14.sp,
                    color = CyberTextSecondary
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 1. Full Name
            CyberAwareTextField(
                value = fullName,
                onValueChange = {
                    fullName = it
                    if (fullNameError != null) fullNameError = null
                },
                label = "Full Name",
                placeholder = "Enter your full name",
                isError = fullNameError != null,
                errorMessage = fullNameError,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Person Icon",
                        tint = CyberTextSecondary
                    )
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 2. Email Address
            CyberAwareTextField(
                value = email,
                onValueChange = {
                    email = it
                    if (emailError != null) emailError = null
                },
                label = "Email Address",
                placeholder = "Enter your email",
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

            // 3. Password
            PasswordTextField(
                value = password,
                onValueChange = {
                    password = it
                    if (passwordError != null) passwordError = null
                },
                label = "Password",
                placeholder = "Create a password",
                isError = passwordError != null,
                errorMessage = passwordError
            )

            // Password Strength Indicator
            if (password.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                PasswordStrengthView(strength = passwordStrength)
            } else {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Password must contain at least 6 characters.",
                    fontSize = 12.sp,
                    color = CyberTextSecondary,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Start
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 4. Confirm Password
            PasswordTextField(
                value = confirmPassword,
                onValueChange = {
                    confirmPassword = it
                    if (confirmPasswordError != null) confirmPasswordError = null
                },
                label = "Confirm Password",
                placeholder = "Confirm your password",
                isError = confirmPasswordError != null,
                errorMessage = confirmPasswordError
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Checkbox for Terms and Privacy Policy
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Checkbox(
                        checked = termsAccepted,
                        onCheckedChange = {
                            termsAccepted = it
                            if (termsError != null) termsError = null
                        },
                        colors = CheckboxDefaults.colors(
                            checkedColor = MaterialTheme.colorScheme.primary,
                            uncheckedColor = CyberTextSecondary
                        )
                    )

                    Row(
                        modifier = Modifier.padding(start = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "I agree to the ",
                            fontSize = 13.sp,
                            color = CyberTextSecondary
                        )
                        Text(
                            text = "Terms and Privacy Policy",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.clickable { showTermsDialog = true }
                        )
                    }
                }

                termsError?.let { err ->
                    Text(
                        text = err,
                        color = CyberError,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(start = 12.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // CREATE ACCOUNT Button
            CyberAwareButton(
                text = "CREATE ACCOUNT",
                onClick = { validateAndSignUp() }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Bottom Link to Login
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Already have an account? ",
                    fontSize = 14.sp,
                    color = CyberTextSecondary
                )
                Text(
                    text = "Login",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.clickable { onNavigateToLogin() }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }

    // Terms and Privacy Policy Dialog
    if (showTermsDialog) {
        AlertDialog(
            onDismissRequest = { showTermsDialog = false },
            title = {
                Text(
                    text = "Terms & Privacy Policy",
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            },
            text = {
                Text(
                    text = "CyberAware is committed to protecting your mobile security and personal privacy. Your data remains secure on your device and is never shared with unauthorized third parties.",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            },
            confirmButton = {
                TextButton(onClick = { showTermsDialog = false }) {
                    Text(
                        text = "Close",
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            },
            containerColor = CyberDarkSurface,
            shape = RoundedCornerShape(16.dp)
        )
    }

    // Success Dialog
    if (showSuccessDialog) {
        AlertDialog(
            onDismissRequest = {
                showSuccessDialog = false
                onSignUpSuccess()
            },
            title = {
                Text(
                    text = "Success!",
                    fontWeight = FontWeight.Bold,
                    color = CyberSuccess
                )
            },
            text = {
                Text(
                    text = "Account created successfully! You can now log in with your credentials.",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showSuccessDialog = false
                        onSignUpSuccess()
                    }
                ) {
                    Text(
                        text = "Go to Login",
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

@Composable
fun PasswordStrengthView(strength: PasswordStrength) {
    val (progress, color, label) = when (strength) {
        PasswordStrength.EMPTY -> Triple(0f, Color.Transparent, "")
        PasswordStrength.WEAK -> Triple(0.33f, CyberError, "Weak")
        PasswordStrength.MEDIUM -> Triple(0.66f, CyberWarning, "Medium")
        PasswordStrength.STRONG -> Triple(1.0f, CyberSuccess, "Strong")
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Password Strength:",
                fontSize = 12.sp,
                color = CyberTextSecondary
            )
            Text(
                text = label,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = color
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp)),
            color = color,
            trackColor = CyberDarkSurface
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Password must contain at least 6 characters.",
            fontSize = 11.sp,
            color = CyberTextSecondary
        )
    }
}
