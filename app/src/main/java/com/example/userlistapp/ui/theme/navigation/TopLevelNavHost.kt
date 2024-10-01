package com.example.userlistapp.ui.theme.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.userlistapp.ui.theme.screen.home.HomeScreen
import com.example.userlistapp.ui.theme.screen.resetpassword.ResetPasswordScreen
import com.example.userlistapp.ui.theme.screen.signin.SignInScreen
import com.example.userlistapp.ui.theme.screen.signup.SignUpScreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TopLevelNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    onNavigateBack: () -> Unit,
    startDestination: String,
    topLevelState: TopLevelState = rememberTopLevelState(navController = navController)
) {

    NavHost(
        navController = navController,
        modifier = modifier,
        route = "top_level_nav_host",
        startDestination = startDestination
    ) {

        composable(route = TopLevelScreen.SignIn.route) {
            SignInScreen(
                modifier = Modifier.fillMaxSize(),
                onNavigateBack = onNavigateBack,
                onNavigateToHome = {
                    topLevelState.navigateAndClearBack(
                        currentRoute = TopLevelScreen.SignIn.route,
                        destinationRoute = TopLevelScreen.Home.route
                    )
                },
                onNavigateToSignUp = {
                    topLevelState.navigate(destinationRoute = TopLevelScreen.SignUp.route)
                },
                onNavigateToResetPassword = {
                    topLevelState.navigate(destinationRoute = TopLevelScreen.ResetPassword.route)
                }
            )
        }

        composable(route = TopLevelScreen.SignUp.route) {
            SignUpScreen(
                modifier = Modifier.fillMaxSize(),
                onNavigateBack = {
                    topLevelState.navigateBackWithFallback(
                        currentRoute = TopLevelScreen.SignUp.route,
                        destinationRoute = TopLevelScreen.SignIn.route
                    )
                },
                onNavigateToHome = {
                    topLevelState.navigateAndClearBack(
                        currentRoute = TopLevelScreen.SignUp.route,
                        destinationRoute = TopLevelScreen.Home.route
                    )
                }
            )
        }

        composable(route = TopLevelScreen.ResetPassword.route) {
            ResetPasswordScreen(
                modifier = Modifier.fillMaxSize(),
                onNavigateBack = {
                    topLevelState.navigateBackWithFallback(
                        currentRoute = TopLevelScreen.ResetPassword.route,
                        destinationRoute = TopLevelScreen.SignIn.route
                    )
                }
            )
        }

        composable(route = TopLevelScreen.Home.route) {
            HomeScreen(
                modifier = Modifier.fillMaxSize(),
                navController = navController,
                onNavigateBack = onNavigateBack,
                onNavigateToSignIn = {
                    topLevelState.navigateAndClearBack(
                        currentRoute = TopLevelScreen.Home.route,
                        destinationRoute = TopLevelScreen.SignIn.route
                    )
                },
            )
        }
    }
}

sealed class TopLevelScreen(val route: String) {
    data object Home : TopLevelScreen(route = "home")
    data object SignIn : TopLevelScreen(route = "sign_in")
    data object SignUp : TopLevelScreen(route = "sign_up")
    data object ResetPassword : TopLevelScreen(route = "reset_password")
}