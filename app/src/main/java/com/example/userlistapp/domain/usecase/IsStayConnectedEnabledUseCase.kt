package com.example.userlistapp.domain.usecase

import com.example.userlistapp.data.repository.AppPreferencesRepository
import javax.inject.Inject

interface IsStayConnectedEnabledUseCase {
    suspend operator fun invoke(): Boolean
}

class IsStayConnectedEnabledUseCaseImpl @Inject constructor(
    private val appPreferencesRepository: AppPreferencesRepository
) : IsStayConnectedEnabledUseCase {
    override suspend fun invoke(): Boolean =
        appPreferencesRepository.isStayConnectedEnabled()
}