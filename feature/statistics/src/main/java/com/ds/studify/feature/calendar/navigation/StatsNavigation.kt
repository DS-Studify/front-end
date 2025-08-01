package com.ds.studify.feature.calendar.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.ds.studify.feature.calendar.FeedbackRoute
import com.ds.studify.feature.calendar.StatsRoute
import kotlinx.serialization.Serializable

@Serializable
data object RouteStats

@Serializable
data class RouteFeedback(val id: Long)

fun NavController.navigateToStats(
    navOptions: NavOptions
) {
    navigate(RouteStats, navOptions)
}

fun NavGraphBuilder.statsScreen(
    navHostController: NavHostController,
    paddingValues: PaddingValues
) {
    composable<RouteStats> {
        StatsRoute(
            paddingValues = paddingValues,
            onNavigateToFeedback = { studyId ->
                navHostController.navigate(RouteFeedback(studyId))
            }
        )
    }

    composable<RouteFeedback> {
        FeedbackRoute(
            onBack = { navHostController.popBackStack() }
        )
    }
}