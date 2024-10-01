package com.example.userlistapp.domain.usecase

import com.example.userlistapp.data.repository.UsersRepository
import javax.inject.Inject

interface IsEmailAvailableUseCase {
    suspend operator fun invoke(email: String): Boolean
}

class IsEmailAvailableUseCaseImpl @Inject constructor(
    private val usersRepository: UsersRepository
) : IsEmailAvailableUseCase {
    override suspend fun invoke(email: String): Boolean =
        usersRepository.isEmailAvailable(email = email)
}