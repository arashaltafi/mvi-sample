package com.arash.altafi.mvisample.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.arash.altafi.mvisample.utils.EncryptionUtils
import com.arash.altafi.mvisample.utils.JsonUtils
import com.arash.altafi.mvisample.utils.base.BaseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class DataStoreRepository @Inject constructor(
    private val dataStore: DataStore<Preferences>,
    private val encryptionUtils: EncryptionUtils,
    private val jsonUtils: JsonUtils
) : BaseRepository() {

    @Inject
    lateinit var jsonUtils1: JsonUtils

    // Token
    fun getTokenString(): String {
        return runBlocking {
            dataStore.data.map { preferences ->
                encryptionUtils.decrypt(preferences[PreferenceKeys.TOKEN] ?: "default_value")
            }.first()
        }
    }

    fun getToken(): Flow<String> {
        return dataStore.data.map { preferences ->
            encryptionUtils.decrypt(preferences[PreferenceKeys.TOKEN] ?: "default_value")
        }
    }

    fun setToken(value: String) = callCache {
        dataStore.edit { preferences ->
            preferences[PreferenceKeys.TOKEN] = encryptionUtils.encrypt(value)
        }
    }

    // Theme
    fun getTheme(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[PreferenceKeys.THEME] ?: ""
        }
    }

    fun setTheme(theme: String) = callCache {
        dataStore.edit { preferences ->
            preferences[PreferenceKeys.THEME] = theme
        }
    }

    fun changeTheme() = callCache {
        dataStore.edit { preferences ->
            preferences[PreferenceKeys.THEME] =
                if (preferences[PreferenceKeys.THEME] == "dark") "light" else "dark"
        }
    }
}

object PreferenceKeys {
    val TOKEN = stringPreferencesKey("user_token")
    val THEME = stringPreferencesKey("app_theme")
    val USERINFO = stringPreferencesKey("user_info")
}