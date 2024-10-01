package com.example.userlistapp.domain.usecase

import com.example.userlistapp.data.repository.AppPreferencesRepository
import javax.inject.Inject

interface GetCurrentUserIdUseCase {
    suspend operator fun invoke(): Int
}

class GetCurrentUserIdUseCaseImpl @Inject constructor(
    private val appPreferencesRepository: AppPreferencesRepository
) : GetCurrentUserIdUseCase {
    override suspend fun invoke(): Int =
        appPreferencesRepository.getCurrentUserId()
}