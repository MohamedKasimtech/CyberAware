package com.example.cyberaware.ui.tools

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Report
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cyberaware.components.CyberAwareButton
import com.example.cyberaware.components.CyberAwareTextField
import com.example.cyberaware.domain.ScamRepository
import com.example.cyberaware.ui.theme.CyberCardBorder
import com.example.cyberaware.ui.theme.CyberDarkSurface
import com.example.cyberaware.ui.theme.CyberError
import com.example.cyberaware.ui.theme.CyberSuccess
import com.example.cyberaware.ui.theme.CyberTextSecondary

@Composable
fun ReportScamScreen(
    onNavigateBack: () -> Unit
) {
    val scamTypes = listOf("Phone Number", "Website", "Message", "QR Code", "Other")
    var selectedType by remember { mutableStateOf(scamTypes[0]) }
    var isTypeMenuExpanded by remember { mutableStateOf(false) }

    var reportedValue by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    var valueError by remember { mutableStateOf<String?>(null) }
    var descriptionError by remember { mutableStateOf<String?>(null) }

    var showSuccessDialog by remember { mutableStateOf(false) }

    fun submitReport() {
        valueError = null
        descriptionError = null

        var isValid = true

        if (reportedValue.trim().isEmpty()) {
            valueError = "Reported value (phone, URL, or sender) is required."
            isValid = false
        }

        if (description.trim().isEmpty()) {
            descriptionError = "Please enter a brief description of the scam."
            isValid = false
        } else if (description.trim().length < 10) {
            descriptionError = "Description should be at least 10 characters."
            isValid = false
        }

        if (isValid) {
            val success = ScamRepository.submitReport(
                type = selectedType,
                reportedValue = reportedValue,
                description = description
            )
            if (success) {
                showSuccessDialog = true
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
                    text = "Report a Scam",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 1. Select Scam Type
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Scam Type",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(bottom = 6.dp)
                )

                Box(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = selectedType,
                        onValueChange = {},
                        readOnly = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { isTypeMenuExpanded = true },
                        trailingIcon = {
                            IconButton(onClick = { isTypeMenuExpanded = !isTypeMenuExpanded }) {
                                Icon(
                                    imageVector = Icons.Default.ArrowDropDown,
                                    contentDescription = "Dropdown",
                                    tint = CyberTextSecondary
                                )
                            }
                        },
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = CyberDarkSurface,
                            unfocusedContainerColor = CyberDarkSurface,
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = CyberCardBorder,
                            focusedTextColor = MaterialTheme.colorScheme.onSurface,
                            unfocusedTextColor = MaterialTheme.colorScheme.onSurface
                        )
                    )

                    DropdownMenu(
                        expanded = isTypeMenuExpanded,
                        onDismissRequest = { isTypeMenuExpanded = false },
                        modifier = Modifier
                            .fillMaxWidth(0.85f)
                            .background(CyberDarkSurface)
                    ) {
                        scamTypes.forEach { type ->
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = type,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                },
                                onClick = {
                                    selectedType = type
                                    isTypeMenuExpanded = false
                                }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 2. Reported Value
            CyberAwareTextField(
                value = reportedValue,
                onValueChange = {
                    reportedValue = it
                    if (valueError != null) valueError = null
                },
                label = "Reported Value",
                placeholder = when (selectedType) {
                    "Phone Number" -> "e.g. +18005550199"
                    "Website" -> "e.g. fake-banking-login.xyz"
                    "Message" -> "e.g. Sender ID or SMS heading"
                    else -> "e.g. Scammer ID, Link, or Details"
                },
                isError = valueError != null,
                errorMessage = valueError,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Report,
                        contentDescription = null,
                        tint = CyberTextSecondary
                    )
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 3. Description
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Scam Description",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(bottom = 6.dp)
                )

                OutlinedTextField(
                    value = description,
                    onValueChange = {
                        description = it
                        if (descriptionError != null) descriptionError = null
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(130.dp),
                    placeholder = {
                        Text(
                            text = "Describe how the scam occurred, message contents, or demands made...",
                            color = CyberTextSecondary,
                            fontSize = 14.sp
                        )
                    },
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = CyberDarkSurface,
                        unfocusedContainerColor = CyberDarkSurface,
                        errorContainerColor = CyberDarkSurface,
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = CyberCardBorder,
                        errorBorderColor = CyberError,
                        focusedTextColor = MaterialTheme.colorScheme.onSurface,
                        unfocusedTextColor = MaterialTheme.colorScheme.onSurface
                    )
                )

                descriptionError?.let { err ->
                    Text(
                        text = err,
                        color = CyberError,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(start = 4.dp, top = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            CyberAwareButton(
                text = "SUBMIT REPORT",
                onClick = { submitReport() }
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
    }

    if (showSuccessDialog) {
        AlertDialog(
            onDismissRequest = {
                showSuccessDialog = false
                onNavigateBack()
            },
            title = {
                Text(
                    text = "Report Submitted",
                    fontWeight = FontWeight.Bold,
                    color = CyberSuccess
                )
            },
            text = {
                Text(
                    text = "Thank you! Your report has been recorded to help protect the CyberAware community.",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    showSuccessDialog = false
                    onNavigateBack()
                }) {
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
