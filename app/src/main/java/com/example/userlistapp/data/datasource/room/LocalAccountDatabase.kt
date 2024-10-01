package com.example.userlistapp.data.datasource.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.userlistapp.data.datasource.room.model.UserEntity
import com.example.userlistapp.data.datasource.room.dao.UsersDao

@Database(
    entities = [UserEntity::class],
    version = 2,
    exportSchema = false
)
abstract class LocalAccountDatabase : RoomDatabase() {
    abstract val usersDao: UsersDao
}