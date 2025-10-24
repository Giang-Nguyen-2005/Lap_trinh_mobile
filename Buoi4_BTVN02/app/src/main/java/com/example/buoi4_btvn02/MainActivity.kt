package com.example.buoi4_btvn02

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.navigation.compose.rememberNavController
import com.example.buoi4_btvn02.data.UserData
import com.example.buoi4_btvn02.navigation.AppNavGraph

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val userData = UserData()

            MaterialTheme {
                AppNavGraph(navController, userData)
            }
        }
    }
}
