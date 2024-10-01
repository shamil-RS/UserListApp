package com.example.userlistapp.domain.usecase

import com.example.userlistapp.data.repository.UsersRepository
import com.example.userlistapp.domain.model.User
import javax.inject.Inject

interface GetUserByEmailUseCase {
    suspend operator fun invoke(email: String): User?
}


class GetUserByEmailUseCaseImpl @Inject constructor(
    private val usersRepository: UsersRepository
) : GetUserByEmailUseCase {
    override suspend fun invoke(email: String): User? =
        usersRepository.getUserByEmail(email = email)
}