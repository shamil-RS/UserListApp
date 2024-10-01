package com.example.userlistapp.ui.theme.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.userlistapp.domain.usecase.GetCurrentUserIdUseCase
import com.example.userlistapp.domain.usecase.IsStayConnectedEnabledUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MainActivityState(
    val isLoading: Boolean,
    val isStayConnectedEnabled: Boolean,
    val currentUserId: Int,
)

private data class MainActivityViewModelState(
    val isLoading: Boolean = true,
    val isStayConnectedEnabled: Boolean = false,
    val currentUserId: Int = 0
) {
    fun asActivityState() = MainActivityState(
        isLoading = isLoading,
        isStayConnectedEnabled = isStayConnectedEnabled,
        currentUserId = currentUserId
    )
}

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val isStayConnectedEnabledUseCase: IsStayConnectedEnabledUseCase,
    private val getCurrentUserIdUseCase: GetCurrentUserIdUseCase
) : ViewModel() {
    private val viewModelState = MutableStateFlow(value = MainActivityViewModelState())

    val activityState = viewModelState.map { it.asActivityState() }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5_000),
        initialValue = viewModelState.value.asActivityState()
    )

    init {
        watchAppConfigurationStream()
    }

    private fun watchAppConfigurationStream() {
        viewModelScope.launch {
//            viewModelState.update { it.copy(isLoading = true) }

            val isStayConnectedEnabled = isStayConnectedEnabledUseCase()
            val currentUserId = if (isStayConnectedEnabled) getCurrentUserIdUseCase() else 0

            viewModelState.update { state ->
                state.copy(
                    isLoading = false,
                    isStayConnectedEnabled = isStayConnectedEnabled,
                    currentUserId = currentUserId
                )
            }
        }
    }
}