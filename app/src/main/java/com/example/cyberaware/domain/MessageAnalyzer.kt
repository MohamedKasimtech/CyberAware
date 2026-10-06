package com.example.cyberaware.domain

import java.util.Locale

data class MessageAnalysisResult(
    val messageText: String,
    val riskScore: Int,
    val riskLevel: RiskLevel,
    val indicatorsFound: List<String>,
    val recommendation: String
)

object MessageAnalyzer {

    fun analyzeMessage(text: String): MessageAnalysisResult {
        val trimmed = text.trim()
        if (trimmed.isEmpty()) {
            return MessageAnalysisResult(
                messageText = "",
                riskScore = 0,
                riskLevel = RiskLevel.LOW,
                indicatorsFound = emptyList(),
                recommendation = "Please enter a message to analyze."
            )
        }

        val lower = trimmed.lowercase(Locale.ROOT)
        val indicators = mutableListOf<String>()
        var score = 0

        // 1. OTP / PIN / Password
        if (lower.contains("otp") || lower.contains("one time password") || lower.contains("verification code")) {
            score += 25
            indicators.add("Requests or mentions OTP / Verification Code")
        }
        if (lower.contains("pin") || lower.contains("atm pin") || lower.contains("m-pin") || lower.contains("mpin")) {
            score += 25
            indicators.add("Asks for or mentions PIN / Security PIN")
        }
        if (lower.contains("password") || lower.contains("passcode") || lower.contains("login credentials")) {
            score += 25
            indicators.add("Mentions login passwords or sensitive credentials")
        }

        // 2. Urgent / Immediately / Suspended
        if (lower.contains("urgent") || lower.contains("immediately") || lower.contains("action required") || lower.contains("24 hours") || lower.contains("expire")) {
            score += 20
            indicators.add("Creates artificial urgency or pressure tactics")
        }
        if (lower.contains("account blocked") || lower.contains("suspended") || lower.contains("account locked") || lower.contains("deactivated") || lower.contains("electricity cut")) {
            score += 25
            indicators.add("Threatens account deactivation, blockage, or penalty")
        }

        // 3. KYC / Bank / UPI
        if (lower.contains("kyc") || lower.contains("pan card") || lower.contains("aadhaar") || lower.contains("update kyc")) {
            score += 25
            indicators.add("Requests KYC or personal identity document updates")
        }
        if (lower.contains("bank account") || lower.contains("account number") || lower.contains("bank details")) {
            score += 15
            indicators.add("Mentions bank account details")
        }
        if (lower.contains("upi") || lower.contains("gpay") || lower.contains("phonepe") || lower.contains("paytm")) {
            score += 15
            indicators.add("Refers to UPI or digital wallet payments")
        }

        // 4. Lottery / Winner / Cashback / Investment / Job
        if (lower.contains("lottery") || lower.contains("prize") || lower.contains("winner") || lower.contains("lucky draw") || lower.contains("won")) {
            score += 30
            indicators.add("Unsolicited lottery, prize, or reward claim")
        }
        if (lower.contains("cashback") || lower.contains("refund") || lower.contains("reward points")) {
            score += 20
            indicators.add("Promises unexpected cashback, refund, or monetary reward")
        }
        if (lower.contains("investment") || lower.contains("guaranteed return") || lower.contains("double your money") || lower.contains("crypto profit")) {
            score += 30
            indicators.add("Promises unrealistic guaranteed returns or investment schemes")
        }
        if (lower.contains("job offer") || lower.contains("work from home") || lower.contains("part time job") || lower.contains("earn daily") || lower.contains("salary per day")) {
            score += 20
            indicators.add("Unsolicited work-from-home or high-salary part-time job offer")
        }

        // 5. Links & URLs
        if (lower.contains("http://") || lower.contains("https://") || lower.contains("bit.ly") || lower.contains("t.co") || lower.contains("click here") || lower.contains("tap link") || lower.contains("visit link")) {
            score += 25
            indicators.add("Contains external link or request to click a URL")
        }

        // 6. Payment requests / Money Transfer / Impersonation
        if (lower.contains("payment request") || lower.contains("money transfer") || lower.contains("send money") || lower.contains("pay now") || lower.contains("transfer Rs")) {
            score += 25
            indicators.add("Direct request for money transfer or payment")
        }
        if (lower.contains("customs") || lower.contains("courier") || lower.contains("parcel") || lower.contains("police") || lower.contains("traffic fine") || lower.contains("challan") || lower.contains("manager")) {
            score += 20
            indicators.add("Impersonates official authority, courier, or official department")
        }

        val finalScore = score.coerceIn(0, 100)

        val level = when {
            finalScore >= 60 -> RiskLevel.HIGH
            finalScore >= 30 -> RiskLevel.MEDIUM
            else -> RiskLevel.LOW
        }

        val recommendation = when (level) {
            RiskLevel.HIGH -> "HIGH SCAM RISK: Do NOT share any OTP, PIN, or banking details. Do NOT click links or transfer money. Report and block the sender."
            RiskLevel.MEDIUM -> "SUSPICIOUS MESSAGE: Proceed with extreme caution. Verify the claims independently through official phone numbers or apps."
            RiskLevel.LOW -> "LOW RISK DETECTED: No obvious scam indicators found. Always stay vigilant when sharing sensitive information."
        }

        return MessageAnalysisResult(
            messageText = trimmed,
            riskScore = finalScore,
            riskLevel = level,
            indicatorsFound = if (indicators.isEmpty()) listOf("No suspicious keywords or patterns detected.") else indicators,
            recommendation = recommendation
        )
    }
}
