package com.example.userlistapp.di

import com.example.userlistapp.data.repository.AppPreferencesRepository
import com.example.userlistapp.data.repository.AppPreferencesRepositoryImpl
import com.example.userlistapp.data.repository.UsersRepository
import com.example.userlistapp.data.repository.UsersRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {
    @Binds
    @Singleton
    fun bindsAppPreferencesRepository(
        repository: AppPreferencesRepositoryImpl
    ): AppPreferencesRepository

    @Binds
    @Singleton
    fun bindsUsersRepository(
        repository: UsersRepositoryImpl
    ): UsersRepository
}