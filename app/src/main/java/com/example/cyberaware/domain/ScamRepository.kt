package com.example.cyberaware.domain

data class ScamReport(
    val id: String,
    val type: String, // Phone Number, Website, Message, QR Code, Other
    val reportedValue: String,
    val description: String,
    val timestamp: Long = System.currentTimeMillis(),
    val categories: List<String> = listOf("Community Scam Report"),
    val isVerifiedByPolice: Boolean = false
)

data class PhoneCheckResult(
    val rawNumber: String,
    val normalizedNumber: String,
    val isReported: Boolean,
    val reportCount: Int,
    val categories: List<String>,
    val recentReportDates: List<String>,
    val disclaimer: String
)

object ScamRepository {

    private val localReports = mutableListOf(
        ScamReport(
            id = "rep_1",
            type = "Phone Number",
            reportedValue = "+18005550199",
            description = "Posing as bank support demanding OTP for account unlock.",
            categories = listOf("Bank Impersonation", "OTP Fraud"),
            isVerifiedByPolice = true
        ),
        ScamReport(
            id = "rep_2",
            type = "Phone Number",
            reportedValue = "+919876543210",
            description = "Fake courier parcel customs fee request via SMS and call.",
            categories = listOf("Courier Customs Scam", "Phishing SMS")
        ),
        ScamReport(
            id = "rep_3",
            type = "Website",
            reportedValue = "fake-banking-login.xyz",
            description = "Phishing clone of bank login page asking for passwords.",
            categories = listOf("Domain Spoofing", "Credential Harvesting"),
            isVerifiedByPolice = true
        ),
        ScamReport(
            id = "rep_4",
            type = "Phone Number",
            reportedValue = "+18889990000",
            description = "Automated robocall claiming electricity disconnect in 1 hour.",
            categories = listOf("Utility Bill Scam", "Urgent Robocall")
        )
    )

    fun normalizePhoneNumber(phone: String): String {
        return phone.filter { it.isDigit() || it == '+' }
    }

    fun getAllReports(): List<ScamReport> = localReports.toList()

    fun checkPhoneNumber(rawNumber: String): PhoneCheckResult {
        val normalized = normalizePhoneNumber(rawNumber)
        if (normalized.isEmpty()) {
            return PhoneCheckResult(
                rawNumber = rawNumber,
                normalizedNumber = "",
                isReported = false,
                reportCount = 0,
                categories = emptyList(),
                recentReportDates = emptyList(),
                disclaimer = "Please enter a valid phone number."
            )
        }

        val matchingReports = localReports.filter {
            it.type == "Phone Number" &&
                    (normalizePhoneNumber(it.reportedValue) == normalized ||
                            normalized.endsWith(normalizePhoneNumber(it.reportedValue).takeLast(10)))
        }

        return if (matchingReports.isNotEmpty()) {
            val categories = matchingReports.flatMap { it.categories }.distinct()
            val policeVerified = matchingReports.any { it.isVerifiedByPolice }
            val disclaimerText = if (policeVerified) {
                "🚨 Verified Scam Number (Cyber Police Warning). Do NOT respond!"
            } else {
                "⚠️ Reported Number by Community. Exercise extreme caution!"
            }

            PhoneCheckResult(
                rawNumber = rawNumber,
                normalizedNumber = normalized,
                isReported = true,
                reportCount = matchingReports.size,
                categories = categories,
                recentReportDates = listOf("Recently Reported"),
                disclaimer = disclaimerText
            )
        } else {
            PhoneCheckResult(
                rawNumber = rawNumber,
                normalizedNumber = normalized,
                isReported = false,
                reportCount = 0,
                categories = emptyList(),
                recentReportDates = emptyList(),
                disclaimer = "No reports were found for this number. This does not guarantee that the number is safe."
            )
        }
    }

    fun submitReport(
        type: String,
        reportedValue: String,
        description: String,
        isPoliceVerified: Boolean = false
    ): Boolean {
        if (reportedValue.isBlank() || description.isBlank()) return false

        val newReport = ScamReport(
            id = "rep_${System.currentTimeMillis()}",
            type = type,
            reportedValue = reportedValue.trim(),
            description = description.trim(),
            categories = if (isPoliceVerified) listOf("Cyber Police Alert", type) else listOf("Community Scam Report", type),
            isVerifiedByPolice = isPoliceVerified
        )
        localReports.add(0, newReport)
        return true
    }

    fun deleteReport(id: String): Boolean {
        return localReports.removeIf { it.id == id }
    }
}
