package com.ds.studify.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.ds.studify.feature.calendar.navigation.StatsNavigationDelegator
import com.ds.studify.feature.home.navigation.HomeNavigationDelegator
import com.ds.studify.ui.MainRoute
import kotlinx.serialization.Serializable

@Serializable
data object RouteMain

fun NavGraphBuilder.mainScreen(
    homeNavigationDelegator: HomeNavigationDelegator,
    statsNavigationDelegator: StatsNavigationDelegator
) {
    composable<RouteMain> {
        MainRoute(
            homeNavigationDelegator,
            statsNavigationDelegator
        )
    }
}