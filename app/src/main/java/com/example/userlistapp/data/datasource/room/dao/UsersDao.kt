package com.example.userlistapp.data.datasource.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.userlistapp.data.datasource.room.model.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UsersDao {

    @Query(value = "SELECT * FROM users WHERE id = :id LIMIT 1")
    fun getUserByIdStream(id: Int): Flow<UserEntity?>

    @Query(value = "SELECT * FROM users WHERE id = :id LIMIT 1")
    suspend fun getUserById(id: Int): UserEntity?

    @Query(value = "SELECT * FROM users WHERE email = :email LIMIT 1")
    suspend fun getUserByEmail(email: String): UserEntity?

    @Query(value = "SELECT COUNT() FROM users WHERE email = :email LIMIT 1")
    suspend fun isEmailInUse(email: String): Boolean

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertUserOrIgnore(userEntity: UserEntity): Long

    @Update
    suspend fun updateUser(userEntity: UserEntity)

    @Query("UPDATE users SET profilePicture = :profilePicture WHERE id = :userId")
    suspend fun updateProfilePicture(userId: Int, profilePicture: String)

    @Query("UPDATE users SET birthDate = :birthDate WHERE id = :userId")
    suspend fun updateBirthDate(userId: Int, birthDate: String)

    @Query("SELECT * FROM users")
    suspend fun getAllUsers(): List<UserEntity>

    @Query("DELETE FROM users WHERE id = :userId")
    suspend fun deleteUser(userId: Int)

}