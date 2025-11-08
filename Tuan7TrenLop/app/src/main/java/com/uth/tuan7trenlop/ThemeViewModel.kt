package com.uth.tuan7trenlop.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.uth.tuan7trenlop.data.AppTheme
import com.uth.tuan7trenlop.data.ThemeRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ThemeViewModel(application: Application) : AndroidViewModel(application) {
    private val repo = ThemeRepository(application.applicationContext)

    val themeState: StateFlow<AppTheme> = repo.themeFlow
        .stateIn(viewModelScope, SharingStarted.Eagerly, AppTheme.LIGHT)

    fun setTheme(theme: AppTheme) {
        viewModelScope.launch {
            repo.saveTheme(theme)
        }
    }
}
