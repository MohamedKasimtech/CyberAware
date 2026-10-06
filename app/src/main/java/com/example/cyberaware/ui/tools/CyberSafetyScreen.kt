package com.example.cyberaware.ui.tools

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cyberaware.ui.theme.CyberCardBorder
import com.example.cyberaware.ui.theme.CyberDarkSurface
import com.example.cyberaware.ui.theme.CyberError
import com.example.cyberaware.ui.theme.CyberSuccess
import com.example.cyberaware.ui.theme.CyberTextSecondary

data class SafetyTopic(
    val id: Int,
    val title: String,
    val icon: String,
    val shortSummary: String,
    val detailedExplanation: String,
    val redFlags: List<String>,
    val preventionTips: List<String>
)

val CYBER_SAFETY_TOPICS = listOf(
    SafetyTopic(
        id = 1,
        title = "1. Phishing Scams",
        icon = "🎣",
        shortSummary = "Fake emails, SMS messages, or websites designed to steal credentials and financial data.",
        detailedExplanation = "Phishing occurs when scammers impersonate trusted organizations (banks, government agencies, popular services) via email, SMS (Smishing), or phone calls (Vishing) to trick you into revealing passwords, PINs, or card details.",
        redFlags = listOf(
            "Generic greetings like 'Dear Customer'.",
            "Urgent threats to close your account or take legal action.",
            "Links with misspellings or unusual web addresses (e.g., bank-verify.com)."
        ),
        preventionTips = listOf(
            "Never click on links in unsolicited emails or SMS messages.",
            "Verify the sender's email address or official website domain.",
            "Use two-factor authentication (2FA) on all important accounts."
        )
    ),
    SafetyTopic(
        id = 2,
        title = "2. OTP Scams",
        icon = "🔑",
        shortSummary = "Fraudulent attempts to trick you into sharing One-Time Passwords.",
        detailedExplanation = "Scammers call or text posing as bank executives, customer care agents, or delivery personnel claiming they sent a verification code by mistake or need it to resolve an issue.",
        redFlags = listOf(
            "Caller asking you to read out a code received on your phone.",
            "SMS message saying 'Do not share this code' while caller insists on sharing it.",
            "Promising a prize or refund in exchange for an OTP."
        ),
        preventionTips = listOf(
            "NEVER share your OTP with anyone, including bank officials or support staff.",
            "Read the full SMS containing the OTP to check the transaction details.",
            "Disconnect suspicious calls immediately and report the number."
        )
    ),
    SafetyTopic(
        id = 3,
        title = "3. UPI Scams",
        icon = "💸",
        shortSummary = "Tricking victims into entering their UPI PIN to 'receive' money.",
        detailedExplanation = "A common misconception is that entering a UPI PIN is required to receive money. Scammers send a 'Pay Request' or generate a QR code claiming it will transfer money to your account.",
        redFlags = listOf(
            "Buyer or seller asking you to enter your UPI PIN to receive payment.",
            "Receiving a payment request link instead of a direct credit notification.",
            "Overpayment claims where the buyer asks for a refund before cheque clearance."
        ),
        preventionTips = listOf(
            "Remember: You NEVER need to enter your UPI PIN to receive money.",
            "Only enter your UPI PIN when YOU are making a payment.",
            "Verify the recipient name on the payment screen before confirming."
        )
    ),
    SafetyTopic(
        id = 4,
        title = "4. QR Code Scams",
        icon = "📷",
        shortSummary = "Tampered or malicious QR codes leading to phishing or unwanted debits.",
        detailedExplanation = "Scammers paste fraudulent QR stickers over legitimate QR codes at payment counters or send QR codes via chat apps claiming it is for cash prizes or payments.",
        redFlags = listOf(
            "QR code stickers placed over original merchant codes.",
            "Unsolicited QR codes received on WhatsApp or email promising rewards.",
            "QR code scanning leading to an unfamiliar website or app download."
        ),
        preventionTips = listOf(
            "Inspect physical QR stickers for tampering before scanning.",
            "Use CyberAware's QR Scanner to check URL safety before opening links.",
            "Never scan a QR code sent by an unknown online buyer or seller."
        )
    ),
    SafetyTopic(
        id = 5,
        title = "5. Fake Job Scams",
        icon = "💼",
        shortSummary = "Unsolicited high-paying part-time job or task-based offers.",
        detailedExplanation = "Fraudsters send messages offering easy part-time work (e.g., liking YouTube videos, rating hotels) paying thousands per day. After small initial payouts, they demand large 'registration' or 'tax' deposits.",
        redFlags = listOf(
            "High daily pay for minimal effort without any interview or skill requirement.",
            "Communication conducted entirely over Telegram or WhatsApp.",
            "Requirement to pay money upfront for training, equipment, or security deposits."
        ),
        preventionTips = listOf(
            "Legitimate employers never ask candidates to pay money for job offers.",
            "Verify company job listings directly on their official careers page.",
            "Be cautious of task-based earning schemes requiring cryptocurrency or UPI deposits."
        )
    ),
    SafetyTopic(
        id = 6,
        title = "6. Investment Scams",
        icon = "📈",
        shortSummary = "Promises of guaranteed high returns, crypto trading, or forex arbitrage.",
        detailedExplanation = "Scammers create fake trading platforms or WhatsApp groups showing astronomical profits. They allow small initial withdrawals to build trust before stealing all deposited funds.",
        redFlags = listOf(
            "Guaranteed returns with zero risk promises.",
            "Pressure to invest quickly before an 'exclusive opportunity' expires.",
            "Requests to transfer investment funds to personal bank accounts or unverified wallets."
        ),
        preventionTips = listOf(
            "There is no such thing as guaranteed high investment returns without risk.",
            "Only invest through SEBI/SEC registered brokers and official platforms.",
            "Do not join unofficial investment advice groups on WhatsApp or Telegram."
        )
    ),
    SafetyTopic(
        id = 7,
        title = "7. Password Security",
        icon = "🛡️",
        shortSummary = "Weak or reused passwords make accounts vulnerable to credential stuffing.",
        detailedExplanation = "Using simple passwords like '123456' or using the same password across multiple websites allows attackers who breach one website to compromise all your online accounts.",
        redFlags = listOf(
            "Using short passwords under 8 characters.",
            "Reusing the same password for banking, email, and social media.",
            "Storing passwords in plain text in notes apps or messaging apps."
        ),
        preventionTips = listOf(
            "Use complex passphrases with uppercase, lowercase, numbers, and symbols.",
            "Enable Two-Factor Authentication (2FA) using an authenticator app.",
            "Consider using a reputable password manager to generate and store unique passwords."
        )
    ),
    SafetyTopic(
        id = 8,
        title = "8. Social Engineering",
        icon = "🎭",
        shortSummary = "Psychological manipulation to trick victims into breaking security protocols.",
        detailedExplanation = "Attackers research targets on social media to impersonate family members in distress, senior company executives (CEO fraud), or law enforcement (Digital Arrest scams) to demand immediate funds.",
        redFlags = listOf(
            "Urgent calls claiming a family member is arrested or in an emergency.",
            "Callers insisting you remain on a video call ('Digital Arrest') without hanging up.",
            "Demands to send money immediately via gift cards, wire transfer, or UPI."
        ),
        preventionTips = listOf(
            "Stay calm and independently verify emergency claims by calling the person directly.",
            "Police and government agencies never arrest people via Skype/WhatsApp video calls.",
            "Never transfer money based on emotional pressure or threats."
        )
    ),
    SafetyTopic(
        id = 9,
        title = "9. Fake Websites & Domain Spoofing",
        icon = "🌐",
        shortSummary = "Lookalike domain names created to mimic popular portals.",
        detailedExplanation = "Scammers register domain names with slight typos (e.g., paypaI.com with a capital 'i' instead of 'l') or added words (e.g., amazon-security-check.com) to capture login details.",
        redFlags = listOf(
            "Misspelled brand names or unusual top-level domain extensions (.xyz, .top).",
            "Lack of HTTPS padlock icon or browser security warnings.",
            "Broken links, poor formatting, or strange font styles on the website."
        ),
        preventionTips = listOf(
            "Bookmark official banking and shopping websites directly in your browser.",
            "Check the exact domain spelling in the browser address bar before logging in.",
            "Use CyberAware's Link Safety Checker to inspect suspicious URLs."
        )
    ),
    SafetyTopic(
        id = 10,
        title = "10. Public Wi-Fi Safety",
        icon = "📶",
        shortSummary = "Unencrypted public networks expose your internet traffic to eavesdropping.",
        detailedExplanation = "Free public Wi-Fi networks at airports, cafes, or hotel lobbies can be monitored by attackers using packet sniffers or rogue hotspots ('Evil Twin' Wi-Fi) to intercept credentials.",
        redFlags = listOf(
            "Public Wi-Fi networks requiring no password or security splash screen.",
            "Browser warnings about invalid security certificates when connected to public Wi-Fi.",
            "Prompting to install network configuration profiles or certificates to connect."
        ),
        preventionTips = listOf(
            "Avoid accessing online banking or sensitive accounts on public Wi-Fi networks.",
            "Use a secure Virtual Private Network (VPN) when connecting to public hotspots.",
            "Disable 'Auto-Connect to open networks' in your device Wi-Fi settings."
        )
    )
)

@Composable
fun CyberSafetyScreen(
    onNavigateBack: () -> Unit
) {
    var expandedTopicId by remember { mutableStateOf<Int?>(1) } // Topic 1 open by default

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
                    text = "Cyber Safety Guide",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Learn how to spot and protect yourself against common online scams and security threats.",
                fontSize = 14.sp,
                color = CyberTextSecondary,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Topic Cards
            CYBER_SAFETY_TOPICS.forEach { topic ->
                val isExpanded = expandedTopicId == topic.id

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                        .clickable {
                            expandedTopicId = if (isExpanded) null else topic.id
                        },
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = CyberDarkSurface),
                    border = BorderStroke(1.dp, if (isExpanded) MaterialTheme.colorScheme.primary else CyberCardBorder)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    text = topic.icon,
                                    fontSize = 22.sp
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = topic.title,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }

                            Icon(
                                imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                                contentDescription = if (isExpanded) "Collapse" else "Expand",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }

                        if (!isExpanded) {
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = topic.shortSummary,
                                fontSize = 13.sp,
                                color = CyberTextSecondary,
                                lineHeight = 18.sp
                            )
                        }

                        AnimatedVisibility(visible = isExpanded) {
                            Column(modifier = Modifier.padding(top = 16.dp)) {

                                Text(
                                    text = topic.detailedExplanation,
                                    fontSize = 14.sp,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    lineHeight = 20.sp
                                )

                                Spacer(modifier = Modifier.height(16.dp))

                                Text(
                                    text = "🚩 Common Red Flags:",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = CyberError
                                )
                                Spacer(modifier = Modifier.height(6.dp))

                                topic.redFlags.forEach { flag ->
                                    Text(
                                        text = "• $flag",
                                        fontSize = 13.sp,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        modifier = Modifier.padding(start = 6.dp, top = 2.dp),
                                        lineHeight = 18.sp
                                    )
                                }

                                Spacer(modifier = Modifier.height(16.dp))

                                Text(
                                    text = "🛡️ Prevention & Best Practices:",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = CyberSuccess
                                )
                                Spacer(modifier = Modifier.height(6.dp))

                                topic.preventionTips.forEach { tip ->
                                    Text(
                                        text = "✓ $tip",
                                        fontSize = 13.sp,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        modifier = Modifier.padding(start = 6.dp, top = 2.dp),
                                        lineHeight = 18.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
