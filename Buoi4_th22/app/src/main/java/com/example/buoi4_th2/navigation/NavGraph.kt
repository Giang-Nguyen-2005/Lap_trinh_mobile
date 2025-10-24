package com.example.buoi4_th2.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.buoi4_th2.ui.OnboardScreen
import com.example.buoi4_th2.ui.SplashScreen

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Onboard : Screen("onboard")
}

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Screen.Splash.route) {
        composable(Screen.Splash.route) {
            SplashScreen(navController)
        }
        composable(Screen.Onboard.route) {
            OnboardScreen(navController)
        }

    }
}
