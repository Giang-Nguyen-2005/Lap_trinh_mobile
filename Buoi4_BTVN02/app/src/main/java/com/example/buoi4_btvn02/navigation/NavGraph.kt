package com.example.buoi4_btvn02.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.buoi4_btvn02.data.UserData
import com.example.buoi4_btvn02.ui.screens.*

/**
 * ✅ Áp dụng OOP:
 * - Dùng sealed class để quản lý route (đảm bảo type-safe, dễ mở rộng)
 * - NavGraph là lớp chịu trách nhiệm điều hướng, tách biệt với giao diện
 * - Giúp tuân theo nguyên tắc SRP (Single Responsibility Principle)
 */

sealed class Screen(val route: String) {
    data object ForgetPassword : Screen("forget")
    data object VerifyCode : Screen("verify")
    data object CreatePassword : Screen("create")
    data object Confirm : Screen("confirm")
}

@Composable
fun AppNavGraph(navController: NavHostController, userData: UserData) {
    NavHost(
        navController = navController,
        startDestination = Screen.ForgetPassword.route
    ) {
        composable(Screen.ForgetPassword.route) {
            ForgetPasswordScreen(navController, userData)
        }
        composable(Screen.VerifyCode.route) {
            VerifyCodeScreen(navController, userData)
        }
        composable(Screen.CreatePassword.route) {
            CreatePasswordScreen(navController, userData)
        }
        composable(Screen.Confirm.route) {
            ConfirmScreen(navController, userData)
        }
    }
}
