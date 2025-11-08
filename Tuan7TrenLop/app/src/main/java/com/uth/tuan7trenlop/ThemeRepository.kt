package com.uth.tuan7trenlop.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore("theme_prefs")

enum class AppTheme {
    LIGHT, DARK, PINK, BLUE;

    companion object {
        fun fromName(name: String?): AppTheme {
            return try {
                if (name == null) LIGHT else valueOf(name)
            } catch (e: Exception) {
                LIGHT
            }
        }
    }
}

class ThemeRepository(private val context: Context) {
    private val THEME_KEY = stringPreferencesKey("app_theme")

    val themeFlow: Flow<AppTheme> = context.dataStore.data
        .map { prefs -> AppTheme.fromName(prefs[THEME_KEY]) }

    suspend fun saveTheme(theme: AppTheme) {
        context.dataStore.edit { prefs ->
            prefs[THEME_KEY] = theme.name
        }
    }
}
