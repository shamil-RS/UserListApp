package com.example.userlistapp.data.datasource.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LocalAccountPreferences @Inject constructor(
    private val dataStorePreferences: DataStore<Preferences>
) {

    suspend fun isStayConnectedEnabled(): Boolean = dataStorePreferences.data
        .map { preferences ->
            preferences[PreferencesKeys.stayConnected] ?: false
        }.first()

    suspend fun setCurrentUserId(userId: Int, stayConnected: Boolean) {
        tryIt {
            dataStorePreferences.edit { preferences ->
                preferences[PreferencesKeys.currentUserId] = userId
                preferences[PreferencesKeys.stayConnected] = stayConnected
            }
        }
    }

    suspend fun getCurrentUserId(): Int = dataStorePreferences.data
        .catch { exception ->
            exception.printStackTrace()
            emit(value = emptyPreferences())
        }
        .map { preferences ->
            preferences[PreferencesKeys.currentUserId] ?: 0
        }.first()

    private suspend fun tryIt(action: suspend () -> Unit) {
        try {
            action()
        } catch (exception: Exception) {
            exception.printStackTrace()
        }
    }

    private object PreferencesKeys {
        val stayConnected = booleanPreferencesKey(name = "stay_connected")
        val currentUserId = intPreferencesKey(name = "current_user_id")
    }
}