package com.example.lazycolumn

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.lazycolumn.ui.theme.LazyColumnTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LazyColumnTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Cài đặt hệ thống điều hướng (Navigation)
                    AppNavigation()
                }
            }
        }
    }
}

// --- Cấu trúc điều hướng cho ứng dụng ---
@Composable
fun AppNavigation() {
    // Tạo NavController để quản lý việc chuyển đổi giữa các màn hình
    val navController = rememberNavController()

    // NavHost là container chứa các màn hình (composable) của bạn
    NavHost(navController = navController, startDestination = "welcome") {
        // Route cho màn hình chào mừng
        composable("welcome") {
            WelcomeScreen(navController = navController)
        }
        // Route cho màn hình lựa chọn
        composable("choice") {
            ChoiceScreen(navController = navController)
        }
        // Route cho màn hình danh sách, nhận một tham số là 'type'
        composable("list/{type}") { backStackEntry ->
            // Lấy tham số 'type' từ route
            val listType = backStackEntry.arguments?.getString("type")
            if (listType != null) {
                ListScreen(navController = navController, type = listType)
            }
        }
        // Route cho màn hình chi tiết
        composable("detail") {
            DetailScreen(navController = navController)
        }
    }
}

// --- Màn hình chào mừng ---
@Composable
fun WelcomeScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.jetpackcompose), // Thay my_logo bằng tên file của bạn
            contentDescription = "Jetpack Compose Logo", // Mô tả ảnh cho mục đích hỗ trợ
            modifier = Modifier.size(120.dp)
        )
        Spacer(modifier = Modifier.height(32.dp))
        Text(
            text = "Jetpack Compose",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Jetpack Compose is a modern UI toolkit for building native Android applications using a declarative programming approach.",
            style = MaterialTheme.typography.bodyLarge,
            fontSize = 14.sp,
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(60.dp))
        Button(
            // Khi click, chuyển đến màn hình "choice"
            onClick = { navController.navigate("choice") },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text(text = "I'm ready")
        }
    }
}

// --- Màn hình lựa chọn Column và LazyColumn ---
@Composable
fun ChoiceScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = {
                // Điều hướng đến màn hình list với type là "column"
                navController.navigate("list/column")
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Load 1 Million Items with Column")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                navController.navigate("list/lazycolumn")
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Load 1 Million Items with LazyColumn")

        }
    }
}

// --- Màn hình hiển thị danh sách ---
@Composable
fun ListScreen(navController: NavController, type: String) {
    val context = LocalContext.current
    if (type == "lazycolumn") {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            items(1_000_000) { index ->
                ListItem(index = index, navController = navController)
            }
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()) // Thêm thanh cuộn
                .padding(horizontal = 16.dp)
        ) {
            // Chúng ta chỉ hiển thị 1000 item để tránh crash ngay lập tức
            // nhưng nguyên tắc vẫn áp dụng cho 1 triệu.
            repeat(1000000) { index ->
                ListItem(index = index, navController = navController)
            }
        }
    }
}

// --- Một item trong danh sách ---
@Composable
fun ListItem(index: Int, navController: NavController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable {
                // Khi click, chuyển đến màn hình "detail"
                navController.navigate("detail")
            },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Text(
            text = "Item number ${index + 1}",
            modifier = Modifier.padding(16.dp),
            fontSize = 18.sp
        )
    }
}

// --- Màn hình chi tiết với nút "backroot" ---
@Composable
fun DetailScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Detail Screen",
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(modifier = Modifier.height(32.dp))
        Button(
            onClick = {
                // Quay về màn hình "welcome" và xóa tất cả các màn hình khác khỏi back stack.
                navController.popBackStack("welcome", inclusive = false)
            }
        ) {
            Text("Back to Welcome (Back Root)")
        }
    }
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    LazyColumnTheme {
        // Preview màn hình Welcome để dễ phát triển
        WelcomeScreen(navController = rememberNavController())
    }
}