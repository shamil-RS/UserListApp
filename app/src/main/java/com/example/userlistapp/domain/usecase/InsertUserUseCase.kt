package com.example.userlistapp.domain.usecase

import com.example.userlistapp.data.repository.UsersRepository
import com.example.userlistapp.domain.model.User
import javax.inject.Inject

interface InsertUserUseCase {
    suspend operator fun invoke(user: User): Int
}

class InsertUserUseCaseImpl @Inject constructor(
    private val usersRepository: UsersRepository
) : InsertUserUseCase {
    override suspend fun invoke(user: User): Int =
        usersRepository.insertUser(user = user)
}