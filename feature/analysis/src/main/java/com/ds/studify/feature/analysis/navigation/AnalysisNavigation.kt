package com.ds.studify.feature.analysis.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.ds.studify.feature.analysis.AnalysisRoute
import kotlinx.serialization.Serializable

@Serializable
data class RouteAnalysis(val id: Long)

fun NavGraphBuilder.analysisScreen(
    analysisNavigationDelegator: AnalysisNavigationDelegator
) {
    composable<RouteAnalysis> {
        AnalysisRoute(
            analysisNavigationDelegator
        )
    }
}

fun NavController.navigateToAnalysis(id: Long) {
    navigate(RouteAnalysis(id))
}

class AnalysisNavigationDelegator(
    val onRestudyClick: () -> Unit = {},
    val onStudyCloseClick: () -> Unit = {}
)