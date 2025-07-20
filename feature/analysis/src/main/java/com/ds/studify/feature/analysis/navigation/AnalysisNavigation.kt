package com.ds.studify.feature.analysis.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.ds.studify.feature.analysis.AnalysisRoute
import kotlinx.serialization.Serializable

@Serializable
data object RouteAnalysis

fun NavGraphBuilder.analysisScreen(
    analysisNavigationDelegator: AnalysisNavigationDelegator
) {
    composable<RouteAnalysis> {
        AnalysisRoute(
            analysisNavigationDelegator
        )
    }
}

fun NavController.navigateToAnalysis() {
    navigate(RouteAnalysis)
}

class AnalysisNavigationDelegator(
    val onRestudyClick: () -> Unit = {},
    val onStudyCloseClick: () -> Unit = {}
)