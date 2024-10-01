package com.example.userlistapp.domain.usecase

import com.example.userlistapp.data.repository.UsersRepository
import javax.inject.Inject

interface UpdateProfilePictureUseCase {
    suspend operator fun invoke(userId: Int, profilePicture: String)
}

class UpdateProfilePictureUseCaseImpl @Inject constructor(
    private val usersRepository: UsersRepository
) : UpdateProfilePictureUseCase {
    override suspend fun invoke(userId: Int, profilePicture: String) {
        usersRepository.updateProfilePicture(userId, profilePicture)
    }
}
