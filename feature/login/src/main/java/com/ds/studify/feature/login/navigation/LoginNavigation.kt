package com.ds.studify.feature.login.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.ds.studify.feature.login.LoginRoute
import kotlinx.serialization.Serializable

@Serializable
data object RouteLogin

fun NavGraphBuilder.loginScreen(
    navigationDelegator: LoginNavigationDelegator
) {
    composable<RouteLogin> {
        LoginRoute(navigationDelegator)
    }
}

class LoginNavigationDelegator(
    val onLoginSuccess: () -> Unit = {},
    val onSignInClicked: () -> Unit = {}
)