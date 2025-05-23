package com.ds.studify.feature.home.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.ds.studify.feature.home.HomeRoute
import kotlinx.serialization.Serializable

@Serializable
data object RouteHome

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