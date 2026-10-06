package com.example.cyberaware.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.cyberaware.domain.UserRole
import com.example.cyberaware.ui.home.HomeScreen
import com.example.cyberaware.ui.login.LoginScreen
import com.example.cyberaware.ui.signup.SignUpScreen
import com.example.cyberaware.ui.tools.CheckLinkScreen
import com.example.cyberaware.ui.tools.CheckPhoneNumberScreen
import com.example.cyberaware.ui.tools.CyberSafetyScreen
import com.example.cyberaware.ui.tools.ReportScamScreen
import com.example.cyberaware.ui.tools.ScanMessageScreen
import com.example.cyberaware.ui.tools.ScanQrScreen

object ScreenRoutes {
    const val LOGIN = "login"
    const val SIGNUP = "signup"
    const val HOME = "home"
    const val SCAN_MESSAGE = "scan_message"
    const val CHECK_LINK = "check_link"
    const val SCAN_QR = "scan_qr"
    const val CHECK_PHONE = "check_phone"
    const val REPORT_SCAM = "report_scam"
    const val CYBER_SAFETY = "cyber_safety"
}

@Composable
fun AppNavigation(
    navController: NavHostController = rememberNavController()
) {
    var currentUserRole by remember { mutableStateOf(UserRole.USER) }

    NavHost(
        navController = navController,
        startDestination = ScreenRoutes.LOGIN
    ) {
        composable(ScreenRoutes.LOGIN) {
            LoginScreen(
                onLoginSuccess = { role ->
                    currentUserRole = role
                    navController.navigate(ScreenRoutes.HOME) {
                        popUpTo(ScreenRoutes.LOGIN) { inclusive = true }
                    }
                },
                onNavigateToSignUp = {
                    navController.navigate(ScreenRoutes.SIGNUP)
                }
            )
        }

        composable(ScreenRoutes.SIGNUP) {
            SignUpScreen(
                onSignUpSuccess = {
                    navController.navigate(ScreenRoutes.LOGIN) {
                        popUpTo(ScreenRoutes.SIGNUP) { inclusive = true }
                    }
                },
                onNavigateToLogin = {
                    navController.popBackStack()
                }
            )
        }

        composable(ScreenRoutes.HOME) {
            HomeScreen(
                role = currentUserRole,
                onLogout = {
                    navController.navigate(ScreenRoutes.LOGIN) {
                        popUpTo(ScreenRoutes.HOME) { inclusive = true }
                    }
                },
                onNavigateToScanMessage = {
                    navController.navigate(ScreenRoutes.SCAN_MESSAGE)
                },
                onNavigateToCheckLink = {
                    navController.navigate(ScreenRoutes.CHECK_LINK)
                },
                onNavigateToScanQr = {
                    navController.navigate(ScreenRoutes.SCAN_QR)
                },
                onNavigateToCheckPhone = {
                    navController.navigate(ScreenRoutes.CHECK_PHONE)
                },
                onNavigateToReportScam = {
                    navController.navigate(ScreenRoutes.REPORT_SCAM)
                },
                onNavigateToCyberSafety = {
                    navController.navigate(ScreenRoutes.CYBER_SAFETY)
                }
            )
        }

        composable(ScreenRoutes.SCAN_MESSAGE) {
            ScanMessageScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(ScreenRoutes.CHECK_LINK) {
            CheckLinkScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(ScreenRoutes.SCAN_QR) {
            ScanQrScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(ScreenRoutes.CHECK_PHONE) {
            CheckPhoneNumberScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(ScreenRoutes.REPORT_SCAM) {
            ReportScamScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(ScreenRoutes.CYBER_SAFETY) {
            CyberSafetyScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
