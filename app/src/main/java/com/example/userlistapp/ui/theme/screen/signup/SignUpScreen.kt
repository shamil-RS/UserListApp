package com.example.userlistapp.ui.theme.screen.signup

import android.Manifest
import android.app.DatePickerDialog
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
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

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(
    modifier: Modifier = Modifier,
    onNavigateBack: () -> Unit,
    onNavigateToHome: () -> Unit,
    viewModel: SignUpViewModel = hiltViewModel()
) {
    val screenState by viewModel.screenState.collectAsStateWithLifecycle()
    val scrollState = rememberScrollState()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

    var birthDate by remember { mutableStateOf("") }
    var showDatePicker by remember { mutableStateOf(false) }

    val imagePickerLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                val imagePath = it.toString()
                viewModel.changeProfilePicture(imagePath)
                val userId = viewModel.getCurrentUserId()
                if (userId != null) {
                    viewModel.updateProfilePicture(userId, imagePath)
                } else {
                    Log.e("SignInScreen", "User ID is null")
                }
            } ?: run {
                Log.e("SignInScreen", "URI is null")
            }
        }

    val permissionLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                imagePickerLauncher.launch("image/*")
            } else {
                Log.e("SignUpScreen", "Gallery access permission denied")
            }
        }

    LaunchedEffect(key1 = Unit) {
        viewModel.channel.collect { channel ->
            when (channel) {
                SignUpChannel.UnavailableEmail -> snackbarHostState.showSnackbar(
                    message = context.getString(R.string.emailAlreadyHasAccountAssociated),
                    withDismissAction = true
                )

                SignUpChannel.SignUpFailed -> snackbarHostState.showSnackbar(
                    message = context.getString(R.string.anErrorOccurred),
                    withDismissAction = true
                )

                SignUpChannel.SignUpSuccessfully -> onNavigateToHome()
            }
        }
    }

    BackHandler(onBack = onNavigateBack)

    Scaffold(
        modifier = modifier,
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = stringResource(id = R.string.localAccountSignUp)) },
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
                value = screenState.name,
                onValueChange = { viewModel.changeName(value = it) },
                placeholderString = stringResource(id = R.string.namePlaceholder),
                errorMessage = safeStringResource(id = screenState.nameErrorResId),
                leadingIcon = {
                    Icon(imageVector = LaIcons.Person, contentDescription = null)
                },
                trailingIcon = if (screenState.name.isEmpty()) null else {
                    {
                        ClearTrailingButton(
                            onClick = { viewModel.changeName(value = "") }
                        )
                    }
                },
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Words,
                    keyboardType = KeyboardType.Text,
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
                        viewModel.signUp()
                    }
                )
            )

            Spacer(modifier = Modifier.height(height = 16.dp))

            CustomOutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                value = birthDate,
                onValueChange = { birthDate = it },
                placeholderString = stringResource(id = R.string.birthDatePlaceholder),
                leadingIcon = {
                    Icon(imageVector = LaIcons.Calendar, contentDescription = null)
                },
                trailingIcon = {
                    IconButton(onClick = { showDatePicker = true }) {
                        Icon(painter = painterResource(id = R.drawable.arrowdown),  modifier = Modifier.size(20.dp), contentDescription = null)
                    }
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(focusDirection = FocusDirection.Down) }
                )
            )

            if (showDatePicker) {
                val datePickerDialog = rememberDatePickerDialog()
                datePickerDialog.showDatePickerDialog(
                    onDateSelected = { year, month, day ->
                        birthDate = "$day.${month + 1}.$year"
                        showDatePicker = false
                    },
                    onCancel = { showDatePicker = false }
                )
            }

            Spacer(modifier = Modifier.height(height = 16.dp))

            CustomButton(text = "Upload Profile Picture", onClick = {
                permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            })


            Spacer(modifier = Modifier.height(height = 16.dp))

            CustomButton(
                text = stringResource(id = R.string.signUp),
                onClick = {
                    viewModel.changeBirthDate(value = birthDate)
                    viewModel.signUp()
                }
            )

            Spacer(modifier = Modifier.height(height = 16.dp))
        }
    }
}

@Composable
fun rememberDatePickerDialog(): DatePickerDialog {
    val context = LocalContext.current
    return remember {
        DatePickerDialog(context, null, 2022, 0, 1)
    }
}

fun DatePickerDialog.showDatePickerDialog(
    onDateSelected: (Int, Int, Int) -> Unit,
    onCancel: () -> Unit
) {
    this.setOnDateSetListener { _, year, month, day ->
        onDateSelected(year, month, day)
    }
    this.setOnCancelListener {
        onCancel()
    }
    this.show()
}