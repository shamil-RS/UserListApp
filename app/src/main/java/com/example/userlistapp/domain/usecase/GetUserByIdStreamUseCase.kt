package com.example.userlistapp.domain.usecase

import com.example.userlistapp.data.repository.UsersRepository
import com.example.userlistapp.domain.model.User
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface GetUserByIdStreamUseCase {
    operator fun invoke(id: Int): Flow<User?>
}

class GetUserByIdStreamUseCaseImpl @Inject constructor(
    private val usersRepository: UsersRepository
) : GetUserByIdStreamUseCase {
    override fun invoke(id: Int): Flow<User?> =
        usersRepository.getUserByIdStream(id = id)
}