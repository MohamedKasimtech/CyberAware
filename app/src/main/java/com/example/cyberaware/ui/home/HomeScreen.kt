package com.example.cyberaware.ui.home

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
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.LocalPolice
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cyberaware.components.CyberAwareButton
import com.example.cyberaware.components.FeatureCard
import com.example.cyberaware.domain.ScamRepository
import com.example.cyberaware.domain.UserRole
import com.example.cyberaware.ui.theme.CyberCardBorder
import com.example.cyberaware.ui.theme.CyberDarkSurface
import com.example.cyberaware.ui.theme.CyberError
import com.example.cyberaware.ui.theme.CyberSuccess
import com.example.cyberaware.ui.theme.CyberTextSecondary

@Composable
fun HomeScreen(
    role: UserRole = UserRole.USER,
    onLogout: () -> Unit,
    onNavigateToScanMessage: () -> Unit,
    onNavigateToCheckLink: () -> Unit,
    onNavigateToScanQr: () -> Unit,
    onNavigateToCheckPhone: () -> Unit,
    onNavigateToReportScam: () -> Unit,
    onNavigateToCyberSafety: () -> Unit
) {
    var showPoliceFeedDialog by remember { mutableStateOf(false) }
    var reportsList by remember { mutableStateOf(ScamRepository.getAllReports()) }

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

            // Top Header Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "🛡️ CyberAware",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = if (role == UserRole.CYBER_POLICE) "🚔 Cyber Police Portal" else "Stay Safe. Stay Aware.",
                        fontSize = 14.sp,
                        color = if (role == UserRole.CYBER_POLICE) MaterialTheme.colorScheme.primary else CyberTextSecondary,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                IconButton(onClick = onLogout) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                        contentDescription = "Logout",
                        tint = CyberError
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // User / Police Role Banner
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = CyberDarkSurface),
                border = BorderStroke(1.dp, if (role == UserRole.CYBER_POLICE) MaterialTheme.colorScheme.primary else CyberCardBorder)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = if (role == UserRole.CYBER_POLICE) Icons.Default.LocalPolice else Icons.Default.VerifiedUser,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.height(28.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = if (role == UserRole.CYBER_POLICE) "Cyber Police Officer" else "Standard User Account",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = if (role == UserRole.CYBER_POLICE)
                            "You are logged in with Cyber Police authority. You can moderate community reports, issue official warnings, and verify threats."
                        else
                            "Check suspicious messages, links, QR codes and phone numbers before trusting them.",
                        fontSize = 14.sp,
                        color = CyberTextSecondary,
                        lineHeight = 20.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Cyber Police Specific Actions
            if (role == UserRole.CYBER_POLICE) {
                Text(
                    text = "Police Command Center",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    textAlign = TextAlign.Start
                )

                FeatureCard(
                    iconText = "🚔",
                    title = "Review Community Scam Reports (${reportsList.size})",
                    onClick = {
                        reportsList = ScamRepository.getAllReports()
                        showPoliceFeedDialog = true
                    }
                )

                FeatureCard(
                    iconText = "🚨",
                    title = "Publish Official Cyber Police Alert",
                    onClick = onNavigateToReportScam
                )

                Spacer(modifier = Modifier.height(16.dp))
            }

            Text(
                text = "Security Tools",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                textAlign = TextAlign.Start
            )

            // Feature Cards
            FeatureCard(
                iconText = "🔍",
                title = "Scan Message",
                onClick = onNavigateToScanMessage
            )

            FeatureCard(
                iconText = "🔗",
                title = "Check Link",
                onClick = onNavigateToCheckLink
            )

            FeatureCard(
                iconText = "📷",
                title = "Scan QR Code",
                onClick = onNavigateToScanQr
            )

            FeatureCard(
                iconText = "📞",
                title = "Check Phone Number",
                onClick = onNavigateToCheckPhone
            )

            FeatureCard(
                iconText = "🚨",
                title = "Report a Scam",
                onClick = onNavigateToReportScam
            )

            FeatureCard(
                iconText = "📚",
                title = "Cyber Safety",
                onClick = onNavigateToCyberSafety
            )

            Spacer(modifier = Modifier.height(24.dp))

            CyberAwareButton(
                text = "Logout",
                onClick = onLogout,
                containerColor = CyberDarkSurface,
                contentColor = CyberError
            )

            Spacer(modifier = Modifier.height(16.dp))
        }
    }

    // Cyber Police Report Moderation Dialog
    if (showPoliceFeedDialog) {
        AlertDialog(
            onDismissRequest = { showPoliceFeedDialog = false },
            title = {
                Text(
                    text = "🚔 Community Scam Reports",
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(350.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    if (reportsList.isEmpty()) {
                        Text(
                            text = "No scam reports found.",
                            color = CyberTextSecondary,
                            fontSize = 14.sp
                        )
                    } else {
                        reportsList.forEach { rep ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background)
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "${rep.type}: ${rep.reportedValue}",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 14.sp,
                                            color = MaterialTheme.colorScheme.primary
                                        )

                                        IconButton(onClick = {
                                            ScamRepository.deleteReport(rep.id)
                                            reportsList = ScamRepository.getAllReports()
                                        }) {
                                            Icon(
                                                imageVector = Icons.Default.Delete,
                                                contentDescription = "Dismiss Report",
                                                tint = CyberError,
                                                modifier = Modifier.height(20.dp)
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(4.dp))

                                    Text(
                                        text = rep.description,
                                        fontSize = 13.sp,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )

                                    if (rep.isVerifiedByPolice) {
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = "✓ Verified Police Alert",
                                            fontSize = 11.sp,
                                            color = CyberSuccess,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showPoliceFeedDialog = false }) {
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
}
