package com.example.userlistapp.domain.usecase

import com.example.userlistapp.data.repository.UsersRepository
import com.example.userlistapp.domain.model.User
import javax.inject.Inject

interface UpdateUserUseCase {
    suspend operator fun invoke(user: User)
}

class UpdateUserUseCaseImpl @Inject constructor(
    private val usersRepository: UsersRepository
) : UpdateUserUseCase {
    override suspend fun invoke(user: User) =
        usersRepository.updateUser(user = user)
}