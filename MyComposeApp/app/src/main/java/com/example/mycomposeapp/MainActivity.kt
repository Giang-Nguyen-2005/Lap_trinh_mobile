package com.example.mycomposeapp

import android.app.Activity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource // Import để sử dụng painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import coil.compose.rememberAsyncImagePainter // Import Coil
import kotlinx.coroutines.launch
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

// --- Các hằng số cho việc điều hướng ---
object Routes {
    const val LOGIN = "login"
    const val PROFILE = "profile"
}

// --- Activity Chính ---
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MyAppNavigation()
                }
            }
        }
    }
}

// --- Bộ điều hướng chính của ứng dụng ---
@Composable
fun MyAppNavigation() {
    val navController = rememberNavController()
    val authViewModel: AuthViewModel = viewModel()
    val signInState by authViewModel.signInState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(signInState.isSuccess) {
        if (signInState.isSuccess && signInState.user != null) {
            val user = signInState.user

            val nameEncoded = (user?.name ?: "").encodeUrl()
            val emailEncoded = (user?.email ?: "").encodeUrl()
            val photoUrlEncoded = (user?.photoUrl ?: "").encodeUrl()

            navController.navigate(
                "${Routes.PROFILE}/$nameEncoded/$emailEncoded/$photoUrlEncoded"
            ) {
                popUpTo(Routes.LOGIN) { inclusive = true }
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = Routes.LOGIN
    ) {
        composable(Routes.LOGIN) {
            LoginScreen(
                navController = navController,
                viewModel = authViewModel
            )
        }
        composable(
            route = "${Routes.PROFILE}/{name}/{email}/{photoUrl}",
            arguments = listOf(
                navArgument("name") { type = NavType.StringType; nullable = true },
                navArgument("email") { type = NavType.StringType; nullable = true },
                navArgument("photoUrl") { type = NavType.StringType; nullable = true },
            )
        ) { backStackEntry ->
            val name = backStackEntry.arguments?.getString("name")?.decodeUrl() ?: "N/A"
            val email = backStackEntry.arguments?.getString("email")?.decodeUrl() ?: "N/A"
            val photoUrl = backStackEntry.arguments?.getString("photoUrl")?.decodeUrl() ?: ""

            ProfileScreen(
                navController = navController,
                viewModel = authViewModel,
                name = name,
                email = email,
                photoUrl = photoUrl
            )
        }
    }
}

// --- Màn hình Đăng nhập (Login-Flow) ---
@Composable
fun LoginScreen(
    navController: NavHostController,
    viewModel: AuthViewModel
) {
    val context = LocalContext.current
    val signInState by viewModel.signInState.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    // State cho UI Phone Auth
    var phoneNumber by remember { mutableStateOf("") }
    var otpCode by remember { mutableStateOf("") }

    // Lấy Activity một cách an toàn
    val activity = context as? Activity

    // --- Cấu hình Google ---
    val webClientId = "886398564907-plro5t0rpltv5hnv8kma3gi278hlehi6.apps.googleusercontent.com"
    val googleSignInClient = com.google.android.gms.auth.api.signin.GoogleSignIn.getClient(
        context,
        com.google.android.gms.auth.api.signin.GoogleSignInOptions.Builder(
            com.google.android.gms.auth.api.signin.GoogleSignInOptions.DEFAULT_SIGN_IN
        )
            .requestIdToken(webClientId)
            .requestEmail()
            .build()
    )
    val googleSignInLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val task = com.google.android.gms.auth.api.signin.GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = task.getResult(com.google.android.gms.common.api.ApiException::class.java)!!
                val idToken = account.idToken!!
                viewModel.signInWithGoogle(idToken)
            } catch (e: com.google.android.gms.common.api.ApiException) {
                Toast.makeText(context, "Google Sign-In failed: ${e.message}", Toast.LENGTH_LONG).show()
            }
        } else {
            Toast.makeText(context, "Google Sign-In cancelled.", Toast.LENGTH_SHORT).show()
        }
    }

    // --- Hiển thị Lỗi ---
    LaunchedEffect(signInState.error) {
        signInState.error?.let {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // --- Logo và Tiêu đề ---
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .padding(8.dp)
                        .border(2.dp, Color.Gray, RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Bài tập FireBase", fontSize = 24.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    "Đăng nhập",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    "Lập trình mobile",
                    fontSize = 16.sp,
                    color = Color.Gray
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // --- Nút Đăng nhập với GOOGLE ---
            Button(
                onClick = {
                    coroutineScope.launch {
                        googleSignInLauncher.launch(googleSignInClient.signInIntent)
                    }
                },
                shape = RoundedCornerShape(50),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                enabled = !signInState.isLoading,
                colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = Color.Black)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.icon_google), // Đảm bảo bạn có file này trong res/drawable
                    contentDescription = "Google Icon",
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    "Đăng nhập với GOOGLE",
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // --- Nút Đăng nhập với GITHUB ---
            Button(
                onClick = {
                    if (activity != null) {
                        coroutineScope.launch {
                            viewModel.signInWithGitHub(activity)
                        }
                    } else {
                        Toast.makeText(context, "Lỗi: Không thể lấy được Activity", Toast.LENGTH_SHORT).show()
                    }
                },
                shape = RoundedCornerShape(50),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                enabled = !signInState.isLoading,
                colors = ButtonDefaults.buttonColors(containerColor = Color.DarkGray, contentColor = Color.White)
            ) {
                // Thêm icon GitHub nếu muốn
                Text(
                    "Đăng nhập với GITHUB",
                    fontWeight = FontWeight.Bold
                )
            }

            // --- KHU VỰC ĐĂNG NHẬP SỐ ĐIỆN THOẠI ---
            Spacer(modifier = Modifier.height(24.dp))
            Divider(modifier = Modifier.fillMaxWidth().padding(horizontal = 32.dp))
            Spacer(modifier = Modifier.height(24.dp))

            if (!signInState.isOtpSent) {
                // === GIAI ĐOẠN 1: NHẬP SỐ ĐIỆN THOẠI ===
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    OutlinedTextField(
                        value = phoneNumber,
                        onValueChange = { phoneNumber = it },
                        label = { Text("Số điện thoại (ví dụ: +84123456789)") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = {
                            if (activity != null) {
                                viewModel.sendOtpToPhone(phoneNumber, activity)
                            } else {
                                Toast.makeText(context, "Lỗi Activity", Toast.LENGTH_SHORT).show()
                            }
                        },
                        modifier = Modifier.fillMaxWidth().height(50.dp),
                        enabled = !signInState.isLoading && phoneNumber.isNotBlank() && activity != null
                    ) {
                        Text("Gửi mã OTP")
                    }
                }
            } else {
                // === GIAI ĐOẠN 2: NHẬP MÃ OTP ===
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        "Đã gửi mã tới $phoneNumber",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedTextField(
                        value = otpCode,
                        onValueChange = { otpCode = it },
                        label = { Text("Nhập mã OTP (6 số)") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = {
                            viewModel.verifyOtpAndSignIn(otpCode)
                        },
                        modifier = Modifier.fillMaxWidth().height(50.dp),
                        enabled = !signInState.isLoading && otpCode.length == 6
                    ) {
                        Text("Xác nhận và Đăng nhập")
                    }
                    TextButton(
                        onClick = { viewModel.resetPhoneAuthState() },
                        enabled = !signInState.isLoading
                    ) {
                        Text("Nhập lại số điện thoại")
                    }
                }
            }
        }

        // --- Loading Spinner ---
        if (signInState.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}

// --- Màn hình Thông tin (User-Profile) ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavHostController,
    viewModel: AuthViewModel,
    name: String,
    email: String,
    photoUrl: String
) {
    val context = LocalContext.current
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Profile") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            // --- TẢI ẢNH BẰNG COIL ---
            if (photoUrl.isNotEmpty()) {
                Image(
                    painter = rememberAsyncImagePainter(model = photoUrl),
                    contentDescription = "Avatar",
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            } else {
                Icon(
                    imageVector = Icons.Filled.AccountCircle,
                    contentDescription = "Avatar",
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape),
                    tint = Color.Gray
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            ProfileInfoRow(label = "Name", value = name.ifEmpty { "N/A" })
            ProfileInfoRow(label = "Email", value = email.ifEmpty { "N/A" })

            Spacer(modifier = Modifier.weight(1f))

            // Nút Đăng xuất
            Button(
                onClick = {
                    viewModel.signOut(context)
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(navController.graph.startDestinationId) { inclusive = true }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
            ) {
                Text("Sign Out", fontSize = 16.sp)
            }
        }
    }
}

// --- Composable phụ ---
@Composable
fun ProfileInfoRow(label: String, value: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
    ) {
        Text(
            text = label,
            fontSize = 14.sp,
            color = Color.Gray
        )
        Text(
            text = value,
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onBackground
        )
        Divider(color = Color.LightGray, thickness = 1.dp, modifier = Modifier.padding(top = 8.dp))
    }
}

// --- Các hàm tiện ích ---
fun String.encodeUrl(): String {
    return URLEncoder.encode(this, StandardCharsets.UTF_8.toString())
}

fun String.decodeUrl(): String {
    try {
        return URLDecoder.decode(this, StandardCharsets.UTF_8.toString())
    } catch (e: Exception) {
        return ""
    }
}

// --- Preview ---
@Preview(showBackground = true, device = "id:pixel_6")
@Composable
fun PreviewLoginScreen() {
    MaterialTheme {
        LoginScreen(navController = rememberNavController(), viewModel = viewModel())
    }
}