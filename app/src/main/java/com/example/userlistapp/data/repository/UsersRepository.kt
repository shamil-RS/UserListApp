package com.example.userlistapp.data.repository

import com.example.userlistapp.data.datasource.datastore.LocalAccountPreferences
import com.example.userlistapp.data.datasource.room.dao.UsersDao
import com.example.userlistapp.data.datasource.room.model.UserEntity
import com.example.userlistapp.domain.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

interface UsersRepository {
    fun getUserByIdStream(id: Int): Flow<User?>
    suspend fun getUserById(id: Int): User?
    suspend fun getUserByEmail(email: String): User?
    suspend fun isEmailAvailable(email: String): Boolean
    suspend fun insertUser(user: User): Int
    suspend fun updateUser(user: User)
    suspend fun getCurrentUser(): User?
    suspend fun updateProfilePicture(userId: Int, profilePicture: String)
    suspend fun updateBirthDate(userId: Int, birthDate: String)
    suspend fun getAllUsers(sortByRegistrationDate: Boolean): List<User>
    suspend fun deleteUser(userId: Int)
}

class UsersRepositoryImpl @Inject constructor(
    private val usersDao: UsersDao,
    private val localAccountPreferences: LocalAccountPreferences
) : UsersRepository {

    override fun getUserByIdStream(id: Int): Flow<User?> =
        usersDao.getUserByIdStream(id = id).map { userEntity ->
            userEntity?.asUser()
        }

    override suspend fun getUserById(id: Int): User? {
        val userEntity = usersDao.getUserById(id = id)
        return userEntity?.asUser()
    }

    override suspend fun getUserByEmail(email: String): User? {
        val userEntity = usersDao.getUserByEmail(email = email)
        return userEntity?.asUser()
    }

    override suspend fun isEmailAvailable(email: String): Boolean =
        !usersDao.isEmailInUse(email = email)

    override suspend fun insertUser(user: User): Int {
        val userEntity = user.asUserEntity()
        return usersDao.insertUserOrIgnore(userEntity = userEntity).toInt()
    }

    override suspend fun updateUser(user: User) {
        val userEntity = user.asUserEntity()
        usersDao.updateUser(userEntity = userEntity)
    }

    override suspend fun updateProfilePicture(userId: Int, profilePicture: String) {
        usersDao.updateProfilePicture(userId, profilePicture)
    }

    override suspend fun getCurrentUser(): User? {
        val currentUserId = localAccountPreferences.getCurrentUserId()
        return if (currentUserId != 0) {
            getUserById(currentUserId)
        } else {
            null
        }
    }

    override suspend fun updateBirthDate(userId: Int, birthDate: String) {
        usersDao.updateBirthDate(userId, birthDate)
    }

    override suspend fun getAllUsers(sortByRegistrationDate: Boolean): List<User> {
        val userEntities = usersDao.getAllUsers()
        return userEntities.sortedBy { it.birthDate }.map { it.asUser() }
    }

    override suspend fun deleteUser(userId: Int) {
        val authorizedUser = getCurrentUser()
        val userToBeDeleted = getUserById(userId)
        if (authorizedUser != null && userToBeDeleted != null) {
            if (authorizedUser.birthDate!! < userToBeDeleted.birthDate!!) {
                usersDao.deleteUser(userId)
            } else {
                throw Exception("Вы не можете удалить пользователя, который зарегистрировался раньше вас")
            }
        } else {
            throw Exception("Пользователь не найден или не авторизован")
        }
    }


    private fun UserEntity.asUser() = User(
        id = id,
        name = name,
        email = email,
        password = password,
        profilePicture = profilePicture,
        birthDate = birthDate
    )

    private fun User.asUserEntity() = UserEntity(
        id = id,
        name = name,
        email = email,
        password = password,
        profilePicture = profilePicture,
        birthDate = birthDate
    )
}