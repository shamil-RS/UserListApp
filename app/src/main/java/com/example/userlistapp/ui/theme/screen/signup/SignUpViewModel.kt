package com.example.userlistapp.ui.theme.screen.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.userlistapp.R
import com.example.userlistapp.data.repository.UsersRepository
import com.example.userlistapp.domain.model.User
import com.example.userlistapp.domain.type.InputError
import com.example.userlistapp.domain.type.InputResult
import com.example.userlistapp.domain.usecase.InsertUserUseCase
import com.example.userlistapp.domain.usecase.IsEmailAvailableUseCase
import com.example.userlistapp.domain.usecase.SetCurrentUserIdUseCase
import com.example.userlistapp.domain.usecase.UpdateProfilePictureUseCase
import com.example.userlistapp.domain.usecase.ValidateEmailFieldUseCase
import com.example.userlistapp.domain.usecase.ValidateSimpleFieldUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface SignUpChannel {
    data object UnavailableEmail : SignUpChannel
    data object SignUpFailed : SignUpChannel
    data object SignUpSuccessfully : SignUpChannel
}

data class SignUpScreenState(
    val name: String,
    val nameErrorResId: Int?,
    val email: String,
    val emailErrorResId: Int?,
    val password: String,
    val passwordErrorResId: Int?,
    val isPasswordVisible: Boolean,
    val profilePicture: String?,
    val birthDate: String? = "",
    val keepLogged: Boolean,
)

private data class SignUpViewModelState(
    val name: String = "",
    val nameErrorResId: Int? = null,
    val email: String = "",
    val emailErrorResId: Int? = null,
    val password: String = "",
    val passwordErrorResId: Int? = null,
    val isPasswordVisible: Boolean = false,
    val profilePicture: String? = "",
    val birthDate: String? = "",
    val keepLogged: Boolean = true,
) {
    fun asScreenState() = SignUpScreenState(
        name = name,
        nameErrorResId = nameErrorResId,
        email = email,
        emailErrorResId = emailErrorResId,
        password = password,
        passwordErrorResId = passwordErrorResId,
        isPasswordVisible = isPasswordVisible,
        profilePicture = profilePicture,
        birthDate = birthDate,
        keepLogged = keepLogged
    )
}

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val validateSimpleFieldUseCase: ValidateSimpleFieldUseCase,
    private val validateEmailFieldUseCase: ValidateEmailFieldUseCase,
    private val isEmailAvailableUseCase: IsEmailAvailableUseCase,
    private val insertUserUseCase: InsertUserUseCase,
    private val setCurrentUserIdUseCase: SetCurrentUserIdUseCase,
    private val updateProfilePictureUseCase: UpdateProfilePictureUseCase,
    private val usersRepository: UsersRepository,
) : ViewModel() {
    private val viewModelState = MutableStateFlow(value = SignUpViewModelState())

    private val _profilePicture = MutableStateFlow<String?>(null)
    val profilePicture: StateFlow<String?> = _profilePicture.asStateFlow()

    val screenState = viewModelState.map { it.asScreenState() }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5_000),
        initialValue = viewModelState.value.asScreenState()
    )

    private val _channel = Channel<SignUpChannel>()
    val channel = _channel.receiveAsFlow()

    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user.asStateFlow()

    private var currentUserId: Int? = null

    init {
        loadUserData()
    }

    fun changeName(value: String) {
        viewModelState.update { it.copy(name = value) }
    }

    fun changeEmail(value: String) {
        viewModelState.update { it.copy(email = value) }
    }

    fun changePassword(value: String) {
        viewModelState.update { it.copy(password = value) }
    }

    fun togglePasswordVisibility() {
        viewModelState.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }
    }

    fun getCurrentUserId(): Int? = currentUserId

    fun updateProfilePicture(userId: Int, imagePath: String) {
        viewModelScope.launch {
            updateProfilePictureUseCase(userId, imagePath)
            viewModelState.update { it.copy(profilePicture = imagePath) }

        }
    }

    fun changeBirthDate(value: String) {
        viewModelState.update { it.copy(birthDate = value) }
    }

    fun changeProfilePicture(imagePath: String) {
        _profilePicture.value = imagePath
        viewModelState.update { it.copy(profilePicture = imagePath) }
    }

    private fun loadUserData() {
        viewModelScope.launch {
            val currentUser = usersRepository.getCurrentUser()
            _user.value = currentUser
        }
    }

    fun signUp() {
        viewModelState.update { it.copy(name = it.name.trim()) }

        val nameResult = validateSimpleFieldUseCase(
            string = viewModelState.value.name
        )

        val emailResult = validateEmailFieldUseCase(
            string = viewModelState.value.email
        )

        val passwordResult = validateSimpleFieldUseCase(
            string = viewModelState.value.password,
            minChar = 4
        )

        viewModelState.update { state ->
            state.copy(
                nameErrorResId = when (nameResult) {
                    InputResult.Success -> null
                    is InputResult.Error -> when (nameResult.inputError) {
                        InputError.FieldEmpty -> R.string.nameEmptyError
                        else -> null
                    }
                },
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
                        InputError.FieldLessMinCharacters -> R.string.passwordTooShortError
                        else -> null
                    }
                }
            )
        }

        listOf(
            nameResult,
            emailResult,
            passwordResult
        ).any { inputResult ->
            inputResult is InputResult.Error
        }.also { hasError ->
            if (hasError) return
        }

        viewModelScope.launch {
            val isEmailAvailable = isEmailAvailableUseCase(email = viewModelState.value.email)
            if (!isEmailAvailable) {
                _channel.send(element = SignUpChannel.UnavailableEmail)
                return@launch
            }

            val user = User(
                name = viewModelState.value.name,
                email = viewModelState.value.email,
                password = viewModelState.value.password,
                profilePicture = viewModelState.value.profilePicture,
                birthDate = viewModelState.value.birthDate
            )

            val userId = insertUserUseCase(user = user)
            if (userId > 0) {
                setCurrentUserIdUseCase(userId = userId, stayConnected = true)

                if (viewModelState.value.profilePicture!!.isNotEmpty()) {
                    updateProfilePicture(userId, viewModelState.value.profilePicture!!)
                }

                _channel.send(element = SignUpChannel.SignUpSuccessfully)
            } else {
                _channel.send(element = SignUpChannel.SignUpFailed)
            }
        }
    }
}