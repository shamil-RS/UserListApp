package com.example.userlistapp.domain.usecase

import com.example.userlistapp.data.repository.UsersRepository
import com.example.userlistapp.domain.model.User
import javax.inject.Inject

interface GetUserByIdUseCase {
    suspend operator fun invoke(id: Int): User?
}

class GetUserByIdUseCaseImpl @Inject constructor(
    private val usersRepository: UsersRepository
) : GetUserByIdUseCase {
    override suspend fun invoke(id: Int): User? =
        usersRepository.getUserById(id = id)
}