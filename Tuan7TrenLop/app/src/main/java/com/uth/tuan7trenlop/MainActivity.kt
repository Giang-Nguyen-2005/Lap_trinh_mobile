package com.uth.tuan7trenlop

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
// import androidx.compose.material.* // Xóa hoặc comment dòng này nếu dùng full M3
import androidx.compose.material3.RadioButton
import androidx.compose.runtime.*
import androidx.compose.material3.Surface
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.uth.tuan7trenlop.data.AppTheme
import com.uth.tuan7trenlop.ui.ThemeViewModel
import com.uth.tuan7trenlop.ui.theme.colorsForTheme
import kotlinx.coroutines.launch
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Button
import androidx.compose.material3.Text

class MainActivity : ComponentActivity() {
    private val vm: ThemeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val theme by vm.themeState.collectAsState()
            val isDark = (theme == AppTheme.DARK)
            val appColors = colorsForTheme(theme.name, isDark)

            // SỬA 1: M3 dùng 'colorScheme' thay vì 'colors'
            // LƯU Ý: Nếu 'appColors' báo lỗi ở đây, nghĩa là hàm colorsForTheme()
            // của bạn đang trả về kiểu M2 Colors. Bạn nên dùng Cách 1 nếu gặp trường hợp đó.
            MaterialTheme(colorScheme = appColors) {
                Surface(modifier = Modifier.fillMaxSize()) {
                    ThemeSelectorScreen(current = theme, onApply = { vm.setTheme(it) })
                }
            }
        }
    }
}

@Composable
fun ThemeSelectorScreen(current: AppTheme, onApply: (AppTheme) -> Unit) {
    var selection by remember { mutableStateOf(current) }
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            // SỬA 2: M3 dùng 'headlineSmall' (hoặc tương tự) thay vì 'h5'
            Text("Chọn giao diện", style = MaterialTheme.typography.headlineSmall)
            Spacer(modifier = Modifier.height(12.dp))

            Box(
                modifier = Modifier
                    .height(300.dp)
                    .fillMaxWidth()
                    // SỬA 3: M3 dùng 'colorScheme' thay vì 'colors'
                    .background(MaterialTheme.colorScheme.primary, RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                // SỬA 4: M3 dùng 'colorScheme' thay vì 'colors'
                Text("Preview", color = MaterialTheme.colorScheme.onPrimary)
            }

            Spacer(Modifier.height(16.dp))

            ThemeOptionRow("Light", AppTheme.LIGHT, selection) { selection = it }
            ThemeOptionRow("Dark", AppTheme.DARK, selection) { selection = it }
            ThemeOptionRow("Pink", AppTheme.PINK, selection) { selection = it }
            ThemeOptionRow("Blue", AppTheme.BLUE, selection) { selection = it }
        }

        Button(
            onClick = { scope.launch { onApply(selection) } },
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp)
        ) {
            Text("Apply")
        }
    }
}

@Composable
fun ThemeOptionRow(label: String, theme: AppTheme, selected: AppTheme, onSelect: (AppTheme) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(selected = (theme == selected), onClick = { onSelect(theme) })
        Spacer(modifier = Modifier.width(8.dp))
        Text(label)
        Spacer(modifier = Modifier.weight(1f))
        Box(
            modifier = Modifier
                .size(30.dp)
                .background(
                    color = when (theme) {
                        AppTheme.LIGHT -> androidx.compose.ui.graphics.Color(0xFF2196F3)
                        AppTheme.DARK -> androidx.compose.ui.graphics.Color(0xFF212121)
                        AppTheme.PINK -> androidx.compose.ui.graphics.Color(0xFFEC407A)
                        AppTheme.BLUE -> androidx.compose.ui.graphics.Color(0xFF64B5F6)
                    },
                    shape = RoundedCornerShape(6.dp)
                )
        )
    }
}