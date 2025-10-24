package com.example.buoi4_btvn02.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.buoi4_btvn02.data.UserData
import com.example.buoi4_btvn02.ui.BaseScreen

@Composable
fun ForgetPasswordScreen(navController: NavHostController, userData: UserData) {
    var email by remember { mutableStateOf(userData.email) }
    val isValid = email.contains("@")

    BaseScreen(
        navController,
        title = "Forget Password?",
        subtitle = "Enter your Email, we will send you a verification code."
    ) {
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Your Email") },
            leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(24.dp))
        Button(
            onClick = {
                userData.email = email
                navController.navigate("verify")
            },
            enabled = isValid,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Next")
        }

        // 🔽 Hiển thị lại thông tin nếu người dùng đã hoàn thành các bước trước
        if (userData.password.isNotEmpty()) {
            Spacer(Modifier.height(40.dp))
            Divider()
            Text("🔒 Your Information:", style = MaterialTheme.typography.titleMedium)
            Text("Email: ${userData.email}")
            Text("Code: ${userData.code}")
            Text("Password: ${userData.password}")
        }
    }
}
