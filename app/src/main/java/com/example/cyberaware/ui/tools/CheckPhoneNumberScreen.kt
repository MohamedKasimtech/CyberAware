package com.example.cyberaware.ui.tools

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.ReportProblem
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cyberaware.components.CyberAwareButton
import com.example.cyberaware.components.CyberAwareTextField
import com.example.cyberaware.domain.PhoneCheckResult
import com.example.cyberaware.domain.ScamRepository
import com.example.cyberaware.ui.theme.CyberDarkSurface
import com.example.cyberaware.ui.theme.CyberError
import com.example.cyberaware.ui.theme.CyberSuccess
import com.example.cyberaware.ui.theme.CyberTextSecondary
import com.example.cyberaware.ui.theme.CyberWarning

@Composable
fun CheckPhoneNumberScreen(
    onNavigateBack: () -> Unit
) {
    var phoneInput by remember { mutableStateOf("") }
    var inputError by remember { mutableStateOf<String?>(null) }
    var checkResult by remember { mutableStateOf<PhoneCheckResult?>(null) }

    fun checkNumber() {
        if (phoneInput.trim().isEmpty()) {
            inputError = "Please enter a phone number to check."
            checkResult = null
            return
        }
        inputError = null
        checkResult = ScamRepository.checkPhoneNumber(phoneInput)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Spacer(modifier = Modifier.height(12.dp))

            // Header Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onNavigateBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Phone Number Checker",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            CyberAwareTextField(
                value = phoneInput,
                onValueChange = {
                    phoneInput = it
                    if (inputError != null) inputError = null
                },
                label = "Enter Phone Number",
                placeholder = "e.g. +18005550199 or 9876543210",
                isError = inputError != null,
                errorMessage = inputError,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Phone
                ),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Phone,
                        contentDescription = "Phone Icon",
                        tint = CyberTextSecondary
                    )
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            CyberAwareButton(
                text = "CHECK NUMBER",
                onClick = { checkNumber() }
            )

            // Results Section
            checkResult?.let { result ->
                Spacer(modifier = Modifier.height(24.dp))

                val borderColor = if (result.isReported) CyberError else CyberSuccess

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = CyberDarkSurface),
                    border = BorderStroke(1.5.dp, borderColor)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {

                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = if (result.isReported) Icons.Default.ReportProblem else Icons.Default.VerifiedUser,
                                contentDescription = null,
                                tint = borderColor,
                                modifier = Modifier.height(28.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = if (result.isReported) "⚠️ Reported Number" else "No reports found.",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = borderColor
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "Evaluated Number:",
                            fontSize = 12.sp,
                            color = CyberTextSecondary
                        )
                        Text(
                            text = result.rawNumber,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        if (result.isReported) {
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "Total Community Reports: ${result.reportCount}",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = CyberError
                            )

                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Reported Categories:",
                                fontSize = 13.sp,
                                color = CyberTextSecondary
                            )

                            result.categories.forEach { cat ->
                                Text(
                                    text = "• $cat",
                                    fontSize = 14.sp,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    modifier = Modifier.padding(start = 8.dp, top = 2.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Required Disclaimer Box
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background)
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Info,
                                    contentDescription = null,
                                    tint = if (result.isReported) CyberError else CyberWarning
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = result.disclaimer,
                                    fontSize = 13.sp,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    lineHeight = 18.sp
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
