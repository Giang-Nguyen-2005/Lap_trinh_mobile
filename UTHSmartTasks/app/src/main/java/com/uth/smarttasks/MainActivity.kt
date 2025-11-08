package com.uth.smarttaskscompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.uth.smarttasks.DetailScreen
import com.uth.smarttasks.HomeScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {

                    val navController = rememberNavController()

                    // Thiết lập Navigation Graph
                    NavHost(navController = navController, startDestination = "home") {

                        // Màn hình Trang chủ
                        composable("home") {
                            HomeScreen(
                                onNavigateToDetail = { taskId ->
                                    navController.navigate("detail/$taskId")
                                }
                            )
                        }

                        // Màn hình Chi tiết (nhận tham số taskId)
                        composable(
                            route = "detail/{taskId}",
                            arguments = listOf(navArgument("taskId") { type = NavType.StringType })
                        ) { backStackEntry ->
                            val taskId = backStackEntry.arguments?.getString("taskId") ?: return@composable
                            DetailScreen(
                                taskId = taskId,
                                onBack = {
                                    navController.popBackStack()
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}