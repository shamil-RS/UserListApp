package com.example.userlistapp.di

import com.example.userlistapp.util.CoroutinesDispatchers
import com.example.userlistapp.util.CoroutinesDispatchersImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface CoroutinesModule {
    @Binds
    @Singleton
    fun bindsCoroutinesDispatchers(
        dispatchers: CoroutinesDispatchersImpl
    ): CoroutinesDispatchers
}