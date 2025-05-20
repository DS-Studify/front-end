package com.ds.studify.feature.home.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.ds.studify.feature.home.HomeRoute
import kotlinx.serialization.Serializable

@Serializable
data object RouteHome

fun NavGraphBuilder.homeScreen(
    homeNavigationDelegator: HomeNavigationDelegator
) {
    composable<RouteHome> {
        HomeRoute(
            navigationDelegator = homeNavigationDelegator
        )
    }
}

class HomeNavigationDelegator(
    val onStartToStudyClick: () -> Unit = {}
)