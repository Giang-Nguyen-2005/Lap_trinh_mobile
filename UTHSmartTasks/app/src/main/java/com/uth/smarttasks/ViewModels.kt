package com.uth.smarttaskscompose

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

// --- HOME VIEWMODEL ---
class HomeViewModel : ViewModel() {
    var uiState by mutableStateOf(HomeUiState())
        private set

    fun fetchTasks() {
        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true, error = null)
            try {
                // Gọi API
                val response = RetrofitClient.api.getTasks()
                // QUAN TRỌNG: Lấy .data từ BaseResponse
                uiState = uiState.copy(isLoading = false, tasks = response.data)
            } catch (e: Exception) {
                // Log lỗi ra để dễ debug nếu cần
                e.printStackTrace()
                uiState = uiState.copy(isLoading = false, error = e.message)
            }
        }
    }
}

data class HomeUiState(
    val isLoading: Boolean = false,
    val tasks: List<Task> = emptyList(),
    val error: String? = null
)

// --- DETAIL VIEWMODEL (Giữ nguyên tạm thời) ---
class DetailViewModel : ViewModel() {
    var uiState by mutableStateOf(DetailUiState())
        private set

    fun fetchTaskDetail(id: String) {
        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true, error = null)
            try {
                val task = RetrofitClient.api.getTaskDetail(id)
                uiState = uiState.copy(isLoading = false, task = task)
            } catch (e: Exception) {
                uiState = uiState.copy(isLoading = false, error = e.message)
            }
        }
    }

    fun deleteTask(id: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            uiState = uiState.copy(isDeleting = true)
            try {
                val response = RetrofitClient.api.deleteTask(id)
                if (response.isSuccessful) {
                    onSuccess()
                } else {
                    uiState = uiState.copy(isDeleting = false, error = "Delete failed: ${response.code()}")
                }
            } catch (e: Exception) {
                uiState = uiState.copy(isDeleting = false, error = e.message)
            }
        }
    }
}

data class DetailUiState(
    val isLoading: Boolean = false,
    val isDeleting: Boolean = false,
    val task: Task? = null,
    val error: String? = null
)