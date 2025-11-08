package com.example.mycomposeapp

import android.app.Activity
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.OAuthProvider
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.concurrent.TimeUnit

data class SignInState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String? = null,
    val user: UserData? = null,
    // Thêm state cho Phone Auth
    val isOtpSent: Boolean = false,
    val verificationId: String? = null
)

data class UserData(
    val uid: String,
    val name: String?,
    val email: String?,
    val photoUrl: String?
)

class AuthViewModel : ViewModel() {

    private val auth: FirebaseAuth = Firebase.auth

    private val _signInState = MutableStateFlow(SignInState())
    val signInState = _signInState.asStateFlow()

    init {
        // Kiểm tra xem user đã đăng nhập từ trước chưa
        // VÀ KIỂM TRA KẾT QUẢ ĐANG CHỜ (TỪ GITHUB)
        checkCurrentUserAndPendingResults()
    }

    private fun checkCurrentUserAndPendingResults() {
        val firebaseUser = auth.currentUser
        if (firebaseUser != null) {
            // Nếu đã đăng nhập, cập nhật ngay
            updateStateWithUser(firebaseUser)
        } else {
            // Nếu chưa đăng nhập, kiểm tra xem có "kết quả đang chờ" không
            // (ví dụ: vừa quay về từ Chrome sau khi đăng nhập GitHub)
            auth.pendingAuthResult
                ?.addOnSuccessListener { authResult ->
                    // Có kết quả đang chờ, đăng nhập thành công
                    authResult?.let { updateStateWithUser(it.user) }
                }
                ?.addOnFailureListener { e ->
                    // Xử lý lỗi từ kết quả đang chờ
                    handleAuthError(e)
                }
        }
    }

    // --- Hàm Cập nhật trạng thái ---
    private fun updateStateWithUser(firebaseUser: com.google.firebase.auth.FirebaseUser?) {
        _signInState.update {
            it.copy(
                isLoading = false,
                isSuccess = true,
                error = null,
                user = firebaseUser?.let { user ->
                    UserData(
                        uid = user.uid,
                        name = user.displayName,
                        email = user.email,
                        photoUrl = user.photoUrl?.toString()
                    )
                }
            )
        }
    }

    // --- Hàm Xử lý lỗi ---
    private fun handleAuthError(e: Exception) {
        val errorMessage = when (e) {
            is FirebaseAuthUserCollisionException -> "Email này đã được sử dụng."
            is FirebaseAuthInvalidCredentialsException -> "Thông tin không chính xác."
            else -> e.message ?: "Đã xảy ra lỗi."
        }
        _signInState.update {
            it.copy(
                isLoading = false,
                isSuccess = false,
                error = errorMessage
            )
        }
    }

    // --- LOGIC PHONE AUTH ---

    // Callbacks để lắng nghe kết quả từ Firebase
    private val _verificationCallbacks =
        object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                _signInState.update { it.copy(isLoading = true) }
                signInWithPhoneCredential(credential)
            }

            override fun onVerificationFailed(e: FirebaseException) {
                _signInState.update { it.copy(isLoading = false, error = e.message) }
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                // Lưu verificationId và báo cho UI biết để hiển thị ô nhập OTP
                _signInState.update {
                    it.copy(
                        isLoading = false,
                        isOtpSent = true, // Bật màn hình nhập OTP
                        verificationId = verificationId // Lưu ID
                    )
                }
            }
        }

    // Hàm để bắt đầu gửi OTP (gọi từ UI)
    fun sendOtpToPhone(phoneNumber: String, activity: Activity) {
        _signInState.update { it.copy(isLoading = true, error = null) }
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)       // SĐT (ví dụ: +84123456789)
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout 60 giây
            .setActivity(activity)
            .setCallbacks(_verificationCallbacks) // Gán callbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    // Hàm để xác thực OTP và đăng nhập (gọi từ UI)
    fun verifyOtpAndSignIn(otpCode: String) {
        val verificationId = _signInState.value.verificationId

        if (verificationId == null) {
            _signInState.update { it.copy(error = "Không tìm thấy ID xác thực. Vui lòng thử lại.") }
            return
        }

        _signInState.update { it.copy(isLoading = true, error = null) }
        val credential = PhoneAuthProvider.getCredential(verificationId, otpCode)
        signInWithPhoneCredential(credential)
    }

    // Hàm để reset (nếu người dùng muốn nhập lại SĐT)
    fun resetPhoneAuthState() {
        _signInState.update {
            it.copy(isOtpSent = false, verificationId = null, isLoading = false, error = null)
        }
    }

    // Hàm đăng nhập (dùng chung cho Phone Auth)
    private fun signInWithPhoneCredential(credential: PhoneAuthCredential) {
        viewModelScope.launch {
            try {
                val authResult = auth.signInWithCredential(credential).await()
                updateStateWithUser(authResult.user)
            } catch (e: Exception) {
                handleAuthError(e)
            } finally {
                // Reset lại state sau khi xong
                resetPhoneAuthState()
            }
        }
    }

    // --- LOGIC GOOGLE AUTH ---
    fun signInWithGoogle(idToken: String) {
        viewModelScope.launch {
            _signInState.update { it.copy(isLoading = true) }
            try {
                val credential = GoogleAuthProvider.getCredential(idToken, null)
                val authResult = auth.signInWithCredential(credential).await()
                updateStateWithUser(authResult.user)
            } catch (e: Exception) {
                handleAuthError(e)
            }
        }
    }

    // --- LOGIC GITHUB AUTH ---
    fun signInWithGitHub(activity: Activity) {
        viewModelScope.launch {
            _signInState.update { it.copy(isLoading = true) }
            val provider = OAuthProvider.newBuilder("github.com").build()

            // Khi gọi hàm này, nó sẽ mở Chrome.
            // Kết quả sẽ được bắt bởi `auth.pendingAuthResult` ở hàm `init`
            // khi app được mở lại.
            auth.startActivityForSignInWithProvider(activity, provider)
                .addOnSuccessListener { authResult ->
                    // (Trong 1 số trường hợp, nó thành công ngay tại đây)
                    updateStateWithUser(authResult.user)
                }
                .addOnFailureListener { e ->
                    // Đăng nhập thất bại (trước khi mở Chrome)
                    handleAuthError(e)
                }
        }
    }

    // --- LOGIC SIGN OUT ---
    fun signOut(context: Context) {
        viewModelScope.launch {
            val googleSignInClient = com.google.android.gms.auth.api.signin.GoogleSignIn.getClient(
                context,
                com.google.android.gms.auth.api.signin.GoogleSignInOptions.Builder(
                    com.google.android.gms.auth.api.signin.GoogleSignInOptions.DEFAULT_SIGN_IN
                )
                    .requestEmail()
                    .build()
            )

            // Đăng xuất Firebase
            auth.signOut()

            // Đăng xuất Google (nếu có)
            googleSignInClient.signOut()

            // Cập nhật lại state
            _signInState.update { SignInState() }
        }
    }
}