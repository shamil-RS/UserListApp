package com.example.userlistapp.ui.theme.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.example.userlistapp.ui.theme.screen.MainActivityViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppScreen(onNavigateBack: () -> Unit) {
    val viewModel: MainActivityViewModel = hiltViewModel()

    val activityState by viewModel.activityState.collectAsStateWithLifecycle()

    val navController = rememberNavController()
    val topLevelState = rememberTopLevelState(navController = navController)

    Scaffold { innerPadding ->
        TopLevelNavHost(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            navController = navController,
            topLevelState = topLevelState,
            onNavigateBack = { onNavigateBack() },
            startDestination = startDestination(
                isStayConnectedEnabled = activityState.isStayConnectedEnabled,
                currentUserId = activityState.currentUserId
            )
        )
    }
}

private fun startDestination(
    isStayConnectedEnabled: Boolean,
    currentUserId: Int
): String = if (isStayConnectedEnabled && currentUserId > 0)
    TopLevelScreen.Home.route else TopLevelScreen.SignIn.route