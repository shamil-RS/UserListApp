package com.example.userlistapp.ui.theme.screen.resetpassword

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.userlistapp.R
import com.example.userlistapp.ui.theme.component.CustomButton
import com.example.userlistapp.ui.theme.component.CustomOutlinedTextField
import com.example.userlistapp.util.ClearTrailingButton
import com.example.userlistapp.util.LaIcons
import com.example.userlistapp.util.ToggleTextVisibilityTrailingButton
import com.example.userlistapp.util.safeStringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResetPasswordScreen(
    modifier: Modifier = Modifier,
    onNavigateBack: () -> Unit,
    viewModel: ResetPasswordViewModel = hiltViewModel()
) {
    val screenState by viewModel.screenState.collectAsStateWithLifecycle()
    val scrollState = rememberScrollState()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

    BackHandler(onBack = onNavigateBack)

    LaunchedEffect(key1 = Unit) {
        viewModel.channel.collect { channel ->
            when (channel) {
                ResetPasswordChannel.EmailNotFound -> snackbarHostState.showSnackbar(
                    message = context.getString(R.string.thereIsNoAccountRegisteredWithThisEmail),
                    withDismissAction = true
                )
                ResetPasswordChannel.PasswordSuccessfullyReset -> {
                    snackbarHostState.showSnackbar(
                        message = context.getString(R.string.passwordSuccessfullyReset),
                        withDismissAction = true
                    )
                }
            }
        }
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = stringResource(id = R.string.localAccountResetPassword)) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = LaIcons.NavigateBefore,
                            contentDescription = stringResource(id = R.string.navigateBack)
                        )
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues = innerPadding)
                .verticalScroll(state = scrollState),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CustomOutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                value = screenState.email,
                onValueChange = { viewModel.changeEmail(value = it) },
                placeholderString = stringResource(id = R.string.emailPlaceholder),
                errorMessage = safeStringResource(id = screenState.emailErrorResId),
                leadingIcon = {
                    Icon(imageVector = LaIcons.Email, contentDescription = null)
                },
                trailingIcon = if (screenState.email.isEmpty()) null else {
                    {
                        ClearTrailingButton(
                            onClick = { viewModel.changeEmail(value = "") }
                        )
                    }
                },
                enableWhiteSpace = false,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(focusDirection = FocusDirection.Down) }
                )
            )

            Spacer(modifier = Modifier.height(height = 16.dp))

            CustomOutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                value = screenState.password,
                onValueChange = { viewModel.changePassword(value = it) },
                placeholderString =
                if (screenState.isPasswordVisible) stringResource(id = R.string.passwordPlaceholderShow)
                else stringResource(id = R.string.passwordPlaceholderHide),
                errorMessage = safeStringResource(id = screenState.passwordErrorResId),
                leadingIcon = {
                    Icon(imageVector = LaIcons.Password, contentDescription = null)
                },
                trailingIcon = {
                    ToggleTextVisibilityTrailingButton(
                        onClick = viewModel::togglePasswordVisibility,
                        isVisible = screenState.isPasswordVisible
                    )
                },
                enableWhiteSpace = false,
                hideText = !screenState.isPasswordVisible,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.clearFocus()
                        viewModel.resetPassword()
                    }
                )
            )

            Spacer(modifier = Modifier.height(height = 16.dp))

            CustomButton(
                text = stringResource(id = R.string.resetPassword),
                onClick = viewModel::resetPassword
            )
        }
    }
}