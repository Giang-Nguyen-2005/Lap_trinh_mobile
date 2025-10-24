package com.example.buoi4_btvn02.ui.screens

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.buoi4_btvn02.data.UserData
import com.example.buoi4_btvn02.ui.BaseScreen

@Composable
fun CreatePasswordScreen(navController: NavHostController, userData: UserData) {
    var pass by remember { mutableStateOf("") }
    var confirm by remember { mutableStateOf("") }
    var visible1 by remember { mutableStateOf(false) }
    var visible2 by remember { mutableStateOf(false) }

    val valid = pass.isNotBlank() && pass == confirm

    BaseScreen(
        navController,
        title = "Create new password",
        subtitle = "Your new password must be different from the previous one.",
        showBack = true
    ) {
        OutlinedTextField(
            value = pass,
            onValueChange = { pass = it },
            label = { Text("Password") },
            trailingIcon = {
                val icon = if (visible1) Icons.Default.Visibility else Icons.Default.VisibilityOff
                IconButton(onClick = { visible1 = !visible1 }) {
                    Icon(icon, contentDescription = "Toggle Password")
                }
            },
            visualTransformation = if (visible1) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(10.dp))

        OutlinedTextField(
            value = confirm,
            onValueChange = { confirm = it },
            label = { Text("Confirm Password") },
            trailingIcon = {
                val icon = if (visible2) Icons.Default.Visibility else Icons.Default.VisibilityOff
                IconButton(onClick = { visible2 = !visible2 }) {
                    Icon(icon, contentDescription = "Toggle Password")
                }
            },
            visualTransformation = if (visible2) VisualTransformation.None else PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(24.dp))
        Button(
            onClick = {
                userData.password = pass
                navController.navigate("confirm")
            },
            enabled = valid,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Next")
        }
    }
}
