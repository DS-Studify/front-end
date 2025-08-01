package com.ds.studify.feature.signup.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.ds.studify.feature.signup.SignupRoute
import kotlinx.serialization.Serializable

@Serializable
data object RouteSignup

fun NavGraphBuilder.signupScreen(
    navHostController: NavHostController
) {
    composable<RouteSignup> {
        SignupRoute(
            onBack = { navHostController.popBackStack() }
        )
    }
}

fun NavController.navigateToSignup() {
    navigate(RouteSignup)
}