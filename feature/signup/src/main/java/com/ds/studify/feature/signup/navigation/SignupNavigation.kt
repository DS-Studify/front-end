package com.ds.studify.feature.signup.navigation

import androidx.annotation.Keep
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.ds.studify.feature.signup.SignupRoute
import com.ds.studify.feature.signup.TermsWebViewRoute
import kotlinx.serialization.Serializable

@Serializable
data object RouteSignup

@Serializable
data class RouteTerms(val type: TermsType)

@Keep
@Serializable
enum class TermsType {
    SERVICE, PRIVACY
}

fun NavGraphBuilder.signupGraph(
    navController: NavController,
    onNavigateLogin: () -> Unit
) {
    composable<RouteSignup> {
        SignupRoute(
            onNavigateLogin = onNavigateLogin,
            onNavigateTerms = { termsType ->
                navController.navigate(RouteTerms(termsType))
            }
        )
    }
    composable<RouteTerms> {
        TermsWebViewRoute(onBack = { navController.popBackStack() })
    }
}

fun NavController.navigateToSignup() {
    navigate(RouteSignup)
}