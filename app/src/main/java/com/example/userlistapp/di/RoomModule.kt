package com.example.userlistapp.di

import android.content.Context
import androidx.room.Room
import com.example.userlistapp.data.datasource.room.LocalAccountDatabase
import com.example.userlistapp.data.datasource.room.dao.UsersDao
import com.example.userlistapp.data.repository.UsersRepository
import com.example.userlistapp.domain.usecase.UpdateProfilePictureUseCase
import com.example.userlistapp.domain.usecase.UpdateProfilePictureUseCaseImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {
    @Provides
    @Singleton
    fun providesAppDatabase(
        @ApplicationContext context: Context
    ): LocalAccountDatabase = Room.databaseBuilder(
        context, LocalAccountDatabase::class.java, "app_database"
    ).fallbackToDestructiveMigration().build()

    @Provides
    @Singleton
    fun providesUsersDao(
        database: LocalAccountDatabase
    ): UsersDao = database.usersDao
}

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideUpdateProfilePictureUseCase(
        usersRepository: UsersRepository
    ): UpdateProfilePictureUseCase {
        return UpdateProfilePictureUseCaseImpl(usersRepository)
    }
}