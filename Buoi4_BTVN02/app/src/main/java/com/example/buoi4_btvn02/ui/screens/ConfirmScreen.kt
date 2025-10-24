package com.example.buoi4_btvn02.ui.screens

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.buoi4_btvn02.data.UserData
import com.example.buoi4_btvn02.ui.BaseScreen

@Composable
fun ConfirmScreen(navController: NavHostController, userData: UserData) {
    BaseScreen(
        navController,
        title = "Confirm",
        subtitle = "We are here to help you!",
        showBack = true
    ) {
        OutlinedTextField(
            value = userData.email,
            onValueChange = {},
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            enabled = false
        )
        OutlinedTextField(
            value = userData.code,
            onValueChange = {},
            label = { Text("Code") },
            modifier = Modifier.fillMaxWidth(),
            enabled = false
        )
        OutlinedTextField(
            value = userData.password,
            onValueChange = {},
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth(),
            enabled = false
        )

        Spacer(Modifier.height(24.dp))
        Button(
            onClick = {
                // Điều hướng về màn ForgetPassword và hiển thị thông tin đã nhập
                navController.navigate("forget") {
                    popUpTo("confirm") { inclusive = true }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Submit")
        }
    }
}
