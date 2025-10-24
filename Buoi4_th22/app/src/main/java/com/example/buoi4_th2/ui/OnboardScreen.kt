package com.example.buoi4_th2.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.buoi4_th2.data.onboardPages
import com.example.buoi4_th2.navigation.Screen

@Composable
fun OnboardScreen(navController: NavController) {
    var currentPage by remember { mutableStateOf(0) }
    val page = onboardPages[currentPage]

    Box(modifier = Modifier.fillMaxSize()) {

        // Nút Skip ở góc phải
        if (currentPage < onboardPages.size - 1) {
            TextButton(
                onClick = {
                    // Quay lại màn hình ban đầu
                    navController.navigate(Screen.Splash.route) {
                        popUpTo(Screen.Onboard.route) { inclusive = true }
                    }
                },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 32.dp, end = 16.dp)
            ) {
                Text("Skip", color = Color.Gray)
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Ảnh minh họa
            Image(
                painter = painterResource(id = page.imageRes),
                contentDescription = page.title,
                modifier = Modifier
                    .height(150.dp)
                    .fillMaxWidth()
            )

            // Tiêu đề + mô tả
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = page.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp,
                    color = Color(0xFF333333)
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = page.description,
                    fontSize = 16.sp,
                    color = Color.Gray,
                    lineHeight = 22.sp
                )
            }

            // Indicator (chấm tròn)
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                repeat(onboardPages.size) { index ->
                    Box(
                        modifier = Modifier
                            .padding(4.dp)
                            .size(if (index == currentPage) 12.dp else 8.dp)
                            .clip(CircleShape)
                            .background(
                                if (index == currentPage) Color(0xFF1E88E5)
                                else Color.LightGray
                            )
                    )
                }
            }

            // Nút điều khiển
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                if (currentPage > 0) {
                    OutlinedButton(
                        onClick = { currentPage-- },
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF1E88E5))
                    ) {
                        Text("Back")
                    }
                } else {
                    Spacer(modifier = Modifier.width(80.dp))
                }

                Button(
                    onClick = {
                        if (currentPage < onboardPages.size - 1) {
                            currentPage++
                        } else {
                            // Get Started → quay lại Splash
                            navController.navigate(Screen.Splash.route) {
                                popUpTo(Screen.Onboard.route) { inclusive = true }
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF1E88E5),
                        contentColor = Color.White
                    )
                ) {
                    Text(if (currentPage == onboardPages.size - 1) "Get Started" else "Next")
                }
            }
        }
    }
}
