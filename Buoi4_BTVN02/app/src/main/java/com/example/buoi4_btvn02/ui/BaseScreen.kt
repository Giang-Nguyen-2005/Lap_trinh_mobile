package com.example.buoi4_btvn02.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.buoi4_btvn02.R

// BaseScreen: lớp cha cho tất cả màn hình (áp dụng OOP - Kế thừa hành vi chung)
@Composable
fun BaseScreen(
    navController: NavHostController,
    title: String,
    subtitle: String,
    showBack: Boolean = false,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (showBack) {
            IconButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier.align(Alignment.Start)
            ) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
            }
        }

        Image(
            painter = painterResource(R.drawable.logo_uth),
            contentDescription = "UTH Logo",
            modifier = Modifier.size(100.dp)
        )

        Text("SmartTasks", fontSize = 22.sp, color = MaterialTheme.colorScheme.primary)
        Spacer(Modifier.height(16.dp))
        Text(title, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Text(subtitle, fontSize = 14.sp)

        Spacer(Modifier.height(20.dp))
        content()
    }
}
