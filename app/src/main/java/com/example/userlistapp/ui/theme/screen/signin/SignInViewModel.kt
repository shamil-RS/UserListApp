package com.example.userlistapp.ui.theme.screen.signin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.userlistapp.R
import com.example.userlistapp.domain.type.InputError
import com.example.userlistapp.domain.type.InputResult
import com.example.userlistapp.domain.usecase.GetUserByEmailUseCase
import com.example.userlistapp.domain.usecase.SetCurrentUserIdUseCase
import com.example.userlistapp.domain.usecase.ValidateEmailFieldUseCase
import com.example.userlistapp.domain.usecase.ValidateSimpleFieldUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface SignInChannel {
    data object EmailNotFound : SignInChannel
    data object IncorrectPassword : SignInChannel
    data object SignInSuccessfully : SignInChannel
}

data class SignInScreenState(
    val email: String,
    val emailErrorResId: Int?,
    val password: String,
    val passwordErrorResId: Int?,
    val isPasswordVisible: Boolean,
    val keepLogged: Boolean,
)

private data class SignInViewModelState(
    val email: String = "",
    val emailErrorResId: Int? = null,
    val password: String = "",
    val passwordErrorResId: Int? = null,
    val isPasswordVisible: Boolean = false,
    val keepLogged: Boolean = true,
) {
    fun asScreenState() = SignInScreenState(
        email = email,
        emailErrorResId = emailErrorResId,
        password = password,
        passwordErrorResId = passwordErrorResId,
        isPasswordVisible = isPasswordVisible,
        keepLogged = keepLogged
    )
}

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val validateSimpleFieldUseCase: ValidateSimpleFieldUseCase,
    private val validateEmailFieldUseCase: ValidateEmailFieldUseCase,
    private val getUserByEmailUseCase: GetUserByEmailUseCase,
    private val setCurrentUserIdUseCase: SetCurrentUserIdUseCase,
) : ViewModel() {

    private val viewModelState = MutableStateFlow(value = SignInViewModelState())

    val screenState = viewModelState.map { it.asScreenState() }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5_000),
        initialValue = viewModelState.value.asScreenState()
    )

    private val _channel = Channel<SignInChannel>()
    val channel = _channel.receiveAsFlow()

    fun changeEmail(value: String) {
        viewModelState.update { it.copy(email = value) }
    }

    fun changePassword(value: String) {
        viewModelState.update { it.copy(password = value) }
    }

    fun togglePasswordVisibility() {
        viewModelState.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }
    }

    fun signIn() {
        val emailResult = validateEmailFieldUseCase(string = viewModelState.value.email)
        val passwordResult = validateSimpleFieldUseCase(string = viewModelState.value.password)

        viewModelState.update { state ->
            state.copy(
                emailErrorResId = when (emailResult) {
                    InputResult.Success -> null
                    is InputResult.Error -> when (emailResult.inputError) {
                        InputError.FieldEmpty -> R.string.emailEmptyError
                        InputError.FieldInvalid -> R.string.emailInvalidError
                        else -> null
                    }
                },
                passwordErrorResId = when (passwordResult) {
                    InputResult.Success -> null
                    is InputResult.Error -> when (passwordResult.inputError) {
                        InputError.FieldEmpty -> R.string.passwordEmptyError
                        else -> null
                    }
                }
            )
        }

        listOf(
            emailResult,
            passwordResult
        ).any { inputResult ->
            inputResult is InputResult.Error
        }.also { hasError ->
            if (hasError) return
        }

        viewModelScope.launch {
            val userResult = getUserByEmailUseCase(email = viewModelState.value.email)
            if (userResult == null) {
                _channel.send(element = SignInChannel.EmailNotFound)
                return@launch
            }

            val isValidPassword = userResult.password == viewModelState.value.password
            if (!isValidPassword) {
                _channel.send(element = SignInChannel.IncorrectPassword)
                return@launch
            }

            setCurrentUserIdUseCase(
                userId = userResult.id,
                stayConnected = viewModelState.value.keepLogged
            )

            _channel.send(element = SignInChannel.SignInSuccessfully)
        }
    }
}