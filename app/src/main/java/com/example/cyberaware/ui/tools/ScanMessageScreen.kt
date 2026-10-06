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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import com.example.cyberaware.domain.MessageAnalysisResult
import com.example.cyberaware.domain.MessageAnalyzer
import com.example.cyberaware.domain.RiskLevel
import com.example.cyberaware.ui.theme.CyberCardBorder
import com.example.cyberaware.ui.theme.CyberDarkSurface
import com.example.cyberaware.ui.theme.CyberError
import com.example.cyberaware.ui.theme.CyberSuccess
import com.example.cyberaware.ui.theme.CyberTextSecondary
import com.example.cyberaware.ui.theme.CyberWarning

@Composable
fun ScanMessageScreen(
    onNavigateBack: () -> Unit
) {
    var messageInput by remember { mutableStateOf("") }
    var inputError by remember { mutableStateOf<String?>(null) }
    var analysisResult by remember { mutableStateOf<MessageAnalysisResult?>(null) }

    fun analyze() {
        if (messageInput.trim().isEmpty()) {
            inputError = "Please enter or paste a message to analyze."
            analysisResult = null
            return
        }
        inputError = null
        analysisResult = MessageAnalyzer.analyzeMessage(messageInput)
    }

    fun clearAll() {
        messageInput = ""
        inputError = null
        analysisResult = null
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
                    text = "Scam Message Detector",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Paste a suspicious SMS, WhatsApp message, email or other message below.",
                fontSize = 14.sp,
                color = CyberTextSecondary,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Input Multiline Field
            Column(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = messageInput,
                    onValueChange = {
                        messageInput = it
                        if (inputError != null) inputError = null
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp),
                    placeholder = {
                        Text(
                            text = "Paste suspicious message here...",
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
                    ),
                    trailingIcon = {
                        if (messageInput.isNotEmpty()) {
                            IconButton(onClick = { clearAll() }) {
                                Icon(
                                    imageVector = Icons.Default.Clear,
                                    contentDescription = "Clear Text",
                                    tint = CyberTextSecondary
                                )
                            }
                        }
                    }
                )

                inputError?.let { err ->
                    Text(
                        text = err,
                        color = CyberError,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(start = 4.dp, top = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (messageInput.isNotEmpty()) {
                    TextButton(
                        onClick = { clearAll() },
                        modifier = Modifier.weight(0.35f)
                    ) {
                        Text(
                            text = "Clear",
                            color = CyberTextSecondary,
                            fontSize = 15.sp
                        )
                    }
                }

                CyberAwareButton(
                    text = "ANALYZE MESSAGE",
                    onClick = { analyze() },
                    modifier = Modifier.weight(1f)
                )
            }

            // Results Section
            analysisResult?.let { result ->
                Spacer(modifier = Modifier.height(24.dp))

                val (badgeColor, badgeText) = when (result.riskLevel) {
                    RiskLevel.HIGH -> Pair(CyberError, "HIGH RISK")
                    RiskLevel.MEDIUM -> Pair(CyberWarning, "MEDIUM RISK")
                    RiskLevel.LOW -> Pair(CyberSuccess, "LOW RISK")
                }

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = CyberDarkSurface),
                    border = BorderStroke(1.5.dp, badgeColor)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "Risk Score",
                                    fontSize = 13.sp,
                                    color = CyberTextSecondary
                                )
                                Text(
                                    text = "${result.riskScore}/100",
                                    fontSize = 28.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = badgeColor
                                )
                            }

                            Card(
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(containerColor = badgeColor.copy(alpha = 0.2f))
                            ) {
                                Text(
                                    text = badgeText,
                                    color = badgeColor,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        Text(
                            text = "Why this message is suspicious:",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        result.indicatorsFound.forEach { indicator ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                verticalAlignment = Alignment.Top
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = badgeColor,
                                    modifier = Modifier.padding(top = 2.dp)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = indicator,
                                    fontSize = 14.sp,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    lineHeight = 20.sp
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        Text(
                            text = "Recommended Action:",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = result.recommendation,
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurface,
                            lineHeight = 20.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
