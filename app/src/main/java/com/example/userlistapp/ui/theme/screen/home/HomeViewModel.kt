package com.example.userlistapp.ui.theme.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.userlistapp.data.repository.UsersRepository
import com.example.userlistapp.domain.model.User
import com.example.userlistapp.domain.usecase.GetCurrentUserIdUseCase
import com.example.userlistapp.domain.usecase.GetUserByIdStreamUseCase
import com.example.userlistapp.domain.usecase.LogoutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface HomeChannel {
    data object Logout : HomeChannel
}

data class HomeViewModelState(
    val userList: List<User> = emptyList(),
    val isLoading: Boolean = true,
    val userName: String = "",
    val userId: Int = 0,
    val profilePicture: String? = null ,
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getCurrentUserIdUseCase: GetCurrentUserIdUseCase,
    private val getUserByIdStreamUseCase: GetUserByIdStreamUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val userRepository: UsersRepository
) : ViewModel() {

    val viewModelState = MutableStateFlow(value = HomeViewModelState())

    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user.asStateFlow()

    private val _channel = Channel<HomeChannel>()
    val channel = _channel.receiveAsFlow()

    init {
        loadUserData()
        loadUserProfile()
        loadAllUsers()
    }

    private fun loadUserProfile() {
        viewModelScope.launch {
            val currentUser = userRepository.getCurrentUser()
            _user.value = currentUser
        }
    }

    private fun loadUserData() {
        viewModelState.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            try {
                val currentUser = userRepository.getCurrentUser()
                _user.value = currentUser

                val currentUserId = getCurrentUserIdUseCase()
                getUserByIdStreamUseCase(id = currentUserId).collectLatest { user ->
                    user?.let {
                        viewModelState.update { state ->
                            state.copy(
                                userId = user.id,
                                userName = user.name,
                                isLoading = false
                            )
                        }
                    } ?: run {
                        viewModelState.update { it.copy(isLoading = false) }
                    }
                }
            } catch (e: Exception) {
                viewModelState.update { it.copy(isLoading = false) }
            }
        }
    }

    private fun loadAllUsers() {
        viewModelScope.launch {
            try {
                val allUsers = userRepository.getAllUsers(sortByRegistrationDate = true)
                viewModelState.update { it.copy(userList = allUsers) }
            } catch (e: Exception) {
                // handle error
            }
        }
    }

    fun deleteUser(user: User) {
        viewModelScope.launch {
            try {
                userRepository.deleteUser(user.id)
                loadAllUsers()
            } catch (e: Exception) {
                // handle error
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            try {
                logoutUseCase()
                _channel.send(HomeChannel.Logout)
            } catch (e: Exception) {
                // handle error
            }
        }
    }
}