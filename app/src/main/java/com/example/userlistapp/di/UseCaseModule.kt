package com.example.userlistapp.di

import com.example.userlistapp.domain.usecase.GetCurrentUserIdUseCase
import com.example.userlistapp.domain.usecase.GetCurrentUserIdUseCaseImpl
import com.example.userlistapp.domain.usecase.GetUserByEmailUseCase
import com.example.userlistapp.domain.usecase.GetUserByEmailUseCaseImpl
import com.example.userlistapp.domain.usecase.GetUserByIdStreamUseCase
import com.example.userlistapp.domain.usecase.GetUserByIdStreamUseCaseImpl
import com.example.userlistapp.domain.usecase.GetUserByIdUseCase
import com.example.userlistapp.domain.usecase.GetUserByIdUseCaseImpl
import com.example.userlistapp.domain.usecase.InsertUserUseCase
import com.example.userlistapp.domain.usecase.InsertUserUseCaseImpl
import com.example.userlistapp.domain.usecase.IsEmailAvailableUseCase
import com.example.userlistapp.domain.usecase.IsEmailAvailableUseCaseImpl
import com.example.userlistapp.domain.usecase.IsStayConnectedEnabledUseCase
import com.example.userlistapp.domain.usecase.IsStayConnectedEnabledUseCaseImpl
import com.example.userlistapp.domain.usecase.LogoutUseCase
import com.example.userlistapp.domain.usecase.LogoutUseCaseImpl
import com.example.userlistapp.domain.usecase.SetCurrentUserIdUseCase
import com.example.userlistapp.domain.usecase.SetCurrentUserIdUseCaseImpl
import com.example.userlistapp.domain.usecase.UpdateUserUseCase
import com.example.userlistapp.domain.usecase.UpdateUserUseCaseImpl
import com.example.userlistapp.domain.usecase.ValidateEmailFieldUseCase
import com.example.userlistapp.domain.usecase.ValidateEmailFieldUseCaseImpl
import com.example.userlistapp.domain.usecase.ValidateSimpleFieldUseCase
import com.example.userlistapp.domain.usecase.ValidateSimpleFieldUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
interface UseCaseModule {

    @Binds
    @ViewModelScoped
    fun bindsIsStayConnectedEnabledUseCase(
        useCase: IsStayConnectedEnabledUseCaseImpl
    ): IsStayConnectedEnabledUseCase

    @Binds
    @ViewModelScoped
    fun bindsSetCurrentUserIdUseCase(
        useCase: SetCurrentUserIdUseCaseImpl
    ): SetCurrentUserIdUseCase

    @Binds
    @ViewModelScoped
    fun bindsGetCurrentUserIdUseCase(
        useCase: GetCurrentUserIdUseCaseImpl
    ): GetCurrentUserIdUseCase

    @Binds
    @ViewModelScoped
    fun bindsLogoutUseCase(
        useCase: LogoutUseCaseImpl
    ): LogoutUseCase

    @Binds
    @ViewModelScoped
    fun bindsGetUserByIdStreamUseCase(
        useCase: GetUserByIdStreamUseCaseImpl
    ): GetUserByIdStreamUseCase

    @Binds
    @ViewModelScoped
    fun bindsGetUserByIdUseCase(
        useCase: GetUserByIdUseCaseImpl
    ): GetUserByIdUseCase

    @Binds
    @ViewModelScoped
    fun bindsGetUserByEmailUseCase(
        useCase: GetUserByEmailUseCaseImpl
    ): GetUserByEmailUseCase

    @Binds
    @ViewModelScoped
    fun bindsIsEmailAvailableUseCase(
        useCase: IsEmailAvailableUseCaseImpl
    ): IsEmailAvailableUseCase

    @Binds
    @ViewModelScoped
    fun bindsInsertUserUseCase(
        useCase: InsertUserUseCaseImpl
    ): InsertUserUseCase

    @Binds
    @ViewModelScoped
    fun bindsUpdateUserUseCase(
        useCase: UpdateUserUseCaseImpl
    ): UpdateUserUseCase

    @Binds
    @ViewModelScoped
    fun bindsValidateSimpleFieldUseCase(
        useCase: ValidateSimpleFieldUseCaseImpl
    ): ValidateSimpleFieldUseCase

    @Binds
    @ViewModelScoped
    fun bindsValidateEmailFieldUseCase(
        useCase: ValidateEmailFieldUseCaseImpl
    ): ValidateEmailFieldUseCase
}