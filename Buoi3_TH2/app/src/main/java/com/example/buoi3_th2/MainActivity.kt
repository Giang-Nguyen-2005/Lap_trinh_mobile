package com.example.buoi3_th2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Adb
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.compose.ui.res.painterResource
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.buoi3_th2.ui.theme.Buoi3_TH2Theme
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Buoi3_TH2Theme {
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
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "welcome") {
        composable("welcome") { WelcomeScreen(navController) }
        composable("component_list") { ComponentListScreen(navController) }
        composable("text_detail") { TextDetailScreen(navController) }
        composable("image_detail") { ImageDetailScreen(navController) }
        composable("textfield_detail") { TextFieldDetailScreen(navController) }
        composable("passwordfield_detail") { PasswordFieldDetailScreen(navController) }
        composable("column_detail") { ColumnDetailScreen(navController) }
        composable("row_detail") { RowDetailScreen(navController) }
        // Thêm route cho các màn hình mới
        composable("slider_detail") { SliderDetailScreen(navController) }
        composable("switch_detail") { SwitchDetailScreen(navController) }
        composable("checkbox_detail") { CheckboxDetailScreen(navController) }
        composable("progress_detail") { ProgressDetailScreen(navController) }
        composable("buttons_detail") { ButtonsDetailScreen(navController) }
        composable("card_detail") { CardDetailScreen(navController) }
        composable("dialog_detail") { DialogDetailScreen(navController) }
        composable("radiobutton_detail") { RadioButtonDetailScreen(navController) }
        composable("fab_snackbar_detail") { FabSnackbarDetailScreen(navController) }
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
            onClick = { navController.navigate("component_list") },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text(text = "I'm ready")
        }
    }
}

// --- Màn hình danh sách các thành phần UI ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ComponentListScreen(navController: NavController) {
    val uiComponents = listOf(
        ListItem.Header("Display"),
        ListItem.ItemData("Text", "Displays text", "text_detail"),
        ListItem.ItemData("Image", "Displays an image", "image_detail"),
        ListItem.Header("Input"),
        ListItem.ItemData("TextField", "Input field for text", "textfield_detail"),
        ListItem.ItemData("PasswordField", "Input field for passwords", "passwordfield_detail"),
        ListItem.Header("Layout"),
        ListItem.ItemData("Column", "Arranges elements vertically", "column_detail"),
        ListItem.ItemData("Row", "Arranges elements horizontally", "row_detail"),
        ListItem.ItemData("Card", "A container for content", "card_detail"),
        // Thêm các component mới vào danh sách
        ListItem.Header("Controls"),
        ListItem.ItemData("Buttons", "Different types of buttons", "buttons_detail"),
        ListItem.ItemData("Slider", "Select a value from a range", "slider_detail"),
        ListItem.ItemData("Switch", "On/off toggle", "switch_detail"),
        ListItem.ItemData("Checkbox", "Select one or more options", "checkbox_detail"),
        ListItem.ItemData("RadioButtons", "Select a single option", "radiobutton_detail"),
        ListItem.Header("Feedback & Actions"),
        ListItem.ItemData("ProgressIndicator", "Shows loading progress", "progress_detail"),
        ListItem.ItemData("Dialog", "Show important information", "dialog_detail"),
        ListItem.ItemData("FAB & Snackbar", "Floating button and messages", "fab_snackbar_detail"),
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("UI Components List") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            items(uiComponents) { item ->
                when (item) {
                    is ListItem.Header -> {
                        Text(
                            text = item.title,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(vertical = 16.dp)
                        )
                    }

                    is ListItem.ItemData -> {
                        ComponentRow(title = item.title, subtitle = item.subtitle) {
                            navController.navigate(item.route)
                        }
                    }
                }
            }
        }
    }
}

// --- Các loại item trong danh sách ---
sealed class ListItem {
    data class Header(val title: String) : ListItem()
    data class ItemData(val title: String, val subtitle: String, val route: String) : ListItem()
}

// --- Composable cho một hàng trong danh sách ---
@Composable
fun ComponentRow(title: String, subtitle: String, onClick: () -> Unit) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clip(RoundedCornerShape(8.dp))
            .clickable(onClick = onClick),
        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = title, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyLarge)
            Text(text = subtitle, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
fun SpecialRow(title: String, subtitle: String) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        color = Color.Red.copy(alpha = 0.1f),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = title, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyLarge, color=Color.Red)
            Text(text = subtitle, style = MaterialTheme.typography.bodyMedium, color=Color.Red.copy(alpha=0.8f))
        }
    }
}


// --- Template chung cho các màn hình chi tiết ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreenScaffold(title: String, navController: NavController, content: @Composable (PaddingValues) -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(title) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        },
        content = content
    )
}

// --- Màn hình chi tiết Text ---
@Composable
fun TextDetailScreen(navController: NavController) {
    DetailScreenScaffold(title = "Text Detail", navController = navController) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            val annotatedString = buildAnnotatedString {
                append("The ")
                // "quick" với gạch ngang
                withStyle(style = SpanStyle(textDecoration = TextDecoration.LineThrough)) {
                    append("quick")
                }
                append(" ")
                // "Brown" in đậm, màu nâu, và cỡ chữ lớn hơn
                withStyle(style = SpanStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 32.sp,
                    color = Color(0xFF964B00) // Mã màu cho màu nâu
                )) {
                    append("Brown")
                }
                append(" fox ")

                // "jumps" với khoảng cách giữa các ký tự
                withStyle(style = SpanStyle(letterSpacing = 6.sp)) {
                    append("jumps")
                }

                append(" ")

                // "over" in đậm và in nghiêng
                withStyle(style = SpanStyle(
                    fontWeight = FontWeight.Bold,
                    fontStyle = FontStyle.Italic
                )) {
                    append("over")
                }

                append(" ")

                // "the" với gạch chân
                withStyle(style = SpanStyle(textDecoration = TextDecoration.Underline)) {
                    append("the")
                }

                append(" ")

                // "lazy" in nghiêng và dùng font viết tay
                withStyle(style = SpanStyle(
                    fontStyle = FontStyle.Italic,
                    fontFamily = FontFamily.Cursive
                )) {
                    append("lazy")
                }

                append(" dog.")
            }
            Text(text = annotatedString, fontSize = 24.sp)
        }
    }
}

@Composable
fun ImageDetailScreen(navController: NavController) {
    DetailScreenScaffold(title = "Images from Device", navController = navController) { paddingValues ->
        // Sử dụng LazyColumn để có thể cuộn nếu ảnh quá lớn
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Text("Ảnh thứ nhất (image_one.png)", style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(8.dp))

                // Hiển thị ảnh thứ nhất từ drawable
                Image(
                    painter = painterResource(id = R.drawable.image_one), // <-- THAY TÊN FILE CỦA BẠN
                    contentDescription = "Image One from local resources",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop // Cắt ảnh để vừa với khung nhìn
                )

                Spacer(Modifier.height(24.dp))
            }

            item {
                Text("Ảnh thứ hai (image_two.jpg)", style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(8.dp))

                // Hiển thị ảnh thứ hai từ drawable
                Image(
                    painter = painterResource(id = R.drawable.image_two), // <-- THAY TÊN FILE CỦA BẠN
                    contentDescription = "Image Two from local resources",
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.FillWidth // Hiển thị toàn bộ chiều rộng ảnh
                )
            }
        }
    }
}

// --- Màn hình chi tiết TextField ---
@Composable
fun TextFieldDetailScreen(navController: NavController) {
    var text by remember { mutableStateOf("") }
    DetailScreenScaffold(title = "TextField", navController = navController) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = text,
                onValueChange = { text = it },
                label = { Text("Thông tin nhập") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(16.dp))
            Text("Nội dung đang nhập: $text")
        }
    }
}

// --- Màn hình chi tiết PasswordField ---
@Composable
fun PasswordFieldDetailScreen(navController: NavController) {
    var password by remember { mutableStateOf("") }
    DetailScreenScaffold(title = "PasswordField", navController = navController) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Enter password") },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

// --- Màn hình chi tiết Column ---
@Composable
fun ColumnDetailScreen(navController: NavController) {
    DetailScreenScaffold(title = "Column Layout", navController = navController) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            (1..5).forEach {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                        .padding(vertical = 4.dp)
                        .background(MaterialTheme.colorScheme.primary, RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ){
                    Text("Item $it", color = MaterialTheme.colorScheme.onPrimary)
                }
            }
        }
    }
}

// --- Màn hình chi tiết Row ---
@Composable
fun RowDetailScreen(navController: NavController) {
    DetailScreenScaffold(title = "Row Layout", navController = navController) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            (1..4).forEach { _ ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    (1..3).forEach {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(80.dp)
                                .background(
                                    MaterialTheme.colorScheme.secondary,
                                    RoundedCornerShape(8.dp)
                                )
                        )
                    }
                }
            }
        }
    }
}

// --- Màn hình chi tiết Slider ---
@Composable
fun SliderDetailScreen(navController: NavController) {
    var age by remember { mutableFloatStateOf(18f) }
    DetailScreenScaffold(title = "Slider", navController = navController) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Tuổi của bạn: ${age.roundToInt()}", style = MaterialTheme.typography.headlineSmall)
            Spacer(Modifier.height(16.dp))
            Slider(
                value = age,
                onValueChange = { age = it },
                valueRange = 1f..100f,
                steps = 98 // (100-1) - 1 = 98 steps for integer values
            )
        }
    }
}

// --- Màn hình chi tiết Switch ---
@Composable
fun SwitchDetailScreen(navController: NavController) {
    var isEnabled by remember { mutableStateOf(false) }
    DetailScreenScaffold(title = "Switch", navController = navController) { paddingValues ->
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(if (isEnabled) "ON" else "OFF", style = MaterialTheme.typography.titleLarge)
            Spacer(Modifier.width(16.dp))
            Switch(
                checked = isEnabled,
                onCheckedChange = { isEnabled = it }
            )
        }
    }
}

// --- Màn hình chi tiết Checkbox ---
@Composable
fun CheckboxDetailScreen(navController: NavController) {
    var isChecked by remember { mutableStateOf(true) }
    DetailScreenScaffold(title = "Checkbox", navController = navController) { paddingValues ->
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .clickable { isChecked = !isChecked }, // Click cả hàng để thay đổi
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Checkbox(
                checked = isChecked,
                onCheckedChange = { isChecked = it }
            )
            Spacer(Modifier.width(16.dp))
            Text("Đồng ý với điều khoản", style = MaterialTheme.typography.titleLarge)
        }
    }
}

// --- Màn hình chi tiết ProgressIndicator ---
@Composable
fun ProgressDetailScreen(navController: NavController) {
    DetailScreenScaffold(title = "Progress Indicators", navController = navController) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Circular Progress Indicator", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(16.dp))
            CircularProgressIndicator(
                modifier = Modifier.size(64.dp)
            )
            Spacer(Modifier.height(48.dp))
            Text("Linear Progress Indicator", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(16.dp))
            LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
        }
    }
}

// --- CÁC MÀN HÌNH MỚI ---

// --- Màn hình chi tiết Buttons ---
@Composable
fun ButtonsDetailScreen(navController: NavController) {
    DetailScreenScaffold(title = "Buttons", navController = navController) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically)
        ) {
            Button(onClick = { /*TODO*/ }) {
                Text("Standard Button")
            }
            OutlinedButton(onClick = { /*TODO*/ }) {
                Text("Outlined Button")
            }
            TextButton(onClick = { /*TODO*/ }) {
                Text("Text Button")
            }
            IconButton(onClick = { /*TODO*/ }) {
                Icon(Icons.Default.Favorite, contentDescription = "Favorite")
            }
        }
    }
}

// --- Màn hình chi tiết Card ---
@Composable
fun CardDetailScreen(navController: NavController) {
    DetailScreenScaffold(title = "Card", navController = navController) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Đây là một Card", style = MaterialTheme.typography.titleLarge)
                    Spacer(Modifier.height(8.dp))
                    Text("Card có thể chứa nhiều loại nội dung khác nhau và có một lớp đổ bóng nhẹ để tạo sự nổi bật.")
                }
            }
        }
    }
}

// --- Màn hình chi tiết Dialog ---
@Composable
fun DialogDetailScreen(navController: NavController) {
    var showDialog by remember { mutableStateOf(false) }
    DetailScreenScaffold(title = "Dialog", navController = navController) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            Button(onClick = { showDialog = true }) {
                Text("Hiển thị Dialog")
            }
        }

        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text("Đây là tiêu đề") },
                text = { Text("Đây là nội dung của hộp thoại. Bạn có muốn thực hiện hành động này không?") },
                confirmButton = {
                    TextButton(onClick = { showDialog = false }) {
                        Text("Đồng ý")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDialog = false }) {
                        Text("Hủy")
                    }
                }
            )
        }
    }
}

// --- Màn hình chi tiết RadioButton ---
@Composable
fun RadioButtonDetailScreen(navController: NavController) {
    val options = listOf("Kotlin", "Java", "Dart", "Swift")
    var selectedOption by remember { mutableStateOf(options[0]) }

    DetailScreenScaffold(title = "Radio Buttons", navController = navController) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Text("Ngôn ngữ lập trình yêu thích của bạn là gì?", style = MaterialTheme.typography.titleLarge)
            Spacer(Modifier.height(16.dp))
            options.forEach { text ->
                Row(
                    Modifier
                        .fillMaxWidth()
                        .selectable(
                            selected = (text == selectedOption),
                            onClick = { selectedOption = text },
                            role = Role.RadioButton
                        )
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = (text == selectedOption),
                        onClick = null // null recommended for accessibility with selectable
                    )
                    Text(
                        text = text,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                }
            }
            Spacer(Modifier.height(24.dp))
            Text("Bạn đã chọn: $selectedOption", style = MaterialTheme.typography.titleMedium)
        }
    }
}

// --- Màn hình chi tiết FAB & Snackbar ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FabSnackbarDetailScreen(navController: NavController) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("FAB & Snackbar") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                scope.launch {
                    snackbarHostState.showSnackbar("Bạn đã nhấn nút FAB!")
                }
            }) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            Button(onClick = {
                scope.launch {
                    val result = snackbarHostState.showSnackbar(
                        message = "Đây là một Snackbar với action.",
                        actionLabel = "Ẩn",
                        duration = SnackbarDuration.Short
                    )
                    if (result == SnackbarResult.ActionPerformed) {
                        // Xử lý khi người dùng nhấn action
                    }
                }
            }) {
                Text("Hiển thị Snackbar")
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    Buoi3_TH2Theme {
        AppNavigation()
    }
}

