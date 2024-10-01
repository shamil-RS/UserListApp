package com.example.userlistapp.data.repository

import com.example.userlistapp.data.datasource.datastore.LocalAccountPreferences
import javax.inject.Inject

interface AppPreferencesRepository {
    suspend fun isStayConnectedEnabled(): Boolean
    suspend fun setCurrentUserId(userId: Int, stayConnected: Boolean)
    suspend fun getCurrentUserId(): Int
}

class AppPreferencesRepositoryImpl @Inject constructor(
    private val localAccountPreferences: LocalAccountPreferences
) : AppPreferencesRepository {
    override suspend fun isStayConnectedEnabled(): Boolean =
        localAccountPreferences.isStayConnectedEnabled()

    override suspend fun setCurrentUserId(userId: Int, stayConnected: Boolean) =
        localAccountPreferences.setCurrentUserId(userId = userId, stayConnected = stayConnected)

    override suspend fun getCurrentUserId(): Int =
        localAccountPreferences.getCurrentUserId()
}