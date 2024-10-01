package com.example.userlistapp.domain.usecase

import com.example.userlistapp.data.repository.AppPreferencesRepository
import javax.inject.Inject

interface SetCurrentUserIdUseCase {
    suspend operator fun invoke(userId: Int, stayConnected: Boolean)
}

class SetCurrentUserIdUseCaseImpl @Inject constructor(
    private val appPreferencesRepository: AppPreferencesRepository
) : SetCurrentUserIdUseCase {
    override suspend fun invoke(userId: Int, stayConnected: Boolean) =
        appPreferencesRepository.setCurrentUserId(userId = userId, stayConnected = stayConnected)
}