package com.ds.studify.feature.feedback.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.ds.studify.feature.feedback.FeedbackRoute
import kotlinx.serialization.Serializable

@Serializable
data class RouteFeedback(val id: Long)

fun NavGraphBuilder.feedbackScreen(
    navHostController: NavHostController
) {
    composable<RouteFeedback> {
        FeedbackRoute(
            onBack = { navHostController.popBackStack() }
        )
    }
}

fun NavController.navigateToFeedback(id: Long) {
    navigate(RouteFeedback(id))
}