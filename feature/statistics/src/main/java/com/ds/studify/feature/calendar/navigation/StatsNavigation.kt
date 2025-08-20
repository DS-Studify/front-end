package com.ds.studify.feature.calendar.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.ds.studify.feature.calendar.StatsRoute
import kotlinx.serialization.Serializable

@Serializable
data object RouteStats

fun NavController.navigateToStats(
    navOptions: NavOptions
) {
    navigate(RouteStats, navOptions)
}

fun NavGraphBuilder.statsScreen(
    statsNavigationDelegator: StatsNavigationDelegator
) {
    composable<RouteStats> {
        StatsRoute(
            onNavigateToFeedback = { studyId ->
                statsNavigationDelegator.onTimeLineClick(studyId)
            }
        )
    }
}

class StatsNavigationDelegator(
    val onTimeLineClick: (Long) -> Unit = {}
)