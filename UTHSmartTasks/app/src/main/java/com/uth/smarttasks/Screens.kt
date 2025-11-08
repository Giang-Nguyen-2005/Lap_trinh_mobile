package com.uth.smarttasks

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.Assignment
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material.icons.outlined.AttachFile
import androidx.compose.material.icons.outlined.Category
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Flag
import androidx.compose.material.icons.outlined.RadioButtonUnchecked
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.uth.smarttaskscompose.DetailViewModel
import com.uth.smarttaskscompose.HomeViewModel
import com.uth.smarttaskscompose.Task

// --- MÀU SẮC TÙY CHỈNH (PASTEL) ---
val PastelPink = Color(0xFFF4D6D6)
val PastelGreen = Color(0xFFE7F4D6)
val PastelBlue = Color(0xFFD6F1F4)
val DarkText = Color(0xFF2D2D2D)
val SubText = Color(0xFF5F6368)

// Hàm chọn màu dựa trên Priority hoặc Category
fun getTaskColor(priority: String?): Color {
    return when (priority?.lowercase()) {
        "high" -> PastelPink
        "medium" -> PastelBlue
        "low" -> PastelGreen
        else -> Color(0xFFF5F5F5) // Màu xám mặc định
    }
}

// ================== HOME SCREEN ĐẸP HƠN ==================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToDetail: (String) -> Unit,
    viewModel: HomeViewModel = viewModel()
) {
    val state = viewModel.uiState

    LaunchedEffect(Unit) {
        viewModel.fetchTasks()
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "UTH SmartTasks",
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.White
                )
            )
        },
        containerColor = Color(0xFFF8F9FA) // Màu nền xám rất nhạt cho sang
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when {
                state.isLoading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                state.error != null -> {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(Icons.Outlined.Flag, contentDescription = null, tint = Color.Red, modifier = Modifier.size(48.dp))
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Đã có lỗi xảy ra!", fontWeight = FontWeight.Bold)
                        Text(state.error ?: "", color = Color.Gray, fontSize = 12.sp)
                        Button(onClick = { viewModel.fetchTasks() }, modifier = Modifier.padding(top = 16.dp)) {
                            Text("Thử lại")
                        }
                    }
                }
                state.tasks.isEmpty() -> {
                    // Empty View đẹp hơn
                    EmptyStateView()
                }
                else -> {
                    // List View đẹp hơn
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp) // Khoảng cách giữa các item
                    ) {
                        // Tiêu đề nhỏ "Your Tasks"
                        item {
                            Text(
                                "Your Tasks (${state.tasks.size})",
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                        }
                        items(state.tasks) { task ->
                            TaskItemImproved(task = task, onClick = { onNavigateToDetail(task.id.toString()) })
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun EmptyStateView() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Outlined.Assignment,
            contentDescription = null,
            modifier = Modifier.size(100.dp),
            tint = Color(0xFFE0E0E0)
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "No Tasks Yet!",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = DarkText
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Stay productive—add something to do",
            fontSize = 16.sp,
            color = SubText
        )
    }
}

@Composable
fun TaskItemImproved(task: Task, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp), // Bo góc mềm mại hơn
        colors = CardDefaults.cardColors(
            containerColor = getTaskColor(task.priority) // Màu nền động
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp) // Phẳng, không đổ bóng (giống thiết kế mẫu)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            // Dòng 1: Title
            Text(
                text = task.title ?: "No Title",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = DarkText,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            // Dòng 2: Status & Date
            Spacer(modifier = Modifier.height(12.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Status Badge nhỏ
                Surface(
                    color = Color.Black.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = task.status ?: "Unknown",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        color = DarkText
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                // Due date (giả lập)
                Icon(
                    Icons.Outlined.AccessTime,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = SubText
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "14:00 25 Mar", // Bạn có thể format từ task.dueDate
                    fontSize = 12.sp,
                    color = SubText
                )
            }
        }
    }
}

// ================== DETAIL SCREEN ĐẸP HƠN ==================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    taskId: String,
    onBack: () -> Unit,
    viewModel: DetailViewModel = viewModel()
) {
    val state = viewModel.uiState
    val context = LocalContext.current

    LaunchedEffect(taskId) {
        viewModel.fetchTaskDetail(taskId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detail", fontWeight = FontWeight.SemiBold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                actions = {
                    // Nút xóa với background tròn đỏ nhạt
                    IconButton(
                        onClick = {
                            viewModel.deleteTask(taskId) {
                                Toast.makeText(context, "Deleted!", Toast.LENGTH_SHORT).show()
                                onBack()
                            }
                        },
                        enabled = !state.isDeleting
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFFFEBEE)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.Red, modifier = Modifier.size(20.dp))
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        containerColor = Color.White
    ) { padding ->
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(padding)) {
            if (state.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else {
                state.task?.let { task ->
                    Column(
                        modifier = Modifier
                            .verticalScroll(rememberScrollState())
                            .padding(20.dp)
                    ) {
                        // 1. Tiêu đề to
                        Text(
                            text = task.title ?: "",
                            fontSize = 26.sp,
                            fontWeight = FontWeight.Bold,
                            color = DarkText,
                            lineHeight = 34.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = task.description ?: "No description",
                            fontSize = 16.sp,
                            color = SubText,
                            lineHeight = 24.sp
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        // 2. Các thẻ thông tin (Metadata Chips)
                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            InfoChip(icon = Icons.Outlined.Category, label = "Category", value = task.category ?: "Work", color = PastelPink)
                            InfoChip(icon = Icons.Outlined.Flag, label = "Priority", value = task.priority ?: "Medium", color = PastelBlue)
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        InfoChip(icon = Icons.Outlined.CheckCircle, label = "Status", value = task.status ?: "Pending", color = PastelGreen)

                        Spacer(modifier = Modifier.height(32.dp))

                        // 3. Subtasks Section
                        Text("Subtasks", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        Spacer(modifier = Modifier.height(12.dp))
                        task.subtasks?.forEach { subtask ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(vertical = 8.dp)
                            ) {
                                Icon(
                                    if (subtask.isCompleted) Icons.Outlined.CheckCircle else Icons.Outlined.RadioButtonUnchecked,
                                    contentDescription = null,
                                    tint = if (subtask.isCompleted) MaterialTheme.colorScheme.primary else SubText,
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = subtask.title ?: "",
                                    fontSize = 16.sp,
                                    color = if (subtask.isCompleted) SubText else DarkText,
                                    textDecoration = if (subtask.isCompleted) androidx.compose.ui.text.style.TextDecoration.LineThrough else null
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(32.dp))

                        // 4. Attachments Section
                        if (!task.attachments.isNullOrEmpty()) {
                            Text("Attachments", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                            Spacer(modifier = Modifier.height(12.dp))
                            task.attachments.forEach { attachment ->
                                AttachmentItem(fileName = attachment.fileName ?: "Unknown file")
                            }
                        }
                    }
                }
            }
        }
    }
}

// Component con: Thẻ thông tin (Category, Status...)
@Composable
fun InfoChip(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, value: String, color: Color) {
    Surface(
        color = color.copy(alpha = 0.3f), // Màu nền nhạt
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, contentDescription = null, modifier = Modifier.size(18.dp), tint = DarkText.copy(alpha = 0.7f))
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(text = label, fontSize = 10.sp, color = SubText)
                Text(text = value, fontSize = 14.sp, fontWeight = FontWeight.Medium, color = DarkText)
            }
        }
    }
}

// Component con: File đính kèm
@Composable
fun AttachmentItem(fileName: String) {
    Surface(
        color = Color(0xFFF5F5F5),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Outlined.AttachFile, contentDescription = null, tint = SubText)
            Spacer(modifier = Modifier.width(12.dp))
            Text(fileName, fontWeight = FontWeight.Medium, color = DarkText)
        }
    }
}