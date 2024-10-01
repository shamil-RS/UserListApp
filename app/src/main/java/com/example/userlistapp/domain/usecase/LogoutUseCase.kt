package com.example.userlistapp.domain.usecase

import com.example.userlistapp.data.repository.AppPreferencesRepository
import javax.inject.Inject

interface LogoutUseCase {
    suspend operator fun invoke()
}

class LogoutUseCaseImpl @Inject constructor(
    private val appPreferencesRepository: AppPreferencesRepository
) : LogoutUseCase {
    override suspend fun invoke() =
        appPreferencesRepository.setCurrentUserId(userId = 0, stayConnected = false)
}