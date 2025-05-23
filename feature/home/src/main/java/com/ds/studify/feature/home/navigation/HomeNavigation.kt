package com.ds.studify.feature.home.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.ds.studify.feature.home.HomeRoute
import kotlinx.serialization.Serializable

@Serializable
data object RouteHome

fun NavController.navigateToHome(
    navOptions: NavOptions
) {
    navigate(RouteHome, navOptions)
}

fun NavGraphBuilder.homeScreen(
    homeNavigationDelegator: HomeNavigationDelegator,
    paddingValues: PaddingValues,
) {
    composable<RouteHome> {
        HomeRoute(
            navigationDelegator = homeNavigationDelegator,
            paddingValues = paddingValues
        )
    }
}

class HomeNavigationDelegator(
    val onStartToStudyClick: () -> Unit = {}
)