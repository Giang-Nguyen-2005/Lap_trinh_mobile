package com.example.buoi4_btvn02.ui.screens

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Password
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.buoi4_btvn02.data.UserData
import com.example.buoi4_btvn02.ui.BaseScreen

@Composable
fun VerifyCodeScreen(navController: NavHostController, userData: UserData) {
    var code by remember { mutableStateOf("") }

    BaseScreen(
        navController,
        title = "Verify Code",
        subtitle = "Enter the code we just sent to your registered email.",
        showBack = true
    ) {
        OutlinedTextField(
            value = code,
            onValueChange = { if (it.length <= 6) code = it },
            label = { Text("Enter Code") },
            leadingIcon = { Icon(Icons.Default.Password, contentDescription = null) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(24.dp))
        Button(
            onClick = {
                userData.code = code
                navController.navigate("create")
            },
            enabled = code.length == 6,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Next")
        }
    }
}
