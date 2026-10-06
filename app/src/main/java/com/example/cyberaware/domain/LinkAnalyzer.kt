package com.example.cyberaware.domain

import android.util.Patterns
import java.net.URI
import java.util.Locale

enum class RiskLevel {
    LOW, MEDIUM, HIGH
}

data class LinkAnalysisResult(
    val url: String,
    val isValidUrl: Boolean,
    val riskScore: Int,
    val riskLevel: RiskLevel,
    val warnings: List<String>,
    val recommendation: String,
    val disclaimer: String = "This is a preliminary security check and cannot guarantee that a website is safe."
)

object LinkAnalyzer {

    private val SUSPICIOUS_KEYWORDS = listOf(
        "login", "verify", "account", "bank", "update", "secure", "free",
        "bonus", "claim", "support", "wallet", "signin", "credential",
        "paypal", "appleid", "microsoft", "google", "netflix", "amazon", "kyc"
    )

    private val SUSPICIOUS_TLDS = listOf(
        ".xyz", ".top", ".work", ".click", ".link", ".loan", ".zip",
        ".mov", ".cc", ".tk", ".ga", ".cf", ".gq", ".ml"
    )

    private val URL_SHORTENERS = listOf(
        "bit.ly", "tinyurl.com", "t.co", "is.gd", "cutt.ly", "rb.gy",
        "goo.gl", "shorturl.at"
    )

    fun analyzeUrl(rawUrl: String): LinkAnalysisResult {
        val trimmedUrl = rawUrl.trim()
        if (trimmedUrl.isEmpty()) {
            return LinkAnalysisResult(
                url = rawUrl,
                isValidUrl = false,
                riskScore = 0,
                riskLevel = RiskLevel.LOW,
                warnings = listOf("URL input is empty."),
                recommendation = "Please enter a valid website URL to analyze."
            )
        }

        val formattedUrl = if (!trimmedUrl.startsWith("http://") && !trimmedUrl.startsWith("https://")) {
            "https://$trimmedUrl"
        } else {
            trimmedUrl
        }

        val isValid = Patterns.WEB_URL.matcher(formattedUrl).matches()
        if (!isValid) {
            return LinkAnalysisResult(
                url = trimmedUrl,
                isValidUrl = false,
                riskScore = 80,
                riskLevel = RiskLevel.HIGH,
                warnings = listOf("Invalid or malformed URL structure."),
                recommendation = "Do not open this link. The URL structure is malformed or invalid."
            )
        }

        var score = 0
        val warnings = mutableListOf<String>()

        val lowerUrl = formattedUrl.lowercase(Locale.ROOT)

        // 1. Protocol check
        if (lowerUrl.startsWith("http://")) {
            score += 20
            warnings.add("Unencrypted HTTP connection (lacks SSL/HTTPS encryption).")
        }

        // Parse URI details
        try {
            val uri = URI(formattedUrl)
            val host = uri.host?.lowercase(Locale.ROOT) ?: ""

            // 2. Raw IP address check
            val ipRegex = Regex("^\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}$")
            if (ipRegex.matches(host)) {
                score += 35
                warnings.add("Uses raw IP address instead of a domain name (common phishing indicator).")
            }

            // 3. '@' symbol check
            if (formattedUrl.contains("@")) {
                score += 40
                warnings.add("Contains '@' symbol used to obscure the true destination host.")
            }

            // 4. Excessive subdomains check
            val dotCount = host.count { it == '.' }
            if (dotCount > 3) {
                score += 25
                warnings.add("Multiple subdomains detected ($dotCount dots) - potential domain spoofing.")
            }

            // 5. Hyphen count check in host
            val hyphenCount = host.count { it == '-' }
            if (hyphenCount >= 3) {
                score += 15
                warnings.add("Excessive hyphens in domain name (often mimics legitimate brands).")
            }

            // 6. URL shortener check
            if (URL_SHORTENERS.any { host.endsWith(it) || host == it }) {
                score += 25
                warnings.add("Uses a URL shortener which masks the final destination website.")
            }

            // 7. Suspicious TLD check
            SUSPICIOUS_TLDS.forEach { tld ->
                if (host.endsWith(tld)) {
                    score += 20
                    warnings.add("Uses high-risk top-level domain ($tld) frequently linked to scams.")
                }
            }

            // 8. Suspicious keywords check in path or host
            val matchedKeywords = SUSPICIOUS_KEYWORDS.filter { lowerUrl.contains(it) }
            if (matchedKeywords.isNotEmpty()) {
                val kwString = matchedKeywords.take(3).joinToString(", ")
                score += 15 * matchedKeywords.size.coerceAtMost(2)
                warnings.add("Contains sensitive keywords: [$kwString].")
            }

        } catch (e: Exception) {
            score += 25
            warnings.add("Complex or unusual URL structure.")
        }

        // 9. Length check
        if (formattedUrl.length > 75) {
            score += 15
            warnings.add("Excessively long URL (${formattedUrl.length} characters).")
        }

        val finalScore = score.coerceIn(0, 100)

        val level = when {
            finalScore >= 60 -> RiskLevel.HIGH
            finalScore >= 30 -> RiskLevel.MEDIUM
            else -> RiskLevel.LOW
        }

        if (warnings.isEmpty()) {
            warnings.add("HTTPS enabled and standard domain structure verified.")
        }

        val recommendation = when (level) {
            RiskLevel.HIGH -> "HIGH RISK: Do not enter personal credentials, credit card info, or passwords on this site."
            RiskLevel.MEDIUM -> "MEDIUM RISK: Exercise caution. Verify the domain carefully before proceeding."
            RiskLevel.LOW -> "LOW RISK: Standard checks passed. Always stay alert when sharing sensitive data."
        }

        return LinkAnalysisResult(
            url = formattedUrl,
            isValidUrl = true,
            riskScore = finalScore,
            riskLevel = level,
            warnings = warnings,
            recommendation = recommendation
        )
    }
}
