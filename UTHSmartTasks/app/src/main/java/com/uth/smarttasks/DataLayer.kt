package com.uth.smarttaskscompose

import com.google.gson.annotations.SerializedName
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Path

// --- WRAPPER DÙNG CHUNG (Hứng cục { isSuccess, message, data }) ---
// Dùng Generic <T> để tái sử dụng cho cả List và Detail nếu cần
data class BaseResponse<T>(
    val isSuccess: Boolean,
    val message: String,
    val data: T
)

// --- MODEL CHÍNH ---
data class Task(
    val id: Int, // JSON trả về số nguyên (1, 2...) nên dùng Int
    val title: String?,
    val description: String?,
    val status: String?,
    val priority: String?,
    val category: String?,
    val dueDate: String?,
    val createdAt: String?,
    // Các danh sách con (có thể null hoặc rỗng)
    val subtasks: List<Subtask>?,
    val attachments: List<Attachment>?,
    val reminders: List<Reminder>?
)

// --- CÁC MODEL CON ---
data class Subtask(
    val id: Int,
    val title: String?,
    val isCompleted: Boolean
)

data class Attachment(
    val id: Int,
    val fileName: String?,
    val fileUrl: String?
)

data class Reminder(
    val id: Int,
    val time: String?,
    val type: String?
)

// --- API INTERFACE ---
interface ApiService {
    // 1. Lấy danh sách -> Trả về BaseResponse chứa List<Task>
    @GET("researchUTH/tasks")
    suspend fun getTasks(): BaseResponse<List<Task>>

    // 2. Lấy chi tiết -> TẠM THỜI giả định nó trả về Task trực tiếp.
    // NẾU VẪN CRASH KHI VÀO CHI TIẾT, BẠN CẦN GỬI JSON CỦA API CHI TIẾT CHO TÔI.
    // Có khả năng nó cũng là BaseResponse<Task>
    @GET("researchUTH/task/{id}")
    suspend fun getTaskDetail(@Path("id") id: String): Task

    @DELETE("researchUTH/task/{id}")
    suspend fun deleteTask(@Path("id") id: String): retrofit2.Response<Unit>
}

// --- RETROFIT CLIENT ---
object RetrofitClient {
    private const val BASE_URL = "https://amock.io/api/"
    val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}